package com.kakarote.build.labour.apipm.service.impl;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.labour.apipm.service.SubcontractorDataService;
import com.kakarote.build.labour.entity.PO.*;
import com.kakarote.build.labour.mapper.LabourSubcontractorGrpMapper;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.build.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 分包商数据接取外放接口
 * </p>
 *
 * @author author
 * @since 2022-02-18
 */
@Service
public class SubcontractorDataServiceImpl extends BaseServiceImpl<LabourSubcontractorGrpMapper, LabourSubcontractor> implements SubcontractorDataService {



    @Autowired
    private ApiUtils apiUtils;


    @Override
    public Result  subcontractorAdd(Map<String, Object> parpam){
        try {
            LabourSubcontractor labourSubcontractor= new LabourSubcontractor();
            Map<String, Object> data = new HashMap<String, Object>();
            ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
            labourSubcontractor.setProjectId(projectInfo.getId());
            labourSubcontractor.setTenantId(projectInfo.getTenantId());
            if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
                data = (Map<String, Object>) parpam.get("data");
                if (data.get("corpCode") == null || "".equals(data.get("corpCode"))) {
                    throw new CrmException(500, "分包商统一社会信用代码不能为空");
                }
                labourSubcontractor.setCorpCode((String)data.get("corpCode"));
                if (data.get("realName") == null || "".equals(data.get("realName"))) {
                    throw new CrmException(500, "分包商名称不能为空");
                }
                labourSubcontractor.setRealName((String)data.get("realName"));
                if (data.get("shortName") == null || "".equals(data.get("shortName"))) {
                    throw new CrmException(500, "分包商简称不能为空");
                }
                labourSubcontractor.setShortName((String)data.get("shortName"));
                if (data.get("subcontractorType") == null || "".equals(data.get("subcontractorType"))) {
                    throw new CrmException(500, "分包商类型不能为空");
                }
                labourSubcontractor.setSubcontractorType((String)data.get("subcontractorType"));
                if (data.get("province") == null || "".equals(data.get("province"))) {
                    throw new CrmException(500, "分包商所属省编号不能为空");
                }
                labourSubcontractor.setProvince(Integer.valueOf((String) data.get("province")));
                if (data.get("city") == null || "".equals(data.get("city"))) {
                    throw new CrmException(500, "分包商所属市编号不能为空");
                }
                labourSubcontractor.setCity(Integer.valueOf((String) data.get("city")));
                if (data.get("district") == null || "".equals(data.get("district"))) {
                    throw new CrmException(500, "分包商所属区编号不能为空");
                }
                labourSubcontractor.setDistrict(Integer.valueOf((String) data.get("district")));
                if (data.get("corpType") == null || "".equals(data.get("corpType"))) {
                    throw new CrmException(500, "公司类型/参建单位类型不能为空");
                }
                labourSubcontractor.setCorpType((String)data.get("corpType"));
                labourSubcontractor.setOverallMerit((String)data.get("overallMerit"));
                if (data.get("isConformity") == null || "".equals(data.get("isConformity"))) {
                    throw new CrmException(500, "是否生效不能为空");
                }
                labourSubcontractor.setIsConformity((String)data.get("isConformity"));
                if (data.get("unitAddress") == null || "".equals(data.get("unitAddress"))) {
                    throw new CrmException(500, "单位地址不能为空");
                }
                labourSubcontractor.setUnitAddress((String)data.get("unitAddress"));
                if (data.get("legalRepresentative") == null || "".equals(data.get("legalRepresentative"))) {
                    throw new CrmException(500, "法人名称不能为空");
                }
                labourSubcontractor.setLegalRepresentative((String)data.get("legalRepresentative"));
                labourSubcontractor.setLegalRepresentativePhone((String)data.get("legalRepresentativePhone"));
                if (data.get("registeredCapital") == null || "".equals(data.get("registeredCapital"))) {
                    throw new CrmException(500, "注册资金不能为空");
                }
                labourSubcontractor.setRegisteredCapital(new BigDecimal((String)data.get("registeredCapital")));
                if (data.get("regdate") != null && !"".equals(data.get("regdate"))) {
                    String regdate=(String)data.get("regdate");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                    labourSubcontractor.setRegdate(dft.parse(regdate));
                }
                if (data.get("principal") == null || "".equals(data.get("principal"))) {
                    throw new CrmException(500, "联系人不能为空");
                }
                labourSubcontractor.setPrincipal((String)data.get("principal"));
                labourSubcontractor.setPrincipaltel((String)data.get("principaltel"));
                labourSubcontractor.setIdCard((String)data.get("idCard"));
                labourSubcontractor.setSubject((String)data.get("subject"));
                if (data.get("nameSpell") == null || "".equals(data.get("nameSpell"))) {
                    throw new CrmException(500, "公司简称首拼不能为空");
                }
                labourSubcontractor.setNameSpell((String)data.get("nameSpell"));
                labourSubcontractor.setBuildComplaintCall((String)data.get("buildComplaintCall"));
                labourSubcontractor.setSocietyComplaintCall((String)data.get("societyComplaintCall"));
                labourSubcontractor.setCompanyScore((String)data.get("companyScore"));
                labourSubcontractor.setCompanySummary((String)data.get("companySummary"));
                save(labourSubcontractor);
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok(labourSubcontractor.getId());
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("分包商数据上传接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("分包商数据上传接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分包商数据上传接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

    @Override
    public Result  subcontractorEdit(Map<String, Object> parpam){
        try {
            LabourSubcontractor labourSubcontractor= new LabourSubcontractor();
            Map<String, Object> data = new HashMap<String, Object>();
            ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
            labourSubcontractor.setProjectId(projectInfo.getId());
            labourSubcontractor.setTenantId(projectInfo.getTenantId());
            if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
                data = (Map<String, Object>) parpam.get("data");
                if (data.get("id") == null || "".equals(data.get("id"))) {
                    throw new CrmException(500, "数据修改主题ID不能为空");
                }
                LabourSubcontractor labourSubcontractorData = getBaseMapper().selectById2(Integer.valueOf((String) data.get("id")),projectInfo.getTenantId());
                if(labourSubcontractorData == null){
                    throw new CrmException(500, "未查询到修改数据的主体，请确认传输的id是否正确");
                }
                labourSubcontractor.setId(Integer.valueOf((String) data.get("id")));
                if(data.containsKey("corpCode")){
                    if (data.get("corpCode") == null || "".equals(data.get("corpCode"))) {
                        throw new CrmException(500, "分包商统一社会信用代码不能为空");
                    }
                    labourSubcontractor.setCorpCode((String)data.get("corpCode"));
                }
                if(data.containsKey("realName")){
                    if (data.get("realName") == null || "".equals(data.get("realName"))) {
                        throw new CrmException(500, "分包商名称不能为空");
                    }
                    labourSubcontractor.setRealName((String)data.get("realName"));
                }
                if(data.containsKey("shortName")){
                    if (data.get("shortName") == null || "".equals(data.get("shortName"))) {
                        throw new CrmException(500, "分包商简称不能为空");
                    }
                    labourSubcontractor.setShortName((String)data.get("shortName"));
                }
                if(data.containsKey("subcontractorType")){
                    if (data.get("subcontractorType") == null || "".equals(data.get("subcontractorType"))) {
                        throw new CrmException(500, "分包商类型不能为空");
                    }
                    labourSubcontractor.setSubcontractorType((String)data.get("subcontractorType"));
                }
                if(data.containsKey("province")){
                    if (data.get("province") == null || "".equals(data.get("province"))) {
                        throw new CrmException(500, "分包商所属省编号不能为空");
                    }
                    labourSubcontractor.setProvince(Integer.valueOf((String) data.get("province")));
                }
                if(data.containsKey("city")){
                    if (data.get("city") == null || "".equals(data.get("city"))) {
                        throw new CrmException(500, "分包商所属市编号不能为空");
                    }
                    labourSubcontractor.setCity(Integer.valueOf((String) data.get("city")));
                }
                if(data.containsKey("district")){
                    if (data.get("district") == null || "".equals(data.get("district"))) {
                        throw new CrmException(500, "分包商所属区编号不能为空");
                    }
                    labourSubcontractor.setDistrict(Integer.valueOf((String) data.get("district")));
                }
                if(data.containsKey("corpType")){
                    if (data.get("corpType") == null || "".equals(data.get("corpType"))) {
                        throw new CrmException(500, "公司类型/参建单位类型不能为空");
                    }
                    labourSubcontractor.setCorpType((String)data.get("corpType"));
                }
                if(data.containsKey("overallMerit")){
                    labourSubcontractor.setOverallMerit((String)data.get("overallMerit"));
                }
                if(data.containsKey("isConformity")){
                    if (data.get("isConformity") == null || "".equals(data.get("isConformity"))) {
                        throw new CrmException(500, "是否生效不能为空");
                    }
                    labourSubcontractor.setIsConformity((String)data.get("isConformity"));
                }
                if(data.containsKey("unitAddress")){
                    if (data.get("unitAddress") == null || "".equals(data.get("unitAddress"))) {
                        throw new CrmException(500, "单位地址不能为空");
                    }
                    labourSubcontractor.setUnitAddress((String)data.get("unitAddress"));
                }
                if(data.containsKey("legalRepresentative")){
                    if (data.get("legalRepresentative") == null || "".equals(data.get("legalRepresentative"))) {
                        throw new CrmException(500, "法人名称不能为空");
                    }
                    labourSubcontractor.setLegalRepresentative((String)data.get("legalRepresentative"));
                }
                if(data.containsKey("legalRepresentativePhone")){
                    labourSubcontractor.setLegalRepresentativePhone((String)data.get("legalRepresentativePhone"));
                }
                if(data.containsKey("registeredCapital")){
                    if (data.get("registeredCapital") == null || "".equals(data.get("registeredCapital"))) {
                        throw new CrmException(500, "注册资金不能为空");
                    }
                    labourSubcontractor.setRegisteredCapital(new BigDecimal((String)data.get("registeredCapital")));
                }
                if(data.containsKey("regdate")){
                    if (data.get("regdate") != null && !"".equals(data.get("regdate"))) {
                        String regdate=(String)data.get("regdate");
                        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                        labourSubcontractor.setRegdate(dft.parse(regdate));
                    }
                }
                if(data.containsKey("principal")){
                    if (data.get("principal") == null || "".equals(data.get("principal"))) {
                        throw new CrmException(500, "联系人不能为空");
                    }
                    labourSubcontractor.setPrincipal((String)data.get("principal"));
                }
                if(data.containsKey("principaltel")){
                    labourSubcontractor.setPrincipaltel((String)data.get("principaltel"));
                }
                if(data.containsKey("idCard")){
                    labourSubcontractor.setIdCard((String)data.get("idCard"));
                }
                if(data.containsKey("subject")){
                    labourSubcontractor.setSubject((String)data.get("subject"));
                }
                if(data.containsKey("nameSpell")){
                    if (data.get("nameSpell") == null || "".equals(data.get("nameSpell"))) {
                        throw new CrmException(500, "公司简称首拼不能为空");
                    }
                    labourSubcontractor.setNameSpell((String)data.get("nameSpell"));
                }
                if(data.containsKey("buildComplaintCall")){
                    labourSubcontractor.setBuildComplaintCall((String)data.get("buildComplaintCall"));
                }
                if(data.containsKey("societyComplaintCall")){
                    labourSubcontractor.setSocietyComplaintCall((String)data.get("societyComplaintCall"));
                }
                if(data.containsKey("companyScore")){
                    labourSubcontractor.setCompanyScore((String)data.get("companyScore"));
                }
                if(data.containsKey("companySummary")){
                    labourSubcontractor.setCompanySummary((String)data.get("companySummary"));
                }
                updateById(labourSubcontractor);
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok();
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("分包商数据修改接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("分包商数据修改接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分包商数据修改接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

    @Override
    public Result  subcontractorDelete(Map<String, Object> parpam){
        try {
            LabourSubcontractor labourSubcontractor= new LabourSubcontractor();
            Map<String, Object> data = new HashMap<String, Object>();
            ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
            if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
                data = (Map<String, Object>) parpam.get("data");
                if (data.get("id") == null || "".equals(data.get("id"))) {
                    throw new CrmException(500, "数据删除主体ID不能为空");
                }
                LabourSubcontractor labourSubcontractorData = getBaseMapper().selectById2(Integer.valueOf((String) data.get("id")),projectInfo.getTenantId());
                if(labourSubcontractorData == null){
                    throw new CrmException(500, "未查询到删除数据的主体，请确认传输的id是否正确");
                }
                getBaseMapper().deleteDetails(Integer.valueOf((String) data.get("id")),projectInfo.getTenantId());
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok();
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("分包商数据删除接口错误：" + e);
            throw e;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("分包商数据删除接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

    @Override
    public Result  subcontractorGet(Map<String, Object> parpam){
        try {
            List<LabourSubcontractor> labourSubcontractor= new ArrayList<LabourSubcontractor>();
            Map<String, Object> data = new HashMap<String, Object>();
            Map<String, Object> parpamData = new HashMap<String, Object>();
            ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
            int  currentPage=1;
            int  pageSize=10;
            int  pagingState=0;
            int  pagingEnd=10;
            if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
                data = (Map<String, Object>) parpam.get("data");
                if(data.containsKey("id")){
                    parpamData.put("id",parpam.get("id"));
                }
                if(data.containsKey("currentPage")){ // 可选择第几页
                    if(data.get("currentPage") != null && !"".equals(data.get("currentPage"))){
                        currentPage=Integer.valueOf((String)data.get("currentPage"));
                    }
                }
                if(data.containsKey("pageSize")){  // 可选择每页多少条
                    if(data.get("pageSize") != null && !"".equals(data.get("pageSize"))){
                        pageSize= Integer.valueOf((String)data.get("pageSize"));
                    }
                }
                // 分页处理
                pagingEnd=currentPage*pageSize;
                pagingState=pagingEnd-pageSize;
                parpamData.put("pagingStart",pagingState);
                parpamData.put("pagingEnd",pagingEnd);
                parpamData.put("projectId",projectInfo.getProjectId());
                parpamData.put("tenantId",projectInfo.getTenantId());
                labourSubcontractor = getBaseMapper().subcontractorGet(parpamData);

            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok(labourSubcontractor);
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("分包商数据查询接口错误：" + e);
            throw e;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("分包商数据查询接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }
}
