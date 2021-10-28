package com.jq;

import com.jq.config.Configuration;
import com.jq.config.ConfigurationBuilder;
import com.jq.enums.KeyConversionConfig;

import java.sql.Timestamp;

public class TransTest {

    public static void main(String[] args) {
        String json = "[{\"id\":\"1\",\"name\":\"谷歌\",\"url\":\"www.google.com\"},{\"id\":\"2\",\"name\":\"百度\",\"url\":\"www.baidu.com\"}]";

        Configuration configuration = ConfigurationBuilder.config()
                .withKeyAlias("url", "link")
                .addNewKey("time", new Timestamp(System.currentTimeMillis()).getTime())
                .withIgnoreKeys("name")
                .withKeyConversionConfig(KeyConversionConfig.nothing)
                .build();
        Json2sql.setConfiguration(configuration);

        String ddd = Json2sql.parse2String(json, "user");
        System.out.println(ddd);
    }
}
