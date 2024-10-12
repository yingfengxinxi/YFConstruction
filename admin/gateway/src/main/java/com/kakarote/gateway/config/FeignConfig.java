package com.kakarote.gateway.config;

import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.exception.FeignServiceException;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * feign客户端解码设置
 */
@Configuration
public class FeignConfig {
    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new FeignDecode(feignHttpMessageConverter()));
    }

    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new GateWayMappingJackson2HttpMessageConverter());

        return () -> httpMessageConverters;
    }

    private class GateWayMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        GateWayMappingJackson2HttpMessageConverter() {
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.APPLICATION_JSON);
            setSupportedMediaTypes(mediaTypes);
        }
    }

    public class FeignDecode extends SpringDecoder {


        FeignDecode(ObjectFactory<HttpMessageConverters> messageConverters) {
            super(messageConverters);
        }

        @Override
        public Object decode(Response response, Type type) throws FeignException, IOException, CrmException {
            Object data = super.decode(response, type);
            if (data instanceof Result) {
                if (!((Result) data).hasSuccess()) {
                    throw new FeignServiceException(((Result) data).getCode(), ((Result) data).getMsg());
                }
            }
            return data;
        }
    }
}
