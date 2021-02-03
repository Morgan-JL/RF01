package com.fs.web.beanfactory;

import com.fs.web.annotation.CallController;
import com.fs.web.annotation.Controller;
import com.fs.web.annotation.ControllerAdvice;
import com.fs.web.annotation.RestController;
import com.fs.framework.annotation.Component;
import com.fs.framework.container.FusionStrategy;
import com.fs.framework.container.Module;
import com.fs.framework.container.factory.IOCBeanFactory;
import com.fs.utils.base.Assert;
import com.fs.utils.reflect.AnnotationUtils;
import com.fs.utils.reflect.ClassUtils;
import com.fs.web.httpclient.callcontroller.CallControllerProxy;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fk7075
 * @version 1.0
 * @date 2020/11/23 16:25
 */
public class LuckyWebBeanFactory extends IOCBeanFactory {

    public LuckyWebBeanFactory(){
        super();
    }

    public LuckyWebBeanFactory(FusionStrategy fusionStrategy){
        super(fusionStrategy);
    }

    private static final Class<? extends Annotation>[] CONTROLLER_ANNOTATION=
            new Class[]{Controller.class, CallController.class, RestController.class, ControllerAdvice.class};

    @Override
    public List<Module> createBean() {
        List<Module> modules=new ArrayList<>();
        List<Class<?>> controllerClasses = getPluginByAnnotation(CONTROLLER_ANNOTATION);
        for (Class<?> controllerClass : controllerClasses) {
            if(AnnotationUtils.strengthenIsExist(controllerClass,Controller.class)){
                modules.add(new Module(getBeanName(controllerClass),
                                   getBeanType(controllerClass),
                                   ClassUtils.newObject(controllerClass)));
            }else{
                modules.add(new Module(getBeanName(controllerClass),
                                getBeanType(controllerClass),
                                CallControllerProxy.getCallControllerProxyObject(controllerClass)));
            }
        }
        return modules;
    }

    @Override
    public String getBeanName(Class<?> aClass) {
        Annotation controllerAnnotation = AnnotationUtils.getByArray(aClass, CONTROLLER_ANNOTATION);
        String id= (String) AnnotationUtils.getValue(controllerAnnotation,"id");
        return Assert.isBlankString(id)?super.getBeanName(aClass):id;
    }

    @Override
    public String getBeanType(Class<?> aClass) {
        return AnnotationUtils.strengthenGet(aClass, Component.class).get(0).type();
    }
}
