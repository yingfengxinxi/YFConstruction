package com.kakarote.core.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author pz
 * @since 2021-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_sequence")
@ApiModel(value="Sequence对象", description="")
public class Sequence implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "1：质量巡检、2：物料称重、3：安全巡检、4：环保事件、5：静态风险、6：动态风险、7：安全隐患排查、8：质量隐患排查")
    private String type;

    private Date date;

    private Integer indez;

    private Integer tenantId;

    private Integer projectId;
}
