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
 * 教育视频观看记录
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_wifi_video_watch_record")
@ApiModel(value="WifiVideoWatchRecord对象", description="教育视频观看记录")
public class WifiVideoWatchRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "教育模板id")
    @TableField(value = "v_id")
    private String vId;

    @ApiModelProperty(value = "教育模板名称")
    @TableField(value = "v_name")
    private String vName;

    @ApiModelProperty(value = "视频明细id")
    private String vdId;

    @ApiModelProperty(value = "观看时间")
    private String watchTime;

    @ApiModelProperty(value = "观看人员id")
    private Long watchPeopleId;

    @ApiModelProperty(value = "观看人员姓名")
    private String watchPeopleName;

    @ApiModelProperty(value = "观看人员手机号")
    private String watchPeoplePhone;

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
    private String videoName;

    @TableField(exist = false)
    private String videoId;

    @TableField(exist = false)
    private String fileId;
}
