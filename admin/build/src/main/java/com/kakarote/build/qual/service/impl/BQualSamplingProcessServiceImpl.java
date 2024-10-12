package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.BQualSamplingProcessBO;
import com.kakarote.build.qual.entity.PO.BQualSamplingProcess;
import com.kakarote.build.qual.mapper.BQualSamplingProcessMapper;
import com.kakarote.build.qual.service.IBQualSamplingProcessService;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 见证取样过程表 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-07-30
 */
@Service
public class BQualSamplingProcessServiceImpl extends BaseServiceImpl<BQualSamplingProcessMapper, BQualSamplingProcess> implements IBQualSamplingProcessService {


    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BQualSamplingProcessBO> selectList(BQualSamplingProcessBO bQualSamplingProcessBO)
    {
        BasePage<BQualSamplingProcessBO> adminMessageBasePage = getBaseMapper().selectDataList(bQualSamplingProcessBO.parse(),bQualSamplingProcessBO);

        return adminMessageBasePage;
    }

    public String add(BQualSamplingProcess bQualSamplingProcess){
        bQualSamplingProcess.setCreateTime(new Date());
        bQualSamplingProcess.setCreateBy(UserUtil.getUser().getRealname());
        bQualSamplingProcess.setOperationTime(new Date());
        bQualSamplingProcess.setOperationUser(UserUtil.getUser().getRealname());
        this.save(bQualSamplingProcess);
        return"";
    }

    public String edit(BQualSamplingProcess bQualSamplingProcess){
        bQualSamplingProcess.setUpdateBy(UserUtil.getUser().getRealname());
        bQualSamplingProcess.setUpdateTime(new Date());
        this.updateById(bQualSamplingProcess);
        return"";
    }

    public String  deletePid(String pid){
        Integer k= getBaseMapper().deletePid(pid);
        return "";
    }

    /**
     * 查询数据列表
     *
     */
    @Override
    public List<BQualSamplingProcess> getByPId(String pid)
    {
        List<BQualSamplingProcess>  list = getBaseMapper().selectDataListPid(pid);
        return list;
    }


}
