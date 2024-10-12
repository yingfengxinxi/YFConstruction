package com.qdoner.message.entity.PO;

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
 * 
 * </p>
 *
 * @author wudw
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wm_message_his")
@ApiModel(value="MessageHis对象", description="")
public class MessageHis implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private String type;

    private Integer businessType;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "数据内容")
    private String data;

    private Long byUser;

    @ApiModelProperty(value = "接收人")
    private Long toUser;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "是否已读 0:否 1:是")
    private String ifRead;

    private Date readTime;

    private Date delTime;

    @ApiModelProperty(value = "批次ID")
    private String batchId;

    @TableField(select = false)
    private Integer tenantId;


}
