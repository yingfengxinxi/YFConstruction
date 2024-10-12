package com.kakarote.build.qual.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author pz
 * @since 2021-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="单位工程划分", description="")
public class QualAcceptanceBO extends PageEntity {

    private String unitP;

    private String subUnitP;

    private String parcelP;

    private String subParcelP;

    private String itemP;

    private String inspectionLot;

    private String itemName;

    private String criteria;

}
