package com.kakarote.authorization.service;

import com.kakarote.core.common.Result;

public interface DTalkLoginService {

    Result getCorpId(String configId);

    Result login(String code,String configId);
}
