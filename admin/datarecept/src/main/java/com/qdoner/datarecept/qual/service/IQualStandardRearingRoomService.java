package com.qdoner.datarecept.qual.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.BaseService;
import com.qdoner.datarecept.qual.entity.PO.QualStandardRearingRoom;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标养室监测信息 服务类
 * </p>
 *
 * @author lzy
 * @since 2023-05-07
 */
public interface IQualStandardRearingRoomService extends BaseService<QualStandardRearingRoom> {

    Result standardRearinaRoom(List<Map<String, Object>> list);
}
