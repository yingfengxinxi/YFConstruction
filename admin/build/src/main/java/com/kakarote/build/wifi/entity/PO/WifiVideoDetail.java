package com.kakarote.build.wifi.entity.PO;

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
 * 教育视频配置--明细表
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_wifi_video_detail")
@ApiModel(value="WifiVideoDetail对象", description="教育视频配置--明细表")
public class WifiVideoDetail implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "教育视频模板id")
    private String vid;

//    @ApiModelProperty(value = "视频URL")
//    private String videoUrl;

    @ApiModelProperty(value = "批次")
    private String batchId;

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


}
