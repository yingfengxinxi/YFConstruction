package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 安全教育受教人员列表
 * </p>
 *
 * @author author
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_safety_training_members")
@ApiModel(value="BSafetySafetyTrainingMembers对象", description="安全教育受教人员列表")
public class BSafetySafetyTrainingMembers implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "所属培训教育id")
    private String trainingId;

    @ApiModelProperty(value = "人员id")
    private String infocollectionId;

    @ApiModelProperty(value = "人员名称")
    private String infocollectionName;

    @ApiModelProperty(value = "人员班组")
    private Integer infocollectionTeamName;


}
