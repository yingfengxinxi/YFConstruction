package com.kakarote.build.equipment.common;
import com.kakarote.build.equipment.entity.PO.EquipmentTowerEffect;
import com.kakarote.build.equipment.service.impl.EquipmentTowerEffectServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class TowerCraneEfficiencyManager {

    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private EquipmentTowerEffectServiceImpl equipmentTowerEffectService;

    public void entrance(Integer assetId, BigDecimal value){
        boolean bo =  redisTemplate.hasKey(assetId);
        //根据吊重是否为0判断
        if(value.compareTo(new BigDecimal(0)) < 1){
            if(bo){
                //一次吊重完成，存入数据库
                List listKey1 = redisTemplate.boundListOps(assetId).range(0, 2);
                EquipmentTowerEffect equipmentTowerEffect = new EquipmentTowerEffect();
                equipmentTowerEffect.setAssetId(assetId);
                equipmentTowerEffect.setEndTime(new Date());
                BigDecimal bigDecimal = value.add((BigDecimal)listKey1.get(0)).divide(new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP);
                equipmentTowerEffect.setWeight(bigDecimal.doubleValue());
                equipmentTowerEffect.setStartTime((Date) listKey1.get(1));
                equipmentTowerEffectService.save(equipmentTowerEffect);
            }
        }else {
            if(bo){
                //更新吊重平衡值
                List listKey1 = redisTemplate.boundListOps(assetId).range(0, 2);
                redisTemplate.boundListOps(assetId).set(1,value.add((BigDecimal)listKey1.get(0)).divide(new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP));
            }else {
                //塔吊开始工作，缓存本次吊重值
                List list = new ArrayList();
                list.add(value);
                list.add(new Date());
                redisTemplate.boundListOps(assetId).rightPushAll(list);
            }
        }
    }
}
