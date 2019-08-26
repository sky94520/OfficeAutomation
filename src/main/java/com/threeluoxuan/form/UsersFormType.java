package com.threeluoxuan.form;

import org.activiti.engine.form.AbstractFormType;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public class UsersFormType extends AbstractFormType {
    @Override
    public Object convertFormValueToModelValue(String s) {
        String[] splits = StringUtils.split(s, ",");
        return Arrays.asList(splits);
    }

    @Override
    public String convertModelValueToFormValue(Object o) {
        return ObjectUtils.toString(o);
    }

    @Override
    public String getName() {
        return "users";
    }
}
