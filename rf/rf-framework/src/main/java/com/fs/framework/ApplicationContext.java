package com.fs.framework;

import com.fs.framework.container.factory.Destroy;
import com.fs.framework.container.Module;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * @author fk7075
 * @version 1.0.0
 * @date 2020/11/14 7:04 上午
 */
public interface ApplicationContext extends Destroy {

    Object getBean(String id);

    Module getModule(String id);

    <T> List<T> getBean(Class<T>...aClass);

    List<Module> getModule(Class<?>...aClass);

    List<?> getBeanByAnnotation(Class<?extends Annotation>...annotationClasses);

    List<Module> getModuleByAnnotation(Class<?extends Annotation>...annotationClasses);

    List<?> getBeans();

    List<Module> getModules();

    List<?> getBeanByType(String...iocType);

    List<Module> getModuleByType(String...iocType);

    boolean isIOCType(String type);

    boolean isIOCId(String id);

    boolean isIOCClass(Class<?> componentClass);

    void put(Module module);

    Set<Class<?>> getClasses(Class<?>...Class);
}
