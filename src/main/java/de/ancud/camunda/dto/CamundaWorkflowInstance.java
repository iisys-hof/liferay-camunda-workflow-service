package de.ancud.camunda.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import com.liferay.portal.kernel.workflow.DefaultWorkflowInstance;
import com.liferay.portal.kernel.workflow.WorkflowInstance;

public class CamundaWorkflowInstance extends DefaultWorkflowInstance {

	public CamundaWorkflowInstance(HistoricProcessInstance procInstance, String workflowDefinitionName, Integer workflowDefinitionVersion, Map<String, Serializable> workflowContext) {
		
		this.setWorkflowInstanceId(Long.valueOf(procInstance.getId()));
		this.setWorkflowDefinitionName(workflowDefinitionName);
		this.setWorkflowDefinitionVersion(workflowDefinitionVersion);
		this.setWorkflowContext(workflowContext);
		
	}
	
	public CamundaWorkflowInstance(ProcessInstance procInstance, String workflowDefinitionName, Integer workflowDefinitionVersion, Map<String, Serializable> workflowContext) {
		
		this.setWorkflowInstanceId(Long.valueOf(procInstance.getProcessInstanceId()));
		this.setWorkflowDefinitionName(workflowDefinitionName);
		this.setWorkflowDefinitionVersion(workflowDefinitionVersion);
		this.setWorkflowContext(workflowContext);
		
	}

}
