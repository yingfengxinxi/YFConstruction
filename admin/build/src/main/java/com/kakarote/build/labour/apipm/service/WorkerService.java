package com.kakarote.build.labour.apipm.service;

import com.kakarote.core.common.Result;

import java.util.Map;

public interface WorkerService {
    Result postWorker(Map<String, Object> parpam);

    Result postWorkerAdvance(Map<String, Object> parpam);

    Result putWorkerContract(Map<String, Object> parpam);
}
