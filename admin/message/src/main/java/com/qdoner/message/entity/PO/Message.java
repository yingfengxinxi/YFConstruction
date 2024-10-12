package com.qdoner.message.entity.PO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@TableName("wm_message")
@ApiModel(value="Message对象", description="")
public class Message implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private String type;

    @ApiModelProperty(value = "标题")
    private String title;

    private Integer businessType;

    @ApiModelProperty(value = "数据内容")
    private String data;

    @ApiModelProperty(value = "发送内容")
    private String msgContent;

    private Long byUser;

    @TableField(exist = false)
    private String byUserName;

    @ApiModelProperty(value = "接收人")
    private Long toUser;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "是否已读 0:否 1:是")
    private String ifRead;

    private Date readTime;

    @ApiModelProperty(value = "批次ID")
    private String batchId;

    @TableField(select = false)
    private Integer tenantId;

    //web端配置
    @TableField(exist = false)
    private String isSys;

    @TableField(exist = false)
    private String icon;

    @TableField(exist = false)
    private String clickOpen;

    @TableField(exist = false)
    private String url;

//    public String getMsgContentHtml(){
//        if(this.msgContent != null){
//            return this.msgContent.replace(this.title,"<span style='color: #2362fb;cursor: pointer;'>"+this.title+"</span>");
//        }
//        return "";
//    }
}
