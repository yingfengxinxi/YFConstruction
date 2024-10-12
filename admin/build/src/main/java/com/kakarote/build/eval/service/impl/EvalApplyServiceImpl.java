package com.kakarote.build.eval.service.impl;

import com.alibaba.fastjson.JSON;
import com.kakarote.build.eval.entity.BO.EvalApplyBO;
import com.kakarote.build.eval.entity.PO.EvalApply;
import com.kakarote.build.eval.entity.PO.EvalApplyItems;
import com.kakarote.build.eval.entity.PO.EvalItems;
import com.kakarote.build.eval.mapper.EvalApplyMapper;
import com.kakarote.build.eval.service.IEvalApplyItemsService;
import com.kakarote.build.eval.service.IEvalApplyService;
import com.kakarote.build.eval.service.IEvalItemsService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.build.safety.entity.PO.SafetyInspectionDetail;
import com.kakarote.build.safety.entity.PO.SafetyInspectionInfo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stringtemplate.v4.ST;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 智慧工地评价-评估打分申请表 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-02-23
 */
@Service
public class EvalApplyServiceImpl extends BaseServiceImpl<EvalApplyMapper, EvalApply> implements IEvalApplyService {

    @Autowired
    private IEvalApplyItemsService evalApplyItemsService;

    @Autowired
    private IProjectInfoService projectInfoService;

    @Autowired
    private IEvalItemsService evalItemsService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveApplyAndApplyItemData(Map<String, Object> params) {

        // 获取数据
        Map<String, Object> apply = (Map<String, Object>) params.get("applyInfo");
        List<Map<String, Object>> applyItems = (List<Map<String, Object>>) params.get("applyItemList");

        if (apply == null) {
            throw new CrmException(1001, "参数applyInfo不能为空");
        }

        if (applyItems == null || applyItems.size() <= 0) {
            throw new CrmException(1001, "参数applyItemList不能为空");
        }

        // 根据选中打分项应用类别，查询所有打分项
        List<String> itemNameList = new ArrayList<>();
        for (Map<String, Object> map: applyItems) {
            itemNameList.add((String) map.get("itemName"));
        }
        List<EvalItems> evalItemsList = evalItemsService.selectListByItemName(itemNameList);

        if (evalItemsList == null && evalItemsList.size() <= 0) {
            throw new CrmException(500, "打分项不能为空，请选择打分项！");
        }

        EvalApply evalApply = JSON.parseObject(JSON.toJSONString(apply), EvalApply.class);

        String uuid= UUID.randomUUID().toString().replaceAll("-", "");

        evalApply.setId(uuid);
        evalApply.setApplyTime(new Date());

        // 保持申请信息
        getBaseMapper().insert(evalApply);

        List<EvalApplyItems> list = new ArrayList<>();
        // 保持打分项
        for (EvalItems evalItems: evalItemsList) {
            String itemId= UUID.randomUUID().toString().replaceAll("-", "");

            EvalApplyItems evalApplyItems = new EvalApplyItems();
            evalApplyItems.setId(itemId);
            evalApplyItems.setApplyId(uuid);
            evalApplyItems.setItemId(evalItems.getId());
            evalApplyItems.setClassType(evalItems.getClassType());
            evalApplyItems.setItemType(evalItems.getItemType());
            evalApplyItems.setItemName(evalItems.getItemName());
            evalApplyItems.setItemDes(evalItems.getItemDes());
            evalApplyItems.setMaxData(evalItems.getMaxData());
            evalApplyItems.setMinData(evalItems.getMinData());
            evalApplyItems.setSort(evalItems.getSort());

            list.add(evalApplyItems);

        }

        evalApplyItemsService.saveBatch(list);

        return uuid;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> selectDataById(String id) {

        Map<String, Object> data = new HashMap<>();

        // 查询申请信息
        EvalApply evalApply = getBaseMapper().selectById(id);

        // 查询所有应用类别数据
        List<Map<String,Object>> classTypeItemList = evalApplyItemsService.selectClassTypeItemList(id);

        // 查询打分项数据
        List<EvalApplyItems> evalApplyItemsList = evalApplyItemsService.selectItemList(id);

        // 设置下标
        for (int i = 0; i<evalApplyItemsList.size();i++) {
            EvalApplyItems item = evalApplyItemsList.get(i);
            item.setIndex(i);
        }

        data.put("evalApply", evalApply);
        data.put("evalApplyItemsList", evalApplyItemsList);
        data.put("classTypeItemList", classTypeItemList);

        return data;
    }

    @Override
    public BasePage<EvalApply> selectByPage(EvalApplyBO evalApplyBO) {
        evalApplyBO.setOrdersBd("a.status",true);
        evalApplyBO.setOrdersBd("a.apply_time",true);
        BasePage<EvalApply> list = this.getBaseMapper().selectByPage(evalApplyBO.parse(), evalApplyBO);
        return list;
    }

    @Override
    public String gradeOrHold(Map<String, Object> params) {
        // 获取数据
        Map<String, Object> apply = (Map<String, Object>) params.get("applyInfo");
        List<Map<String, Object>> applyItems = (List<Map<String, Object>>) params.get("applyItemList");

        if (apply == null) {
            throw new CrmException(1001, "参数applyInfo不能为空");
        }

        if (applyItems == null || applyItems.size() <= 0) {
            throw new CrmException(1001, "参数applyItemList不能为空");
        }

        EvalApply evalApply = JSON.parseObject(JSON.toJSONString(apply), EvalApply.class);
        // 判断评分状态是否存在
        if (evalApply.getStatus() == null || evalApply.getStatus().equals("")) {
            throw new CrmException(1001, "applyInfo下的参数status不能为空");
        }

        Long userId = UserUtil.getUser().getUserId();

        evalApply.setAppBy(userId);
        evalApply.setAppTime(new Date());

        // 保持申请信息
        getBaseMapper().updateById(evalApply);

        List<EvalApplyItems> list = new ArrayList<>();
        // 保持打分项
        for (Map<String, Object> map : applyItems) {
            EvalApplyItems evalItems = JSON.parseObject(JSON.toJSONString(map), EvalApplyItems.class);

            EvalApplyItems evalApplyItems = new EvalApplyItems();
            evalApplyItems.setId(evalItems.getId());
            evalApplyItems.setStatus("1");
            evalApplyItems.setItemScore(evalItems.getItemScore());
            evalApplyItems.setScoreBy(userId);
            evalApplyItems.setScoreTime(evalItems.getScoreTime());
            evalApplyItems.setScoreDes(evalItems.getScoreDes());
            evalApplyItems.setBatchId(evalItems.getBatchId());

            list.add(evalApplyItems);

        }

        evalApplyItemsService.updateBatchById(list);

        // 如果申请状态 status 等于 2，则当前申请通过，更新申请项目的申请数据id。
        if (evalApply.getStatus().equals("2")) {
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setId(evalApply.getProjectId());
            projectInfo.setEvalApplyId(evalApply.getId());

            // 更新数据
            projectInfoService.updateById(projectInfo);
        }

        return "";
    }

    @Override
    public BasePage<EvalApply> selectRankingList(EvalApplyBO evalApplyBO) {
        evalApplyBO.setOrdersBd("b.score desc",false);
        evalApplyBO.setOrdersBd("b.apply_time",false);
        BasePage<EvalApply> list = this.getBaseMapper().selectRankingList(evalApplyBO.parse(), evalApplyBO);
        return list;
    }

    @Override
    public Map<String, Object> selectExportDataById(String id) {
        Map<String, Object> data = new HashMap<>();

        // 查询申请信息
        EvalApply evalApply = getBaseMapper().selectInfoById(id);
        // 查询当前申请评价的所有应用类别
        List<Map<String,Object>> classTypeList = getBaseMapper().selectClassTypeById(id);
        if (evalApply == null || classTypeList.size() <=0) {
            return data;
        }

        // 查询打分项数据 根据应用类别进行排序，配合上方应用类别数据生成需要返回的数据。
        List<EvalApplyItems> evalApplyItemsList = evalApplyItemsService.lambdaQuery()
                .eq(EvalApplyItems::getApplyId, id).last("order by class_type * 1, sort").list();

        for (int i = 0; i<classTypeList.size(); i++) {
            List<EvalApplyItems> tableList = new ArrayList<>();
            Map<String, Object> classType = classTypeList.get(i);
            classType.put("tableList", tableList);
        }
        // 生成通用表单数据
        List<Map<String,Object>> dataList = this.createExportData(classTypeList, evalApplyItemsList);


        // 查询每个基础项 总信息
        List<Map<String,Object>> basicsItemData = getBaseMapper().selectBasicsItemTotalInfo(id);
        // 计算总基础分
        Double basisPoints = 0d;
        for (Map<String, Object> map : basicsItemData) {
            basisPoints += (Double) map.get("weighingScore");
        }
        // 查询非基础项总分
        Double otherScore = getBaseMapper().selectOtherScore(id);

        Map<String, Object> summaryStatement = new HashMap<>();
        summaryStatement.put("projectName", evalApply.getProjectName());
        summaryStatement.put("company", evalApply.getCompany());
        summaryStatement.put("total", evalApply.getScore());
        summaryStatement.put("basisPoints", basisPoints);
        summaryStatement.put("awardedMarks", otherScore);
        summaryStatement.put("tableList", basicsItemData);

        /*
         返回数据大概格式
         dataList: [
                classTypeMap: {
                    name: "项目类别",
                    value: "1",
                    total: 100, // 总分数
                    sum: 90.5, // 实际总分
                    tableList: [
                        EvalApplyItems
                    ]
                }
         ],
         summaryStatement: {
                projectName: "项目名称",
                company: "施工单位",
                basisPoints: "基础分",
                awardedMarks: "推广分",
                total: "总分",
                tableList: [ // 单基础项信息
                ]
         }
         */
        data.put("dataList", dataList);
        data.put("summaryStatement", summaryStatement);

        return data;
    }

    @Override
    public Map<String, Object> getInfoById(String id) {
        Map<String, Object> data = new HashMap<>();

        // 查询申请信息
        EvalApply evalApply = getBaseMapper().selectById(id);

        // 查询打分项数据
        List<EvalApplyItems> evalApplyItemsList = evalApplyItemsService.selectItemList(id);

        // 计算分数
        Double score = calculateScore(evalApplyItemsList);
        evalApply.setScore(String.valueOf(score));

        // 查询所有应用类别数据
        List<Map<String,Object>> classTypeItemList = evalApplyItemsService.selectClassTypeItemList(id);
        
        // 查询每个类别下 不同分项名称的打分项数据
        for (Map<String, Object> classTypeItem:classTypeItemList) {
            Long tenantId = UserUtil.getUser().getTenantId();
            List<Map<String,Object>> itemList = getBaseMapper().selectItemListByClassType(evalApply.getId(),(String) classTypeItem.get("classType"),tenantId);
            classTypeItem.put("itemList", itemList);
        }

        data.put("evalApply", evalApply);
        data.put("classTypeItemList", classTypeItemList);

        return data;
    }

    @Override
    public List<Map<String, Object>> getItemList(Map<String, Object> params) {
        return getBaseMapper().getItemList(params);
    }

    @Override
    public String updateInfoAndGrade(EvalApply evalApply) {

        // 查询打分项并计算得分
        List<EvalApplyItems> evalApplyItemsList = evalApplyItemsService.selectItemList(evalApply.getId());

        // 计算分数
        Double score = calculateScore(evalApplyItemsList);
        evalApply.setScore(String.valueOf(score));

        // 更新数据
        getBaseMapper().updateById(evalApply);

        // 如果申请状态 status 等于 2，则当前申请通过，更新申请项目的申请数据id。
        if (evalApply.getStatus().equals("2")) {
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setId(evalApply.getProjectId());
            projectInfo.setEvalApplyId(evalApply.getId());

            // 更新数据
            projectInfoService.updateById(projectInfo);
        }

        return "";
    }

    /**
     *
     * @param classTypeList 应用类别列表 从小到大排序
     * @param evalApplyItemsList 打分项列表 根据应用类别从小到大排序
     * @return
     */
    private List<Map<String,Object>> createExportData(List<Map<String,Object>> classTypeList, List<EvalApplyItems> evalApplyItemsList){
        // 应用类别下标
        int index = 0;
        // 总分值
        BigDecimal total = new BigDecimal(0);
        // 当前应用类别实际总分值
        double sum = 0;
        // 当前应用类别对象
        Map<String, Object> classType = classTypeList.get(index);
        // 当前应用类别的打分项
        List<EvalApplyItems> tableList = (List<EvalApplyItems>) classType.get("tableList");

        // 理论上打分项下标未0的应用类别 等于应用类别表的值
        for (int i = 0; i<evalApplyItemsList.size(); i++) {
            EvalApplyItems item = evalApplyItemsList.get(i);

            // 如果当前应用类别对象和item对象，两者的应用类别不相等
            // 下标加1 总分值保存并归零 获取新的应用类别对象
            if (!classType.get("value").equals(item.getClassType())){
                classType.put("sum", sum);
                classType.put("total", total);

                // 旧的归零
                total = new BigDecimal(0);
                index += 1;
                sum = 0;
                classType = classTypeList.get(index);
                tableList = (List<EvalApplyItems>) classType.get("tableList");
            }

            total = total.add(item.getMaxData());
            sum += Double.valueOf(item.getItemScore());
            tableList.add(item);

            if (i == evalApplyItemsList.size()-1) {
                classType.put("sum", sum);
                classType.put("total", total);
            }
        }

        return classTypeList;

    }

    private Double calculateScore( List<EvalApplyItems> evalApplyItemsList) {
        Double score = 0.0;
        for (EvalApplyItems applyItem : evalApplyItemsList) {
            if (applyItem.getItemScore() == null || applyItem.getItemScore().equals("")) {
                score += 0.0;
            } else if ("10".equals(applyItem.getItemType())) {
                Double weight = 0.0;
                if ("1".equals(applyItem.getClassType())) {
                    weight = 0.15;
                } else if ("2".equals(applyItem.getClassType())) {
                    weight = 0.15;
                } else if ("3".equals(applyItem.getClassType())) {
                    weight = 0.4;
                } else if ("4".equals(applyItem.getClassType())) {
                    weight = 0.1;
                } else if ("5".equals(applyItem.getClassType())) {
                    weight = 0.1;
                } else if ("6".equals(applyItem.getClassType())) {
                    weight = 0.1;
                }
                Double itemScore = Double.valueOf(applyItem.getItemScore());
                Double weigthScore = itemScore * weight;
                score += Double.valueOf(String.format("%.4f", weigthScore));

            } else if ("20".equals(applyItem.getItemType())) {
                score += Double.valueOf(applyItem.getItemScore());
            } else if ("30".equals(applyItem.getItemType())) {
                score += Double.valueOf(applyItem.getItemScore());
            }
        }
        return score;
    }
}
