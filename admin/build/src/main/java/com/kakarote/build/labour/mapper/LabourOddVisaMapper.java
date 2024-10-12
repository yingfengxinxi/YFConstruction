package com.kakarote.build.labour.mapper;

import com.kakarote.build.labour.entity.BO.LabourOddVisaBO;
import com.kakarote.build.labour.entity.PO.LabourOddVisa;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiVideo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 零工签证 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-06-07
 */
public interface LabourOddVisaMapper extends BaseMapper<LabourOddVisa> {

    BasePage<Map<String,Object>> selectList(BasePage<LabourOddVisa> page, @Param("data") LabourOddVisaBO labourOddVisa);

    List<Map> selectListNoPage(@Param("data") LabourOddVisaBO labourOddVisa);

}
