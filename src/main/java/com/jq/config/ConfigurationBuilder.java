package com.jq.config;

import com.jq.api.BeanProcessor;
import com.jq.enums.KeyConversionConfig;

import java.util.*;

/**
 * Description:  <br>
 *
 * @author byw
 * @create 2020/9/30
 */
public final class ConfigurationBuilder {
    private String varcharLength = "(255)";
    private String decimalPrecision = "(10,4)";
    private KeyConversionConfig keyConversionConfig = KeyConversionConfig.nothing;
    private List<String> ignoreKeys = new ArrayList<>();
    private List<Map<String, BeanProcessor>> beanProcessorMaps = new ArrayList<>();
    private Map<String, Object> addNewKeys = new HashMap<>();

    private ConfigurationBuilder() {
    }

    public static ConfigurationBuilder config() {
        return new ConfigurationBuilder();
    }

    public ConfigurationBuilder withVarcharLength(String varcharLength) {
        this.varcharLength = "(" + varcharLength + ")";
        return this;
    }

    public ConfigurationBuilder withDecimalPrecision(String decimalPrecision) {
        this.decimalPrecision = "(" + decimalPrecision + ")";
        return this;
    }

    public ConfigurationBuilder withKeyConversionConfig(KeyConversionConfig keyConversionConfig) {
        this.keyConversionConfig = keyConversionConfig;
        return this;
    }

    public ConfigurationBuilder withIgnoreKeys(List<String> ignoreKeys) {
        this.ignoreKeys = ignoreKeys;
        return this;
    }

    /**
     * 忽略的字段名
     *
     * @param key 忽略字段
     */
    public ConfigurationBuilder withIgnoreKeys(String key) {
        this.withIgnoreKeys(Collections.singletonList(key));
        return this;
    }

    /**
     * 额外添加新的字段
     *
     * @param key   新的key
     * @param value 对应的值
     */
    public ConfigurationBuilder addNewKey(String key, Object value) {
        addNewKeys.put(key, value);
        return this;
    }

    /**
     * json字段重命名
     *
     * @param key   原json字段名
     * @param alias 新名称
     */
    public ConfigurationBuilder withKeyAlias(String key, String alias) {
        Map<String, BeanProcessor> beanProcessorMap = new HashMap<>();
        beanProcessorMap.put(key, (BeanProcessor<Map<String, String>>) stringStringMap -> {
            String value = stringStringMap.get(key);
            return Map.of(alias, value);
        });
        this.withBeanProcessorMap(beanProcessorMap);
        return this;
    }

    public ConfigurationBuilder withBeanProcessorMap(String key, BeanProcessor beanProcessor) {
        Map<String, BeanProcessor> beanProcessorMap = new HashMap<>();
        beanProcessorMap.put(key, beanProcessor);
        this.withBeanProcessorMap(beanProcessorMap);
        return this;
    }

    public ConfigurationBuilder withBeanProcessorMap(Map<String, BeanProcessor> beanProcessorMap) {
        this.beanProcessorMaps.add(beanProcessorMap);
        return this;
    }

    public ConfigurationBuilder withBeanProcessorMaps(List<Map<String, BeanProcessor>> beanProcessorMaps) {
        this.beanProcessorMaps.addAll(beanProcessorMaps);
        return this;
    }

    public Configuration build() {
        Configuration configuration = new Configuration();
        configuration.setVarcharLength(varcharLength);
        configuration.setDecimalPrecision(decimalPrecision);
        configuration.setKeyConversionConfig(keyConversionConfig);
        configuration.setIgnoreKeys(ignoreKeys);
        configuration.setBeanProcessorMaps(beanProcessorMaps);
        configuration.setAddNewKeys(addNewKeys);
        return configuration;
    }
}
