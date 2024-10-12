package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.BO.QualStandardRearingRoomBO;
import com.kakarote.build.qual.entity.PO.QualStandardRearingRoom;
import com.kakarote.build.qual.mapper.QualStandardRearingRoomMapper;
import com.kakarote.build.qual.service.IQualStandardRearingRoomService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标养室监测信息 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2023-05-07
 */
@Service
public class QualStandardRearingRoomServiceImpl extends BaseServiceImpl<QualStandardRearingRoomMapper, QualStandardRearingRoom> implements IQualStandardRearingRoomService {

    @Override
    public BasePage<QualStandardRearingRoom> selectByPage(QualStandardRearingRoomBO standardRearingRoomBO) {
        BasePage<QualStandardRearingRoom> list = this.baseMapper.selectByPage(standardRearingRoomBO.parse(), standardRearingRoomBO);
        return list;
    }
}
