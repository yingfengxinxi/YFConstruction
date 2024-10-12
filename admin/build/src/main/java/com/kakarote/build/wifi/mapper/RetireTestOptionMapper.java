package com.kakarote.build.wifi.mapper;

import com.kakarote.build.wifi.entity.BO.RetireTestOptionBO;
import com.kakarote.build.wifi.entity.PO.RetireTestOption;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 试卷问题选项 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
public interface RetireTestOptionMapper extends BaseMapper<RetireTestOption> {
    List<Map<String,Object>> getAllTestOption(@Param("data") Map<String, Object> map2);

    int MyUpdateById(@Param("data") RetireTestOptionBO rtob);

    int insertOption(@Param("data") Map<String, Object> TPO);
}
