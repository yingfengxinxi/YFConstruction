package com.kakarote.build.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人员进场退场管理
 * </p>
 *
 * @author shz
 * @since 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_advance_retreat")
@ApiModel(value="LabourAdvanceRetreat对象", description="人员进场退场管理")
public class LabourAdvanceRetreat implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "工人所属企业统一社会信用编码")
    private String corpCode;

    @ApiModelProperty(value = "工人所属企业名称")
    private String corpName;

    @ApiModelProperty(value = "分包商id")
    private Integer subconId;

    @ApiModelProperty(value = "班组编号")
    private Integer teamId;

    @ApiModelProperty(value = "证件类型")
    private String idCardType;

    @ApiModelProperty(value = "证件号码")
    private String idCardNumber;

    @ApiModelProperty(value = "进退场日期")
    private Date date;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "证件图片")
    private Integer batchId;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;


}
