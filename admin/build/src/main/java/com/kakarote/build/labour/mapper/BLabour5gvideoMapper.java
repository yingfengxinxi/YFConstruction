package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 5G超高清全景视频上传 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-05-27
 */
public interface BLabour5gvideoMapper extends BaseMapper<BLabour5gvideo> {

    BasePage<BLabour5gvideoBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BLabour5gvideoBO bLabour5gvideoBO);
}
