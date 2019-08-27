package com.threeluoxuan.config;

import com.threeluoxuan.form.BigtextFormType;
import com.threeluoxuan.form.DoubleFormType;
import com.threeluoxuan.form.JavascriptFormType;
import com.threeluoxuan.form.UsersFormType;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Activiti的配置
 * 宋体可以保证生成的流程图不乱码
 * 自定义表单类型主要用于activiti的表单的类型转换
 */
@Configuration
public class ActivitiConfiguration implements ProcessEngineConfigurationConfigurer {
    @Override
    public void configure(SpringProcessEngineConfiguration configuration) {
        //图片不乱码
        configuration.setActivityFontName("宋体");
        configuration.setLabelFontName("宋体");
        configuration.setAnnotationFontName("宋体");
        //添加自定义表单类型
        List<AbstractFormType> customFormTypes =
                Arrays.asList(new JavascriptFormType(), new UsersFormType(),
                        new BigtextFormType(), new DoubleFormType());
        configuration.setCustomFormTypes(customFormTypes);
    }
}
