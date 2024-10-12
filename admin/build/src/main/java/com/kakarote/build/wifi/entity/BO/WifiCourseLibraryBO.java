package com.kakarote.build.wifi.entity.BO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * wifi教育-课程库
 * </p>
 *
 * @author wnj
 * @since 2021-08-19
 */
@Data
@ApiModel("wifi教育-课程库")
public class WifiCourseLibraryBO extends PageEntity {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value="课程视频名称")
    private String courseVideoName;

    @ApiModelProperty(value = "适应工种id")
    private String workTypeId;
}
