package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.BLabourDroneBO;
import com.kakarote.build.labour.entity.PO.BLabourDrone;
import com.kakarote.build.labour.mapper.BLabourDroneMapper;
import com.kakarote.build.labour.service.IBLabourDroneService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.Base64Utils;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 无人机现场巡检 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-05-27
 */
@Service
public class BLabourDroneServiceImpl extends BaseServiceImpl<BLabourDroneMapper, BLabourDrone> implements IBLabourDroneService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BLabourDroneBO> selectList(BLabourDroneBO bLabourDroneBO)
    {
        bLabourDroneBO.setOrdersBd("create_time",false);
        BasePage<BLabourDroneBO> adminMessageBasePage = getBaseMapper().selectDataList(bLabourDroneBO.parse(),bLabourDroneBO);
        for(int i=0;i<adminMessageBasePage.getList().size();i++){
            Map<String, Object> params = new HashMap<>();
            params.put("batchId",adminMessageBasePage.getList().get(i).getInspectionImage());
            params.put("tenantId",adminMessageBasePage.getList().get(i).getTenantId());
            Map<String, Object> data = getBaseMapper().getBatchIdFile(params);
            if(data!=null){
                if(data.containsKey("fileId")){
                    adminMessageBasePage.getList().get(i).setUrl("/adminFile/down/"+data.get("fileId"));
                }
            }

        }

        return adminMessageBasePage;
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String add(BLabourDrone bLabourDrone)
    {
        bLabourDrone.setCreateTime(new Date());
        save(bLabourDrone);
        return  "1";
    }

    /**
     * 修改数据
     *
     */
    @Override
    public String edit(BLabourDrone bLabourDrone)
    {
        bLabourDrone.setUpdateTime(new Date());
        updateById(bLabourDrone);
        return  "1";
    }

    /**
     * 新增数据
     *
     */
    @Override
    public List<Map<String, Object>>  selectFileList(String bithid)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("batchId",bithid);
        params.put("tenantId",UserUtil.getUser().getTenantId());
        List<Map<String, Object>>  data = getBaseMapper().selectFileList(params);
        for(int i=0;i<data.size();i++){
            Map<String, Object> map  = data.get(i);
            try {
                String background = Base64Utils.fileToBase64String(map.get("path").toString());
                data.get(i).put("Base64","data:image/jpg;base64,"+background);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
