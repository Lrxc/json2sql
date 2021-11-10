package com.jq;


import com.jq.api.JSONParser;
import com.jq.api.JSONWriter;
import com.jq.config.Configuration;
import com.jq.impl.ConvertProcessor;
import com.jq.impl.DefaultJSONParser;
import com.jq.impl.DefaultJSONWriter;

import java.util.List;
import java.util.Map;

public class Json2sql {
    public static final String DECIMAL = "DECIMAL";
    public static final String DATE = "DATE";
    public static final String INTEGER = "INTEGER";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String VARCHAR = "VARCHAR";

    private static Configuration configuration = new Configuration();

    /**
     * 将json解析成sql
     *
     * @param json      json字符串
     * @param tableName 表名字
     * @param path      生成sql文件存储路径 如果不传默认生成在当前项目目录
     */
    public static void parse(String json, String tableName, String... path) {
        Map<String, Object> sqlParamMap = parser(json, tableName);
        String outPath = null;
        if (path != null && path.length > 0) {
            outPath = path[0];
        }
        JSONWriter jsonWriter = new DefaultJSONWriter();
        jsonWriter.writer(sqlParamMap, outPath, tableName + ".sql");
    }

    /**
     * 将json解析成sql
     *
     * @param json      json字符串
     * @param tableName 表名字
     * @return string
     */
    public static String parse2String(String json, String tableName) {
        Map<String, Object> sqlParamMap = parser(json, tableName);
        JSONWriter jsonWriter = new DefaultJSONWriter();
        return jsonWriter.writer(sqlParamMap);
    }

    /**
     * 将json解析成map
     *
     * @param json json字符串
     * @return map
     */
    public static Map<String, Object> parse2Map(String json) {
        return parser(json, "test");
    }

    /**
     * 将json解析成 list
     *
     * @param json  json字符串
     * @param clazz list类型
     * @return list集合
     */
    public static <T> List<T> parse2List(String json, Class<T> clazz) {
        Map<String, Object> map = parser(json, "test");
        ConvertProcessor convertProcessor = new ConvertProcessor();
        return convertProcessor.mapToList(map, clazz);
    }

    private static Map<String, Object> parser(String json, String tableName) {
        JSONParser parser = new DefaultJSONParser();
        return parser.parse(json, tableName);
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        Json2sql.configuration = configuration;
    }
}
