package com.kakarote.build.qual.service;

import com.kakarote.build.qual.entity.BO.QualStandardRearingRoomBO;
import com.kakarote.build.qual.entity.PO.QualStandardRearingRoom;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 标养室监测信息 服务类
 * </p>
 *
 * @author lzy
 * @since 2023-05-07
 */
public interface IQualStandardRearingRoomService extends BaseService<QualStandardRearingRoom> {

    BasePage<QualStandardRearingRoom> selectByPage(QualStandardRearingRoomBO standardRearingRoomBO);
}
