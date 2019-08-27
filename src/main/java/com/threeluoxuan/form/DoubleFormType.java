package com.threeluoxuan.form;

import org.activiti.engine.form.AbstractFormType;
import org.apache.commons.lang.ObjectUtils;

public class DoubleFormType extends AbstractFormType {
    @Override
    public Object convertFormValueToModelValue(String s) {
        return new Double(s);
    }

    @Override
    public String convertModelValueToFormValue(Object o) {
        return ObjectUtils.toString(o);
    }

    @Override
    public String getName() {
        return "double";
    }
}
