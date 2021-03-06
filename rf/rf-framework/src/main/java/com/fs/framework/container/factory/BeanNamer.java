package com.fs.framework.container.factory;

import com.fs.framework.annotation.Configuration;
import com.fs.framework.annotation.Service;
import com.fs.framework.annotation.Component;
import com.fs.framework.annotation.Repository;
import com.fs.utils.base.Assert;
import com.fs.utils.base.BaseUtils;
import com.fs.utils.exception.LuckyReflectionException;
import com.fs.utils.reflect.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 默认的起名器
 * @author fk7075
 * @version 1.0.0
 * @date 2020/11/15 下午11:50
 */
public class BeanNamer implements Namer {

    private Class<? extends Annotation>[] COMPONENT_ANNOTATION=
            new Class[]{Component.class, Repository.class, Service.class, Configuration.class};

    @Override
    public String getBeanName(Class<?> beanClass) {
        Annotation annotation;
        try {
            annotation= AnnotationUtils.getByArray(beanClass, COMPONENT_ANNOTATION);
        }catch (LuckyReflectionException e){
            return getDefBeanName(beanClass);
        }
        if(Assert.isNull(annotation)){
            return getDefBeanName(beanClass);
        }
        String id = (String) AnnotationUtils.getValue(annotation, "id");
        String value= (String) AnnotationUtils.getValue(annotation,"value");
        if(!Assert.isBlankString(id)){
            return id;
        }
        if(!Assert.isBlankString(value)){
            return value;
        }
        return getDefBeanName(beanClass);
    }


    @Override
    public String getBeanType(Class<?> beanClass) {
        List<Component> components = AnnotationUtils.strengthenGet(beanClass, Component.class);
        if(Assert.isEmptyCollection(components)){

        }
        if(components.size()!=1){

        }
        return components.get(0).type();
    }

    protected String getDefBeanName(Class<?> beanClass){
        return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
    }
}
