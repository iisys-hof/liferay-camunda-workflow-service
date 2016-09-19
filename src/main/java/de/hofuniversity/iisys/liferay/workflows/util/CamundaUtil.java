package de.hofuniversity.iisys.liferay.workflows.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class CamundaUtil
{
	private static final String AUTHORIZATION_FRAG = "authorization/";
	private static final String CASE_DEF_FRAG = "case-definition/";
	private static final String CASE_EXEC_FRAG = "case-execution/";
	private static final String CASE_INST_FRAG = "case-instance/";
	private static final String CASE_ACT_INST_FRAG = "case-activity-instance/";
	private static final String DEPLOYMENT_FRAG = "deployment/";
	private static final String ENGINE_FRAG = "engine/";
	private static final String EXECUTION_FRAG = "execution/";
	private static final String FILTER_FRAG = "filter/";
	private static final String GROUP_FRAG = "group/";
	private static final String GROUPS_FRAG = "groups/";
	private static final String HISTORY_FRAG = "history/";
	private static final String INCIDENT_FRAG = "incident/";
	private static final String JOB_FRAG = "job/";
	private static final String JOB_DEF_FRAG = "job-definition/";
	private static final String MESSAGE_FRAG = "message/";
	private static final String PROCESS_DEF_FRAG = "process-definition/";
	private static final String PROCESS_INST_FRAG = "process-instance/";
	private static final String TASK_FRAG = "task/";
	private static final String USER_FRAG = "user/";
	private static final String VARIABLE_INST_FRAG = "variable-instance/";
	private static final String ACTIVITY_INST_FRAG = "activity-instance/";
	
	private static final String ACTIVITY_INSTS_FRAG = "/activity-instances";
	private static final String SUSPENDED_FRAG = "/suspended";
	private static final String VARIABLES_FRAG = "/variables/";

	private static final String USER_ID_PARAM = "userId=";
	
	private final String fRestUrl;
	
	//TODO: JSONObject vs. JSONArray for list queries
	
	public CamundaUtil()
	{
		this(CamundaConfig.getInstance()
				.getConfiguration().get(CamundaConfig.CAMUNDA_URL));
	}
	
	public CamundaUtil(String restUrl)
	{
		if(!restUrl.endsWith("/"))
		{
			restUrl += "/";
		}
		
		fRestUrl = restUrl;
	}
	
	
	public JSONObject getAuthorizations() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + AUTHORIZATION_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONArray getCaseDefinitions() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + CASE_DEF_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONObject getCaseDefinition(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + CASE_DEF_FRAG + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getCaseDefinitionByKey(String key) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + CASE_DEF_FRAG
				+ "key/" + key));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject createCaseInstance(String defId, JSONObject parameters)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + CASE_DEF_FRAG
				+ defId + "/create"), "POST", parameters.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONArray getCaseExecutions() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + CASE_EXEC_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONObject getCaseExecution(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + CASE_EXEC_FRAG
				+ id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject completeCaseExecution(String id, JSONObject parameters)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + CASE_EXEC_FRAG
				+ id + "/complete"), "POST", parameters.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject disableCaseExecution(String id, JSONObject parameters)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + CASE_EXEC_FRAG
				+ id + "/disable"), "POST", parameters.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject reenableCaseExecution(String id, JSONObject parameters)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + CASE_EXEC_FRAG
				+ id + "/reenable"), "POST", parameters.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject startCaseExecutionManually(String id, JSONObject parameters)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + CASE_EXEC_FRAG
				+ id + "/manual-start"), "POST", parameters.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONArray getCaseInstances() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + CASE_INST_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONArray getDeployments() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + DEPLOYMENT_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONObject deleteDeployment(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + DEPLOYMENT_FRAG
			+ id), "DELETE", null);
		if(text != null && text.startsWith("{"))
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONArray getEngines() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + ENGINE_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONArray getExecutions() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + EXECUTION_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONObject getExecution(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + EXECUTION_FRAG
				+ id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject triggerExecution(String id, JSONObject parameters)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + EXECUTION_FRAG
				+ id), "POST", parameters.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONArray getFilters() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + FILTER_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONArray getGroups() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + GROUP_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONObject getGroups(String userId) throws Exception
	{
		JSONObject response = null;
		
		String url = fRestUrl + GROUPS_FRAG + "?" + USER_ID_PARAM + userId;
		
		String text = HttpUtil.getText(new URL(url));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getIncidents() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + INCIDENT_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getJobs() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + JOB_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getJob(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + JOB_FRAG + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject executeJob(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + JOB_FRAG + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject activateJob(String id) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":false}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + JOB_FRAG + id
				+ "/suspended"), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject suspendJob(String id) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":true}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + JOB_FRAG + id
				+ "/suspended"), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject activateJobByDefinitionId(String defId) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":false}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + JOB_DEF_FRAG + defId
				+ "/suspended"), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject suspendJobByDefinitionId(String defId) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":true}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + JOB_DEF_FRAG + defId
				+ "/suspended"), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getJobDefinitions() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + JOB_DEF_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getJobDefinition(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + JOB_DEF_FRAG + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject deliverMessage(JSONObject parameters)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + MESSAGE_FRAG),
				"POST", parameters.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getProcessDefinitionByName(String name, int version)
		throws Exception
	{
		JSONObject response = null;
		
		String url = fRestUrl + PROCESS_DEF_FRAG;
		
		url += "?name=" + name;
		url += "&version=" + version;
		
		String text = HttpUtil.getText(new URL(url));
		if(text != null)
		{
			JSONArray result = new JSONArray(text);
			if(result.length() > 0)
			{
				response = result.getJSONObject(0);
			}
		}
		
		return response;
	}
	
	public JSONArray getProcessDefinitionsByName(String name)
			throws Exception
		{
			JSONArray response = null;
			
			String url = fRestUrl + PROCESS_DEF_FRAG;
			
			url += "?name=" + name;
			
			String text = HttpUtil.getText(new URL(url));
			if(text != null)
			{
				response = new JSONArray(text);
			}
			
			return response;
		}
	
	public JSONArray getProcessDefinitions(int first, int max,
			boolean latestVersion, boolean activeOnly) throws Exception
	{
		JSONArray response = null;
		
		String url = fRestUrl + PROCESS_DEF_FRAG;
		
		url += "?firstResult=" + first;
		url += "&maxResults=" + max;
		
		if(latestVersion)
		{
			url += "&latestVersion=true";
		}
		
		if(activeOnly)
		{
			url += "&active=true";
		}
		
		String text = HttpUtil.getText(new URL(url));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONObject getProcessDefinitionById(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getProcessDefinitionByKey(String key) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ "key/" + key));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject activateProcessDefinition(String id) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":false}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ id + "/suspended"), "PUT", payload);
		if(text != null && text.startsWith("{"))
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject activateProcessDefinitionByKey(String key) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":false}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ "key/" + key + "/suspended"), "PUT", payload);
		if(text != null && text.startsWith("{"))
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject suspendProcessDefinition(String id) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":true}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ id + "/suspended"), "PUT", payload);
		if(text != null && text.startsWith("{"))
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject suspendProcessDefinitionByKey(String key) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":true}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ "key/" + key + "/suspended"), "PUT", payload);
		if(text != null && text.startsWith("{"))
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject startProcessInstance(String defId, JSONObject parameters)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ defId + "/start"), "POST", parameters.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject startProcessIntanceByKey(String defKey, JSONObject parameters)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ "key/" + defKey + "/start"), "POST", parameters.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	
	public JSONObject getStartFormVariables(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ id + "/form-variables"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getStartFormVariablesByKey(String key) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_DEF_FRAG
				+ "key/" + key + "/form-variables"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONArray getProcessInstances() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_INST_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	public JSONArray getProcessInstances(String varKey, String varValue) throws Exception
	{
		JSONArray response = null;
		
		String url = fRestUrl + PROCESS_INST_FRAG;
		
		url += "?variables=" + varKey + "_eq_" + varValue;
		
		String text = HttpUtil.getText(new URL(url));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONObject getProcessInstanceCount() throws Exception
	{
		//TODO: parameters
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_INST_FRAG + "count"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getProcessInstance(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_INST_FRAG
				+ id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getActivityInstances(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_INST_FRAG
				+ id + ACTIVITY_INSTS_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	/**
	 * Deletes the process instance with the given ID.
	 * 
	 * @param id process instance id to delete
	 * @return either null or an error response
	 * @throws Exception in case sending the request fails
	 */
	public JSONObject deleteProcessInstance(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ id), "DELETE", null);
		if(text != null && text.startsWith("{"))
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getProcessVariables(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_INST_FRAG
				+ id + VARIABLES_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getProcessVariable(String id, String varId) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + PROCESS_INST_FRAG
				+ id + VARIABLES_FRAG + varId));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject updateProcessVariables(String id, JSONObject request) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ id + VARIABLES_FRAG), "POST", request.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject putProcessVariable(String id, String varId, JSONObject values) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ id + VARIABLES_FRAG + varId), "PUT", values.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject deleteProcessVariable(String id, String varId) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ id + VARIABLES_FRAG + varId), "DELETE", null);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject activateProcessById(String id) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":false}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ "/" + id + SUSPENDED_FRAG), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject suspendProcessById(String id) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"suspended\":true}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ "/" + id + SUSPENDED_FRAG), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject activateProcessByDefinitionId(String id) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"processDefinitionId\":\"" + id
				+ "\",\"suspended\":false}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ SUSPENDED_FRAG), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject suspendProcessByDefinitionId(String id) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"processDefinitionId\":\"" + id
				+ "\",\"suspended\":true}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ SUSPENDED_FRAG), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject activateProcessByDefinitionKey(String key) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"processDefinitionKey\":\"" + key
				+ "\",\"suspended\":false}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ SUSPENDED_FRAG), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject suspendProcessByDefinitionKey(String key) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"processDefinitionKey\":\"" + key
				+ "\",\"suspended\":true}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + PROCESS_INST_FRAG
				+ SUSPENDED_FRAG), "PUT", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONArray getTasks(List<String> urlParams) throws Exception
	{
		JSONArray response = null;
		
		String url = fRestUrl + TASK_FRAG;
		
		//add request parameters, already in the form "key=value"
		if(urlParams != null && !urlParams.isEmpty())
		{
			Iterator<String> iter = urlParams.iterator();
			url += "?" + iter.next();
			while(iter.hasNext())
			{
				url += "&" + iter.next();
			}
		}
		
		String text = HttpUtil.getText(new URL(url));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	
	public JSONObject getTaskCount() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + TASK_FRAG +
				"count"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getTask(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + TASK_FRAG + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getTaskFormKey(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + TASK_FRAG + id
				+ "/form"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject claimTask(String id, String userId) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"userId\":\"" + userId + "\"}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/claim"), "POST", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject unclaimTask(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/unclaim"), "POST", null);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject completeTask(String id, JSONObject data) throws Exception
	{
		JSONObject response = null;
		
		String payload = null;
		if(data != null)
		{
			payload = data.toString();
		}
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/complete"), "POST", payload);
		if(text != null && !text.isEmpty())
		{
			if(text.charAt(0) == '{')
			{
				response = new JSONObject(text);
			}
			else
			{
				response = new JSONObject();
				response.put("response", text);
			}
		}
		
		return response;
	}
	
	
	public JSONObject submitTaskForm(String id, JSONObject data) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/submit-form"), "POST", data.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject resolveTask(String id, JSONObject data) throws Exception
	{
		JSONObject response = null;
		
		String payload = null;
		if(data != null)
		{
			payload = data.toString();
		}
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/resolve"), "POST", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject setTaskAsignee(String id, String userId) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"userId\":\"" + userId + "\"}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/assignee"), "POST", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject delegateTask(String id, String userId) throws Exception
	{
		JSONObject response = null;
		
		String payload = "{\"userId\":\"" + userId + "\"}";
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/delegate"), "POST", payload);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getTaskIdentityLinks(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + TASK_FRAG + id
				+ "/identity-links"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject addTaskIdentityLink(String id, JSONObject relation) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/identity-links"), "POST", relation.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject deleteTaskIdentityLink(String id, JSONObject relation) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/identity-links/delete"), "POST", relation.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getTaskAttachments(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + TASK_FRAG + id
				+ "/attachment"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getTaskAttachment(String id, String attachmentId)
		throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + TASK_FRAG + id
				+ "/attachment/" + attachmentId));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject addTaskAttachment(String id, JSONObject attachment) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/attachment/create"), "POST", attachment.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject deleteTaskAttachment(String id, String attachmentId)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/attachment/" + attachmentId), "DELETE", null);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getLocalTaskVariable(String id, String varId)
		throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + TASK_FRAG + id
				+ "/localVariables/" + varId));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getLocalTaskVariables(String id)
		throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + TASK_FRAG + id
				+ "/localVariables"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject updateLocalTaskVariables(String id,
			JSONObject modifications) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/localVariables"), "POST", modifications.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject setLocalTaskVariable(String id, String varId,
			JSONObject modification) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/localVariables/" + varId), "PUT", modification.toString());
		if(text != null)
		{
			// should technically not return anything at all
			try
			{
				response = new JSONObject(text);
			}
			catch(Exception e)
			{
				response = new JSONObject();
				response.put("response", text);
			}
		}
		
		return response;
	}
	
	
	public JSONObject deleteLocalTaskVariable(String id, String varId)
			throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG + id
				+ "/localVariables/" + varId), "DELETE", null);
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getTaskFormVariables(String id)
		throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + TASK_FRAG + id
				+ "/form-variables"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject createTask(JSONObject task) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG
				+ "create"), "POST", task.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}

	
	public JSONObject updateTask(String id, JSONObject task) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.sendJson(new URL(fRestUrl + TASK_FRAG
				+ id + "/"), "PUT", task.toString());
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getUsers() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + USER_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	public JSONObject getVariableInstances() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + VARIABLE_INST_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricActivityInstances() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ ACTIVITY_INST_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricActivityInstance(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ ACTIVITY_INST_FRAG + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricCaseActivityInstances() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ CASE_ACT_INST_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricCaseActivityInstance(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ CASE_ACT_INST_FRAG + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricCaseInstances() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ CASE_INST_FRAG));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricCaseInstance(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ CASE_INST_FRAG + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricDetails() throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ "detail/"));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricDetail(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ "detail/" + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONArray getHistoricProcessInstances() throws Exception
	{
		JSONArray response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ PROCESS_INST_FRAG));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	public JSONArray getHistoricProcessInstances(String varKey, String varValue)
		throws Exception
	{
		JSONArray response = null;
		
		String url = fRestUrl + HISTORY_FRAG + PROCESS_INST_FRAG;
		
		url += "?variables=" + varKey + "_eq_" + varValue;
		
		String text = HttpUtil.getText(new URL(url));
		if(text != null)
		{
			response = new JSONArray(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricProcessInstance(String id) throws Exception
	{
		JSONObject response = null;
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ PROCESS_INST_FRAG + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	public JSONObject getHistoricTask(String id) throws Exception
	{
		JSONObject response = null;
		
		//TODO: additional parameters
		
		String text = HttpUtil.getText(new URL(fRestUrl + HISTORY_FRAG
				+ PROCESS_INST_FRAG + "?taskId=" + id));
		if(text != null)
		{
			response = new JSONObject(text);
		}
		
		return response;
	}
	
	
	
	
	
	public static void main(String[] args) throws Exception
	{
		CamundaUtil util = new CamundaUtil("http://127.0.0.1:8080/engine-rest/");
		
		JSONArray definitions = util.getDeployments();
		System.out.println("deployments: " + definitions);
		
		
		List<String> urlParams = new ArrayList<String>();
		urlParams.add("processInstanceId=0b7276ce-de9d-11e4-9ea2-22b5c971964e");
		
		JSONArray tasks = util.getTasks(urlParams);
		System.out.println("tasks num: " + tasks.length());
		System.out.println("tasks: " + tasks);
		
		
//		JSONObject def = definitions.getJSONObject(0);
//		String defId = def.getString("id");
//		
//		
//		//start process
//		//set parameters
//		JSONObject parameters = new JSONObject();
//		
//		JSONObject variables = new JSONObject();
//		parameters.put("variables", variables);
		
		//TODO: ???
//		String businessKey = "arbitrary business key";
//		parameters.put("businessKey", businessKey);

		//TODO: ???
//		String caseInstanceId = "sample_case";
//		parameters.put("caseInstanceId", caseInstanceId);
		
	
		//start
//		JSONObject response = util.startProcessInstance(defId, parameters);
//		System.out.println("start response: " + response);
//		String processId = null;
//		if(response.has("id"))
//		{
//			processId = response.getString("id");
//		}
//		System.out.println("processId: " + processId);
//		
//		
//		//list instances
//		JSONArray instances = util.getProcessInstances();
//		System.out.println("process instances: " + instances);
//		
//		
//		instances = util.getTasks();
//		System.out.println("tasks: " + instances);
		
		
//		JSONObject response = util.deleteProcessInstance("eba903c2-c189-11e4-9cd7-22b5c971964e");
//		System.out.println(response);
	}
}
