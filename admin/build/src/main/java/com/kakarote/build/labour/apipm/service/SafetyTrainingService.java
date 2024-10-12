package com.kakarote.build.labour.apipm.service;

import com.kakarote.core.common.Result;

import java.util.Map;

public interface SafetyTrainingService {

    Result postSafetyTraining(Map<String, Object> parpam);

    Result safetyTrainingPeople(Map<String, Object> parpam);

    Result syncSafetyTraining(Map<String, Object> parpam);
}
