package com.kakarote.build.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentSpraySystemBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.entity.PO.EquipmentSpraySystem;
import com.kakarote.build.equipment.hardware.wanqun.WanQunServer;
import com.kakarote.build.equipment.mapper.EquipmentSpraySystemMapper;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.equipment.service.IEquipmentSpraySystemService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 自动喷淋系统 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-06-01
 */
@Service
public class EquipmentSpraySystemServiceImpl extends BaseServiceImpl<EquipmentSpraySystemMapper, EquipmentSpraySystem> implements IEquipmentSpraySystemService {

    @Autowired
    private IEquipmentAssetsService equipmentAssetsService;

    @Autowired
    private IProjectInfoService iProjectInfoService;

    @Autowired
    private WanQunServer wanQunServer;

    @Override
    public BasePage<EquipmentSpraySystem> selectByPage(EquipmentSpraySystemBO spraySystemBO) {
        spraySystemBO.setOrdersBd("s.operation_time",false);
        BasePage<EquipmentSpraySystem> list = getBaseMapper().selectByPage(spraySystemBO.parse(), spraySystemBO);
        return list;
    }


    @Override
    public BasePage<EquipmentSpraySystem> selectByPageOperation(EquipmentSpraySystemBO spraySystemBO) {
        BasePage<EquipmentSpraySystem> list = getBaseMapper().selectByPageOperation(spraySystemBO.parse(), spraySystemBO);
        return list;
    }
    @Override
    public Result spraySwitch(Map<String, Object> map) {
        Integer equipmentId = (Integer) map.get("equipmentId");
        String operationType = String.valueOf(map.get("operationType"));



        // 查询设备
        EquipmentAssets equipmentAssets = equipmentAssetsService.getById(equipmentId);

        if (equipmentAssets == null) {
            throw new CrmException(500, "设备不存在!");
        }
        // 创建喷淋系统记录
        EquipmentSpraySystem sprayTask = this.createSpraySystem(equipmentAssets, operationType);

        // 保存数据
        getBaseMapper().insert(sprayTask);

        return R.ok();
    }

    @Override
    public Result sprayUnifiedSwitch(String isSwitch) {
        // 查询全部喷淋设备
        List<EquipmentAssets> list = equipmentAssetsService.selectSprayEquiList();
        if (list == null) {
            return R.ok();
        }

        List<EquipmentSpraySystem> spraySystemList = new ArrayList<>();
        for (EquipmentAssets equipmentAssets:list) {
            // 如当设备当前状态和需修改状态一直，不插入数据
            LambdaQueryWrapper<EquipmentSpraySystem> sprayQueryWrapper = new LambdaQueryWrapper();
            sprayQueryWrapper.eq(EquipmentSpraySystem::getEquipmentId, equipmentAssets.getAssetId());
            sprayQueryWrapper.orderByDesc(EquipmentSpraySystem::getOperationTime);
            sprayQueryWrapper.last("limit 1");

            EquipmentSpraySystem currentStatus = getBaseMapper().selectOne(sprayQueryWrapper);

            // 判断设备当前状态 如设备当前状态一直，跳过本次循环.
            if (currentStatus != null && isSwitch.equals(currentStatus.getOperationType())) {
                continue;
            }

            // 生成插入数据
            EquipmentSpraySystem spraySystem = this.createSpraySystem(equipmentAssets, isSwitch);
            spraySystemList.add(spraySystem);
        }

        // 批量插入
        this.saveBatch(spraySystemList, spraySystemList.size());

        return R.ok();
    }

    @Override
    public List<Map<String, Object>> selectNoPage(Map<String,Object> params) {
        return getBaseMapper().selectNoPage(params);
    }

    private EquipmentSpraySystem createSpraySystem(EquipmentAssets equipmentAssets, String operationType) {

        EquipmentSpraySystem spraySystem = new EquipmentSpraySystem();
        spraySystem.setEquipmentId(equipmentAssets.getAssetId());
        spraySystem.setOperationType(operationType);
        spraySystem.setOperationTime(new Date());
        spraySystem.setTenantId(equipmentAssets.getTenantId());
        spraySystem.setProjectId(equipmentAssets.getProjectId());

        String realname = UserUtil.getUser().getRealname();

        if (operationType.equals("0")) {
            spraySystem.setOperationReason(realname + "手机端关闭");
            wanQunServer.startAll(equipmentAssets);
        } else if (operationType.equals("1")) {
            spraySystem.setOperationReason(realname + "手机端开启");
            wanQunServer.stopAll(equipmentAssets);
        }

        return spraySystem;
    }

    @Override
    public List<Map<String, Object>> selectPieData() {
        List<Map<String, Object>> list = getBaseMapper().selectPieData();
        Map<String, Object> lx =new HashMap<String, Object>();
        lx.put("name","离线");
        lx.put("value","0");
        list.add(lx);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectBarData() {
        Map<String, Object> params =new HashMap<String, Object>();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String createTime = dateFormat.format(now);
        params.put("day",createTime);
        List<Map<String, Object>> list = getBaseMapper().selectBarData(params);
        return list;
    }


    @Override
    public List<Map<String, Object>> selectKqsdfxData() {
        Map<String, Object> params =new HashMap<String, Object>();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String createTime = dateFormat.format(now);
        params.put("day",createTime);
        List<Map<String, Object>> cs = new ArrayList<Map<String, Object>>();
        for(int i=23;i>-1;i--){
            if(i==0){
                params.put("sz",i);
            }else{
                params.put("sz","-"+i);
            }

            List<Map<String, Object>> list = getBaseMapper().selectKqsdfxData(params);
            int zj=0;
            String rq ="";
            for(int j=0;j<list.size();j++){
                Map k= list.get(j);
                if(k.get("operationType")!=null){
                    String hj=k.get("operationType").toString();
                    zj=zj+Integer.valueOf(hj);
                }
                rq=(String)k.get("rq");
            }
            Map<String, Object>  zsj= new HashMap<String, Object>();
            zsj.put("name",rq);
            zsj.put("value",zj);
            cs.add(zsj);
        }


        return cs;
    }

    @Override
    public List<Map<String, Object>> selectPlscfxData() {
        Map<String, Object> params =new HashMap<String, Object>();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String createTime = dateFormat.format(now); //获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, -1);  //获取当前日期的一个月前的日期
        String badTime = dateFormat.format(calendar.getTime()); //获取一个月前的日期

        List<Map<String, Object>> cs = new ArrayList<Map<String, Object>>();
        QueryWrapper<ProjectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id, project_abbreviation as name").ne("project_id", 0).orderByDesc("create_time");
        List<Map<String, Object>> projectList = iProjectInfoService.listMaps(queryWrapper);

        for(int i=0;i<projectList.size();i++){
            Map zz=projectList.get(i);
            params.put("pid",zz.get("id"));
            params.put("jssj",createTime);
            params.put("kssj",badTime);
            List<Map<String, Object>> list = getBaseMapper().selectPlscfxData(params);//获取当前项目下有喷淋设备开启关闭时间数据
            int assterNumber = getBaseMapper().selectassterNumber(params); //获取当前项目下有多少个喷淋设备
            double timeInt =0;
            double timeAvg =0;
            Long  timeLong ;
            for(int j=0;j<list.size();j++){
                Map cl=list.get(j);
                Date qdsj  = (Date) cl.get("operationTime");
                Date gbsj = new Date();
                if(cl.get("gbsj")!=null){
                    gbsj = (Date) cl.get("gbsj");
                }
                Map time=  timesBetween (qdsj,gbsj);  //获取两个时间之间的差距
                if(time.get("Hour")!=null){
                    timeLong=(Long) time.get("Hour"); //获取两个时间之间的小时差距
                    timeInt=timeInt +(double)timeLong ;//合计该项目下总的喷淋时间
                }
            }
            timeInt=(double) Math.round(timeInt * 100) / 100;  //保留两位小数
            timeAvg=timeInt/assterNumber ;   //获取平均数
            timeAvg=(double) Math.round(timeAvg * 100) / 100;  //保留两位小数
            Map<String, Object> csh= new HashMap<String, Object>();
            csh.put("xmmc",zz.get("name"));
            csh.put("id",zz.get("id"));
            csh.put("zsc",timeInt);
            csh.put("pjz",timeAvg);
            cs.add(csh);
        }


        return cs;
    }

    public static Map<String,Long> timesBetween(Date sdate,Date bdate) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long day = 0;

        long hour = 0;

        long min = 0;

        long sec = 0;

        long diff = 0;

        try {
            sdate= df.parse(df.format(sdate));

            bdate= df.parse(df.format(bdate));

            long stime = sdate.getTime();

            long btime = bdate.getTime();

            if(stime>btime){
                diff = stime - btime;

            }else{
                diff = btime - stime;

            }

            day = diff/(24*60*60*1000);

            hour = diff/(60*60*1000) - day*24;

            min = diff/(60*1000) - day*24*60 - hour*60;

            sec = diff/1000-day*24*60*60-hour*60*60-min*60;

        } catch (ParseException e) {
            e.printStackTrace();

        }

        Map<String, Long> timeMap = new HashMap<String, Long>();

        timeMap.put("Day", day);
        int xs=0;
        if(day>0){
            xs=(int)day*24;
        }
        hour=hour+xs;
        timeMap.put("Hour", hour);

        timeMap.put("Min", min);

        timeMap.put("Sec", sec);

        return timeMap;

    }





}
