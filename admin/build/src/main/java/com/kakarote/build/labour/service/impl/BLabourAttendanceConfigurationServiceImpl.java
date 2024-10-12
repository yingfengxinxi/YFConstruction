package com.kakarote.build.labour.service.impl;

import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceConfigurationBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceConfiguration;
import com.kakarote.build.labour.mapper.BLabourAttendanceConfigurationMapper;
import com.kakarote.build.labour.service.IBLabourAttendanceConfigurationService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 考勤日期设定 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-12-17
 */
@Service
public class BLabourAttendanceConfigurationServiceImpl extends BaseServiceImpl<BLabourAttendanceConfigurationMapper, BLabourAttendanceConfiguration> implements IBLabourAttendanceConfigurationService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BLabourAttendanceConfiguration> selectList(BLabourAttendanceConfigurationBO bLabourAttendanceConfigurationBO)
    {
        bLabourAttendanceConfigurationBO.setOrdersBd("week_data",true);
        bLabourAttendanceConfigurationBO.setOrdersBd("create_time",false);

        BasePage<BLabourAttendanceConfiguration> adminMessageBasePage = getBaseMapper().selectDataList(bLabourAttendanceConfigurationBO.parse(),bLabourAttendanceConfigurationBO);

        return adminMessageBasePage;
    }

    public List<BLabourAttendanceConfiguration> dataList(BLabourAttendanceConfiguration bLabourAttendanceConfiguration){

        List<BLabourAttendanceConfiguration>  list = getBaseMapper().dataList(bLabourAttendanceConfiguration);
        return list;
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String add(BLabourAttendanceConfiguration bLabourAttendanceConfiguration)
    {
        bLabourAttendanceConfiguration.setCreateBy(UserUtil.getUser().getRealname());
        bLabourAttendanceConfiguration.setCreateTime(new Date());
        if(!"".equals(bLabourAttendanceConfiguration.getWeekData())&&bLabourAttendanceConfiguration.getWeekData()!=null){
            if("1".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期一");
            }else if("2".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期二");
            }else if("3".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期三");
            }else if("4".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期四");
            }else if("5".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期五");
            }else if("6".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期六");
            }else if("7".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期日");
            }
        }
        save(bLabourAttendanceConfiguration);
        return "";
    }

    /**
     * 新增数据
     *
     */
    @Override
    public String edit(BLabourAttendanceConfiguration bLabourAttendanceConfiguration)
    {
        bLabourAttendanceConfiguration.setUpdateBy(UserUtil.getUser().getRealname());
        bLabourAttendanceConfiguration.setUpdateTime(new Date());
        if(!"".equals(bLabourAttendanceConfiguration.getWeekData())&&bLabourAttendanceConfiguration.getWeekData()!=null){
            if("1".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期一");
            }else if("2".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期二");
            }else if("3".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期三");
            }else if("4".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期四");
            }else if("5".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期五");
            }else if("6".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期六");
            }else if("7".equals(bLabourAttendanceConfiguration.getWeekData())){
                bLabourAttendanceConfiguration.setConfigurationContent("星期日");
            }
        }
        updateById(bLabourAttendanceConfiguration);
        return "";
    }

    /**
     * 新增数据验证
     *
     */
    @Override
    public String validation(BLabourAttendanceConfiguration bLabourAttendanceConfiguration)
    {
        String falg="0"; //判断是否已存在该类型数据 0不存  1已存在
       List<BLabourAttendanceConfiguration> data =  getBaseMapper().selectValidationt(bLabourAttendanceConfiguration);
        if(data.size()>0){
            falg ="1";
        }
        return falg;
    }
}
