package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.mapper.BLabour5gvideoMapper;
import com.kakarote.build.labour.service.IBLabour5gvideoService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 5G超高清全景视频上传 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-05-27
 */
@Service
public class BLabour5gvideoServiceImpl extends BaseServiceImpl<BLabour5gvideoMapper, BLabour5gvideo> implements IBLabour5gvideoService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BLabour5gvideoBO> selectList(BLabour5gvideoBO bLabour5gvideoBO)
    {
        bLabour5gvideoBO.setOrdersBd("create_time", false);
        BasePage<BLabour5gvideoBO> adminMessageBasePage = getBaseMapper().selectDataList(bLabour5gvideoBO.parse(),bLabour5gvideoBO);
        return adminMessageBasePage;
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String add(BLabour5gvideo bLabour5gvideo)
    {
        bLabour5gvideo.setCreateTime(new Date());
        save(bLabour5gvideo);
        return  "1";
    }

    /**
     * 修改数据
     *
     */
    @Override
    public String edit(BLabour5gvideo bLabour5gvideo)
    {
        bLabour5gvideo.setUpdateTime(new Date());
        updateById(bLabour5gvideo);
        return  "1";
    }
}
