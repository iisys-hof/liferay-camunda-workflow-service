package de.ancud.camunda.spring;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;

/**
 * @author bnmaxim.
 */
public class CamundaProcessEngineBeanManagerUtil {

    public static RepositoryService exposeRepositoryServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getRepositoryService();
    }

    public static ProcessEngine exposeProcessEngineBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper);
    }

    public static RuntimeService exposeRuntimeServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getRuntimeService();
    }
    
    public static HistoryService exposeHistoryServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getHistoryService();
    }
    
    public static TaskService exposeTaskServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getTaskService();
    }
    
    public static ManagementService exposeManagementServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getManagementService();
    }
    
    public static IdentityService exposeIdentityServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getIdentityService();
    }
    
    public static AuthorizationService exposeAuthorizationServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getAuthorizationService();
    }
    
    public static CaseService exposeCaseServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getCaseService();
    }
    
    public static FilterService exposeFilterServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getFilterService();
    }
    
    public static FormService exposeFormServiceBean(CamundaProcessEngineWrapper processEngineWrapper) {
        return getProcessEngine(processEngineWrapper).getFormService();
    }

    private static ProcessEngine getProcessEngine(CamundaProcessEngineWrapper processEngineWrapper) {
        if (processEngineWrapper==null || processEngineWrapper.getProcessEngine()==null){
            throw new ExceptionInInitializerError("Failed to initialize the Camunda Repository Service");
        }
        return processEngineWrapper.getProcessEngine();

    }


}
