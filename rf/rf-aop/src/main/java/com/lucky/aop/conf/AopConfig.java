package com.lucky.aop.conf;

import com.lucky.aop.core.AopExecutionChecker;
import com.lucky.aop.core.DefaultAopExecutionChecker;

/**
 * @author fk
 * @version 1.0
 * @date 2020/12/10 0010 14:33
 */
public class AopConfig {

    private static AopConfig aopConfig;

    private AopExecutionChecker aopExecutionChecker;

    public AopExecutionChecker getAopExecutionChecker() {
        return aopExecutionChecker;
    }

    public void setAopExecutionChecker(AopExecutionChecker aopExecutionChecker) {
        this.aopExecutionChecker = aopExecutionChecker;
    }

    private AopConfig(){

    }

    public static AopConfig defaultAopConfig(){
        if(aopConfig==null){
            aopConfig=new AopConfig();
            aopConfig.setAopExecutionChecker(new DefaultAopExecutionChecker());
        }
        return aopConfig;
    }
}
