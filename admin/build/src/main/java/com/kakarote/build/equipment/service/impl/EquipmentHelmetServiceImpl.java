package com.kakarote.build.equipment.service.impl;

import cn.hutool.core.io.FileUtil;
import com.kakarote.build.equipment.entity.BO.EquipmentHelmetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHelmet;
import com.kakarote.build.equipment.mapper.EquipmentHelmetMapper;
import com.kakarote.build.equipment.service.IEquipmentHelmetService;
import com.kakarote.build.project.service.IProjectInfoService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 智能安全帽 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
@Service
public class EquipmentHelmetServiceImpl extends BaseServiceImpl<EquipmentHelmetMapper, EquipmentHelmet> implements IEquipmentHelmetService {

    @Autowired
    private IProjectInfoService projectInfoService;

    @Autowired
    private AdminFileService adminFileService;

    @Override
    public BasePage<EquipmentHelmet> selectByPage(EquipmentHelmetBO equipmentHelmetBO) {
        equipmentHelmetBO.setOrdersBd("a.create_time",false);
        return getBaseMapper().selectByPage(equipmentHelmetBO.parse(), equipmentHelmetBO);
    }

    @Override
    public BasePage<EquipmentHelmet> selectProList(EquipmentHelmetBO equipmentHelmetBO) {
        equipmentHelmetBO.setOrdersBd("a.create_time",false);
        return getBaseMapper().selectProList(equipmentHelmetBO.parse(), equipmentHelmetBO);
    }

    @Override
    public List<Map<String, Object>> selectLatestLocationList() {
        List<Map<String, Object>> list = getBaseMapper().selectLatestLocationList();
        return list;
    }

    @Override
    public List<Map<String, Object>> selectPeopleTrackList(Map<String, Object> params) {
        if (params.get("peopleId") == null){
            throw new CrmException(500, "参数peopleId不能为空!");
        }
        List<Map<String, Object>> list = getBaseMapper().selectPeopleTrackList(params);
        return list;
    }

    @Override
    public String getSvg(Integer id) {
        Map info =  projectInfoService.getLocationInfoById(id);
        String planeImg =  ((String)info.get("planeImg")).split("/")[3];
        String zb = (String)info.get("planeCoordinate");
        FileEntity fileEntity =   adminFileService.queryById(Long.valueOf(planeImg)).getData();
        String b64 = "";
        Integer width = 0;
        Integer height = 0;
        BufferedImage image ;
        try {
            b64 = Base64Utils.fileToBase64String(fileEntity.getPath());
            image = Base64Utils.getImageInfo(b64);
            width = image.getWidth();
            height = image.getHeight();
        }catch (Exception e){
            e.printStackTrace();
        }
        String str = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n" +
                "<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'>\n" +
                "<svg version='1.1' id='Layer_1' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' x='0px' y='0px' width='"+width+"px' height='"+height+"px' viewBox='0 0 "+width+" "+height+"' enable-background='new 0 0 "+width+" "+height+"' xml:space='preserve'>  <image id='image0' width='"+width+"' height='"+height+"' x='0' y='0'\n" +
                "    href='data:image/jpeg;base64,"+b64+"' />\n" +
                "</svg>";
        return str;
    }
}
