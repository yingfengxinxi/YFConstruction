package com.kakarote.build.qual.service.impl;

import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.qual.entity.BO.QualRiskDetectionBO;
import com.kakarote.build.qual.entity.PO.QualRiskDetection;
import com.kakarote.build.qual.mapper.QualRiskDetectionMapper;
import com.kakarote.build.qual.service.IQualRiskDetectionService;
import com.kakarote.build.safety.entity.PO.SafetyRiskDetection;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageFeignService;
import com.kakarote.core.message.MessageType;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-07-11
 */
@Service
public class QualRiskDetectionServiceImpl extends BaseServiceImpl<QualRiskDetectionMapper, QualRiskDetection> implements IQualRiskDetectionService {

    @Autowired
    private IProjectInfoService projectInfoService;

    @Autowired
    private MessageFeignService messageFeignService;

    @Override
    public BasePage<QualRiskDetection> selectByPage(QualRiskDetectionBO riskDetectionBO) {
        riskDetectionBO.setOrdersBd("create_time", false);
        BasePage<QualRiskDetection> list = getBaseMapper().selectByPage(riskDetectionBO.parse(), riskDetectionBO);
        return list;
    }

    @Override
    public Result saveInfo(QualRiskDetection riskDetection) {

        boolean result = this.save(riskDetection);
        if (!result) {
            throw new CrmException(500, "保存失败！");
        }

        // 推送消息
        try {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setByUser(UserUtil.getUserId());
            messageEntity.setTitle(riskDetection.getProblemDescription());
            messageEntity.setData(String.valueOf(riskDetection.getId()));
            //List<Long> toUsers = messageFeignService.getQueuesUserList(MessageType.MESSAGE_SECURITY_RISKS);
            List<Long> toUsers = getBaseMapper().getQueuesUserList(riskDetection.getId());
            messageEntity.setToUsers(toUsers);//指定消息推送人
            messageEntity.setCode(MessageType.QUAL_RISKS_DETECTION);
            Result sendResult = messageFeignService.send(messageEntity);

            if (sendResult.getCode() != 0) {
                System.out.println("消息推送错误：============" + sendResult.getMsg());
                throw new CrmException(0, "消息推送失败");
            }

            // 成功更新数据状态
            QualRiskDetection updateInfo = new QualRiskDetection();
            updateInfo.setId(riskDetection.getId());
            updateInfo.setInfoState("1");
            updateById(updateInfo);

        } catch (Exception e) {
            return R.ok("任务下发失败！");
        }


        // 保存数据
        return R.ok("添加成功!");
    }

    @Override
    public boolean updateInfo(QualRiskDetection riskDetection) {
        riskDetection.setUpdateTime(new Date());
        riskDetection.setUpdateUserId(UserUtil.getUserId());

        // 如果隐患状态 1：已下发，表示当前操作是整改保存，2：已整改，表示当前操作是复检保存。
        if ("1".equals(riskDetection.getInfoState())) {
            riskDetection.setInfoState("2");
        } else if ("2".equals(riskDetection.getInfoState())) {
            riskDetection.setInfoState("3");
        }

        return this.updateById(riskDetection);
    }

    @Override
    public List<Map<String, Object>> selectPieData() {
        List<Map<String, Object>> list = getBaseMapper().selectPieData();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectBarData(Map<String, Object> params) {
        String type = (String) params.get("type");

        if (type == null || type.equals("")) {
            throw new CrmException(500, "查询类型type不能为空，1：查询最近7天；2：查询最近30天；3：查询最近12个月；");
        }

        if (type.equals("1")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "6");
            params.put("dateType", "day");
        } else if (type.equals("2")) {
            params.put("format", "%Y-%m-%d");
            params.put("num", "29");
            params.put("dateType", "day");
        } else if (type.equals("3")) {
            params.put("format", "%Y-%m");
            params.put("num", "11");
            params.put("dateType", "month");
        }

        List<Map<String, Object>> list = getBaseMapper().selectBarData(params);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectRectifyDateWarning() {
        Map<String, Object> data = getBaseMapper().selectRectifyDateWarning();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(null);
        }

        Integer index = null;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            if ("count".equals(entry.getKey())) {
                map.put("name", "隐患总个数");
                index = 0;
            } else if ("underwayNumber".equals(entry.getKey())) {
                map.put("name", "隐患整改中个数");
                index = 2;
            } else if ("timeoutNumber".equals(entry.getKey())) {
                map.put("name", "隐患整改超时个数");
                index = 3;
            } else if ("completeNumber".equals(entry.getKey())) {
                map.put("name", "隐患整改完成个数");
                index = 1;
            }

            map.put("value", entry.getValue());
            list.set(index, map);
        }


        return list;
    }

    @Override
    public List<Map<String, Object>> selectQualDataByPrinipal() {
        List<Map<String, Object>> list = getBaseMapper().selectQualDataByPrinipal();
        return list;
    }

    @Override
    public Map<String, Object> selectQualQuestionsQuantity() {
        Map<String, Object> questionData = getBaseMapper().selectQualQuestionsQuantity();

        // 总数
        Integer count = Integer.valueOf(String.valueOf(questionData.get("count")));
        // 未完成数
        Integer unfinished = Integer.valueOf(String.valueOf(questionData.get("unfinished")));

        // 计算百分比
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(0);
        if (unfinished == 0) { // 除数为0，百分比直接为100%
            questionData.put("percent", "100%");
        } else {
            double retio = unfinished * 1.0 / count;
            String percent = format.format(retio);
            questionData.put("percent", percent);
        }

        return questionData;
    }
}
