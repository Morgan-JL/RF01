package com.fs.web.beanfactory;

import com.fs.web.mapping.UrlMappingCollection;
import com.fs.framework.AutoScanApplicationContext;
import com.fs.framework.container.factory.Destroy;

/**
 * @author fk7075
 * @version 1.0.0
 * @date 2020/12/6 下午7:42
 */
public class WebDestroy implements Destroy {

    @Override
    public void destroy() {
        AutoScanApplicationContext applicationContext = AutoScanApplicationContext.create();
        if(applicationContext.isIOCId("lucky_UrlMappingCollection")){
            UrlMappingCollection urlMappingCollection = (UrlMappingCollection) applicationContext.getBean("lucky_UrlMappingCollection");
            urlMappingCollection.closeRun();
        }
    }
}
