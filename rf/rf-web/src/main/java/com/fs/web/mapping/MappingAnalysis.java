package com.fs.web.mapping;

import com.fs.web.annotation.CloseRun;
import com.fs.web.annotation.Controller;
import com.fs.web.annotation.DeleteMapping;
import com.fs.web.annotation.GetMapping;
import com.fs.web.annotation.InitRun;
import com.fs.web.annotation.PostMapping;
import com.fs.web.annotation.PutMapping;
import com.fs.web.annotation.RequestMapping;
import com.fs.web.annotation.RestController;
import com.fs.framework.container.Module;

import java.lang.annotation.Annotation;

/**
 * 类的映射解析，将一个Controller分解为多个URL映射
 * @author fk7075
 * @version 1.0
 * @date 2020/11/17 9:51
 */
public interface MappingAnalysis {

    public static final Class<? extends Annotation>[] MAPPING_ANNOTATIONS=
            new Class[]{RequestMapping.class, GetMapping.class, PostMapping.class,
                    PutMapping.class, DeleteMapping.class};

    public static final Class<? extends Annotation>[] CONTROLLER_ANNOTATIONS=
            new Class[]{Controller.class, RestController.class};

    public static final Class<? extends Annotation>[] RUN_ANNOTATIONS=
            new Class[]{InitRun.class, CloseRun.class};

    /**
     * 将一个Controller解析为多个URL映射
     * @param controller Controller对象
     * @return
     */
    UrlMappingCollection analysis(Module controller);


    /**
     * 将一个ControllerAdvice解析为多个异常处理器
     * @param controllerAdvice ControllerAdvice对象
     * @return
     */
    ExceptionMappingCollection exceptionAnalysis(Module controllerAdvice);

}
