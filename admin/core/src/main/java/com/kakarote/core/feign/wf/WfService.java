package com.kakarote.core.feign.wf;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.wf.entity.WfVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * 流程处理
 */
@FeignClient(name = "wf", contextId = "wfHandle")
public interface WfService {

    /**
     * 审批提交
     * @param wfVO 业务审批对象
     * @return
     */
    @PostMapping("/wfExamine/submit")
    Result submit(WfVO wfVO);

    /**
     * 获取业务对象
     * @return
     */
    @PostMapping("/wfExamine/getObject")
    Result<Map<String, String>>  getObject(WfVO wfVO);
}
