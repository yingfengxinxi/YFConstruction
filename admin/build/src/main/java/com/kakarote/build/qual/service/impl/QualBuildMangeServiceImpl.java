package com.kakarote.build.qual.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.qual.entity.PO.QualBuildMange;
import com.kakarote.build.qual.mapper.QualBuildMangeMapper;
import com.kakarote.build.qual.service.IQualBuildMangeService;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 施工质量--分户验收--楼栋管理 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-06-22
 */
@Service
public class QualBuildMangeServiceImpl extends BaseServiceImpl<QualBuildMangeMapper, QualBuildMange> implements IQualBuildMangeService {

    @Override
    public List<QualBuildMange> queryByParentId(Integer parentId) {
        return getBaseMapper().queryByParentId(parentId);
    }

    @Override
    public boolean removeListById(Integer buildingId) {

        // 需删除的主键列表
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(buildingId);
        // 递归
        this.getChildId(ids, buildingId);
        // 批量删除集合中的数据
        int i = getBaseMapper().deleteBatchIds(ids);

        return i > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int Save(QualBuildMange qualBuildMange) {
        int count = 0;
        if (qualBuildMange != null) {
            if (qualBuildMange.getFloorNumber() != null && qualBuildMange.getRoomInFloor() != null) {
                Map<String, String> map = formatIsTrue(qualBuildMange.getFloorIsCheck());
                String floorSuffixStart = map.get("suffixStart");
                String floorSuffixEnd = map.get("suffixEnd");

                Map<String, String> map2 = formatIsTrue(qualBuildMange.getRoomIsCheck());
                String roomSuffixStart = map2.get("suffixStart");
                String roomSuffixEnd = map2.get("suffixEnd");
                qualBuildMange.setCreateBy(UserUtil.getUserId());
                qualBuildMange.setCreateTime(new Date());
                qualBuildMange.setTreeNodeType("0");
                getBaseMapper().Save(qualBuildMange);
                int BuildingId = qualBuildMange.getBuildingId();
                count++;
                for (int i = 0; i < qualBuildMange.getFloorNumber(); i++) {
                    int floor = i + 1;
                    QualBuildMange Twolevel = new QualBuildMange();
                    Twolevel.setParentId(BuildingId);
                    Twolevel.setBuildingName(floorSuffixStart + floor + floorSuffixEnd);
                    Twolevel.setTreeNodeType("1");
                    Twolevel.setSort(i);
                    Twolevel.setStatus(1);
                    Twolevel.setCreateBy(UserUtil.getUserId());
                    Twolevel.setCreateTime(new Date());
                    getBaseMapper().Save(Twolevel);
                    int parentId = Twolevel.getBuildingId();
                    count++;
                    for (int j = 0; j < qualBuildMange.getRoomInFloor(); j++) {
                        int room = j + 1;
                        QualBuildMange Threelevel = new QualBuildMange();
                        Threelevel.setParentId(parentId);
                        Threelevel.setBuildingName(roomSuffixStart + (floor + "0" + room) + roomSuffixEnd);
                        Threelevel.setTreeNodeType("2");
                        Threelevel.setSort(j);
                        Threelevel.setStatus(1);
                        Threelevel.setCreateBy(UserUtil.getUserId());
                        Threelevel.setCreateTime(new Date());
                        getBaseMapper().Save(Threelevel);
                        int Id = Threelevel.getBuildingId();
                        count++;
                    }
                }
            } else {
                throw new CrmException(403, "请求参数校验失败");
            }

        } else {
            throw new CrmException(403, "请求参数为空");
        }
        return count;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int CopyMenu(QualBuildMange qualBuildMange) {
        List<Map<String, Object>> old_list = queryAllList(qualBuildMange.getBuildingId());
        JSONArray jsArr = JSONObject.parseArray(qualBuildMange.getBuildingNames());
        for(int i=0;i<jsArr.size();i++){
            JSONObject js= (JSONObject) jsArr.get(i);
            QualBuildMange level = new QualBuildMange();
            level.setParentId(qualBuildMange.getParentId());
            level.setBuildingName(StringUtils.isEmpty(String.valueOf(js.get("buildingName")))?((i+1)+"#"):String.valueOf(js.get("buildingName")));
            level.setTreeNodeType(qualBuildMange.getTreeNodeType());
            level.setSort(qualBuildMange.getSort());
            level.setStatus(qualBuildMange.getStatus());
            level.setCreateBy(UserUtil.getUserId());
            level.setCreateTime(new Date());
            getBaseMapper().Save(level);
            int id=level.getBuildingId();
            setTreeChild(old_list,id);
        }
        return 0;
    }

    @Override
    public boolean SaveDistrib(Integer houseTypeId, List<QualBuildMange> qualBuildMange) {
       boolean flag=false;
       if(houseTypeId!=null){
           for(QualBuildMange QBM:qualBuildMange){
               if("2".equals(QBM.getTreeNodeType())){
                   QBM.setDistribHouseTypeId(String.valueOf(houseTypeId));
                   flag= updateById(QBM);
                   if(!flag){
                       throw new CrmException(1001,"批量更新失败");
                   }
               }
           }
       }else{
           throw new CrmException(1002,"分配户型为空");
       }

        return flag;
    }
    @Override
    public List<Map<String, Object>> queryTreeAllList(Integer pid) {
        QueryWrapper<QualBuildMange> wrapper = new QueryWrapper<>();
        // 查询条件
        wrapper.eq("parent_id", pid);
        List<Map<String, Object>> list = baseMapper.selectMaps(wrapper);
        for (Map<String, Object> q : list) {
            List<Map<String, Object>> children = queryTreeAllList((Integer) q.get("buildingId"));
            q.put("children", children);
            q.put("id",(Integer)q.get("buildingId"));
            q.put("name",String.valueOf(q.get("buildingName")));
        }
        return list;
    }
    @Override
    public  List<Map<String, Object>> getAllHouseList(){
        QueryWrapper<QualBuildMange> wrapper = new QueryWrapper<>();
        wrapper.eq("tree_node_type","2");
        List<Map<String, Object>> list = baseMapper.selectMaps(wrapper);
        return list;
    }

    @Override
    public List<QualBuildMange> wXqueryByParentId(QualBuildMange qualBuildMange) {
        return getBaseMapper().wXqueryByParentId(qualBuildMange);
    }

    private void setTreeChild(List<Map<String, Object>> old_list, Integer MainId) {
        for (int i = 0; i < old_list.size(); i++) {
            QualBuildMange level = new QualBuildMange();
            if(MainId!=null){
                level.setParentId(MainId);
            }else{
                level.setParentId(Integer.valueOf(String.valueOf(old_list.get(i).get("parentId"))));
            }
            level.setBuildingName(String.valueOf(old_list.get(i).get("buildingName")));
            level.setTreeNodeType(String.valueOf(old_list.get(i).get("treeNodeType")));
            level.setSort(Integer.valueOf(String.valueOf(old_list.get(i).get("sort"))));
            level.setStatus(Integer.valueOf(String.valueOf(old_list.get(i).get("status"))));
            level.setCreateBy(UserUtil.getUserId());
            level.setCreateTime(new Date());
            getBaseMapper().Save(level);
            int Id = level.getBuildingId();
            if (old_list.get(i).containsKey("children")) {
                List<Map<String, Object>> two = (List) old_list.get(i).get("children");
//                for(Map<String, Object> map:two){
//                    map.put("parentId",Id);
//                }
                this.setTreeChild(two,Id);
            }
        }
    }

    @Override
    public List<Map<String, Object>> queryAllList(Integer pId) {
        QueryWrapper<QualBuildMange> wrapper = new QueryWrapper<>();
        // 查询条件
        wrapper.eq("parent_id", pId);
        List<Map<String, Object>> list = baseMapper.selectMaps(wrapper);

        for (Map<String, Object> q : list) {
            List<Map<String, Object>> children = queryAllList((Integer) q.get("buildingId"));
            q.put("children", children);
            if(children.size()>=0&&!"2".equals(String.valueOf(q.get("treeNodeType")))){
                q.put("disabled", true);
            }else{
                q.put("disabled", false);
            }

        }
        return list;
    }

    //查询所有子级数据
    private List<Map<String, Object>> getChildData(Integer id) {
        QueryWrapper<QualBuildMange> wrapper = new QueryWrapper<>();
        // 查询条件
        wrapper.eq("building_id", id);
        List<Map<String, Object>> list = baseMapper.selectMaps(wrapper);

        for (Map<String, Object> q : list) {
            List<Map<String, Object>> children = getChildData((Integer) q.get("buildingId"));
            q.put("children", children);
        }
        return list;
    }

    private Map<String, String> formatIsTrue(String FloorIsCheck) {
        String suffixStart = "";
        String suffixEnd = "F";
        if (FloorIsCheck != null && FloorIsCheck != "") {
            if (FloorIsCheck.indexOf("[[") == -1 || FloorIsCheck.indexOf("]]") == -1) {
                throw new CrmException(403, "模板名称内容错误");
            }
            suffixStart = FloorIsCheck.substring(0, FloorIsCheck.indexOf("[["));
            suffixEnd = FloorIsCheck.substring(FloorIsCheck.indexOf("]]") + 2, FloorIsCheck.length());
        } else {
            throw new CrmException(403, "模板名称内容为空");
        }
        Map<String, String> map = new HashMap<>();
        map.put("suffixStart", suffixStart);
        map.put("suffixEnd", suffixEnd);
        return map;
    }

    // 递归查询出所有的子级id
    private void getChildId(ArrayList<Integer> ids, Integer id) {
        // 查询二级分类的对象
        QueryWrapper<QualBuildMange> wrapper = new QueryWrapper<>();
        // 查询条件
        wrapper.eq("parent_id", id);
        List<QualBuildMange> list = baseMapper.selectList(wrapper);

        for (QualBuildMange qualBuildMange : list) {
            Integer buildingId = qualBuildMange.getBuildingId();
            ids.add(buildingId);
            this.getChildId(ids, buildingId);
        }
    }
}
