package com.kakarote.build.equipment.constant;

/**
 * 点位数据标记
 * 2021-10-11 HJ 212-2017
 */
public enum CpFlagEnum {
    //在线监控（监测）仪器仪表-->监测设备
    N("N","监测设备工作正常"),
    F("F","监测设备停运"),
    M("M","监测设备处于维护期间产生的数据"),
    S("S","手工输入的设定值"),
    D("D","监测设备故障"),
    C("C","监测设备处于校准状态"),
    T("T","监测设备采样数值超过测量上限"),
    B("B","监测设备与数采仪通讯异常"),
    OTHER("OTHER","其他：监测设备扩充标记"),
    VERSION("VERSION","HJ 212-2017"),
    ;
    CpFlagEnum(String flag, String describe){
        this.flag = flag;
        this.describe = describe;
    }

    private final String  flag;
    private final String describe;

    public static CpFlagEnum  parse(String  flag){
        for(CpFlagEnum cpFlagEnum : CpFlagEnum.values()){
            if(cpFlagEnum.flag.equals(flag)){
                return cpFlagEnum;
            }
        }
        //TODO 未匹配上则范围OTHER
        return CpFlagEnum.OTHER;
    }


    public String getFlag(){
        return flag;
    }

    public String getDescribe(){
        return describe;
    }
}
