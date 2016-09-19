package de.ancud.camunda.portal.impl;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowDefinitionManager;
import com.liferay.portal.kernel.workflow.WorkflowException;
import de.ancud.camunda.dto.CamundaWorkflowDefinition;
import de.ancud.camunda.util.ProcessDefinitionUtils;
import de.hofuniversity.iisys.liferay.workflows.model.CamundaJsonWorkflowDefinition;
import de.hofuniversity.iisys.liferay.workflows.util.CamundaUtil;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.RepositoryServiceImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author bnmaxim.
 */
@Component(
	immediate = true, property = {"proxy.bean=false"},
	service = WorkflowDefinitionManager.class
)
public class CamundaWorkflowDefinitionManagerImpl implements WorkflowDefinitionManager {

    private static Log log = LogFactoryUtil.getLog(CamundaWorkflowDefinitionManagerImpl.class);

    private RepositoryService repositoryService = new RepositoryServiceImpl();
    private ProcessEngine processEngine;

    private CamundaUtil fCamundaUtil = new CamundaUtil();
    
    /**
     * First
     *
     * @param companyId
     * @param userId
     * @param title
     * @param bytes
     * @return
     * @throws WorkflowException
     */
    @Override
    public WorkflowDefinition deployWorkflowDefinition(long companyId, long userId, String title, byte[] bytes) throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: deployWorkflowDefinition");
    	
    	
        try {

            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().enableDuplicateFiltering(false).name(title);
            UnsyncByteArrayInputStream inputStream = new UnsyncByteArrayInputStream(bytes);

            Deployment deployment = null;


            if (bytes != null) {
                try {//import as xml
                    deployment = deploymentBuilder.addInputStream(title + "bpmn20.xml", inputStream).deploy();
                } catch (Exception e) {
                    log.error("failed to deploy XML BPMN 2.0 file because", e);
                    //continue
                }
            }

            if (deployment == null) {//import as zip
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                //todo: do not allow the deployment of zip files containing multiple resources
                try {
                    if (zipEntry != null) {
                        deployment = deploymentBuilder.addZipInputStream(zipInputStream).deploy();
                    }
                } catch (Exception e) {
                    log.error("failed to deploy ZIP BPMN 2.0 file because", e);
                    throw e;
                }
            }

            //deployment was successful, obtain the ProcessDefinition

            List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().
                    deploymentId(deployment.getId()).list();

            if (definitions != null && definitions.size() > 0) {
                log.info("Found " + definitions.size() + " process definitions for deployment with id "
                        + deployment.getId());
                //extract the first ProcessDefinition and convert it to Liferay's WorkflowDefinition
                ProcessDefinition processDefinition = definitions.get(0);
                CamundaWorkflowDefinition workflowDefinition = new CamundaWorkflowDefinition(processDefinition, title, true);
                
//                CamundaWorkflowDefinitionMetadataLocalServiceUtil.addOrUpdateCamundaWorkflowMetadata(companyId, userId,
//                        processDefinition.getVersion(), processDefinition.getId());
                
                return workflowDefinition;
            }

        } catch (Exception e) {
            log.error("Failed to deploy Activiti workflow definition... ", e);
            throw new WorkflowException(e);
        }

        return null;
    }


    @Override
    public int getActiveWorkflowDefinitionCount(long companyId) throws WorkflowException {
        //return CamundaWorkflowDefinitionMetadataUtil.countByCompanyId(companyId);
        //repositoryService.createProcessDefinitionQuery().processDefinitionIdIn("");

		System.err.println("!!!!!!!!! not implemented: getActiveWorkflowDefinitionCount");
		
    	int count = (int) ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId(companyId, null, null, getRepositoryService()).
    			active().
    			count();
    	
    	return count;
    }

    @Override
    public int getActiveWorkflowDefinitionCount(long companyId, String name) throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getActiveWorkflowDefinitionCount");
		
    	int count = (int)ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId(companyId, name, null, getRepositoryService()).
    			active().
    			count();
    	
    	return count;
    }

    @Override
    public List<WorkflowDefinition> getActiveWorkflowDefinitions(long companyId, int start, int end, OrderByComparator orderByComparator) throws WorkflowException {
        //List<ProcessDefinition> definitions = getAllProcessDefinitions();
	
    	// TODO: orderByComparator.
    	log.debug("getActiveWorkflowDefinitions :: ");
    	log.debug("orderByComparator.getOrderBy(): " + orderByComparator.getOrderBy() + ", " + orderByComparator.getOrderByConditionFields().length + ", " + orderByComparator.getOrderByFields()[1]/* + ", " + orderByComparator.getOrderByConditionFields()*/);
        
        return getCamundaDefs(start, end, true, null);					
    }
/*
    private List<ProcessDefinition> subList (List<ProcessDefinition> definitions, int start, int end) {

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
    }*/
    
    @Override
    public List<WorkflowDefinition> getActiveWorkflowDefinitions(long companyId, String name, int start, int end, OrderByComparator orderByComparator) throws WorkflowException {
    	// TODO: orderByComparator.
    	log.debug("getActiveWorkflowDefinitions <name> :: ");
    	log.debug("orderByComparator.getOrderBy(): " + orderByComparator.getOrderBy());
        
        return getCamundaDefs(start, end, true, name);
    }

    @Override
    public WorkflowDefinition getLatestKaleoDefinition(long companyId, String name) throws WorkflowException {
    	
		System.err.println("!!!!!!!!! not implemented: getLatestKaleoDefinition");
		
        return null;
    }

    @Override
    public WorkflowDefinition getWorkflowDefinition(long companyId, String name, int version) throws WorkflowException {
    	
//    	ProcessDefinition procDefinition = ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId(companyId, name, version, getRepositoryService()).
//    			singleResult();
//    	
//    	WorkflowDefinition wfDefinition = new CamundaWorkflowDefinition(procDefinition, procDefinition.getResourceName(), !procDefinition.isSuspended());
    	
    	
    	WorkflowDefinition wfDefinition = null;
    	
		System.err.println("retrieving workflow definition '" + name + "' version " + version);
    	
    	JSONObject result;
		try
		{
			result = fCamundaUtil.getProcessDefinitionByName(name, version);
	    	if(result != null)
	    	{
	    		wfDefinition = new CamundaJsonWorkflowDefinition(result);
	    	}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    	
    	return wfDefinition;
    }

    @Override
    public int getWorkflowDefinitionCount(long companyId) throws WorkflowException {
        //return CamundaWorkflowDefinitionMetadataUtil.countByCompanyId(companyId);   	
    	int count = (int)ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId(companyId, null, null, getRepositoryService()).
    			count();
    	
		System.err.println("!!!!!!!!! not implemented: getWorkflowDefinitionCount");
		
    	return count;
    }

    @Override
    public int getWorkflowDefinitionCount(long companyId, String name) throws WorkflowException {
    	int count = (int) ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId(companyId, name, null, getRepositoryService()).
    			count();

		System.err.println("!!!!!!!!! not implemented: getWorkflowDefinitionCount");
    	
    	return count;
    }

    @Override
    public List<WorkflowDefinition> getWorkflowDefinitions(long companyId, int start, int end, OrderByComparator orderByComparator) throws WorkflowException {

    	// TODO: orderByComparator.
    	log.debug("getWorkflowDefinitions :: ");
    	if (orderByComparator != null) log.debug("orderByComparator.getOrderBy(): " + orderByComparator.getOrderBy());
    	
        return getCamundaDefs(start, end, false, null);
	
    }
    
    public ProcessDefinition getProcessDefinition(String definitionId) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processDefinitionQuery.processDefinitionId(definitionId);
		ProcessDefinition processDef = processDefinitionQuery.singleResult();
		

		System.err.println("!!!!!!!!! not implemented: private getProcessDefinition");
		
		// TODO: not used anymore?
		
		return processDef;
	}
    

    @Override
    public List<WorkflowDefinition> getWorkflowDefinitions(long companyId, String name, int start, int end, OrderByComparator orderByComparator) throws WorkflowException {

//        List<WorkflowDefinition> definitions = this.getWorkflowDefinitions(companyId, start, end, orderByComparator);
//        List<WorkflowDefinition> result = new ArrayList<WorkflowDefinition>();
//        for (WorkflowDefinition def : definitions) {
//            if (def.getName().equals(name)) {
//                result.add(def);
//            }
//        }
    	
    	
        return getCamundaDefs(start, end, false, name);
    }

    @Override
    public void undeployWorkflowDefinition(long companyId, long userId, String name, int version) throws WorkflowException {
    	//TODO: userId
    	ProcessDefinition definition = ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId(companyId, name, version, getRepositoryService()).
        		//startableByUser(String.valueOf(userId)).
        		singleResult();
    	

		System.err.println("!!!!!!!!! not implemented: undeployWorkflowDefinition");
    	
//    	CamundaWorkflowDefinitionMetadataPK primKey = new CamundaWorkflowDefinitionMetadataPK(definition.getId(), version, companyId);
    	
    	try {    		
    		repositoryService.deleteDeployment(definition.getId(), true);
//			CamundaWorkflowDefinitionMetadataLocalServiceUtil.deleteCamundaWorkflowDefinitionMetadata(primKey);
		} catch (Exception e) {
			//TODO: Wrap exception as WorkflowException
			throw new WorkflowException("Failed to access CamundaWorkflowDefinitionMetadata because... ",e);
		}
    	
    }

    @Override
    public WorkflowDefinition updateActive(long companyId, long userId, String name, int version, boolean active) throws WorkflowException {
    	

		System.err.println("!!!!!!!!! not implemented: updateActive");
    	
    	WorkflowDefinition camundaDef = null;
    	//TODO: userId
    	ProcessDefinition definition = ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId(companyId, name, version, getRepositoryService()).
        		//startableByUser(String.valueOf(userId)).
        		singleResult();
    	
    	if (definition != null) {
    		if (active) {
    			repositoryService.activateProcessDefinitionById(definition.getId(), true, null);	
    		} else {
    			repositoryService.suspendProcessDefinitionById(definition.getId(), true, null);
    		}
    		camundaDef = new CamundaWorkflowDefinition(definition, definition.getResourceName(), active);
    		
    		/*
        	CamundaWorkflowDefinitionMetadataPK primKey = new CamundaWorkflowDefinitionMetadataPK(definition.getId(), version, companyId);
        	        	
        	try {    		
    			CamundaWorkflowDefinitionMetadataLocalServiceUtil.getCamundaWorkflowDefinitionMetadata(primKey).;
    		} catch (PortalException e) {
				throw new WorkflowException("Failed to access CamundaWorkflowDefinitionMetadata because... ",e);
    		} 

    		/*
    		CamundaWorkflowDefinitionMetadataLocalServiceUtil.getCamundaWorkflowDefinitionMetadata(primKey).;    		  
            CamundaWorkflowDefinitionMetadataLocalServiceUtil.addOrUpdateCamundaWorkflowMetadata(companyId, userId, version, definition.getId());
    		 */
    	}
    	    	    	
        return camundaDef;
    }

    @Override
    public WorkflowDefinition updateTitle(long companyId, long userId, String name, int version, String title) throws WorkflowException {
    	WorkflowDefinition camundaDef = null;
    	

		System.err.println("!!!!!!!!! not implemented: updateTitle");
    	
    	//TODO: userId
    	ProcessDefinition definition = ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId(companyId, name, version, getRepositoryService()).
        		//startableByUser(String.valueOf(userId)).
        		singleResult();
        
    	if (definition != null) {
    		camundaDef = new CamundaWorkflowDefinition(definition, title, !definition.isSuspended());
    	}
    	    	    	
        return camundaDef;
    }

    @Override
    public void validateWorkflowDefinition(byte[] bytes) throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: validateWorkflowDefinition");
    	
    	//TODO: Implementieren!
    }

    public RepositoryService getRepositoryService() {
        return repositoryService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    
    
    // new Camunda utility methods
    private List<WorkflowDefinition> getCamundaDefs(int start, int end,
    	boolean activeOnly, String name) throws WorkflowException
    {
    	List<WorkflowDefinition> result = null;
    	
    	try
        {
        	// TODO: sorting
        	if(start < 0)
        		start = 0;
        	if(end < 0)
        		end = 1000;
        	JSONArray jsonDefs = null;
        			
        	if(name == null)
        	{
        		jsonDefs = fCamundaUtil.getProcessDefinitions(
            		start, end - start + 1, true, activeOnly);
        	}
        	else
        	{
        		// TODO: paging
        		jsonDefs = fCamundaUtil.getProcessDefinitionsByName(name);
        	}
        	
        	result = new ArrayList<WorkflowDefinition>(jsonDefs.length());
        	
        	for(int i = 0; i < jsonDefs.length(); ++i)
        	{
        		result.add(new CamundaJsonWorkflowDefinition(jsonDefs.getJSONObject(i)));
        	}
        }
        catch(Exception e)
        {
        	throw new WorkflowException(e);
        }
    	
    	return result;
    }
}
