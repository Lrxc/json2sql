package com.jq.impl;

import com.google.common.base.CaseFormat;
import com.jq.bean.InsertDTO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类型转换
 */
public class ConvertProcessor {


    /**
     * json 转 list 对象
     *
     * @param inputPath json 文件路径
     * @param clazz     list类型
     */
//    public static <T> List<T> jsonToList(String inputPath, Class<T> clazz) {
//        final String fileName = "ips_config";
//        String json = FileUtil.readUtf8String(inputPath + File.separator + fileName + ".json");
//        if (StrUtil.isEmpty(json)) {
//            log.err("{}{}.json 文件不存在", inputPath, fileName);
//            return null;
//        }
//
//        Configuration configuration = ConfigurationBuilder.config()
//                //withBeanProcessorMap 注册特殊字段处理器，可以根据使用者的需求定制化处理某个字段，例如你想将tvMeta转变为t_tvMeta，处理器必须实现BeanProcessor接口
//                .withBeanProcessorMap("sid", new IpsRuleBeanProcessor().new sid())
//                .withBeanProcessorMap("cve", new IpsRuleBeanProcessor().new cve())
//                .withBeanProcessorMap("platform", new IpsRuleBeanProcessor().new platform())
//                .withBeanProcessorMap("priority", new IpsRuleBeanProcessor().new priority())
//                .withBeanProcessorMap("new_type", new IpsRuleBeanProcessor().new newType())
//                .withIgnoreKeys(List.of("desc", "name_sc", "app_type", "recommend"))
//                //表字段生成策略，例如Java命名是驼峰式，而数据库是下划线命名法，提供转化的策略，具体策略请看KeyConversionConfig类
//                .withKeyConversionConfig(KeyConversionConfig.nothing)
//                //varchar 类型的长度 默认 255
//                .withVarcharLength("5000")
//                //decimal 类型精度 默认 (10,4)
//                .withDecimalPrecision("10,4")
//                .build();
//        //配置注入Json2sql中
//        Json2sql.setConfiguration(configuration);
//        //json字符串生成map对象
//        Map<String, Object> jsonMap = jsonToMap(json, "random");
//
//        List<InsertDTO> insertDTOS = (List<InsertDTO>) jsonMap.get("insertParam");
//        //通过反射,将 InsertDTO 赋值给对象
//        List<T> collect = insertDTOS.stream()
//                .map(insertDTO -> {
//                    try {
//                        List<String> keys = insertDTO.getKeys();
//                        List<String> values = insertDTO.getValues();
//                        //类初始化
//                        T t = clazz.newInstance();
//
//                        for (int i = 0; i < keys.size(); i++) {
//                            //下划线转驼峰
//                            String key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, keys.get(i));
//                            //通过反射获取字段属性
//                            Field field = t.getClass().getDeclaredField(key);
//                            String type = field.getType().getName();
//                            //根据字段类型转换值的类型
//                            Object value = chooseType(type, values.get(i));
//
//                            field.setAccessible(true);
//                            //通过反射设置字段值
//                            field.set(t, value);
//                        }
//                        return t;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }).collect(Collectors.toList());
//
//        return collect;
//    }

    /**
     * map 转 list 对象
     *
     * @param map   json 文件路径
     * @param clazz list类型
     */
    public <T> List<T> mapToList(Map<String, Object> map, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (map == null) {
            return list;
        }

        List<InsertDTO> insertDTOS = (List<InsertDTO>) map.get("insertParam");
        if (insertDTOS == null) {
            return list;
        }

        //通过反射,将 InsertDTO 赋值给对象
        list = insertDTOS.stream()
                .map(insertDTO -> {
                    try {
                        List<String> keys = insertDTO.getKeys();
                        List<String> values = insertDTO.getValues();
                        //类初始化
                        T t = clazz.newInstance();

                        for (int i = 0; i < keys.size(); i++) {
                            //下划线转驼峰
                            String key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, keys.get(i));
                            //通过反射获取字段属性
                            Field field = t.getClass().getDeclaredField(key);
                            String type = field.getType().getName();
                            //根据字段类型转换值的类型
                            Object value = chooseType(type, values.get(i));

                            field.setAccessible(true);
                            //通过反射设置字段值
                            field.set(t, value);
                        }
                        return t;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());

        return list;
    }

    /**
     * 根据类型转换值的类型
     *
     * @param type  类型
     * @param value 属性值
     */
    private Object chooseType(String type, String value) {
        switch (type) {
            case "java.lang.Integer":
                return Integer.valueOf(value);
            case "java.util.Date":
                return new Date();
            default:
                return value;
        }
    }
}
