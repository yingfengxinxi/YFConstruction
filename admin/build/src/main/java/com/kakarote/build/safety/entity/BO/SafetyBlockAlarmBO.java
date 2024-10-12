package com.kakarote.build.safety.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 周界防护报警
 * </p>
 *
 * @author zy
 * @since 2021-06-10
 */
@Data
@ApiModel(description="周界防护报警")
public class SafetyBlockAlarmBO extends PageEntity {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "边界区块防护网编号")
    private String boundaryBlockCode;

    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;



    @ApiModelProperty(value = "报警类型")
    private String alarmType;

    private String alarmTimeStart;

    private String alarmTimeEnd;






}
