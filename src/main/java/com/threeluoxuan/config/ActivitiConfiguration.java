package com.threeluoxuan.config;

import com.threeluoxuan.form.JavascriptFormType;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ActivitiConfiguration implements ProcessEngineConfigurationConfigurer {
    @Override
    public void configure(SpringProcessEngineConfiguration configuration) {
        //图片不乱码
        configuration.setActivityFontName("宋体");
        configuration.setLabelFontName("宋体");
        configuration.setAnnotationFontName("宋体");
        //添加自定义表单类型
        List<AbstractFormType> customFormTypes = Arrays.asList(new JavascriptFormType());
        configuration.setCustomFormTypes(customFormTypes);
    }
}
