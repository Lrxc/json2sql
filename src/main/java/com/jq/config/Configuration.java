package com.jq.config;

import com.jq.api.BeanProcessor;
import com.jq.enums.KeyConversionConfig;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Configuration {

    /**
     * json key转成sql时如何转换
     */
    private KeyConversionConfig keyConversionConfig = KeyConversionConfig.nothing;

    /**
     * varchar 类型的长度 默认 255
     */
    private String varcharLength = "(255)";

    /**
     * decimal 类型精度 默认 (10,4)
     */
    private String decimalPrecision = "(10,4)";

    /**
     * list类型表示可以注册多个处理器
     * map 的key为要处理javabean的key
     */
    private List<Map<String, BeanProcessor>> beanProcessorMaps = new ArrayList<>();

    /**
     * 要忽略不处理的字段
     */
    private List<String> ignoreKeys = new ArrayList<>();

    /**
     * 额外添加新的字段
     */
    private Map<String, Object> addNewKeys = new HashMap<>();
}
