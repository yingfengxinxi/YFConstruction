package com.kakarote.build.labour.apipm.service.impl;

import cn.hutool.core.date.DateException;
import com.kakarote.build.api.common.EnvMonitorCodeEnum;
import com.kakarote.build.labour.apipm.service.IWageDetailDataService;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceRecord;
import com.kakarote.build.labour.entity.PO.BLabourWageDetail;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.build.labour.mapper.BLabourWageDetailMapper;
import com.kakarote.build.labour.service.IBLabourAttendanceRecordService;
import com.kakarote.build.labour.service.IBLabourWageDetailService;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.utils.ApiUtils;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工资管理详情 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
@Service
public class WageDetailDataServiceImpl extends BaseServiceImpl<BLabourWageDetailMapper, BLabourWageDetail> implements IWageDetailDataService {
    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private ILabourInfocollectionService labourInfocollectionService;

    @Autowired
    private IBLabourAttendanceRecordService iBLabourAttendanceRecordService;

    @Override
    public Result wageDetailDataAdd(Map<String, Object> parpam){
        try {

            List<Map<String, Object>>  data = new ArrayList<Map<String, Object>>();
            List<BLabourWageDetail> dataList = new ArrayList<BLabourWageDetail>();
            ProjectInfo projectInfo= apiUtils.dataValidation(parpam);
            LabourInfocollection labourInfocollectiondata= new LabourInfocollection ();
            labourInfocollectiondata.setProjectId(projectInfo.getId());
            labourInfocollectiondata.setTenantId(projectInfo.getTenantId());
            if(parpam.get("data") != null && !"".equals(parpam.get("data"))){
                data = (List<Map<String, Object>>)parpam.get("data");
                if(data.size()>0){
                    for(int i=0;i<data.size();i++){
                        BLabourWageDetail bLabourWageDetail= new BLabourWageDetail();
                        bLabourWageDetail.setCreateBy((String)parpam.get("createBy"));
                        if (parpam.get("time") != null && !"".equals(parpam.get("time"))) {
                            String time=(String)parpam.get("time");
                            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            bLabourWageDetail.setCreateTime(dft.parse(time));
                        }
                        bLabourWageDetail.setDataGeneration("1");
                        bLabourWageDetail.setProjectId(projectInfo.getId());
                        bLabourWageDetail.setTenantId(projectInfo.getTenantId());
                        if (data.get(i).get("infocollectionName") == null || "".equals(data.get(i).get("infocollectionName"))) {
                            throw new CrmException(500, "人员名称不能为空");
                        }
                        if (data.get(i).get("infocollectionCard") == null || "".equals(data.get(i).get("infocollectionCard"))) {
                            throw new CrmException(500, "身份证号不能为空");
                        }
                        labourInfocollectiondata.setName((String)data.get(i).get("infocollectionName").toString().trim());
                        labourInfocollectiondata.setIdCard((String)data.get(i).get("infocollectionCard").toString().trim());
                        LabourInfocollection labourInfocollection=  labourInfocollectionService.selectInfoRecord(labourInfocollectiondata);
                        if (labourInfocollection == null) {
                            throw new CrmException(500, "人员：" + data.get(i).get("infocollectionName") + "不存在,请先维护该人员基本信息后在进行上传");
                        }
                        bLabourWageDetail.setInfocollectionId(String.valueOf(labourInfocollection.getId()));
                        bLabourWageDetail.setInfocollectionName((String)data.get(i).get("infocollectionName"));
                        bLabourWageDetail.setInfocollectionCard((String)data.get(i).get("infocollectionCard"));

                        if (data.get(i).get("wagesCardNumber") == null || "".equals(data.get(i).get("wagesCardNumber"))) {
                            throw new CrmException(500, "工资卡号不能为空");
                        }
                        bLabourWageDetail.setWagesCardNumber((String)data.get(i).get("wagesCardNumber"));
                        if (data.get(i).get("wagesBankCode") == null || "".equals(data.get(i).get("wagesBankCode"))) {
                            throw new CrmException(500, "工资卡银行代码不能为空");
                        }
                        bLabourWageDetail.setWagesBankCode((String)data.get(i).get("wagesBankCode"));
                        if (data.get(i).get("wagesBankName") == null || "".equals(data.get(i).get("wagesBankName"))) {
                            throw new CrmException(500, "工人工资卡开户行名称不能为空");
                        }
                        bLabourWageDetail.setWagesBankName((String)data.get(i).get("wagesBankName"));

                        if (data.get(i).get("undertakesCardNumber") == null || "".equals(data.get(i).get("undertakesCardNumber"))) {
                            throw new CrmException(500, "代发银行卡号不能为空");
                        }
                        bLabourWageDetail.setUndertakesCardNumber((String)data.get(i).get("undertakesCardNumber"));
                        if (data.get(i).get("undertakesBankCode") == null || "".equals(data.get(i).get("undertakesBankCode"))) {
                            throw new CrmException(500, "代发银行代码不能为空");
                        }
                        bLabourWageDetail.setUndertakesBankCode((String)data.get(i).get("undertakesBankCode"));
                        if (data.get(i).get("undertakesBankName") == null || "".equals(data.get(i).get("undertakesBankName"))) {
                            throw new CrmException(500, "代发银行名称不能为空");
                        }
                        bLabourWageDetail.setUndertakesBankName((String)data.get(i).get("undertakesBankName"));
                        if (data.get(i).get("wageDetailShould") == null || "".equals(data.get(i).get("wageDetailShould"))) {
                            throw new CrmException(500, "应发金额不能为空");
                        }
                        bLabourWageDetail.setWageDetailShould(Double.valueOf((String)data.get(i).get("wageDetailShould")));
                        if (data.get(i).get("actualAmount") == null || "".equals(data.get(i).get("actualAmount"))) {
                            throw new CrmException(500, "实发金额不能为空");
                        }
                        bLabourWageDetail.setActualAmount(Double.valueOf((String)data.get(i).get("actualAmount")));
                        if (data.get(i).get("isBackPay") == null || "".equals(data.get(i).get("isBackPay"))) {
                            throw new CrmException(500, "是否为补发不能为空");
                        }else{
                            String isBackPay=(String)data.get(i).get("isBackPay");
                            if("1".equals(isBackPay)){
                                if (data.get(i).get("backPayMonth") == null || "".equals(data.get(i).get("backPayMonth"))) {
                                    throw new CrmException(500, "补发月份不能为空");
                                }
                                bLabourWageDetail.setBackPayMonth((String)data.get(i).get("backPayMonth"));
                            }
                        }
                        bLabourWageDetail.setIsBackPay((String)data.get(i).get("isBackPay"));
                        if (data.get(i).get("issueDate") != null && !"".equals(data.get(i).get("issueDate"))) {
                            String issueDate=(String)data.get(i).get("issueDate");
                            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                            bLabourWageDetail.setIssueDate(dft.parse(issueDate));
                        }else{
                            throw new CrmException(500, "发放日期不能为空");
                        }
                        if (data.get(i).get("wageDetailMonth") == null || "".equals(data.get(i).get("wageDetailMonth"))) {
                            throw new CrmException(500, "年月不能为空");
                        }
                        bLabourWageDetail.setWageDetailMonth((String)data.get(i).get("wageDetailMonth"));
                        BLabourAttendanceRecord bLabourAttendanceRecord =new BLabourAttendanceRecord ();
                        bLabourAttendanceRecord.setCreateBy((String)data.get(i).get("wageDetailMonth"));
                        bLabourAttendanceRecord.setInfocollectionId(String.valueOf(labourInfocollection.getId()));
                        bLabourAttendanceRecord.setProjectId(projectInfo.getId());
                        bLabourAttendanceRecord.setTenantId(projectInfo.getTenantId());
                        List<BLabourAttendanceRecord> list = iBLabourAttendanceRecordService.selectValidation(bLabourAttendanceRecord);
                        if(list== null || list.size()==0){
                            throw new CrmException(500, "人员"+data.get(i).get("infocollectionName")+"没有查询到"+data.get(i).get("wageDetailMonth")+"对应的考勤数据请先更新该人员的考勤信息");
                        }
                        if (data.get(i).get("thirdPayRollCode") == null || "".equals(data.get(i).get("thirdPayRollCode"))) {
                            throw new CrmException(500, "第三方工资单编号不能为空");
                        }
                        bLabourWageDetail.setThirdPayRollCode((String)data.get(i).get("thirdPayRollCode"));

                        if (data.get(i).get("dayPrice") == null || "".equals(data.get(i).get("dayPrice"))) {
                            throw new CrmException(500, "日单价不能为空");
                        }
                        bLabourWageDetail.setDayPrice(Double.valueOf((String)data.get(i).get("dayPrice")));
                        if (data.get(i).get("manDays") == null || "".equals(data.get(i).get("manDays"))) {
                            throw new CrmException(500, "工日不能为空");
                        }
                        bLabourWageDetail.setManDays(Integer.valueOf((String)data.get(i).get("manDays")));
                        if(data.get(i).get("quantitiesPrice") != null && !"".equals(data.get(i).get("quantitiesPrice"))){
                            bLabourWageDetail.setQuantitiesPrice(Double.valueOf((String)data.get(i).get("quantitiesPrice")));
                        }
                        if(data.get(i).get("quantities") != null && !"".equals(data.get(i).get("quantities"))){
                            bLabourWageDetail.setQuantities(Integer.valueOf((String)data.get(i).get("quantities")));
                        }
                        if(data.get(i).get("wageDetailAdvance") != null && !"".equals(data.get(i).get("wageDetailAdvance"))){
                            bLabourWageDetail.setWageDetailAdvance(Double.valueOf((String)data.get(i).get("wageDetailAdvance")));
                        }
                        if(data.get(i).get("monthBalance") != null && !"".equals(data.get(i).get("monthBalance"))){
                            bLabourWageDetail.setMonthBalance(Double.valueOf((String)data.get(i).get("monthBalance")));
                        }
                        if(data.get(i).get("cumulativeBalance") != null && !"".equals(data.get(i).get("cumulativeBalance"))){
                            bLabourWageDetail.setCumulativeBalance(Double.valueOf((String)data.get(i).get("cumulativeBalance")));
                        }
                        if(data.get(i).get("workHours") != null && !"".equals(data.get(i).get("workHours"))){
                            bLabourWageDetail.setWorkHours(new BigDecimal((String)data.get(i).get("workHours")));
                        }
                        dataList.add(bLabourWageDetail);
                    }
                    // 最后进行循环插入防止数据传输一半部分数据录入了，部分数据没录入的问题
                    for(int i=0;i<dataList.size();i++){
                        save(dataList.get(i));
                    }
                }else{
                    throw new CrmException(500, "您未传输数据主体！");
                }
            }else{
                throw new CrmException(500, "您未传输数据主体！");
            }
            return Result.ok();
        } catch (CrmException e) {
            e.printStackTrace();
            log.error("工资数据上传接口错误：" + e);
            throw e;
        } catch (DateException e){
            e.printStackTrace();
            log.error("工资数据上传接口错误：" + e);
            throw new CrmException(500, "日期时间格式不符合规范!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("工资数据上传接口错误：" + e);
            throw new CrmException(EnvMonitorCodeEnum.ENV_MONITOR_SYSTEM_ERROR);
        }
    }

}
