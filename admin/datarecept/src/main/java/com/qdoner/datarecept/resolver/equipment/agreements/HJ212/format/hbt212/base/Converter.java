package com.qdoner.datarecept.resolver.equipment.agreements.HJ212.format.hbt212.base;

/**
 * Created by xiaoyao9184 on 2018/1/3.
 */
public interface Converter<SRC,TAR> {

    TAR convert(SRC src) throws Exception;
}
