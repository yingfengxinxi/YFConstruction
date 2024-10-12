package com.kakarote.build.labour.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceDetailBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceRecordBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetail;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤记录管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
public interface BLabourAttendanceRecordMapper extends BaseMapper<BLabourAttendanceRecord> {

    BasePage<BLabourAttendanceRecord> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabourAttendanceRecordBO bLabourAttendanceRecordBO);
    public List<BLabourAttendanceRecord> selectDetailList(BLabourAttendanceRecord bLabourAttendanceRecord);

    public List<BLabourAttendanceRecord> selectByMonth(BLabourAttendanceRecord bLabourAttendanceRecord);

    public List<BLabourAttendanceRecord> selectDetailListUpdate(BLabourAttendanceRecord bLabourAttendanceRecord);

    public List<BLabourAttendanceRecord> selectValidation(BLabourAttendanceRecord bLabourAttendanceRecord);

    BasePage<Map<String, Object>> getAttendanceDetailList(BasePage<BLabourAttendanceRecord> parse, @Param("data")BLabourAttendanceRecordBO bLabourAttendanceRecordBO);

    List<Map<String, Object>> selectEntryAndExit(@Param("data") Map<String, Object> params);

    @SqlParser(filter = true)
    Map<String, Object> selectPositionAttendance(@Param("data") Map<String, Object> params);

    @SqlParser(filter = true)
    Map<String, Object> selectPositionAttendanceZs(@Param("data") Map<String, Object> params);

    public List<BLabourAttendanceRecord> selectDynamic(@Param("tenantId") String tenantId);

    String approach(@Param("data") BLabourAttendanceRecord bLabourAttendanceRecord,@Param("days")List<String> days);

    String peopleNumber(@Param("data") BLabourAttendanceRecord bLabourAttendanceRecord,@Param("idList")List<String> idList);

    String peopleNumberXcssrs(@Param("data") BLabourAttendanceRecord bLabourAttendanceRecord,@Param("idList")List<String> idList);

    @SqlParser(filter = true)
    Map<String, Object> workersConstitute(@Param("data") Map<String, Object> params);

    @SqlParser(filter = true)
    Map<String, Object> ageConstitute(@Param("data") Map<String, Object> params);

    @SqlParser(filter = true)
    List<Map<String, Object>>  schoolingConstitute(@Param("data") Map<String, Object> params);

    //wnj  把原sql 租户过滤器的true 修改为false，原因：临沂大屏-项目人员的持证工人与实际工人数不一致问题。
    @SqlParser(filter = false)
    List<Map<String, Object>>  workerTypeList(@Param("data") Map<String, Object> params);

    @SqlParser(filter = true)
    Map<String, Object> workerTypeListTbHb(@Param("data") Map<String, Object> params);

    @SqlParser(filter = true)
    Map<String, Object> wageConstitute(@Param("data") Map<String, Object> params);

    @SqlParser(filter = true)
    Map<String, Object> defaultWageConstitute(@Param("data") Map<String, Object> params);

    List<Map<String, Object>> selectAttendanceNumber(@Param("data") Map<String, Object> map);

    BasePage<BLabourAttendanceRecord> selectDynamicNew(BasePage<Object> parse , @Param("data")Map<String,Object> param);

    BasePage<Map<String,Object>> peopleInfos(BasePage<Object> parse , @Param("data")  BLabourAttendanceRecord bLabourAttendanceRecord , @Param("idList") List<String> idList);

    Map  selectByUserId(Integer id);

    Map selectInfocollectionByUserId(Integer id);
}
