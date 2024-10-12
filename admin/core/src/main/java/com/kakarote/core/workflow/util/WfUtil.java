package com.kakarote.core.workflow.util;


import com.kakarote.core.feign.wf.entity.WfVO;

public class WfUtil {
    /**
     * 组装
     * @param businessType
     * @param checkUserId
     * @param isSubmit
     * @return
     */
    public static WfVO set(Integer businessType, Object obejctId, Long checkUserId, Boolean isSubmit) {
        WfVO wfVO = new WfVO();
        wfVO.setBusinessType(businessType);
        wfVO.setObjectId(obejctId);
        wfVO.setCheckUserId(checkUserId);
        wfVO.setIsSubmit(isSubmit);
        return wfVO;
    }

    /**
     * 是否可以修改
     * @param checkStatus
     * @return
     */
    public static boolean canEdit(Integer checkStatus){
        if(checkStatus.equals(0) || checkStatus.equals(1) || checkStatus.equals(3)){
            return false;
        }
        return true;
    }
}
