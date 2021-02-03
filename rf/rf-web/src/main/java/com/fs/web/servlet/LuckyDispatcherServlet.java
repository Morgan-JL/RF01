package com.fs.web.servlet;

import com.fs.web.core.Model;
import com.fs.web.core.RequestFilter;
import com.fs.web.enums.RequestMethod;
import com.lucky.utils.base.Assert;
import com.fs.web.mapping.ExceptionMapping;
import com.fs.web.mapping.UrlMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fk7075
 * @version 1.0
 * @date 2020/11/17 18:52
 */
public class LuckyDispatcherServlet extends BaseServlet {

    private static final Logger log= LoggerFactory.getLogger(LuckyDispatcherServlet.class);

    @Override
    protected void applyFor(HttpServletRequest request, HttpServletResponse response, RequestMethod requestMethod) {
        Model model=null;
        UrlMapping urlMapping =null;
        try {
          model = new Model(request, response,
                    this.getServletConfig(), requestMethod, webConfig.getEncoding());

            //前置处理，处理URL、RequestMethod、以及WebContext
            beforeDispose(model);

            //全局IP配置校验、静态资源、favicon.ico等简单请求的处理
            if(!RequestFilter.filter(model,webConfig)){
                return;
            }

            urlMapping = urlMappingCollection.getMapping(model);
            // URL、IP、RequestMethod校验
            if(Assert.isNull(urlMapping)){
                return;
            }

            //权限验证
            if(!urlMapping.permissionCheck()){
                model.error("403", "您没有访问该资源的权限！","权限不足！");
                return;
            }


            //后置处理，处理Controller的属性和跨域问题以及包装文件类型的参数
            afterDispose(model, urlMapping);

            //获取执行参数,并为Mapping设置执行参数,并对参数执行增强操作(校验、加密、防注入...)
            urlMapping.setRunParams(getParameterAnalysisChain().analysis(model, urlMapping));

            //执行Controller方法并获取返回结果
            Object invoke = urlMapping.invoke(model);

            //响应请求结果
            response(model,invoke, urlMapping,urlMapping.getRest());

        } catch (Throwable e) {
            e=getCauseThrowable(e);
            ExceptionMapping exceptionMapping = exceptionMappingCollection.findExceptionMapping(urlMapping, e);
            if(exceptionMapping==null){
                model.e500(e);
                return;
            }
            try {
                Object invoke = exceptionMapping.invoke(model, urlMapping, e);
                response(model,invoke, urlMapping,exceptionMapping.getRest());
            } catch (Exception ex) {
                ex.printStackTrace();
                model.e500(ex);
            }
        }finally {
            setFinally(model, urlMapping);
        }
    }
}
