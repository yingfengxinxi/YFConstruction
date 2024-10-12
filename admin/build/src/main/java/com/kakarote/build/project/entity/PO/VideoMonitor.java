package com.kakarote.build.project.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 视频监控
 * </p>
 *
 * @author wudw
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_video_monitor")
@ApiModel(value="VideoMonitor对象", description="视频监控")
public class VideoMonitor implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    private Integer parentId;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "监控名称")
    private String monitorName;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "视频编号")
    private String videoCode;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "分组")
    private String groups;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否存在下级节点")
    private boolean hasChildren;



}
