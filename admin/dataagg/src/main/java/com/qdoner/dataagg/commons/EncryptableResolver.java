package com.qdoner.dataagg.commons;

import com.kakarote.core.utils.EncryptorUtil;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.stereotype.Component;

/**
 * 配置项解密
 */
@Component(value = "encryptablePropertyResolver")
public class EncryptableResolver implements EncryptablePropertyResolver {
    //加密工具
    private static StandardPBEStringEncryptor ENCRYPTOR = new StandardPBEStringEncryptor();

    static {
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setPassword(getPassword());//加密所需的salt(盐)
        ENCRYPTOR.setConfig(config);
    }

    @Override
    public String resolvePropertyValue(String value) {
        if (null != value && value.contains("ENC(")) {
            return EncryptorUtil.decrypt(ENCRYPTOR, value);
        }
        return value;
    }
    //独有的加密盐 这里建议修改
    private static String getPassword(){
        return "DATAAGG_WUDW";
    }
    /**
     * 密码生成
     * @param args
     */
    public static void main(String[] args){
        //EncryptorUtil.encryption("liangka",getPassword());
        EncryptorUtil.encryption("jdbc:${DATASOURCE_DBTYPE:mysql}://${DATASOURCE_HOST:47.104.161.205}:${DATASOURCE_PORT:3306}/wk_build",getPassword());
    }

}
