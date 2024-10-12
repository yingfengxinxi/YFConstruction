package com.kakarote.build.wx.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel("劳务管理--考勤管理")
public class BWxFeedbackBO extends PageEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    @ApiModelProperty(value = "项目")
    private Integer projectId;

    @ApiModelProperty(value = "微信唯一标识")
    private String openid;

    @ApiModelProperty(value = "用户名称")
    private String feedbackName;

    @ApiModelProperty(value = "联系电话")
    private String feedbackTel;

    @ApiModelProperty(value = "反馈类型    类型值feedback_type_zh")
    private String feedbackType;

    @ApiModelProperty(value = "反馈内容")
    private String feedbackContent;

    @ApiModelProperty(value = "数据状态   0:未回复  1:已回复")
    private String feedbackState;

    @ApiModelProperty(value = "答复人")
    private String reply;

    @ApiModelProperty(value = "答复时间")
    private Date replyTime;

    @ApiModelProperty(value = "答复内容")
    private String replyContent;

    @ApiModelProperty(value = "是否解决   0:未解决 1:已解决")
    private String replyState;

    @ApiModelProperty(value = "满意情况  1非常不满意 2不满意 3一般 4比较满意 5非常满意")
    private String evaluationLevel;

    @ApiModelProperty(value = "评价内容")
    private String evaluation;
    @ApiModelProperty(value = "附件id")
    private String batchId;

    @ApiModelProperty(value = "是否已评价   0:未评价 1:已评价")
    private String evaluationInfo;

}