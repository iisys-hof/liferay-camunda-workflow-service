package de.ancud.camunda.portal.impl;

import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalService;
import com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalServiceUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowInstance;
import com.liferay.portal.kernel.workflow.WorkflowInstanceManager;

import de.ancud.camunda.dto.CamundaWorkflowInstance;
import de.hofuniversity.iisys.liferay.workflows.model.CamundaJsonWorkflowInstance;
import de.hofuniversity.iisys.liferay.workflows.util.CamundaUtil;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstanceQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.runtime.VariableInstanceQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author bnmaxim.
 */
@Component(
	immediate = true, property = {"proxy.bean=false"},
	service = WorkflowInstanceManager.class
)
public class CamundaWorkflowInstanceManagerImpl implements WorkflowInstanceManager {

	public static final String VARIABLE_NAME_ASSET_NAME = "entryClassName";
	public static final String VARIABLE_NAME_ASSET_PK = "entryClassPK";
	
	private final CamundaUtil fCamundaUtil;
	
	private RuntimeService runtimeService;
	private HistoryService historyService;
	private ProcessEngine processEngine;
	private RepositoryService repositoryService;

	public CamundaWorkflowInstanceManagerImpl()
	{
		fCamundaUtil = new CamundaUtil();
	}
	
	@Override
	public void deleteWorkflowInstance(long companyId, long workflowInstanceId) throws WorkflowException {
//		runtimeService.deleteProcessInstance(String.valueOf(workflowInstanceId), "deleteReason", true);
		
		// TODO: actually try deleting it in Camunda if it isn't completed yet? probably not needed
		
		System.err.println("!!!!!!!!! not implemented: deleteWorkflowInstance");
	}

	@Override
	public List<String> getNextTransitionNames(long companyId, long userId, long workflowInstanceId) throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getNextTransitionNames");
		
		return null;
	}

	@Override
	public WorkflowInstance getWorkflowInstance(long companyId,
		long workflowInstanceId) throws WorkflowException
	{
		
		// TODO: what if the Workflow can't be found? - causes a plethora of Exceptions
		
		WorkflowInstance instance = null;
		
		System.err.println("### retrieving workflow with ID: " + workflowInstanceId);
		
		// active instances
		try
		{
			// retrieve by internal ID in workflow variable
			JSONArray match = fCamundaUtil.getProcessInstances("workflowId",
					String.valueOf(workflowInstanceId));
			
			if(match.length() > 0)
			{
				instance = convertCamundaInstance(workflowInstanceId,
					match.getJSONObject(0));
			}
		}
		catch(Exception e)
		{
//			throw new WorkflowException(e);
		}
		
		// RETURN if found
		if(instance != null)
			return instance;

		
		
		
		
		// historic instances
		try
		{
			JSONArray match = fCamundaUtil.getHistoricProcessInstances("workflowId",
					String.valueOf(workflowInstanceId));
			
			if(match.length() > 0)
			{
				instance = convertCamundaInstance(workflowInstanceId,
					match.getJSONObject(0));
			}
		}
		catch(Exception e)
		{
//			throw new WorkflowException(e);
		}
		
		
		// DEBUG: empty instance if not found
		// doesn't help - missing entity information
		if(instance == null)
		{
//			instance = new CamundaJsonWorkflowInstance(workflowInstanceId,
//					"", 0, new HashMap<String, Serializable>());
			
			// avoids a NullPointerException
//			instance.getWorkflowContext().put(
//				WorkflowConstants.CONTEXT_ENTRY_CLASS_PK, "0");
			
			System.err.println("### workflow with ID '" + workflowInstanceId + "' not found");
		}

		return instance;
	}
	
	private WorkflowInstance convertCamundaInstance(long workflowInstanceId,
		JSONObject camundaInstance) throws Exception
	{
		CamundaJsonWorkflowInstance instance = null;
		
		// TODO: retrieve variables - is that the context?
		Map<String, Serializable> workflowContext = new HashMap<String, Serializable>();
		JSONObject variables = fCamundaUtil.getProcessVariables(camundaInstance.getString("id"));
		for(String key : variables.keySet())
		{
			Serializable value = (Serializable) variables.getJSONObject(key).get("value");
			
			if(key.startsWith("Liferay_"))
			{
				key = key.substring("Liferay_".length());
			}
			
			workflowContext.put(key, value);
		}
		
		// retrieve definition
		JSONObject wfDef = fCamundaUtil.getProcessDefinitionById(
				camundaInstance.getString("definitionId"));
		
		instance = new CamundaJsonWorkflowInstance(workflowInstanceId,
			wfDef.getString("name"), wfDef.getInt("version"), workflowContext);
		
		// TODO: set a state?
		
		
		return instance;
	}

	@Override
	public int getWorkflowInstanceCount(long companyId, Long userId, String assetClassName, Long assetClassPK, Boolean completed) throws WorkflowException {
		List<WorkflowInstance> list = this.getWorkflowInstances(companyId, userId, assetClassName, assetClassPK, completed, 0, 100, null);
		return list.size();
	}

	@Override
	public int getWorkflowInstanceCount(long companyId, Long userId, String[] assetClassNames, Boolean completed) throws WorkflowException {
		// TODO: Implementieren!
		List<WorkflowInstance> list = this.getWorkflowInstances(companyId, userId, assetClassNames, completed, 0, 100, null);
		return list.size();
	}

	@Override
	public int getWorkflowInstanceCount(long companyId, String workflowDefinitionName, Integer workflowDefinitionVersion, Boolean completed) throws WorkflowException {
		List<WorkflowInstance> list = this.getWorkflowInstances(companyId,	workflowDefinitionName, workflowDefinitionVersion, completed, 0, 100, null);
		return list.size();
	}

	@Override
	public List<WorkflowInstance> getWorkflowInstances(long companyId, Long userId, String assetClassName, Long assetClassPK, Boolean completed, int start, int end, OrderByComparator orderByComparator)
			throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowInstances");
		
		int length = end - start;
		List<WorkflowInstance> fallbackEmpty  = new ArrayList<WorkflowInstance>();
		if (completed == null) {
			completed = false;
		}
		if (completed) {
			HistoricVariableInstanceQuery hQuery = getProcessEngine().getHistoryService().createHistoricVariableInstanceQuery();
			if (assetClassName != null) {
				hQuery.variableValueEquals(VARIABLE_NAME_ASSET_NAME, assetClassName);
			}
			if (assetClassPK != null) {
				hQuery.variableValueEquals(VARIABLE_NAME_ASSET_PK, assetClassPK);
			}
			List<HistoricVariableInstance> result = hQuery.listPage(start, length);
			Set<String> procInstances = new HashSet<String>();
			for (HistoricVariableInstance varInst : result) {
				procInstances.add(varInst.getProcessInstanceId());
			}

			if (procInstances.size() > 0) {
				List<HistoricProcessInstance> rProcess = getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().processInstanceIds(procInstances).finished().listPage(start, length);
				return convertHistoricToWorkflowInstance(rProcess);
			}

			return fallbackEmpty;
		} else {

			VariableInstanceQuery vQuery = getRuntimeService().createVariableInstanceQuery();
			if (assetClassName != null) {
				vQuery.variableValueEquals(VARIABLE_NAME_ASSET_NAME, assetClassName);
			}
			if (assetClassPK != null) {
				vQuery.variableValueEquals(VARIABLE_NAME_ASSET_PK, assetClassPK);
			}
			List<VariableInstance>  result = vQuery.list();
			Set<String> procInstanceIds = new HashSet<String>();
			for (VariableInstance varInst : result) {
				procInstanceIds.add(varInst.getProcessInstanceId());
			}
			
			if (procInstanceIds.size() > 0) {
				List<ProcessInstance> procInstances = runtimeService.createProcessInstanceQuery().processInstanceIds(procInstanceIds).listPage(start, length);				
				return convertToWorkflowInstance(procInstances);
			}
			
			return fallbackEmpty;
		}
	}

	@Override
	//TODO: korrigieren mithilfe von getWorkflowInstances(..., String assetClassNames, ...);
	public List<WorkflowInstance> getWorkflowInstances(long companyId, Long userId, String[] assetClassNames, Boolean completed, int start, int end, OrderByComparator orderByComparator)
			throws WorkflowException {
		// getProcessDefinition
		// TODO: Long userId, String[] assetClassNames, Boolean completed,
		// OrderByComparator orderByComparator
		/*
		 * ProcessDefinitionQuery pdQuery =
		 * ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId
		 * (companyId, null, null, getRepositoryService());
		 * 
		 * for (int i = 0; i < assetClassNames.length; i++) { String className =
		 * assetClassNames[i]; pdQuery =
		 * pdQuery.processDefinitionResourceName(className); }
		 */
		/*
		List<ProcessDefinition> definitions = ProcessDefinitionUtils.getProcessDefinitionQueryByCompanyId(companyId, null, null, getRepositoryService()).list();
		definitions = ProcessDefinitionUtils.subList(definitions, start, end);*/
		// TODO: definition richtig ermitteln
		/*
		 * for (ProcessDefinition pd : definitions) { pd.getResourceName();
		 * //definitions.r }
		 *//*
		if (completed) {
			getProcessEngine().getHistoryService().createHistoricVariableInstanceQuery();
			return Collections.emptyList();
		} else {
			ProcessDefinition definition = definitions.get(0);

			List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionId("adressdatenPruefen:2:403").active().list();
			List<WorkflowInstance> result = new ArrayList<WorkflowInstance>();
			for (ProcessInstance pi : list) {
				Map<String, Object> procVars = runtimeService.getVariables(pi.getProcessInstanceId());
				Map<String, Serializable> pvLiferay = new HashMap<String, Serializable>();
				for (String key : procVars.keySet()) {
					Object val = procVars.get(key);
					if (val instanceof Serializable) {
						pvLiferay.put(key, (Serializable) val);
					}
				}
				result.add(new CamundaWorkflowInstance(pi, definition.getName()/+"SomeName"+/, definition.getVersion()/+2+/, pvLiferay));
			}
			return result;
		}*/

		System.err.println("!!!!!!!!! not implemented: getWorkflowInstances");
		
		
		List<WorkflowInstance> wfInstancesAll = new LinkedList<WorkflowInstance>();
		List<WorkflowInstance> wfInstances = Collections.emptyList();
		
		for (int i = 0; i < assetClassNames.length; i++) {
			String className = assetClassNames[i];
			wfInstances = this.getWorkflowInstances(companyId, userId, className, null, completed, start, end, orderByComparator);
			/*for (WorkflowInstance wfInstance : wfInstances) {
				wfInstancesAll.add(wfInstance);
			}*/
			if (wfInstances != null && wfInstances.size() > 0) {
				wfInstancesAll.addAll(wfInstances);
			}			
		}
		return wfInstancesAll;
	}

	@Override
	public List<WorkflowInstance> getWorkflowInstances(long companyId, String workflowDefinitionName, Integer workflowDefinitionVersion, Boolean completed, int start, int end,
			OrderByComparator orderByComparator) throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowInstances");
		
		return null;
	}

	@Override
	public WorkflowInstance signalWorkflowInstance(long companyId, long userId, long workflowInstanceId, String transitionName, Map<String, Serializable> workflowContext) throws WorkflowException {
		ProcessInstance procInstance = runtimeService.createProcessInstanceQuery().processInstanceId(String.valueOf(workflowInstanceId)).singleResult();
		String executionId = runtimeService.createExecutionQuery().processInstanceId(String.valueOf(workflowInstanceId)).singleResult().getId();
		Map<String, Object> objWorkflowContext = this.convertSerializableToObject(workflowContext);
		runtimeService.signal(executionId, objWorkflowContext);
		

		System.err.println("!!!!!!!!! not implemented: signalWorkflowInstance");
		
		return this.convertToWorkflowInstance(procInstance);
	}
	
	public Map<String, Object> convertSerializableToObject(Map<String, Serializable> workflowContext) {
		Map<String, Object> objWorkflowContext = new HashMap<String, Object>();
		for (Iterator iter = workflowContext.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Object value = (Object) workflowContext.get(key);
			objWorkflowContext.put(key, value);
		}		
		return objWorkflowContext;
	}

	@Override
	public WorkflowInstance startWorkflowInstance(long companyId, long groupId, long userId, String workflowDefinitionName, Integer workflowDefinitionVersion, String transitionName,
			Map<String, Serializable> workflowContext) throws WorkflowException {
		// TODO: long groupId, long userId, String transitionName

		WorkflowInstance wfInstance = null;
		
		try
		{
			JSONObject wfDef = fCamundaUtil.getProcessDefinitions(0, 1, true, true).getJSONObject(0);
			
			JSONObject parameters = new JSONObject();
			JSONObject variables = new JSONObject();
			
			// generate internal numerical ID?
			long internalId = new Random().nextLong();
			JSONObject docVar = new JSONObject();
			docVar.put("type", "String");
			docVar.put("value", String.valueOf(internalId));
			variables.put("workflowId", docVar);
			
			// store worklfow context in process variables
			for(Entry<String, Serializable> conE : workflowContext.entrySet())
			{
				if(conE.getKey().equals("serviceContext"))
				{
					// constructed from request
					continue;
				}
				
				docVar = new JSONObject();
				docVar.put("type", "String");
				docVar.put("value", conE.getValue());
				variables.put("Liferay_" + conE.getKey(), docVar);
			}
			
			// add LDAP user ID (screen name) as initiator
			String screenName = UserLocalServiceUtil.fetchUser(userId).getScreenName();
			docVar = new JSONObject();
			docVar.put("type", "String");
			docVar.put("value", screenName);
			variables.put("iisys_workflow_initiator", docVar);

			// add all variables
			parameters.put("variables", variables);
			
			// start worklfow
			JSONObject camundaWorkflow = fCamundaUtil.startProcessInstance(wfDef.getString("id"), parameters);
			// get external ID, store in context?
			workflowContext.put("camundaId", camundaWorkflow.getString("id"));
			
			wfInstance = new CamundaJsonWorkflowInstance(internalId,
					workflowDefinitionName, workflowDefinitionVersion, workflowContext);
			
			// create internal link
			// TODO: how to delete again?
//			WorkflowInstanceLinkLocalServiceUtil.addWorkflowInstanceLink(
//				userId, companyId, groupId,
//				(String) workflowContext.get("entryClassName"),
//				Long.valueOf(workflowContext.get("entryClassPK").toString()), internalId);
			
			// DEBUG: set completed
//			WorkflowStatusManagerUtil.updateStatus(WorkflowConstants.getLabelStatus("approved"), workflowContext);
		}
		catch(Exception e)
		{
			throw new WorkflowException(e);
		}
		
		return wfInstance;
	}

	@Override
	public WorkflowInstance updateWorkflowContext(long companyId, long workflowInstanceId, Map<String, Serializable> workflowContext) throws WorkflowException {
		ProcessInstance procInstance = runtimeService.createProcessInstanceQuery().processInstanceId(String.valueOf(workflowInstanceId)).singleResult();
		String executionId = runtimeService.createExecutionQuery().processInstanceId(String.valueOf(workflowInstanceId)).singleResult().getId();
		Map<String, Object> objWorkflowContext = this.convertSerializableToObject(workflowContext);
		runtimeService.setVariables(executionId, objWorkflowContext);

		System.err.println("!!!!!!!!! not implemented: updateWorkflowContext");
		
		return this.convertToWorkflowInstance(procInstance);
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	private WorkflowInstance convertToWorkflowInstance(ProcessInstance procInstance) {

		ProcessDefinition procDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procInstance.getProcessDefinitionId()).singleResult();
		VariableInstanceQuery vQuery = getRuntimeService().createVariableInstanceQuery().processInstanceIdIn(procInstance.getProcessInstanceId());
		List<VariableInstance> lstVarInstance = vQuery.list();
		Map<String, Serializable> workflowContext = new HashMap<String, Serializable>();
		for (VariableInstance varInst : lstVarInstance) {
			workflowContext.put(varInst.getName(), (Serializable) varInst.getValue());
		}
		
		WorkflowInstance wfInstance = new CamundaWorkflowInstance(procInstance, procDefinition.getName(), procDefinition.getVersion(),  workflowContext);
					
		return wfInstance;
	}

	private List<WorkflowInstance> convertToWorkflowInstance(List<ProcessInstance> procInstances) {
		List<WorkflowInstance> lstWFInstances = new LinkedList<WorkflowInstance>();
		WorkflowInstance wfInstance = null;
		for (ProcessInstance procInstance : procInstances) {
			wfInstance = this.convertToWorkflowInstance(procInstance);
			lstWFInstances.add(wfInstance);
		}						
		return lstWFInstances;
	}

	private List<WorkflowInstance> convertHistoricToWorkflowInstance(List<HistoricProcessInstance> histProcInstances) {
		List<WorkflowInstance> lstWFInstances = new LinkedList<WorkflowInstance>();
		ProcessDefinition procDefinition = null;
		WorkflowInstance wfInstance = null;
		List<HistoricVariableInstance> lstVarInstance = Collections.emptyList();
		Map<String, Serializable> workflowContext = Collections.emptyMap();
		for (HistoricProcessInstance procInstance : histProcInstances) {
			procDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procInstance.getProcessDefinitionId()).singleResult();

			//lstVarInstance = getRuntimeService().createVariableInstanceQuery().processInstanceIdIn(procInstance.getId()).list();
			lstVarInstance = getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(procInstance.getId()).list();
			workflowContext = new HashMap<String, Serializable>();
			for (HistoricVariableInstance varInst : lstVarInstance) {
				workflowContext.put(varInst.getName(), (Serializable) varInst.getValue());
			}			
			
			wfInstance = new CamundaWorkflowInstance(procInstance, procDefinition.getName(), procDefinition.getVersion(), workflowContext);
			lstWFInstances.add(wfInstance);
		}						
		return lstWFInstances;
	}
	
	public HistoryService getHistoryService() {
		return historyService;
	}
	
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	@Override
	public List<WorkflowInstance> search(long companyId, Long userId, String assetType, String nodeName,
			String kaleoDefinitionName, Boolean completed, int start, int end,
			OrderByComparator<WorkflowInstance> orderByComparator) throws WorkflowException {
		// TODO Auto-generated method stub
		
		System.err.println("!!!!!!!!! not implemented: List<WorkflowInstance> search");
		
		return new ArrayList<WorkflowInstance>();
	}

	@Override
	public int searchCount(long companyId, Long userId, String assetType, String nodeName, String kaleoDefinitionName,
			Boolean completed) throws WorkflowException {
		// TODO Auto-generated method stub
		
		System.err.println("!!!!!!!!! not implemented: List<WorkflowInstance> searchCount");
		
		return 0;
	}
}
