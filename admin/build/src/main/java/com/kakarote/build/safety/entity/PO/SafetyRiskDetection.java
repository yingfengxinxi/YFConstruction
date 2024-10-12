package com.kakarote.build.safety.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.servlet.upload.FileEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 安全隐患排查
 * </p>
 *
 * @author lzy
 * @since 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_safety_risk_detection")
@ApiModel(value="SafetyRiskDetection对象", description="安全隐患排查")
public class SafetyRiskDetection implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "隐患编号")
    private String hiddenNum;

    @ApiModelProperty(value = "隐患类别")
    private String category;

    @ApiModelProperty(value = "隐患等级")
    private String level;

    @ApiModelProperty(value = "风险类型 1：静态风险数据；2：动态风险数据；")
    private String riskType;

    @ApiModelProperty(value = "风险点Id")
    private Integer riskId;

    @ApiModelProperty(value = "风险点编号")
    private String riskNum;

    @ApiModelProperty(value = "风险点名称")
    private String riskName;

    @ApiModelProperty(value = "隐患描述")
    private String riskDesc;

    @ApiModelProperty(value = "事故类型")
    private String accidentType;

    @ApiModelProperty(value = "隐患状态描述 0：已检查；1：已下发；:2：已整改；3：已复检；4：无需整改")
    private String infoState;

    @ApiModelProperty(value = "主要治理措施")
    private String administerStep;

    @ApiModelProperty(value = "检查单位 当前项目建设单位")
    private String examineUnit;

    @ApiModelProperty(value = "检查单位统一社会信用代码  当前项目建设单位")
    private String examineUnitCode;

    @ApiModelProperty(value = "检查负责人 当前登录用户")
    private Long checkPersonId;

    @ApiModelProperty(value = "检查负责人姓名")
    private String checkPersonName;

    @ApiModelProperty(value = "检查负责人手机号")
    private String checkPersonPhone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "检查日期")
    private Date checkTime;

    @ApiModelProperty(value = "检查类型")
    private String routineCheckType;

    @ApiModelProperty(value = "检查结果 无需整改-需整改-合格")
    private String chekcResult;

    @ApiModelProperty(value = "检查区域")
    private String checkArea;

    @ApiModelProperty(value = "整改责任单位 数据源：分包商")
    private Integer dutyUnitId;

    @ApiModelProperty(value = "整改情况")
    private String dutySituation;

    @ApiModelProperty(value = "整改负责人 分包商下组长")
    private Integer rectifyPersonId;

    @ApiModelProperty(value = "整改负责人手机号")
    private String rectifyPersonPhone;

    @ApiModelProperty(value = "实际整改人姓名")
    private String actualRectifyPersonId;

    @ApiModelProperty(value = "实际整改人姓名")
    private String actualRectifyPersonName;

    @ApiModelProperty(value = "实际整改人手机号")
    private String actualRectifyPersonPhone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "整改期限")
    private Date rectifyTime;

    @ApiModelProperty(value = "整改时间")
    private Date rectifyCompleteTime;

    @ApiModelProperty(value = "复查情况")
    private String reExamineSituttion;

    @ApiModelProperty(value = "复查完成时间")
    private Date reviewTime;

    @ApiModelProperty(value = "复查人id 当前登录用户")
    private Long examinePersonId;

    @ApiModelProperty(value = "复查人姓名")
    private String examinePersonName;

    @ApiModelProperty(value = "复查人手机号")
    private String examinePersonPhone;

    @ApiModelProperty(value = "备注")
    private String supplementInfo;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUserId;

    @ApiModelProperty(value = "隐患图片id")
    private String batchId;

    @ApiModelProperty(value = "整改图片id")
    private String rectifyBatchId;

    @ApiModelProperty(value = "是否删除 0：未删除 1：删除")
    private String isDelete;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片")
    private List<FileEntity> imgs;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否整改")
    private String isRectify;

}
