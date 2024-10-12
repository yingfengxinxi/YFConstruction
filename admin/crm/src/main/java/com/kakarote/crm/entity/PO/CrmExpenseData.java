package com.kakarote.crm.entity.PO;

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
 * 费用自定义字段存值表
 * </p>
 *
 * @author author
 * @since 2021-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_expense_data")
@ApiModel(value="CrmExpenseData对象", description="费用自定义字段存值表")
public class CrmExpenseData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer fieldId;

    @ApiModelProperty(value = "字段名称")
    private String name;

    private String value;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String batchId;

    private Integer tenantId;


}
