package com.threeluoxuan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import cn.org.rapid_framework.freemarker.directive.BlockDirective;
import cn.org.rapid_framework.freemarker.directive.ExtendsDirective;
import cn.org.rapid_framework.freemarker.directive.OverrideDirective;

/**
 * 需要和启动器放在同一级
 * freemarker的模板继承配置
 */
@Configuration
public class FreemarkerConfig {
    @Autowired
    freemarker.template.Configuration configuration;

    @PostConstruct
    public void setSharedVariable(){
        configuration.setSharedVariable("block", new BlockDirective());
        configuration.setSharedVariable("override", new OverrideDirective());
        configuration.setSharedVariable("extends", new ExtendsDirective());
    }
}
