package com.jq.api;

import java.util.Map;

public interface JSONParser {

    Map<String, Object> parse(String json, String tableName);
}
