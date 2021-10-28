package com.jq.bean;


import com.google.common.base.CaseFormat;
import com.jq.Json2sql;
import com.jq.api.BeanProcessor;
import com.jq.api.JSONParser;
import com.jq.config.Configuration;
import com.jq.enums.KeyConversionConfig;
import com.jq.impl.DefaultBeanProcessor;

import java.util.*;
import java.util.function.Function;

import static com.jq.Json2sql.*;

public abstract class AbstractJSONParser implements JSONParser {
    private final Configuration configuration = Json2sql.getConfiguration();

    protected String getVarchar() {
        return VARCHAR + configuration.getVarcharLength();
    }

    protected String getDecimal() {
        return DECIMAL + configuration.getDecimalPrecision();
    }

    protected String getDate() {
        return DATE;
    }

    protected String getInteger() {
        return INTEGER;
    }

    protected String getTimeStamp() {
        return TIMESTAMP;
    }

    /**
     * 将根据json解析出的map 生成sql需要的数据类型
     *
     * @param maps
     * @return
     */

    protected List<Map<String, String>> createTableMap(List<Map<String, Object>> maps, Function<String, String> function) {
        addNewKeys(maps);

        List<Map<String, String>> mapList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Map<String, String> stringMap = mapProcessor(map);
            Map<String, String> tableMap = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                if (ifIgnore(entry.getKey())) {
                    continue;
                }
//                String key = KeyConversionEnum.valueOf(configuration.getKeyConversionConfig().toString()).getKeyConversionStrategy().cover(entry.getKey());
                String key = trans(configuration.getKeyConversionConfig(), entry.getKey());
                String paramType;
                paramType = function.apply(entry.getValue());
                tableMap.put(key, paramType);
            }
            mapList.add(tableMap);
        }
        return mapList;
    }

    private String trans(KeyConversionConfig keyConversionConfig, String key) {
        switch (keyConversionConfig) {
            case hump2UnderscoreLower://驼峰转下划线——小写
                return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
            case hump2UnderscoreCapital://驼峰转下划线——大写
                return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, key);
            case nothing:
            default:
                return key;
        }
    }

    /**
     * 源数据添加新增的key
     */
    protected List<Map<String, Object>> addNewKeys(List<Map<String, Object>> maps) {
        Map<String, Object> addKeys = configuration.getAddNewKeys();
        maps.forEach(map -> map.putAll(addKeys));
        return maps;
    }

    private boolean ifIgnore(String key) {
        List<String> ignoreKeys = configuration.getIgnoreKeys();
        return ignoreKeys.contains(key);
    }

    private Map<String, String> mapProcessor(Map<String, Object> map) {

        List<Map<String, BeanProcessor>> beanProcessorMaps = configuration.getBeanProcessorMaps();
        Map<String, String> processNestedResult = processNestedBean(beanProcessorMaps, map);
        return processNestedResult;
    }

    private Map<String, String> processNestedBean(List<Map<String, BeanProcessor>> beanProcessorMaps, Map<String, Object> map) {
        Map<String, Object> temp = new HashMap<>(map);
        Map<String, String> stringMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(beanProcessorMaps)) {
        if (beanProcessorMaps != null && beanProcessorMaps.size() > 0) {
            //用BeanProcessor处理json中的特殊字段
            for (Map<String, BeanProcessor> beanProcessorMap : beanProcessorMaps) {
                for (Map.Entry<String, BeanProcessor> entry : beanProcessorMap.entrySet()) {
                    Object obj = temp.get(entry.getKey());
                    if (obj == null) {
                        continue;
                    }
                    BeanProcessor beanProcessor = entry.getValue();
                    //如果obj不是map就构造一个Map
                    if (!(obj instanceof Map)) {
                        Map<String, Object> hashMap = new HashMap<>();
                        hashMap.put(entry.getKey(), obj);
                        obj = hashMap;

                    }
                    Map<String, String> beanProcessorResultMap = beanProcessor.processor(obj);
                    stringMap.putAll(beanProcessorResultMap);
                    //处理完移除原来的元素
                    temp.remove(entry.getKey());
                }
            }
        }

        //如果使用者没有手动处理嵌套对象就自动处理
        Map<String, Object> autoProcessResult = autoProcess(temp);

        //剩下的元素都转换成String
        for (Map.Entry<String, Object> stringObjectEntry : autoProcessResult.entrySet()) {
            String value = null;
            if (stringObjectEntry.getValue() != null) {
                value = stringObjectEntry.getValue().toString();
            }
            stringMap.put(stringObjectEntry.getKey(), value);
        }
        return stringMap;
    }

    protected Map<String, Object> autoProcess(Map<String, Object> temp) {
        Map<String, Object> result = new HashMap<>(temp);

        for (Map.Entry<String, Object> entry : temp.entrySet()) {
            if (!(entry.getValue() instanceof Map)) {
                continue;
            }
            BeanProcessor<Map<String, Object>> beanProcessor = new DefaultBeanProcessor();
            Map<String, String> processorResult = beanProcessor.processor((Map<String, Object>) entry.getValue());
            result.remove(entry.getKey());
            result.putAll(processorResult);
        }
        return result;
    }
}
