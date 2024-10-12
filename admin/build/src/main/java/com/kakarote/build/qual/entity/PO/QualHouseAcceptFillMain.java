package com.kakarote.build.qual.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 施工质量--分户验收--填报--主表
 * </p>
 *
 * @author wnj
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_qual_house_accept_fill_main")
@ApiModel(value="QualHouseAcceptFillMain对象", description="施工质量--分户验收--填报--主表")
public class QualHouseAcceptFillMain implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "accept_id", type = IdType.AUTO)
    private Integer acceptId;

    @ApiModelProperty(value = "楼座id")
    private Integer buildingId;

    @ApiModelProperty(value = "房号id")
    private Integer houseId;

    @ApiModelProperty(value = "验收工序")
    private String acceptWork;

    @ApiModelProperty(value = "验收模板id")
    private Integer templateNameId;

    @ApiModelProperty(value = "验收时间")
    private Date acceptTime;

    @ApiModelProperty(value = "验收人")
    private String acceptBy;

    @ApiModelProperty(value = "验收结论")
    private String acceptConclusion;

    @ApiModelProperty(value = "是否通过")
    private String isAdopt;

    @ApiModelProperty(value = "定位--经度(APP专用)")
    private String locationLon;

    @ApiModelProperty(value = "定位--纬度(APP专用)")
    private String locationLat;

    @ApiModelProperty(value = "人员现场照片批次(APP专用)")
    private String batchId;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

}
