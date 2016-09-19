package de.ancud.camunda.dto;

import com.liferay.portal.kernel.workflow.DefaultWorkflowDefinition;
//import org.camunda.bpm.engine.repository.ProcessDefinition;
import java.util.Map;

import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.util.HashMap;

/**
 * @author bnmaxim.
 */
public class CamundaWorkflowDefinition extends DefaultWorkflowDefinition {

    public CamundaWorkflowDefinition(ProcessDefinition procDef, String title, boolean isActive) {
        this.setActive(isActive);
        this.setTitle(title/*procDef.getResourceName()*/);
        this.setName(procDef.getName());
        this.setVersion(procDef.getVersion());
        Map<String, Object> optAttributes = new HashMap <String, Object>();
        optAttributes.put("Category", procDef.getCategory());
        optAttributes.put("DeploymentId", procDef.getDeploymentId());
        optAttributes.put("Description", procDef.getDescription());
        optAttributes.put("DiagramResourceName", procDef.getDiagramResourceName());
        optAttributes.put("Id", procDef.getId());
        optAttributes.put("Key", procDef.getKey());
        optAttributes.put("hasStartFormKey", new Boolean(procDef.hasStartFormKey()));
        optAttributes.put("isSuspended", new Boolean(procDef.isSuspended()));
        this.setOptionalAttributes(optAttributes);
    }
}

