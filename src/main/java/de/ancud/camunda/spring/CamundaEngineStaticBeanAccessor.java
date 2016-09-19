package de.ancud.camunda.spring;

import org.camunda.bpm.engine.ProcessEngine;

/**
 * @author bnmaxim.
 */
public class CamundaEngineStaticBeanAccessor {

    private static CamundaProcessEngineWrapper processEngineWrapper;

    public static CamundaProcessEngineWrapper getProcessEngineWrapper() {
        return processEngineWrapper;
    }

    public void setProcessEngineWrapper(CamundaProcessEngineWrapper processEngineWrapper) {
        CamundaEngineStaticBeanAccessor.processEngineWrapper = processEngineWrapper;
    }

    public static ProcessEngine getProcessEngine(){
        if (processEngineWrapper!=null && processEngineWrapper.getProcessEngine()!=null){
            return processEngineWrapper.getProcessEngine();
        }
        return null;
    }


}
