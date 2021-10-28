package com.jq.api;

import java.util.Map;

public interface BeanProcessor<T> {

    /**
     * @param t javabean类型
     * @return
     */
    Map<String, String> processor(T t);
}
