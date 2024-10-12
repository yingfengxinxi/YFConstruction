package com.kakarote.build.labour.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.kakarote.build.labour.entity.BO.LabourInfocollectionBO;
import com.kakarote.build.labour.entity.BO.LabourWorktypeBO;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.build.labour.mapper.LabourInfocollectionMapper;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.labour.service.ILabourSpeworktypeService;
import com.kakarote.build.labour.service.ILabourWorktypeService;
import com.kakarote.core.common.Result;
import com.kakarote.core.config.MybatisPlusConfig;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileServiceFactory;
import com.kakarote.core.servlet.upload.UploadEntity;
import com.kakarote.core.utils.UserUtil;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.util.*;

/**
 * <p>
 * 劳务管理--信息采集 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-03-17
 */
@Service
public class LabourInfocollectionServiceImpl extends BaseServiceImpl<LabourInfocollectionMapper, LabourInfocollection> implements ILabourInfocollectionService {

    public static final String ENTRY_COUNT = "entryCount";
    @Autowired
    private ILabourSpeworktypeService labourSpeworktypeService;
    @Autowired
    private ILabourWorktypeService iLabourWorktypeService;

    @Override
    public BasePage<LabourInfocollection> selectList(LabourInfocollectionBO labourInfocollectionBO) {
        BasePage<LabourInfocollection> labourInfocollectionBasePage = getBaseMapper().selectList(labourInfocollectionBO.parse(), labourInfocollectionBO);
        return labourInfocollectionBasePage;
    }

    @Override
    public BasePage<Map<String, Object>> getTeamLeaderList(LabourInfocollectionBO labourInfocollectionBO) {
        BasePage<Map<String, Object>> labourInfocollectionBasePage = getBaseMapper().getTeamLeaderList(labourInfocollectionBO.parse(), labourInfocollectionBO);
        return labourInfocollectionBasePage;
    }

    @Override
    @Transactional
    public Result saveInfo(MultipartFile file, LabourInfocollection labourInfocollection, LabourSpeworktype labourSpeworktype) {

//        try{
//            if (file != null && file.getSize() > 1) {
//
//                UploadEntity entity = new UploadEntity(MybatisPlusConfig.getNextId() + "", file.getOriginalFilename(), file.getSize(), null,"0");
//
//                entity = FileServiceFactory.build().uploadFile(file.getInputStream(), entity);
//                labourInfocollection.setImgCardPath(entity.getPath());
//                labourInfocollection.setImgCardUrl(entity.getUrl());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        labourInfocollection.setJobNumber(IdUtil.simpleUUID());
        if("".equals(labourInfocollection.getId())||labourInfocollection.getId() ==null){
            labourInfocollection.setCreateTime(new Date());
        }else{
            labourInfocollection.setUpdateTime(new Date());
        }
        //先保存采集信息
        boolean b = saveOrUpdate(labourInfocollection);
        //工种ID，判断是否是特殊工种
        //Integer workTypeId = labourInfocollection.getWorkTypeId();
        //LabourWorktype byId = null;
        //根据采集信息主键查询特殊工种证书信息
        LabourSpeworktype labourSpeworktype1 = labourSpeworktypeService.selectByInfoId(labourInfocollection.getId());

        /*if(workTypeId != null){
            byId = iLabourWorktypeService.getById(workTypeId);
        }*/
        //若不是特殊工种，但是证书信息存在，需要根据采集信息主键，将证书信息删除
        /*if(byId != null && !byId.getWorkTypeIfspecial().equals("Y") && labourSpeworktype1 != null){
            labourSpeworktypeService.deleteByInfoId(labourInfocollection.getId());
        }*/

        //若是特殊工种，再保存证书信息
        //if(byId != null && byId.getWorkTypeIfspecial().equals("Y") && b){
        if(b){
            labourSpeworktype.setInfocolId(labourInfocollection.getId());
            //labourSpeworktype.setWorkTypeId(String.valueOf(labourInfocollection.getWorkTypeId()));
            labourSpeworktype.setWorkTypeId(String.valueOf(labourInfocollection.getWorkTypeDict()));
            labourSpeworktype.setSubcontractor(labourInfocollection.getSubcontractor());
            //根据采集信息主键查询特殊工种证书信息，若是存在，更新
            if(labourSpeworktype1 != null){
                labourSpeworktype.setId(labourInfocollection.getWorkTypeId());
                b =  labourSpeworktypeService.updateById(labourSpeworktype);
            }else{
                b = labourSpeworktypeService.save(labourSpeworktype);
            }
        }

        return Result.ok(b);

    }

    /**
     * 保存零工信息
     * @param file
     * @param labourInfocollection
     * @return
     */
    @Override
    public Result saveOddJobInfo(MultipartFile file, LabourInfocollection labourInfocollection) {
//        try{
//            if (file != null && file.getSize() > 1) {
//
//                UploadEntity entity = new UploadEntity(MybatisPlusConfig.getNextId() + "", file.getOriginalFilename(), file.getSize(), null,"0");
//
//                entity = FileServiceFactory.build().uploadFile(file.getInputStream(), entity);
//                labourInfocollection.setImgCardUrl(entity.getUrl());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        //先保存采集信息
        boolean b = saveOrUpdate(labourInfocollection);

        return Result.ok(b);
    }

    @Override
    public List<Map> selectInfoByMapNoPage(LabourInfocollectionBO labourInfocollectionBO) {
        return getBaseMapper().selectInfoByMapNoPage(labourInfocollectionBO);
    }

    //2021-04-11新增给安全教育使用花名册查询时移除已存在的人员数据使用列表
    @Override
    public BasePage<LabourInfocollection> selectListSafetyTraining(LabourInfocollectionBO labourInfocollectionBO) {
        BasePage<LabourInfocollection> labourInfocollectionBasePage = getBaseMapper().selectListSafetyTraining(labourInfocollectionBO.parse(), labourInfocollectionBO);
        return labourInfocollectionBasePage;
    }

    //2021-04-15新增统计班组人数
    @Override
    public List<LabourInfocollection> ByTeamName(String teamName ) {
        return getBaseMapper().ByTeamName(teamName);
    }

    //2021-04-16新增查询人员id
    public LabourInfocollection selectInfoRecord(LabourInfocollection labourInfocollection){
        LabourInfocollection data=getBaseMapper().selectInfoRecord(labourInfocollection);
        return data;

    }

    //2021-11-25新增查询当前租户下的全部人员
    public List<LabourInfocollection> selectInfoRecordAll(LabourInfocollection labourInfocollection){
        List<LabourInfocollection> data=getBaseMapper().selectInfoRecordAll(labourInfocollection);
        return data;
    }

    @Override
    public Map<String, Object> selectAttendanceData(Map<String, Object> params) {
        // 查询今日出勤人数
        Map<String, Object> attendanceData = getBaseMapper().selectAttendanceToday(params);

        // 总人数
        Long headcount = (Long) attendanceData.get("headcount");
        // 出勤人数
        Long attendanceNum = (Long) attendanceData.get("attendanceNum");
        // 缺勤人数
        Long absenteeism = headcount - attendanceNum;
        attendanceData.put("absenteeism", absenteeism);

        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(0);
        if (headcount == 0) { // 除数为0，百分比直接为100%
            attendanceData.put("percent", "100%");
        } else {
            double retio = attendanceNum * 1.0 / headcount;
            String percent = format.format(retio);
            attendanceData.put("percent", percent);
        }

        return attendanceData;
    }

    /**
     * 集团数据今日施工在场人数
     * @param list
     * @return
     */
    @Override
    public List<Map> selectAttendanceDataByProjectIds(List<Map> list) {
        List<Map> listAll = new ArrayList<>();

        // 查询今日出勤人数
        for(int i = 0 ; i < list.size(); i++){
            Map map = list.get(i);
            Integer projectId = (Integer) map.get("projectId");
            Map<String, Object> stringObjectMap = getBaseMapper().selectAttendanceTodayByProjectId(projectId);
            stringObjectMap.put("projectName",map.get("projectName"));
            listAll.add(stringObjectMap);
        }

        return listAll;
    }

    @Override
    public List<Map<String, Object>> selectSceneWorkTypeList() {
        List<Map<String, Object>> list = getBaseMapper().selectSceneWorkTypeList();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectTeamList() {
        List<Map<String, Object>> list = getBaseMapper().selectTeamList();
        return list;
    }

    @Override
    public Map<String, Object> selectNationAndGender() {

        Map<String, Object> map = new HashMap<>();
        // 查询员工民族数量
        List<Map<String, Object>> nationList = getBaseMapper().selectNationList();
        // 查询员工性别数量
        List<Map<String, Object>> genderList = getBaseMapper().selectGenderList();
        // 查询年龄分布分布
        List<Map<String, Object>> list = getBaseMapper().selectAgeRange();
        map.put("nationList", nationList);
        map.put("genderList", genderList);
        map.put("ageList", list);


        return map;
    }

    /**
     * 查询零工的id和name 零工签证使用
     * @return
     */
    @Override
    public List<Map<String, Object>> selectOddIdAndName() {
        return getBaseMapper().selectOddIdAndName();
    }

    /**
     * 根据身份证查询项目履历信息
     * @param idCard
     * @return
     */
    @Override
    public List<Map<String, Object>> queryProjectsByIdCard(String idCard,Integer projectId) {
        return getBaseMapper().queryProjectsByIdCard(idCard,projectId);
    }

    @Override
    public List<Map<String, Object>> selectAgeRange() {
        List<Map<String, Object>> list = getBaseMapper().selectAgeRange();
        return list;
    }

    @Override
    public Map<String, Object> selectRealTimeNumber() {
        // 获取劳务管理总人数
        Map<String, Object> headcountData = getBaseMapper().selectHeadCount();



        Map<String, Object> data = getBaseMapper().selectEntryOrExitCount();

        // 根据进场人数和出场人数计算现场人数
        Integer entryCount = Integer.valueOf(String.valueOf(data.get("entryCount")));
        Integer exitCount = Integer.valueOf(String.valueOf(data.get("exitCount")));

        Integer sceneCount = entryCount - exitCount;

        // 封装返回数据
        headcountData.put(ENTRY_COUNT, entryCount);
        headcountData.put("exitCount", exitCount);
        headcountData.put("sceneCount", sceneCount);

        return headcountData;
    }

    @Override
    public List<Map<String, Object>> selectListByTeam(Integer teamId) {
        List<Map<String, Object>> list = getBaseMapper().selectListByTeam(teamId);
        return list;
    }

    /**
     * 2021-12-20新增统计特殊职位人数
     * @param labourInfocollection
     * @return
     */
    @Override
    public List<LabourInfocollection>  byManageType(LabourInfocollection labourInfocollection ) {
        return getBaseMapper().byManageType(labourInfocollection);
    }
    @Override
    public List<LabourInfocollection>  byManageType2(LabourInfocollection labourInfocollection ) {
        return getBaseMapper().byManageType2(labourInfocollection);
    }

    @Override
    public BasePage<Map<String,Object>> byManageType3(BasePage<Object> parse, LabourInfocollection labour) {
        return getBaseMapper().byManageType3(parse,labour);
    }

    @Override
    public BasePage<Map<String,Object>> selectAttendanceDataList(Map<String,Object> param) {
        PageEntity page=new PageEntity();
        page.setPage(Integer.valueOf(String.valueOf(param.get("page"))));
        page.setLimit(Integer.valueOf(String.valueOf(param.get("limit"))));
        return  getBaseMapper().selectAttendanceDataList(page.parse(), param);
    }

    @Override
    public Map<String, Object> selectInfoByName(Map<String, Object> params) {
        if (params.get("name") == null || params.get("name").equals("")) {
            throw new CrmException(500, "姓名不能为空");
        }
        Map<String, Object> personInfo = this.getBaseMapper().selectInfoByName(params);
        return personInfo;
    }

    @Override
    public Map<String, Object> getInfocollectionByPhone(Integer phone) {
        return getBaseMapper().getInfocollectionByPhone(phone);
    }
}
