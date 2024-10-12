package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.BLabourDroneBO;
import com.kakarote.build.labour.entity.PO.BLabourDrone;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 无人机现场巡检 服务类
 * </p>
 *
 * @author author
 * @since 2022-05-27
 */
public interface IBLabourDroneService extends BaseService<BLabourDrone> {
    /**
     * 查询数据列表
     *
     */
    public BasePage<BLabourDroneBO> selectList(BLabourDroneBO bLabourDroneBO);

    public String add(BLabourDrone bLabourDrone);

    public String edit(BLabourDrone bLabourDrone);

    public List<Map<String, Object>> selectFileList(String bithid);
}
