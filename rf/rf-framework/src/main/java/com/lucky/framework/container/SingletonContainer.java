package com.lucky.framework.container;

import com.lucky.framework.exception.LuckyBeanCreateException;
import com.lucky.utils.reflect.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author fk7075
 * @version 1.0.0
 * @date 2020/11/14 1:12 下午
 */
public class SingletonContainer implements Map<String, Module> {

    private static final Logger log= LoggerFactory.getLogger(SingletonContainer.class);

    /** 单例池*/
    private Map<String,Module> singletonPool=new ConcurrentHashMap<>(256);

    public Map<String, Module> getSingletonPool() {
        return singletonPool;
    }

    @Override
    public int size() {
        return singletonPool.size();
    }

    @Override
    public boolean isEmpty() {
        return singletonPool.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return singletonPool.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return singletonPool.containsValue(value);
    }

    @Override
    public Module get(Object key) {
        return singletonPool.get(key);
    }

    @Override
    public Module put(String key, Module value) {
        if(containsKey(key)){
            LuckyBeanCreateException lex = new LuckyBeanCreateException("ID为 `" + key + "` 的组件已经存在，无法重复创建！  具体信息："+value);
            log.error("LuckyBeanCreateException",lex);
            throw lex;
        }
        return singletonPool.put(key, value);
    }

    @Override
    public Module replace(String key, Module value){
        if(containsKey(key)){
            remove(key);
            put(key,value);
        }else {
            put(key,value);
        }
        return value;
    }

    @Override
    public Module remove(Object key) {
        return singletonPool.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Module> m) {
        for(Map.Entry<? extends String,?extends Module> entry:m.entrySet()){
            put(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public void clear() {
        singletonPool.clear();
    }

    @Override
    public Set<String> keySet() {
        return singletonPool.keySet();
    }

    @Override
    public Collection<Module> values() {
        return singletonPool.values();
    }

    @Override
    public Set<Entry<String, Module>> entrySet() {
        return singletonPool.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return singletonPool.equals(o);
    }

    @Override
    public int hashCode() {
        return singletonPool.hashCode();
    }

    public List<Module> getBeanByType(String...types){
        return singletonPool.values()
                .stream()
                .filter((m)->{
                    for (String type : types) {
                        if(m.getType().equals(type)){
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public List<Module> getBeanByClass(Class<?>...componentClasses){
        return singletonPool.values()
                .stream()
                .filter((m)->{
                    for (Class<?> componentClass : componentClasses) {
                        if(componentClass.isAssignableFrom(m.getOriginalType())){
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public List<Module> getBeanByAnnotation(Class<? extends Annotation>...annotationClasses){
        return singletonPool.values()
                .stream()
                .filter((m)-> {
                    for (Class<? extends Annotation> annotationClass : annotationClasses) {
                        if(AnnotationUtils.isExist(m.getOriginalType(),annotationClass)){
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public Module getBean(String id){
        return singletonPool.get(id);
    }

    public boolean containsType(String type){
        Collection<Module> values = values();
        for (Module value : values) {
            if(value.getType().equals(type)){
                return true;
            }
        }
        return false;
    }

    public boolean containsClass(Class<?> beanClass){
        Collection<Module> values = values();
        for (Module value : values) {
            if(beanClass.isAssignableFrom(value.getOriginalType())){
                return true;
            }
        }
        return false;
    }


}
