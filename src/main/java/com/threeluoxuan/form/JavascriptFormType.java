package com.threeluoxuan.form;

import org.activiti.engine.form.AbstractFormType;

/**
 * 主要在于1.唯一标识类型 2.序列化和反序列化
 */
public class JavascriptFormType extends AbstractFormType {
    /**
     * 把表单填写的内容转换为java对象类型
     * @param s 表单的值 为字符串类型
     * @return 对象
     */
    @Override
    public Object convertFormValueToModelValue(String s) {
        return s;
    }

    /**
     * 把java对象转换为字符串
     * @param o 要转换的对象
     * @return 字符串
     */
    @Override
    public String convertModelValueToFormValue(Object o) {
        return (String)o;
    }

    /**
     * 设置唯一的表单字段类型标识符
     * @return 类型标识符
     */
    @Override
    public String getName() {
        return "javascript";
    }
}
