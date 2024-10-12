package com.qdoner.datarecept.env.mapper;

import com.qdoner.datarecept.env.entity.PO.EnvPostData;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 扬尘在线监测设备数据 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2021-08-23
 */
public interface EnvPostDataMapper extends BaseMapper<EnvPostData> {

    int batchSaveData(@Param("list") List<EnvPostData> list);
}
