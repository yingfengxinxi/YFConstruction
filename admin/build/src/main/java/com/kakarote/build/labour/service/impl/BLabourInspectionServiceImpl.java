package com.kakarote.build.labour.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.BO.BLabourInspectionBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.entity.PO.BLabourInspection;
import com.kakarote.build.labour.mapper.BLabourInspectionMapper;
import com.kakarote.build.labour.service.IBLabourInspectionService;
import com.kakarote.build.supervision.entity.PO.SupervisionEnterprise;
import com.kakarote.build.supervision.service.ISupervisionEnterpriseService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageFeignService;
import com.kakarote.core.message.MessageType;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 企业报验 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@Service
public class BLabourInspectionServiceImpl extends BaseServiceImpl<BLabourInspectionMapper, BLabourInspection> implements IBLabourInspectionService {

    @Autowired
    private MessageFeignService messageFeignService;

    @Autowired
    private ISupervisionEnterpriseService supervisionEnterpriseService;
    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BLabourInspectionBO> selectList(BLabourInspectionBO bLabourInspectionBO)
    {
        bLabourInspectionBO.setOrdersBd("create_time",false);
        BasePage<BLabourInspectionBO> adminMessageBasePage = getBaseMapper().selectDataList(bLabourInspectionBO.parse(),bLabourInspectionBO);
        return adminMessageBasePage;
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String add(BLabourInspection bLabourInspection)
    {
        String id = UuidUtils.generateUuid().replace("-","");
        bLabourInspection.setId(id);
        bLabourInspection.setCreateTime(new Date());
        bLabourInspection.setInspectionType("0");
        save(bLabourInspection);
        SupervisionEnterprise supervisionEnterprise =new SupervisionEnterprise();
        String projectId = String.valueOf(UserUtil.getUser().getProjectId());
        supervisionEnterprise.setProjectId(Integer.valueOf(projectId));
        List<SupervisionEnterprise> list = supervisionEnterpriseService.selectByProjectId(supervisionEnterprise);
        String ids="";
        if(list.size()>0){
            ids=list.get(0).getSupe();
        }
       try{
            if(!"".equals(ids)){
                List<Long> idsList =new ArrayList<Long>();
                StringTokenizer st = new StringTokenizer(ids, ",");
                String l = null;
                while (st.hasMoreElements()) {
                    l = st.nextToken();
                    idsList.add(Long.valueOf(l));
                }
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setByUser(UserUtil.getUserId());
                messageEntity.setTitle(bLabourInspection.getRemarks()+"报验申请");
                messageEntity.setData(id);
                messageEntity.setToUsers(idsList);
                messageEntity.setCode(MessageType.ENTERPRISE_SUBMISSION);
                messageFeignService.send(messageEntity);
            }
        }catch (Exception e){
           e.printStackTrace();
            log.error("socket:系统公告推送失败");
        }
        return  "1";
    }

    /**
     * 修改数据
     *
     */
    @Override
    public String edit(BLabourInspection bLabourInspection)
    {
        bLabourInspection.setUpdateTime(new Date());
        updateById(bLabourInspection);
        return  "1";
    }

    public Map<String, Object> inspectionStatistical(){
        Map<String, Object> data =new HashMap<String, Object>();
        Map<String, Object> inspectionStatistical =new HashMap<String, Object>();
        List<Map<String, Object>> inspectionStatisticalHistogram =new ArrayList<Map<String, Object>>();
        Map<String, Object> params = new HashMap<>();
        params.put("tenantId",UserUtil.getUser().getTenantId());
        inspectionStatistical =  getBaseMapper().inspectionStatistical(params);
        inspectionStatisticalHistogram =  getBaseMapper().inspectionStatisticalHistogram(params);
        data.put("inspectionStatistical",inspectionStatistical);
        data.put("inspectionStatisticalHistogram",inspectionStatisticalHistogram);
        return data;
    }

    @Override
    public BasePage<Map> selectApplyList(BLabourInspectionBO bLabourInspectionBO) {
        bLabourInspectionBO.setOrdersBd("a.create_time",false);
        Long userId = UserUtil.getUserId();
        BasePage<Map> list = getBaseMapper().selectApplyList(bLabourInspectionBO.parse(),bLabourInspectionBO,userId);
        return list;
    }

    @Override
    public Map<String, Object> selectApplyAndProjectInfo(String id) {
        Map<String,Object> data = getBaseMapper().selectApplyAndProjectInfo(id);
        return data;
    }

    @Override
    public BasePage<Map> selectAllDataList(BLabourInspectionBO bLabourInspectionBO) {
        bLabourInspectionBO.setOrdersBd("a.create_time",false);
        BasePage<Map> list = getBaseMapper().selectAllDataList(bLabourInspectionBO.parse(),bLabourInspectionBO);
        return list;
    }
}
