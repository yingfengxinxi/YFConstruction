package com.kakarote.build.jg.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 工地垃圾分类管理
 * </p>
 *
 * @author author
 * @since 2022-02-09
 */

@Data
@ApiModel(value = "工地垃圾分类管理")
public class BJgSiteRubbishBO extends PageEntity {

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

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "垃圾场名称")
    private String dumpName;

    @ApiModelProperty(value = "垃圾类型 b_site_rubbish 字典类型 1:干垃圾 2:湿垃圾 3:可回收垃圾 4:有害垃圾")
    private String garbageType;

    @ApiModelProperty(value = "内容说明")
    private String content;

    @ApiModelProperty(value = "数量")
    private String garbageNumber;

    @ApiModelProperty(value = "数量单位")
    private String garbageUnit;

    @ApiModelProperty(value = "处理人")
    private String dealwithPeople;

    @ApiModelProperty(value = "处理时间")
    private String dealwithTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

}