package de.ancud.camunda.util;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.workflow.WorkflowException;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bnmaxim.
 */
public class ProcessDefinitionUtils {

    public static ProcessDefinitionQuery getProcessDefinitionQueryByCompanyId(long companyId, String name, Integer version, RepositoryService repositoryService) throws WorkflowException {
        try {
//            List<String> procDefIds = CamundaWorkflowDefinitionMetadataLocalServiceUtil.getProcessDefinitionIds(companyId);

        	//TODO
        	List<String> procDefIds = new ArrayList<>();
        	String [] arrProcDefIds = new String [procDefIds.size()];
            procDefIds.toArray(arrProcDefIds);

            ProcessDefinitionQuery defQuery = repositoryService.createProcessDefinitionQuery().
                    processDefinitionIdIn(arrProcDefIds);

            if (name != null) {
                defQuery = defQuery.processDefinitionName(name);
            }

            if (version != null) {
                defQuery = defQuery.processDefinitionVersion(version);
            }

            return defQuery;
        } catch (SystemException e) {
            throw new WorkflowException("Failed to access CamundaWorkflowDefinitionMetadata because... ",e);
        }
    }

    public static List<ProcessDefinition> subList (List<ProcessDefinition> definitions, int start, int end) {

    	if (start < 0) {
        	start = 0;
        } else if (start > definitions.size()) {
        	start = definitions.size() - 1;
        }
        
        if (end < 0 || end > definitions.size()) {
        	end = definitions.size();
        }
        
        definitions = definitions.subList(start, end);        
    	
    	return definitions;
    }   

}
