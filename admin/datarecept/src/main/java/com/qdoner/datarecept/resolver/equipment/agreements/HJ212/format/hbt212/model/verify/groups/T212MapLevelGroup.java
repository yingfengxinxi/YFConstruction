package com.qdoner.datarecept.resolver.equipment.agreements.HJ212.format.hbt212.model.verify.groups;

import com.qdoner.datarecept.resolver.equipment.agreements.HJ212.format.hbt212.model.DataFlag;

/**
 * T212Map级别 验证组
 * @see DataFlag#V0
 * Created by xiaoyao9184 on 2018/1/10.
 */
public interface T212MapLevelGroup {

    /**
     * 数据段级别
     */
    interface DataLevel{}

    /**
     * 数据区级别
     */
    interface CpDataLevel{}
}
