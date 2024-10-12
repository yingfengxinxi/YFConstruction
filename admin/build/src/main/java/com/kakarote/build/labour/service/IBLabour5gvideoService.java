package com.kakarote.build.labour.service;

import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 5G超高清全景视频上传 服务类
 * </p>
 *
 * @author author
 * @since 2022-05-27
 */
public interface IBLabour5gvideoService extends BaseService<BLabour5gvideo> {

    /**
     * 查询数据列表
     *
     */
    public BasePage<BLabour5gvideoBO> selectList(BLabour5gvideoBO bLabour5gvideoBO);

    public String add(BLabour5gvideo bLabour5gvideo);

    public String edit(BLabour5gvideo bLabour5gvideo);

}
