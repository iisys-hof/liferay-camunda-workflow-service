package de.ancud.camunda.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.liferay.portal.kernel.workflow.DefaultWorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskAssignee;


public class CamundaWorkflowTask extends DefaultWorkflowTask {
	
	public CamundaWorkflowTask(Date createDate, Date dueDate, Date completionDate, String name, String description, boolean asynchronous, 
		Map<String, Serializable> optionalAttributes, long workflowDefinitionId, String workflowDefinitionName, int workflowDefinitionVersion, 
		long workflowInstanceId, Collection<WorkflowTaskAssignee> workflowTaskAssignees, long workflowTaskId) {
		
		super();
		this.setCreateDate(createDate);
		this.setDueDate(dueDate);
		this.setCompletionDate(completionDate);
		this.setName(name);
		this.setDescription(description);
		this.setAsynchronous(asynchronous);
		this.setOptionalAttributes(optionalAttributes);
		this.setWorkflowDefinitionId(workflowDefinitionId);
		this.setWorkflowDefinitionName(workflowDefinitionName);
		this.setWorkflowDefinitionVersion(workflowDefinitionVersion);
		this.setWorkflowInstanceId(workflowInstanceId);
		this.setWorkflowTaskAssignees(workflowTaskAssignees);
		this.setWorkflowTaskId(workflowTaskId);
	}
	
}
