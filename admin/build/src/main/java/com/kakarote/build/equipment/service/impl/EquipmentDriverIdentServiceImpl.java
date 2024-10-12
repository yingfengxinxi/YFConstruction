package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.date.DateException;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.equipment.entity.BO.EquipmentDriverIdentBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.equipment.entity.PO.EquipmentDriverIdent;
import com.kakarote.build.equipment.mapper.EquipmentAssetsMapper;
import com.kakarote.build.equipment.mapper.EquipmentDriverIdentMapper;
import com.kakarote.build.equipment.service.IBEquipmentCertificateService;
import com.kakarote.build.equipment.service.IEquipmentAssetsService;
import com.kakarote.build.equipment.service.IEquipmentDriverIdentService;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.labour.mapper.LabourInfocollectionMapper;
import com.kakarote.build.labour.service.ILabourSpeworktypeService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.safety.entity.PO.SafetyBlockAlarm;
import com.kakarote.build.utils.DateUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 机械设备司机识别 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-07-26
 */
@Service
public class EquipmentDriverIdentServiceImpl extends BaseServiceImpl<EquipmentDriverIdentMapper, EquipmentDriverIdent> implements IEquipmentDriverIdentService {

    @Autowired
    private EquipmentAssetsMapper equipmentAssetsMapper;
    @Autowired
    private LabourInfocollectionMapper labourInfocollectionMapper;

    @Autowired
    private IBEquipmentCertificateService iBEquipmentCertificateService;

    @Autowired
    private ILabourSpeworktypeService iLabourSpeworktypeService;
    @Autowired
    private IProjectInfoService projectInfoService;

    @Override
    public BasePage<Map<String, Object>> selectList(EquipmentDriverIdentBO equipmentDriverIdentBO) {
        equipmentDriverIdentBO.setOrdersBd("ending_date",false);
        equipmentDriverIdentBO.setOrdersBd("starting_date",false);
        return getBaseMapper().selectDriverIdentList(equipmentDriverIdentBO.parse(),equipmentDriverIdentBO);
    }

    @Override
    public Map<String, Object> getByIdInApp(Integer id) {
        return getBaseMapper().getByIdInApp(id);
    }

    @Override
    public JSONObject excelImport(MultipartFile file) throws Exception {
        List<EquipmentDriverIdent> listAdd = new ArrayList<>();

        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());

        List<Map<String, Object>> list = reader.read(1, 2, Integer.MAX_VALUE);

        for(int i= 1; i <= list.size(); i++){

            Map<String, Object> map = list.get(i - 1);

            EquipmentDriverIdent equipmentDriverIdent = new EquipmentDriverIdent();

            //设备编码
            Object equiCode = map.get("设备编号(*)");
            if (equiCode == null || equiCode.equals("")) {
                throw new CrmException(1001, "设备编码不能为空");
            }
            String boundaryBlockCode = "";
            if(equiCode instanceof String){
                boundaryBlockCode = (String)equiCode;
            }else if(equiCode instanceof Long){
                Long a = (Long)equiCode;
                boundaryBlockCode = String.valueOf(a);
            }else{
                throw new CrmException(1001, "设备编码数据类型不正确");
            }

            //判断 设备编码是否是【塔吊】、【升降机】
            List<Map> list1 = equipmentAssetsMapper.selectEquipTypeByMap(boundaryBlockCode);
            if(list1.size() == 0){
                throw new CrmException(1001, "设备编码【"+boundaryBlockCode+"】在设备类型【塔吊、升降机】里面不存在");
            }

            //身份证号
            Object idCard = map.get("身份证号(*)");
            if(idCard == null || idCard.equals("")){
                throw new CrmException(1001, "身份证号不能为空");
            }
            String alarmText = "";
            if(idCard instanceof String){
                alarmText = (String)idCard;
            }else if(idCard instanceof Long){
                Long a= (Long)idCard;
                alarmText = String.valueOf(a);
            }else{
                throw new CrmException(1001, "身份证号数据类型不正确");
            }

            //判断 身份证是否在 花名册里面存在
            LabourInfocollection labourInfocollection = new LabourInfocollection();
            labourInfocollection.setIdCard(alarmText);
            LabourInfocollection labourInfocollection1 = labourInfocollectionMapper.selectInfoRecord(labourInfocollection);
            if(labourInfocollection1 == null){
                throw new CrmException(1001, "身份证号【"+alarmText+"】未录入花名册");
            }

            //获取当前用户的所有证书数据 2021-08-06
            List<LabourSpeworktype> labourSpeworktype=   iLabourSpeworktypeService.selectByInfocolId(labourInfocollection1.getId().toString());
            //获取对应的设备类型应存在的证书
            List<BEquipmentCertificate> bEquipmentCertificate = iBEquipmentCertificateService.selectByEquipmentType( list1.get(0).get("typeId").toString());
            String certificateId="";
            //若设备未配置任何证书则不需要对其进行证书验证。
            if(bEquipmentCertificate.size()>0){
                String zhId="";
                String falg="0";

                if(labourSpeworktype.size()==0){
                    throw new CrmException(1001, "身份证号【"+alarmText+"】不存在任何证书");
                } else{
                    //获取当前用户所有的证书数据，并将证书数据组合成一个证书判断串。
                    for(int zs=0;zs<labourSpeworktype.size();zs++){
                      if("".equals(zhId)){
                          zhId=labourSpeworktype.get(zs).getCerCategoryType();
                      }else{
                          zhId=zhId+","+labourSpeworktype.get(zs).getCerCategoryType();
                      }
                    }
                    //获取当前设备所配置的证书id串（在库中用，间隔）
                    String CertificateType = bEquipmentCertificate.get(0).getCertificateType();
                    if (CertificateType != null && !"".equals(CertificateType)) {
                        StringTokenizer st = new StringTokenizer(CertificateType, ",");
                        String l = null;
                        List<String> t = new ArrayList();
                        while (st.hasMoreElements()) {
                            l = st.nextToken();
                            t.add(l);//根据，号分隔证书串为单个证书
                        }
                        if(t.size()!=0){//判断是否成功分隔出对应的单个证书列表
                            for(int zwzs=0;zwzs<t.size();zwzs++){//循环设备的证书列表，
                                //用当前人员的证书串为基础匹配其中是否存在设备需要的证书
                                if(zhId.contains(t.get(zwzs))){
                                    certificateId = t.get(zwzs);
                                    falg="1";//为1时匹配成功直接结束循环
                                    break;
                                }else{
                                    falg="2";//为2时未匹配到则继续循环
                                }
                            }
                        }
                    }

                }
                //最后判断是否匹配到相关证书
                if("2".equals(falg)){
                    throw new CrmException(1001, "身份证号【"+alarmText+"】无申请设备中对应的操作证书");
                }
            }


            //上岗时间
            Object alarmTime1 = map.get("上岗时间(*)");
            if(alarmTime1 == null || alarmTime1.equals("")){
                throw new CrmException(1001, "报警时间不能为空");
            }
            String alarmTime = "";
            if(alarmTime1 instanceof String){
                alarmTime = (String)alarmTime1;
            }else if(alarmTime1 instanceof Date){
                Date date = (Date)alarmTime1;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                alarmTime = simpleDateFormat.format(date);
            }else{
                throw new CrmException(1001, "上岗时间时间格式不正确，应为：yyyy-MM-dd HH:mm:ss");
            }

            boolean legalDate = DateUtil.isLegalDate(alarmTime);
            if(!legalDate){
                throw new CrmException(1001, "上岗时间格式不正确，应为：yyyy-MM-dd HH:mm:ss");
            }

            //下岗时间
            Object alarmTime2 = map.get("下岗时间");
            String endingTime = "";
            if(alarmTime2 != null){
                if(alarmTime2 instanceof String){
                    endingTime = (String)alarmTime2;
                }else if(alarmTime2 instanceof Date){
                    Date date = (Date)alarmTime2;

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                    endingTime = simpleDateFormat.format(date);
                }else{
                    throw new CrmException(1001, "下岗时间时间格式不正确，应为：yyyy-MM-dd HH:mm:ss");
                }

            }

            boolean legalDate2 = DateUtil.isLegalDate(endingTime);
            if(!legalDate2){
                throw new CrmException(1001, "下岗时间格式不正确，应为：yyyy-MM-dd HH:mm:ss");
            }


            equipmentDriverIdent.setEquCode(boundaryBlockCode);

            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
            Date date =  formatter.parse(alarmTime);
            Date parse = formatter.parse(endingTime);

            equipmentDriverIdent.setStartingDate(date);
            equipmentDriverIdent.setEndingDate(parse);
            equipmentDriverIdent.setIdCard(alarmText);
            equipmentDriverIdent.setCertificateId(certificateId);
            listAdd.add(equipmentDriverIdent);

        }

        saveBatch(listAdd);

        JSONObject result = new JSONObject().fluentPut("totalSize", listAdd.size()).fluentPut("errSize", 0);

        return result;
    }

    @Override
    public BasePage<Map<String, Object>> selectListNew(EquipmentDriverIdentBO equipmentDriverIdentBO) {
        equipmentDriverIdentBO.setOrdersBd("ending_date",false);
        equipmentDriverIdentBO.setOrdersBd("starting_date",false);
        return getBaseMapper().selectListNew(equipmentDriverIdentBO.parse(),equipmentDriverIdentBO);
    }

    @Override
    public Result saveData(List<Map<String, Object>> list) {
        try {

            if(list != null && list.size()>0){
                List<EquipmentDriverIdent> dataList = new ArrayList<>();
                for(Map data:list){
                    EquipmentDriverIdent equipmentDriverIdent = new EquipmentDriverIdent();
                    if (data.get("projectId") == null || "".equals(data.get("projectId"))) {
                        throw new CrmException(500, "项目id不能为空");
                    }

                    // 判断项目id是否存在

                    Integer projectId = (Integer) data.get("projectId");

                    LambdaQueryWrapper<ProjectInfo> projectQueryWrapper = new LambdaQueryWrapper<>();
                    projectQueryWrapper.eq(ProjectInfo::getId, projectId);
                    projectQueryWrapper.ge(ProjectInfo::getTenantId, "0");
                    List<ProjectInfo> proList = projectInfoService.list(projectQueryWrapper);
                    if (proList == null || proList.size() <= 0) {
                        throw new CrmException(500, "项目id：" + projectId + "不存在,请项目施工单位先在住建委云平台申请后再进行数据推送");
                    }

                    ProjectInfo projectInfo = proList.get(0);
                    equipmentDriverIdent.setTenantId(projectInfo.getTenantId());
                    equipmentDriverIdent.setProjectId(projectId);
                    if (data.get("deviceSn") == null || "".equals(data.get("deviceSn"))) {
                        throw new CrmException(500, "设备编号不能为空");
                    }
                    equipmentDriverIdent.setEquCode(data.get("deviceSn")+"");
                    if (data.get("deviceType") == null || "".equals(data.get("deviceType"))) {
                        throw new CrmException(500, "设备类型不能为空");
                    }
                    equipmentDriverIdent.setDeviceType(data.get("deviceType")+"");
                    if (data.get("idCard") == null || "".equals(data.get("idCard"))) {
                        throw new CrmException(500, "身份证号不能为空");
                    }
                    equipmentDriverIdent.setIdCard(data.get("idCard")+"");
                    equipmentDriverIdent.setCertificateId( data.get("specialNo")+"");

                    if (data.get("driverDate") == null || "".equals(data.get("driverDate"))) {
                        throw new CrmException(500, "设备数据时间不能为空");
                    }
                    String regdate=(String)data.get("driverDate");
                    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    equipmentDriverIdent.setStartingDate(dft.parse(regdate));
                    String lastOperationDate=(String)data.get("lastOperationDate");
                    equipmentDriverIdent.setEndingDate(dft.parse(lastOperationDate));
                    equipmentDriverIdent.setPhotoUrl((String) data.get("photoUrl"));
                    //getBaseMapper().insert(equipmentDriverIdent);
                    dataList.add(equipmentDriverIdent);
                }
                getBaseMapper().batchSaveData(dataList);
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok();
        }catch (CrmException e) {
            e.printStackTrace();
            log.error("数据接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("数据接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }
}
