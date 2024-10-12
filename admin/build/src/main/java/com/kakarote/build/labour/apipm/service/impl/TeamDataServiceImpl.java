package com.kakarote.build.labour.apipm.service.impl;

import cn.hutool.core.date.DateException;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.labour.apipm.service.ITeamDataService;
import com.kakarote.build.labour.entity.BO.LabourTeamBO;
import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.build.labour.entity.PO.LabourTeam;
import com.kakarote.build.labour.mapper.LabourTeamMapper;
import com.kakarote.build.labour.service.ILabourSubcontractorService;
import com.kakarote.build.labour.service.ILabourTeamService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.utils.ApiUtils;
import com.kakarote.core.common.Result;
import com.kakarote.core.config.MybatisPlusConfig;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 * 劳务管理--班组 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2021-03-19
 */
@Service
public class TeamDataServiceImpl extends BaseServiceImpl<LabourTeamMapper, LabourTeam> implements ITeamDataService {

    @Autowired
    private ApiUtils apiUtils;
    @Autowired
    private ILabourSubcontractorService subcontractorService;

    public static String PATH="E:\\tpcf\\";


    @Override
    public Result teamDataAdd(Map<String, Object> parpam){
        try {
            LabourTeam labourTeam= new LabourTeam();
            Map<String, Object> data = new HashMap<String, Object>();
            Map<String, Object> parpamData = new HashMap<String, Object>();
            ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
            labourTeam.setProjectId(projectInfo.getId());
            labourTeam.setTenantId(projectInfo.getTenantId());
            parpamData.put("projectId",projectInfo.getId());
            parpamData.put("tenantId",projectInfo.getTenantId());
            String entryBathid=UUID.randomUUID().toString().replaceAll("-", "");
            String exitBathid=UUID.randomUUID().toString().replaceAll("-", "");
            if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
                data = (Map<String, Object>) parpam.get("data");
                if (data.get("teamLeaderName") == null || "".equals(data.get("teamLeaderName"))) {
                    throw new CrmException(500, "班组长名称不能为空");
                }
                labourTeam.setTeamLeaderName((String)data.get("teamLeaderName"));
                if (data.get("idCard") == null || "".equals(data.get("idCard"))) {
                    throw new CrmException(500, "班组长身份证号不能为空");
                }
                labourTeam.setIdCard((String)data.get("idCard"));
                if (data.get("phone") == null || "".equals(data.get("phone"))) {
                    throw new CrmException(500, "班组长联系方式不能为空");
                }
                labourTeam.setPhone((String)data.get("phone"));
                if (data.get("workTypeId") == null || "".equals(data.get("workTypeId"))) {
                    throw new CrmException(500, "工种不能为空");
                }
                labourTeam.setWorkTypeId((String)data.get("workTypeId"));

                if (data.get("teamName") == null || "".equals(data.get("teamName"))) {
                    throw new CrmException(500, "班组名称不能为空");
                }
                labourTeam.setTeamName((String)data.get("teamName"));

                if (data.get("teamSpell") == null || "".equals(data.get("teamSpell"))) {
                    throw new CrmException(500, "班组首拼不能为空");
                }
                labourTeam.setTeamSpell((String)data.get("teamSpell"));

                if (data.get("corpCode") == null || "".equals(data.get("corpCode"))) {
                    throw new CrmException(500, "分包商统一社会信用代码不能为空");
                }
                parpamData.put("corpCode",data.get("corpCode"));
                List<LabourSubcontractor> LabourSubcontractor=  subcontractorService.selcetCorpCodeData(parpamData);
                if(LabourSubcontractor.size()==0){
                    throw new CrmException(500, "未匹配到对应分包商请重新核对统一社会信用代码");
                }
                labourTeam.setSubcontractorId(LabourSubcontractor.get(0).getId());
                labourTeam.setSubcontractorName(LabourSubcontractor.get(0).getRealName());
                if (data.get("entryTime") != null && !"".equals(data.get("entryTime"))) {
                    String entryTime=(String)data.get("entryTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                    labourTeam.setEntryTime(dft.parse(entryTime));
                    if(data.get("entryAttachments") != null && !"".equals(data.get("entryAttachments"))){
                        String entryAttachments =(String)data.get("entryAttachments");
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
                                getBaseMapper().deleteFileData(entryBathid,projectInfo.getTenantId());
                                throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                            }
                            imgName=(String)Base64Data.get("imgName");
                            if(Base64Data.get("path")  == null || "".equals(Base64Data.get("path"))){
                                getBaseMapper().deleteFileData(entryBathid,projectInfo.getTenantId());
                                throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                            }
                            path=(String)Base64Data.get("path");
                            if(Base64Data.get("size") == null || "".equals(Base64Data.get("size"))){
                                getBaseMapper().deleteFileData(entryBathid,projectInfo.getTenantId());
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
                            getBaseMapper().saveFile(fileData);
                        }
                        labourTeam.setEntryAttachments(entryBathid);
                    }
                }
                if (data.get("exitTime") != null && !"".equals(data.get("exitTime"))) {
                    String exitTime=(String)data.get("exitTime");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                    labourTeam.setExitTime(dft.parse(exitTime));
                    if(data.get("exitAttachments") != null && !"".equals(data.get("exitAttachments"))){
                        String entryAttachments =(String)data.get("exitAttachments");
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
                                getBaseMapper().deleteFileData(exitBathid,projectInfo.getTenantId());
                                throw new CrmException(500, "您上传的第"+i+1+"张出场图片Base64码有问题请重新上传");
                            }
                            imgName=(String)Base64Data.get("imgName");
                            if(Base64Data.get("path") == null || "".equals(Base64Data.get("path"))){
                                getBaseMapper().deleteFileData(exitBathid,projectInfo.getTenantId());
                                throw new CrmException(500, "您上传的第"+i+1+"张出场图片Base64码有问题请重新上传");
                            }
                            path=(String)Base64Data.get("path");
                            if(Base64Data.get("size") == null || "".equals(Base64Data.get("size"))){
                                getBaseMapper().deleteFileData(exitBathid,projectInfo.getTenantId());
                                throw new CrmException(500, "您上传的第"+i+1+"张出场图片Base64码有问题请重新上传");
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
                            fileData.put("batchId",exitBathid);
                            fileData.put("tenantId",projectInfo.getTenantId());
                            getBaseMapper().saveFile(fileData);
                        }
                        labourTeam.setExitAttachments(exitBathid);
                    }
                }

                save(labourTeam);
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok(labourTeam);
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("班组数据上传接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("班组数据上传接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("班组数据上传接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

    @Override
    public Result  teamDataEdit(Map<String, Object> parpam){
        try {
            LabourTeam labourTeam= new LabourTeam();
            Map<String, Object> data = new HashMap<String, Object>();
            Map<String, Object> parpamData = new HashMap<String, Object>();
            ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
            labourTeam.setProjectId(projectInfo.getId());
            labourTeam.setTenantId(projectInfo.getTenantId());
            parpamData.put("projectId",projectInfo.getId());
            parpamData.put("tenantId",projectInfo.getTenantId());
            if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
                data = (Map<String, Object>) parpam.get("data");
                if (data.get("id") == null || "".equals(data.get("id"))) {
                    throw new CrmException(500, "数据修改主题ID不能为空");
                }
                LabourTeam labourTeamData = getBaseMapper().selectByIdData(Integer.valueOf((String) data.get("id")),projectInfo.getTenantId(),projectInfo.getId());
                if(labourTeamData == null){
                    throw new CrmException(500, "未查询到修改数据的主体，请确认传输的id是否正确");
                }
                labourTeam.setId(Integer.valueOf((String) data.get("id")));
                if(data.containsKey("teamLeaderName")){
                    if (data.get("teamLeaderName") == null || "".equals(data.get("teamLeaderName"))) {
                        throw new CrmException(500, "班组长名称不能为空");
                    }
                    labourTeam.setTeamLeaderName((String)data.get("teamLeaderName"));
                }
                if(data.containsKey("idCard")){
                    if (data.get("idCard") == null || "".equals(data.get("idCard"))) {
                        throw new CrmException(500, "班组长身份证号不能为空");
                    }
                    labourTeam.setIdCard((String)data.get("idCard"));
                }
                if(data.containsKey("phone")){
                    if (data.get("phone") == null || "".equals(data.get("phone"))) {
                        throw new CrmException(500, "班组长联系方式不能为空");
                    }
                    labourTeam.setPhone((String)data.get("phone"));
                }

                if(data.containsKey("workTypeId")){
                    if (data.get("workTypeId") == null || "".equals(data.get("workTypeId"))) {
                        throw new CrmException(500, "工种不能为空");
                    }
                    labourTeam.setWorkTypeId((String)data.get("workTypeId"));
                }
                if(data.containsKey("teamName")){
                    if (data.get("teamName") == null || "".equals(data.get("teamName"))) {
                        throw new CrmException(500, "班组名称不能为空");
                    }
                    labourTeam.setTeamName((String)data.get("teamName"));
                }
                if(data.containsKey("teamSpell")){
                    if (data.get("teamSpell") == null || "".equals(data.get("teamSpell"))) {
                        throw new CrmException(500, "班组首拼不能为空");
                    }
                    labourTeam.setTeamSpell((String)data.get("teamSpell"));
                }
                if(data.containsKey("corpCode")){
                    if (data.get("corpCode") == null || "".equals(data.get("corpCode"))) {
                        throw new CrmException(500, "分包商统一社会信用代码不能为空");
                    }
                    parpamData.put("corpCode",data.get("corpCode"));
                    List<LabourSubcontractor> LabourSubcontractor=  subcontractorService.selcetCorpCodeData(parpamData);
                    if(LabourSubcontractor.size()==0){
                        throw new CrmException(500, "未匹配到对应分包商请重新核对统一社会信用代码");
                    }
                    labourTeam.setSubcontractorId(LabourSubcontractor.get(0).getId());
                    labourTeam.setSubcontractorName(LabourSubcontractor.get(0).getRealName());
                }

                if(data.containsKey("entryTime")){  // 修改进场时间
                    if (data.get("entryTime") != null && !"".equals(data.get("entryTime"))) {
                        String entryTime=(String)data.get("entryTime");
                        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                        labourTeam.setEntryTime(dft.parse(entryTime));
                    }
                }
                if(data.containsKey("entryAttachments")){  // 修改进场图片附件
                    String falg="0";
                    if(labourTeamData.getEntryTime()!=null){
                        falg="1";
                    }else if(data.get("entryTime") != null && !"".equals(data.get("entryTime"))){
                        falg="1";
                    }
                    if("1".equals(falg)){
                        if(data.get("entryAttachments") != null && !"".equals(data.get("entryAttachments"))){
                            if(data.containsKey("entryAttachmentsInfo")){ // 是否移除原上传图片 0 不移除原上传图片  1移除原上传图片
                                String entryAttachmentsInfo=(String)data.get("entryAttachmentsInfo");
                                if("1".equals(entryAttachmentsInfo)){ //
                                    getBaseMapper().deleteFileData(labourTeamData.getEntryAttachments(),projectInfo.getTenantId());
                                }
                            }
                            String entryAttachments =(String)data.get("entryAttachments");
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
                                String file=t.get(i);
                                file = file.replaceAll(" ", "+");
                                Long size=0L;
                                Map<String,Object> Base64Data =  apiUtils.convertBase64ToFile(file,PATH,imgName);
                                if((Base64Data.get("imgName") == null || "".equals(Base64Data.get("imgName")))){
                                    throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                                }
                                imgName=(String)Base64Data.get("imgName");
                                if(Base64Data.get("path") == null || "".equals(Base64Data.get("path"))){
                                    throw new CrmException(500, "您上传的第"+i+1+"张进场图片Base64码有问题请重新上传");
                                }
                                path=(String)Base64Data.get("path");
                                if(Base64Data.get("size") == null || "".equals(Base64Data.get("size"))){
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
                                fileData.put("batchId",labourTeamData.getEntryAttachments());
                                fileData.put("tenantId",projectInfo.getTenantId());
                                getBaseMapper().saveFile(fileData);
                            }
                            labourTeam.setEntryAttachments(labourTeamData.getEntryAttachments());
                        }
                    }
                }
                if(data.containsKey("exitTime")){ // 修改退场时间
                    if (data.get("exitTime") != null && !"".equals(data.get("exitTime"))) {
                        String exitTime=(String)data.get("exitTime");
                        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                        labourTeam.setExitTime(dft.parse(exitTime));
                    }
                }
                if (data.containsKey("exitAttachments")) {// 修改退场图片附件
                    String falg="0";
                    if(labourTeamData.getExitTime()!=null){
                        falg="1";
                    }else if(data.get("exitTime") != null && !"".equals(data.get("exitTime"))){
                        falg="1";
                    }
                    if("1".equals(falg)){
                        if(data.get("exitAttachments") != null && !"".equals(data.get("exitAttachments"))){
                            if(data.containsKey("exitAttachmentsInfo")){ // 是否移除原上传图片 0 不移除原上传图片  1移除原上传图片
                                String exitAttachmentsInfo=(String)data.get("exitAttachmentsInfo");
                                if("1".equals(exitAttachmentsInfo)){ //
                                    getBaseMapper().deleteFileData(labourTeamData.getExitAttachments(),projectInfo.getTenantId());
                                }
                            }
                            String entryAttachments =(String)data.get("exitAttachments");
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
                                String file=t.get(i);
                                file = file.replaceAll(" ", "+");
                                Long size=0L;
                                Map<String,Object> Base64Data =  apiUtils.convertBase64ToFile(file,PATH,imgName);
                                if((Base64Data.get("imgName") == null || "".equals(Base64Data.get("imgName")))){
                                    throw new CrmException(500, "您上传的第"+i+1+"张出场图片Base64码有问题请重新上传");
                                }
                                imgName=(String)Base64Data.get("imgName");
                                if(Base64Data.get("path") == null || "".equals(Base64Data.get("path"))){
                                    throw new CrmException(500, "您上传的第"+i+1+"张出场图片Base64码有问题请重新上传");
                                }
                                path=(String)Base64Data.get("path");
                                if(Base64Data.get("size") == null || "".equals(Base64Data.get("size"))){
                                    throw new CrmException(500, "您上传的第"+i+1+"张出场图片Base64码有问题请重新上传");
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
                                fileData.put("batchId",labourTeamData.getExitAttachments());
                                fileData.put("tenantId",projectInfo.getTenantId());
                                getBaseMapper().saveFile(fileData);
                            }
                            labourTeam.setExitAttachments(labourTeamData.getExitAttachments());
                        }
                    }
                }
                updateById(labourTeam);
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok();
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("班组数据修改接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("班组数据修改接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("班组数据修改接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

    @Override
    public Result  teamDataDelete(Map<String, Object> parpam){
        try {
            LabourSubcontractor labourSubcontractor= new LabourSubcontractor();
            Map<String, Object> data = new HashMap<String, Object>();
            ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
            if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
                data = (Map<String, Object>) parpam.get("data");
                if (data.get("id") == null || "".equals(data.get("id"))) {
                    throw new CrmException(500, "数据删除主体ID不能为空");
                }
                LabourTeam labourTeamData = getBaseMapper().selectByIdData(Integer.valueOf((String) data.get("id")),projectInfo.getTenantId(),projectInfo.getId());
                if(labourTeamData == null){
                    throw new CrmException(500, "未查询到删除数据的主体，请确认传输的id是否正确");
                }
                getBaseMapper().deleteFileData(labourTeamData.getEntryAttachments(),projectInfo.getTenantId());
                getBaseMapper().deleteFileData(labourTeamData.getExitAttachments(),projectInfo.getTenantId());
                getBaseMapper().deleteDetailsGrp(Integer.valueOf((String) data.get("id")),projectInfo.getTenantId(),projectInfo.getId());
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok();
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("班组数据删除接口错误：" + e);
            throw e;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("班组数据删除接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }




}
