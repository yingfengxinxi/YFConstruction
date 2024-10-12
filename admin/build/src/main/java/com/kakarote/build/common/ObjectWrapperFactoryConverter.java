package com.kakarote.build.common;

/**
 * @Description TODO
 * @Author PangZhen
 * @Date 2021-03-17 14:07
 */
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ObjectWrapperFactoryConverter implements Converter<String,ObjectWrapperFactory> {
    @Override
    public ObjectWrapperFactory convert(String source) {
        try {
            return (ObjectWrapperFactory) Class.forName(source).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}