package com.fs.framework.scan;

import com.fs.framework.container.FusionStrategy;
import com.fs.framework.container.enums.Strategy;
import com.fs.framework.container.factory.IOCBeanFactory;
import com.fs.utils.reflect.ClassUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 扩展清单
 * @author fk
 * @version 1.0
 * @date 2020/12/9 0009 10:23
 */
public class JarExpandChecklist {

    private static final Map<String, FusionStrategy> enumsMap;

    static {
        enumsMap=new HashMap<>(9);
        enumsMap.put("REPLACE", Strategy.REPLACE);
        enumsMap.put("CONTINUE", Strategy.CONTINUE);
        enumsMap.put("SUPPLEMENT", Strategy.SUPPLEMENT);
        enumsMap.put("REPLACE_PLUGINS", Strategy.REPLACE_PLUGINS);
        enumsMap.put("REPLACE_SINGLETON", Strategy.REPLACE_SINGLETON);
        enumsMap.put("SUPPLEMENT_SINGLETON", Strategy.SUPPLEMENT_SINGLETON);
        enumsMap.put("SUPPLEMENT_SINGLETON_REPLACE_PLUGINS",
            Strategy.SUPPLEMENT_SINGLETON_REPLACE_PLUGINS);
        enumsMap.put("REPLACE_SINGLETON_SUPPLEMENT_PLUGINS",
            Strategy.REPLACE_SINGLETON_SUPPLEMENT_PLUGINS);
        enumsMap.put("CONTINUE_SINGLETON_SUPPLEMENT_PLUGINS",
            Strategy.CONTINUE_SINGLETON_SUPPLEMENT_PLUGINS);

    }

    private Set<Class<?>> beanClass;
    private Set<IOCBeanFactory> beanFactories;

    public JarExpandChecklist(){
        beanClass=new HashSet<>(50);
        beanFactories=new HashSet<>(10);
    }

    public Set<Class<?>> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Set<Class<?>> beanClass) {
        this.beanClass = beanClass;
    }

    public void addBeanClass(Class<?> beanClass){
        this.beanClass.add(beanClass);
    }

    public Set<IOCBeanFactory> getBeanFactories() {
        return beanFactories;
    }

    public void setBeanFactories(Set<IOCBeanFactory> beanFactories) {
        this.beanFactories = beanFactories;
    }

    public void addBeanFactory(ClassLoader classLoader,String factoryFullName,String fusionStrategyFullName)  {
        try {
            Class<?> aClass = classLoader.loadClass(factoryFullName);
            if(!IOCBeanFactory.class.isAssignableFrom(aClass)){
                throw new RuntimeException();
            }
            IOCBeanFactory beanFactory = (IOCBeanFactory) ClassUtils.newObject(aClass);
            if(enumsMap.containsKey(fusionStrategyFullName.toUpperCase())){
                beanFactory.setFusionStrategy(enumsMap.get(fusionStrategyFullName.toUpperCase()));
                beanFactories.add(beanFactory);
                return;
            }
            Class<?> fusionStrategyClass = classLoader.loadClass(fusionStrategyFullName);
            if(!FusionStrategy.class.isAssignableFrom(fusionStrategyClass)){
                throw new RuntimeException();
            }
            beanFactory.setFusionStrategy((FusionStrategy)ClassUtils.newObject(fusionStrategyClass));
            beanFactories.add(beanFactory);
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }

    }
}
