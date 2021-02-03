package com.fs.web.core.parameter.analysis;

import com.fs.web.core.Model;
import com.fs.framework.ApplicationContext;
import com.fs.framework.AutoScanApplicationContext;
import com.fs.utils.base.Assert;
import com.fs.utils.conversion.JavaConversion;
import com.fs.utils.reflect.ClassUtils;
import com.fs.web.annotation.RequestBody;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * 基本类型解析
 * @author fk7075
 * @version 1.0
 * @date 2020/11/20 9:17
 */
public class BaseParameterAnalysis implements ParameterAnalysis{

    @Override
    public double priority() {
        return 6;
    }

    @Override
    public boolean can(Model model, Method method, Parameter parameter,String asmParamName) {
        if(parameter.isAnnotationPresent(RequestBody.class)){
            return false;
        }
        Class<?> parameterType = parameter.getType();
        //Java基本类型以及基本类型的包装类型以及他们的数组类型
        if(ClassUtils.isPrimitive(parameterType)
                ||ClassUtils.isSimple(parameterType)
                || ClassUtils.isSimpleArray(parameterType)){
            return true;
        }
        return false;
    }

    @Override
    public Object analysis(Model model, Method method, Parameter parameter,Type genericParameterType, String asmParamName) {
        String paramName=getParamName(parameter,asmParamName);
        Class<?> parameterType = parameter.getType();

        //可以在Request参数中拿到
        if(model.parameterMapContainsKey(paramName)){
            if(parameterType.isArray()){
                return model.getParams(paramName,parameterType);
            }
            return JavaConversion.strToBasic(model.getParameter(paramName),parameterType);
        }else if(model.restMapContainsKey(paramName)){
            return JavaConversion.strToBasic(model.getRestParam(paramName),parameterType);
        }else{  //尝试解析@Param中配置的默认值

            String defaultValue = getParamDefaultValue(parameter);
            if(Assert.isNull(defaultValue)){
                return null;
            }
            //以ioc:开头的表示从IOC容器中取值
            if(defaultValue.startsWith("ioc:")){
                String iocId=defaultValue.substring(4);
                ApplicationContext appContext=AutoScanApplicationContext.create();
                if(appContext.isIOCId(iocId)){
                    return appContext.getBean(iocId);
                }
                return null;
            }
            if(parameterType.isArray()){
                return JavaConversion.strArrToBasicArr(defaultValue.split(","),parameterType);
            }
            return JavaConversion.strToBasic(defaultValue,parameterType);
        }
    }

}
