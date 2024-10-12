package com.kakarote.build.wifi.entity.PO;

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
 * 教育视频配置--主表
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_wifi_video")
@ApiModel(value="WifiVideo对象", description="教育视频配置--主表")
public class WifiVideo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "教育模板名称")
    private String name;

    @ApiModelProperty(value = "适应工种id")
    private String workTypeId;

    @ApiModelProperty(value = "是否启用")
    private String ifStartUse;

    @ApiModelProperty(value = "教育模式（1：视频；2：答题；3：随机）")
    private String educationModel;

    @ApiModelProperty(value = "课程库的id组")
    private String courseLibraryIds;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Integer projectId;

    private Integer tenantId;

    @TableField(exist = false)
    private String batchId;

    @TableField(exist = false)
    private List<String> batchIds;
}
