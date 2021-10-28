package com.jq.bean;

import lombok.Data;

import java.util.List;

@Data
public class InsertDTO {

    private List<String> keys;

    private List<String> values;
}
