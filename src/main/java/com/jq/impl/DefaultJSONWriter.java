package com.jq.impl;

import com.jq.Json2sql;
import com.jq.api.JSONWriter;
import com.jq.bean.InsertDTO;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultJSONWriter implements JSONWriter {

    @Override
    public void writer(Map<String, Object> sqlParamMap, String path, String sqlFileName) {
        String sqlStr = buildInsertSql(sqlParamMap);

        try {
            String outFileName = path + sqlFileName;
            File outputFile = new File(outFileName);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            FileWriter writer = new FileWriter(outFileName, true);
            writer.write(sqlStr);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String writer(Map<String, Object> sqlParamMap) {
        return buildInsertSql(sqlParamMap);
    }

    private static String buildInsertSql(Map<String, Object> sqlParamMap) {
        StringBuilder insertSql = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator", "\n");

        String tableName = (String) sqlParamMap.get("tableName");
        Map<String, String> tableParam = (Map<String, String>) sqlParamMap.get("tableParam");
        List<InsertDTO> insertParam = (List<InsertDTO>) sqlParamMap.get("insertParam");

        insertParam.forEach(treeBean -> {
            List<String> keys = treeBean.getKeys();
            List<String> values = treeBean.getValues();

            List<String> newValue = new ArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                String key = keys.get(i);
                String value = values.get(i);
                //字段类型
                String type = tableParam.getOrDefault(key, Json2sql.VARCHAR);

                if (type.contains(Json2sql.DECIMAL)) {
                    newValue.add(value);
                } else if (type.contains(Json2sql.DATE)) {
                    newValue.add(value);
                } else if (type.contains(Json2sql.INTEGER)) {
                    newValue.add(value);
                } else if (type.contains(Json2sql.TIMESTAMP)) {
                    newValue.add(value);
                } else if (type.contains(Json2sql.VARCHAR)) {
                    newValue.add("'" + value + "'");
                }
            }

//            values = values.stream().map(s -> "'" + s + "'").collect(Collectors.toList());

            insertSql.append("insert into ").append(tableName)
                    .append("(")
                    .append(String.join(",", keys))
                    .append(")")
                    .append(" values (")
                    .append(String.join(",", newValue))
                    .append(");")
                    .append(lineSeparator);
        });

        return insertSql.toString();
    }
}
