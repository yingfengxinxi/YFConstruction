package com.kakarote.build.labour.apipm.service.impl;

import com.kakarote.build.labour.apipm.service.WorkerService;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.mapper.LabourInfocollectionMapper;
import com.kakarote.build.labour.mapper.LabourTeamMapper;
import com.kakarote.build.labour.service.IBLabourAttendanceRecordService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.utils.ApiUtils;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.config.MybatisPlusConfig;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkerServiceImpl extends BaseServiceImpl<LabourInfocollectionMapper, LabourInfocollection> implements WorkerService {

    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private IBLabourAttendanceRecordService iBLabourAttendanceRecordService;

    @Autowired
    private LabourTeamMapper labourTeamMapper;

    public final static String PATH="E:\\tpcf\\";

    @Override
    public Result postWorker(Map<String, Object> parpam) {
        Map<String, Object> data;
        ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
        if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
            data = (Map<String, Object>) parpam.get("data");
            List<Map> list = (List)data.get("workerList");
            if (data.get("workerList") == null || list.size()<=0) {
                throw new CrmException(500, "人员列表数据不能为空");
            }
            for(Map map:list){
                LabourInfocollection infocollection = new LabourInfocollection();
                infocollection.setProjectId(projectInfo.getProjectId());
                infocollection.setTenantId(projectInfo.getTenantId());
                if (data.get("teamId") == null || "".equals(data.get("teamId"))) {
                    throw new CrmException(500, "班组ID不能为空");
                }
                infocollection.setTeamName((Integer) data.get("teamId"));
                if (map.get("workerName") == null || "".equals(map.get("workerName"))) {
                    throw new CrmException(500, "工人姓名不能为空");
                }
                infocollection.setName((String) map.get("workerName"));
                if (map.get("isTeamLeader") == null || "".equals(map.get("isTeamLeader"))) {
                    throw new CrmException(500, "是否班组长不能为空");
                }
                infocollection.setIfTeamLeader((String) map.get("isTeamLeader"));
                if (map.get("idCardNumber") == null || "".equals(map.get("idCardNumber"))) {
                    throw new CrmException(500, "身份证号不能为空");
                }
                infocollection.setIdCard((String) map.get("idCardNumber"));
                if (map.get("workRole") == null || "".equals(map.get("workRole"))) {
                    throw new CrmException(500, "人员类型不能为空");
                }
                infocollection.setWorkRole((String) map.get("workRole"));
                if (map.get("workType") == null || "".equals(map.get("workType"))) {
                    throw new CrmException(500, "工种不能为空");
                }
                infocollection.setWorkTypeId((Integer) map.get("workType"));
                if (map.get("gender") == null || "".equals(map.get("gender"))) {
                    throw new CrmException(500, "性别不能为空");
                }
                infocollection.setSex((String) map.get("gender"));
                if (map.get("birthday") == null || "".equals(map.get("birthday"))) {
                    throw new CrmException(500, "出生日期不能为空");
                }
                infocollection.setBirthday((String) map.get("birthday"));
                infocollection.setAttendanceNumber((String) map.get("cardNumber"));
                infocollection.setBankLinkNumber((String) map.get("payRollBankCardNu"));
                infocollection.setPayRollBankName((String) map.get("payRollBankName"));
                if (map.get("nation") == null || "".equals(map.get("nation"))) {
                    throw new CrmException(500, "民族不能为空");
                }
                infocollection.setNation((String) map.get("nation"));
                if (map.get("address") == null || "".equals(map.get("address"))) {
                    throw new CrmException(500, "住址不能为空");
                }
                infocollection.setAddress((String) map.get("address"));
                if (map.get("callPhone") == null || "".equals(map.get("callPhone"))) {
                    throw new CrmException(500, "工人电话不能为空");
                }
                infocollection.setPhone((String) map.get("address"));
                if (map.get("grantOrg") == null || "".equals(map.get("grantOrg"))) {
                    throw new CrmException(500, "发证机关不能为空");
                }
                infocollection.setIssuingAuthority((String) map.get("grantOrg"));
                if (map.get("startDate") == null || "".equals(map.get("startDate"))) {
                    throw new CrmException(500, "证件有效期起不能为空");
                }
                infocollection.setDateStart((String) map.get("startDate"));
                if (map.get("expiryDate") == null || "".equals(map.get("expiryDate"))) {
                    throw new CrmException(500, "证件有效期止不能为空");
                }
                String entryBathid = UUID.randomUUID().toString().replaceAll("-", "");
                if(map.get("headImage") != null && !"".equals(map.get("headImage"))){
                    String entryAttachments =(String)map.get("headImage");
                    StringTokenizer st = new StringTokenizer(entryAttachments, "||");
                    String l = null;
                    List<String> t = new ArrayList<String>();
                    while (st.hasMoreElements()) {
                        l = st.nextToken();
                        t.add(l);
                    }
                    for(int i=0;i<t.size();i++){
                        Map<String, Object> fileData = new HashMap<String, Object>();
                        String imgName= UUID.randomUUID().toString().replaceAll("-", "");
                        String path="";
                        Long size=0L;
                        String file=t.get(i);
                        file = file.replaceAll(" ", "+");
                        Map<String,Object> Base64Data =  apiUtils.convertBase64ToFile(file,PATH,imgName);
                        if((Base64Data.get("imgName") == null || "".equals(Base64Data.get("imgName")))){
                            labourTeamMapper.deleteFileData(entryBathid,projectInfo.getTenantId());
                            throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                        }
                        imgName=(String)Base64Data.get("imgName");
                        if(Base64Data.get("path")  == null || "".equals(Base64Data.get("path"))){
                            labourTeamMapper.deleteFileData(entryBathid,projectInfo.getTenantId());
                            throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                        }
                        path=(String)Base64Data.get("path");
                        if(Base64Data.get("size") == null || "".equals(Base64Data.get("size"))){
                            labourTeamMapper.deleteFileData(entryBathid,projectInfo.getTenantId());
                            throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                        }
                        size=(Long)Base64Data.get("size");
                        fileData.put("fileId",MybatisPlusConfig.getNextId() + "");
                        fileData.put("name",imgName);
                        fileData.put("size",size);
                        fileData.put("createUserId","11111");
                        fileData.put("createTime","2022-02-23 12:00:00");
                        fileData.put("path",path);
                        fileData.put("fileType","img");
                        fileData.put("type","1");
                        fileData.put("source","");
                        fileData.put("isPublic","0");
                        fileData.put("batchId",entryBathid);
                        fileData.put("tenantId",projectInfo.getTenantId());
                        labourTeamMapper.saveFile(fileData);
                    }
                    infocollection.setBatchId(entryBathid);
                }
                infocollection.setDateEnd((String) map.get("expiryDate"));
                save(infocollection);
            }
        }else{
            throw new CrmException(500, "您未传输数据主体！");
        }
        return Result.ok();
    }

    @Override
    public Result postWorkerAdvance(Map<String, Object> parpam) {
        Map<String, Object> data;
        ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
        if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
            data = (Map<String, Object>) parpam.get("data");
            List<Map> list = (List)data.get("workerList");
            if (data.get("workerList") == null || list.size()<=0) {
                throw new CrmException(500, "人员列表数据不能为空");
            }
            if (data.get("corpCode") == null || "".equals(data.get("corpCode"))) {
                throw new CrmException(500, "参建单位统一社会信用代码不能为空");
            }
            if (data.get("teamId") == null || "".equals(data.get("teamId"))) {
                throw new CrmException(500, "班组ID不能为空");
            }
            for(Map map:list){
                BLabourAttendanceRecord labourAttendanceRecord = new BLabourAttendanceRecord();
                labourAttendanceRecord.setProjectId(projectInfo.getProjectId());
                labourAttendanceRecord.setTenantId(projectInfo.getTenantId());
                labourAttendanceRecord.setAttendanceRecordTeamName((String)data.get("teamId"));
                if (map.get("idCardNumber") == null || "".equals(map.get("idCardNumber"))) {
                    throw new CrmException(500, "工人身份证号不能为空");
                }
                LabourInfocollection info =  getBaseMapper().getByIdCard((String)map.get("idCardNumber"),projectInfo.getProjectId(),projectInfo.getTenantId());
                if(info == null){
                    throw new CrmException(500, "未查询到有工人信息");
                }
                labourAttendanceRecord.setInfocollectionId(String.valueOf(info.getId()));
                labourAttendanceRecord.setInfocollectionName(info.getName());
                labourAttendanceRecord.setInfocollectionCard((String)map.get("idCardNumber"));
                labourAttendanceRecord.setAttendanceNumber(info.getAttendanceNumber());
                labourAttendanceRecord.setDataGeneration("2");
                if (map.get("date") == null || "".equals(map.get("date"))) {
                    throw new CrmException(500, "进退场时间不能为空");
                }
                labourAttendanceRecord.setClockTime(DateUtil.parseDate((String)map.get("date"),DateUtil.YMDHMS_));
                if (map.get("type") == null || "".equals(map.get("type"))) {
                    throw new CrmException(500, "人员类型不能为空");
                }

                if (map.get("entryType") == null || "".equals(map.get("entryType"))) {
                    throw new CrmException(500, "类型不能为空");
                }
                labourAttendanceRecord.setClockDirection((String)map.get("entryType"));
                iBLabourAttendanceRecordService.save(labourAttendanceRecord);
            }
        }else{
            throw new CrmException(500, "您未传输数据主体！");
        }
        return Result.ok();
    }

    @Override
    public Result putWorkerContract(Map<String, Object> parpam) {
        Map<String, Object> data;
        ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
        if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
            data = (Map<String, Object>) parpam.get("data");
            List<Map> list = (List)data.get("contractList");
            if (data.get("contractList") == null || list.size()<=0) {
                throw new CrmException(500, "人员列表数据不能为空");
            }
            if (data.get("corpCode") == null || "".equals(data.get("corpCode"))) {
                throw new CrmException(500, "参建单位统一社会信用代码不能为空");
            }
            for(Map map:list){
                if (map.get("idCardNumber") == null || "".equals(map.get("idCardNumber"))) {
                    throw new CrmException(500, "工人身份证号不能为空");
                }
                LabourInfocollection info =  getBaseMapper().getByIdCard((String)map.get("idCardNumber"),projectInfo.getProjectId(),projectInfo.getTenantId());
                if (map.get("contractPeriodType") == null || "".equals(map.get("contractPeriodType"))) {
                    throw new CrmException(500, "合同类型不能为空");
                }
                info.setContractPeriodType((String)map.get("contractPeriodType"));
                if (map.get("startDate") == null || "".equals(map.get("startDate"))) {
                    throw new CrmException(500, "生效日期不能为空");
                }
                info.setEffectiveDate((String)map.get("startDate"));
                if (map.get("endDate") == null || "".equals(map.get("endDate"))) {
                    throw new CrmException(500, "失效日期不能为空");
                }
                info.setExpirationDate((String)map.get("endDate"));
                String entryBathid = UUID.randomUUID().toString().replaceAll("-", "");
                if(map.get("attachments") != null && !"".equals(map.get("attachments"))){
                    String entryAttachments =(String)map.get("attachments");
                    StringTokenizer st = new StringTokenizer(entryAttachments, "||");
                    String l = null;
                    List<String> t = new ArrayList<String>();
                    while (st.hasMoreElements()) {
                        l = st.nextToken();
                        t.add(l);
                    }
                    for(int i=0;i<t.size();i++){
                        Map<String, Object> fileData = new HashMap<String, Object>();
                        String imgName= UUID.randomUUID().toString().replaceAll("-", "");
                        String path="";
                        Long size=0L;
                        String file=t.get(i);
                        file = file.replaceAll(" ", "+");
                        Map<String,Object> Base64Data =  apiUtils.convertBase64ToFile(file,PATH,imgName);
                        if((Base64Data.get("imgName") == null || "".equals(Base64Data.get("imgName")))){
                            labourTeamMapper.deleteFileData(entryBathid,projectInfo.getTenantId());
                            throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                        }
                        imgName=(String)Base64Data.get("imgName");
                        if(Base64Data.get("path")  == null || "".equals(Base64Data.get("path"))){
                            labourTeamMapper.deleteFileData(entryBathid,projectInfo.getTenantId());
                            throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                        }
                        path=(String)Base64Data.get("path");
                        if(Base64Data.get("size") == null || "".equals(Base64Data.get("size"))){
                            labourTeamMapper.deleteFileData(entryBathid,projectInfo.getTenantId());
                            throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                        }
                        size=(Long)Base64Data.get("size");
                        fileData.put("fileId",MybatisPlusConfig.getNextId() + "");
                        fileData.put("name",imgName);
                        fileData.put("size",size);
                        fileData.put("createUserId","11111");
                        fileData.put("createTime","2022-02-23 12:00:00");
                        fileData.put("path",path);
                        fileData.put("fileType","img");
                        fileData.put("type","1");
                        fileData.put("source","");
                        fileData.put("isPublic","0");
                        fileData.put("batchId",entryBathid);
                        fileData.put("tenantId",projectInfo.getTenantId());
                        labourTeamMapper.saveFile(fileData);
                    }
                    info.setAttachments(entryBathid);
                }
                getBaseMapper().updateWorkerContract(info);
            }
        }else{
            throw new CrmException(500, "您未传输数据主体！");
        }
        return Result.ok();
    }
}
