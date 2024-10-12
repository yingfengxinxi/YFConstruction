package com.qdoner.datarecept.labour.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 劳务管理--班组
 * </p>
 *
 * @author shz
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_labour_team")
@ApiModel(value="LabourTeam对象", description="劳务管理--班组")
public class LabourTeam implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "班组长")
    private Integer teamLeaderId;

    @ApiModelProperty(value = "班组长名称")
    private String teamLeaderName;

    @ApiModelProperty(value = "工种")
    private String workTypeId;

    @ApiModelProperty(value = "班组名称")
    private String teamName;

    @ApiModelProperty(value = "分包单位Id")
    private Integer subcontractorId;

    @ApiModelProperty(value = "分包单位名称")
    private String subcontractorName;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "联系方式")
    private String phone;

    @ApiModelProperty(value = "提交平台状态 字典项")
    private String isSendStatus;

    @ApiModelProperty(value = "班组首拼")
    private String teamSpell;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "进场日期")
    private Date entryTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "退场日期")
    private Date exitTime;

    @ApiModelProperty(value = "进场附件，有进场日期时，此字段必填 批次id")
    private String entryAttachments;

    @ApiModelProperty(value = "退场附件 有出场日期时，此字段必填 批次id")
    private String exitAttachments;

    @ApiModelProperty(value = "班组编号-全国建筑工人管理服务信息平台返回的编码")
    private Integer teamSysNo;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;


}
