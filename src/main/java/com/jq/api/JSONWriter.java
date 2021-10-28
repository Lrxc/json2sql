package com.jq.api;

import java.util.Map;

public interface JSONWriter {

    void writer(Map<String, Object> sqlParamMap, String path, String sqlFileName);


    String writer(Map<String, Object> sqlParamMap);
}
