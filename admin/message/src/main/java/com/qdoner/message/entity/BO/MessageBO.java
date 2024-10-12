package com.qdoner.message.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class MessageBO extends PageEntity implements Serializable  {

    private static final long serialVersionUID=1L;

    private String id;

    /**
     * 消息类型
     */
    private String type;

    @ApiModelProperty(value = "接收人")
    private Long toUser;

    @ApiModelProperty(value = "是否已读 0:否 1:是")
    private String ifRead;

    private Date createTime;
}
