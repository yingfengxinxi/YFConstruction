package com.kakarote.build.wifi.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
 * WiFi教育答题--问题类别管理
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_wifi_quest_type")
@ApiModel(value="WifiQuestType对象", description="WiFi教育答题--问题类别管理")
public class WifiQuestType implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "父级ID")
    @TableField("parentId")
    private String parentId;

    @ApiModelProperty(value = "试题类型名称")
    private String typeName;

    @ApiModelProperty(value = "试题类型编码")
    private String typeCode;

    @ApiModelProperty(value = "试题描述")
    private String typeDescribe;

    @ApiModelProperty(value = "排序号")
    private Integer typeNum;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    private Integer tenantId;

    private Integer projectId;

    @TableField(exist = false)
    private String pname;

}
