package com.qdoner.message.entity.BO;

import com.kakarote.core.entity.PageEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
@Data
public class ExchangesBO extends PageEntity implements Serializable {

    private static final long serialVersionUID=1L;

    private String descr;

    private String name;
}
