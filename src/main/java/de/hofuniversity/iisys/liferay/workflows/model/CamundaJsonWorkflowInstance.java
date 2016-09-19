package de.hofuniversity.iisys.liferay.workflows.model;

import java.io.Serializable;
import java.util.Map;

import com.liferay.portal.kernel.workflow.DefaultWorkflowInstance;

public class CamundaJsonWorkflowInstance extends DefaultWorkflowInstance
{
	public CamundaJsonWorkflowInstance(long id, String defName,
			int defVersion, Map<String, Serializable> workflowContext)
	{
		setWorkflowInstanceId(id);
		setWorkflowDefinitionName(defName);
		setWorkflowDefinitionVersion(defVersion);
		setWorkflowContext(workflowContext);
	}
}
