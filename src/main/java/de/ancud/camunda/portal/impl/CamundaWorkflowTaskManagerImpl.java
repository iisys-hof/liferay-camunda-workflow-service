package de.ancud.camunda.portal.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskAssignee;
import com.liferay.portal.kernel.workflow.WorkflowTaskManager;

import de.ancud.camunda.dto.CamundaWorkflowTask;
import de.hofuniversity.iisys.liferay.workflows.model.CamundaJsonTask;
import de.hofuniversity.iisys.liferay.workflows.util.CamundaUtil;


/**
 * @author bnmaxim.
 */
@Component(
	immediate = true, property = {"proxy.bean=false"},
	service = WorkflowTaskManager.class
)
public class CamundaWorkflowTaskManagerImpl implements WorkflowTaskManager {
	
	private final CamundaUtil fCamundaUtil;
	
	private final Random fRandom;
	
	private ProcessEngine processEngine;
	private RuntimeService runtimeService;
	private TaskService taskService;
	private IdentityService identityService;
	private HistoryService historyService;
	
	private CamundaWorkflowDefinitionManagerImpl workflowDefinitionManager;

	public CamundaWorkflowTaskManagerImpl()
	{
		fCamundaUtil = new CamundaUtil();
		
		fRandom = new Random();
	}
	
	@Override
	public WorkflowTask assignWorkflowTaskToRole(
		long companyId, long userId, long workflowTaskId, long roleId,
		String comment, Date dueDate, Map<String, Serializable> workflowContext)
		throws WorkflowException {
		

		System.err.println("!!!!!!!!! not implemented: assignWorkflowTaskToRole");
	
		return null;
	}
	
	@Override
	public WorkflowTask assignWorkflowTaskToUser(
		long companyId, long userId, long workflowTaskId, long assigneeUserId,
		String comment, Date dueDate, Map<String, Serializable> workflowContext)
		throws WorkflowException {
		

		System.err.println("!!!!!!!!! not implemented: assignWorkflowTaskToUser");
	
		try {
			identityService.setAuthenticatedUserId(String.valueOf(userId));
			
			String taskId = String.valueOf(workflowTaskId);
			Task task =
				taskService.createTaskQuery().taskId(taskId).singleResult();
			
			if (dueDate != null) {
				task.setDueDate(dueDate);
			}
			taskService.saveTask(task);
			
			String currentAssignee = task.getAssignee();
			
			taskService.setAssignee(taskId, String.valueOf(assigneeUserId));
			
			/*
			 * workflowLogEntry workflowLogEntry = new WorkflowLogEntry();
			 * workflowLogEntry.setType(WorkflowLog.TASK_ASSIGN);
			 * workflowLogEntry.setAssigneeUserId(assigneeUserId);
			 */
			
			Long prevUserId = null;
			try {
				prevUserId = Long.valueOf(currentAssignee);
			} catch (Exception ex) {
			}
			
			if (prevUserId != null) {
				// workflowLogEntry.setPreviousUserId(prevUserId);
			}
			// workflowLogEntry.setComment(comment);
			
			// addWorkflowLogEntryToProcess(task, workflowLogEntry);
			
			task = taskService.createTaskQuery().taskId(taskId).singleResult();
			WorkflowTask workflowTask =
				getWorkflowTask(companyId, workflowTaskId);
			
			return workflowTask;
			
		} catch (Exception ex) {
			throw new WorkflowException(ex);
		}
	}
	
	@Override
	public WorkflowTask completeWorkflowTask(
		long companyId, long userId, long workflowTaskId,
		String transitionName, String comment,
		Map<String, Serializable> workflowContext)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: completeWorkflowTask");
	
		return null;
	}
	
	@Override
	public List<String> getNextTransitionNames(
		long companyId, long userId, long workflowTaskId)
		throws WorkflowException {
		
		System.err.println("!!!!!!!!! not properly implemented: getNextTransitionNames");
	
		return new ArrayList<String>();
	}
	
	@Override
	public long[] getPooledActorsIds(long companyId, long workflowTaskId)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getPooledActorsIds");
	
		return new long[0];
	}
	
	@Override
	public WorkflowTask getWorkflowTask(long companyId, long workflowTaskId)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not fully implemented: getWorkflowTask");
	
		WorkflowTask task = null;
		
		List<String> urlParams = new ArrayList<>();
		
		// search by generated Liferay ID (needs to be set beforehand)
		urlParams.add("taskId_eq_" + Long.toString(workflowTaskId));
		
		try
		{
			JSONArray matches = fCamundaUtil.getTasks(urlParams);
			if(matches.length() > 0)
			{
				JSONObject camTask = matches.getJSONObject(0);
				
				task = convertTask(companyId, camTask);
			}
		}
		catch (Exception e)
		{
			throw new WorkflowException(e);
		}
		
		// TODO: historic tasks?
		
		return task;
	}

	@Override
	public WorkflowTask fetchWorkflowTask(long companyId, long workflowTaskInstanceId) throws WorkflowException {
		// TODO Auto-generated method stub

//		System.err.println("!!!!!!!!! not implemented: fetchWorkflowTask");
		
		return getWorkflowTask(companyId, workflowTaskInstanceId);
	}
	
	private WorkflowTask getWorkflowTask(
		long companyId, HistoricTaskInstance task)
		throws WorkflowException {
	
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		
		ProcessDefinition processDef = workflowDefinitionManager.getProcessDefinition(task.getProcessDefinitionId());
		
		Map<String, Serializable> optionalAttributes = new HashMap<String, Serializable>();
		optionalAttributes.put("deleteReason", task.getDeleteReason());
		
		
		List<WorkflowTaskAssignee> workflowTaskAssignees = new ArrayList<WorkflowTaskAssignee>();
		String assigneeUserId = task.getAssignee();
		if (Validator.isNotNull(assigneeUserId)) {
			WorkflowTaskAssignee workflowTaskAssignee = new WorkflowTaskAssignee(User.class.getName(), Long.valueOf(assigneeUserId));
			workflowTaskAssignees.add(workflowTaskAssignee);
		} else {
			List<IdentityLink> participations = new ArrayList<IdentityLink>();
			if (task.getEndTime() == null) {
				participations = taskService.getIdentityLinksForTask(task.getId());
			}
			
			for (IdentityLink participation : participations) {
				if (Validator.isNotNull(participation.getGroupId())) {
					try {
						Role role = RoleLocalServiceUtil.getRole(companyId, participation.getGroupId()); //find role by name, not roleId!
						
						WorkflowTaskAssignee workflowTaskAssignee = new WorkflowTaskAssignee(Role.class.getName(), role.getRoleId());
						workflowTaskAssignees.add(workflowTaskAssignee);
					} catch (PortalException e) {
						e.printStackTrace();
					} catch (SystemException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		boolean asynchronous = true; //!task.isBlocking()
		
		CamundaWorkflowTask workflowTask = new CamundaWorkflowTask(task.getStartTime(), task.getDueDate(), task.getEndTime(), task.getName(), 
			task.getDescription(), asynchronous, optionalAttributes, Long.valueOf(processDef.getId()), processDef.getName(), processDef.getVersion(), 
			Long.valueOf(processInstanceId), workflowTaskAssignees, Long.valueOf(task.getId()));
		
		return workflowTask;
	}
	
	private WorkflowTask getWorkflowTask(HistoricTaskInstance task) throws WorkflowException {
		long companyId = GetterUtil.getLong((String) taskService.getVariable(task.getId(), "companyId"));
		

		System.err.println("!!!!!!!!! not implemented: getWorkflowTask");
		
		return getWorkflowTask(companyId, task);
	}
	
	@Override
	public int getWorkflowTaskCount(long companyId, Boolean completed)
		throws WorkflowException {
	

		System.err.println("!!!!!!!!! not implemented: getWorkflowTaskCount");
		
		return 0;
	}
	
	@Override
	public int getWorkflowTaskCountByRole(
		long companyId, long roleId, Boolean completed)
		throws WorkflowException {
		
		System.err.println("!!!!!!!!! not implemented: getWorkflowTaskCountByRole");
	
		return 0;
	}
	
	@Override
	public int getWorkflowTaskCountBySubmittingUser(
		long companyId, long userId, Boolean completed)
		throws WorkflowException {
	

		System.err.println("!!!!!!!!! not implemented: getWorkflowTaskCountBySubmittingUser");
		
		return 0;
	}
	
	@Override
	public int getWorkflowTaskCountByUser(
		long companyId, long userId, Boolean completed)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowTaskCountByUser");
	
		return 0;
	}
	
	@Override
	public int getWorkflowTaskCountByUserRoles(
		long companyId, long userId, Boolean completed)
		throws WorkflowException {
		
		System.err.println("!!!!!!!!! not implemented: getWorkflowTaskCountByUserRoles");
	
		return 0;
	}
	
	@Override
	public int getWorkflowTaskCountByWorkflowInstance(
		long companyId, Long userId, long workflowInstanceId, Boolean completed)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowTaskCountByWorkflowInstance");
		
		return 0;
	}
	
	@Override
	public List<WorkflowTask> getWorkflowTasks(
		long companyId, Boolean completed, int start, int end,
		OrderByComparator orderByComparator)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowTasks");
	
		return new ArrayList<WorkflowTask>();
	}
	
	@Override
	public List<WorkflowTask> getWorkflowTasksByRole(
		long companyId, long roleId, Boolean completed, int start, int end,
		OrderByComparator orderByComparator)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowTasksByRole");
		
		return new ArrayList<WorkflowTask>();
	}
	
	@Override
	public List<WorkflowTask> getWorkflowTasksBySubmittingUser(
		long companyId, long userId, Boolean completed, int start, int end,
		OrderByComparator orderByComparator)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowTasksBySubmittingUser");
		
		return new ArrayList<WorkflowTask>();
	}
	
	@Override
	public List<WorkflowTask> getWorkflowTasksByUser(
		long companyId, long userId, Boolean completed, int start, int end,
		OrderByComparator orderByComparator)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowTasksByUser");
		
		return new ArrayList<WorkflowTask>();
	}
	
	@Override
	public List<WorkflowTask> getWorkflowTasksByUserRoles(
		long companyId, long userId, Boolean completed, int start, int end,
		OrderByComparator orderByComparator)
		throws WorkflowException {
		
		System.err.println("!!!!!!!!! not implemented: getWorkflowTasksByUserRoles");
	
		return new ArrayList<WorkflowTask>();
	}
	
	@Override
	public List<WorkflowTask> getWorkflowTasksByWorkflowInstance(
		long companyId, Long userId, long workflowInstanceId,
		Boolean completed, int start, int end,
		OrderByComparator orderByComparator)
		throws WorkflowException {
	

		System.err.println("!!!!!!!!! not implemented: getWorkflowTasksByWorkflowInstance");
		
		if (completed == null) {
			completed = false;
		}
		
		if (completed) {
			List<HistoricTaskInstance> hiList = historyService.createHistoricTaskInstanceQuery().processInstanceId(String.valueOf(workflowInstanceId)).finished().list();
			List<WorkflowTask> result = new ArrayList<WorkflowTask>();
			
			for (HistoricTaskInstance hiTask : hiList) {
				try {
					result.add(getWorkflowTask(companyId, hiTask));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			return result;
		} else {
			List<Task> list = getActiveTasksForProcessInstance(userId, workflowInstanceId);
			
			if ((start != QueryUtil.ALL_POS) && (end != QueryUtil.ALL_POS)) {
				if (end > list.size()) {
					end = list.size();
				}
				
				list = list.subList(start, end);
			}
			
			return getWorkflowTasks(list);
		}
	}
	
	protected List<Task> getActiveTasksForProcessInstance(Long userId, long workflowInstanceId) throws WorkflowException {
		TaskService taskService = processEngine.getTaskService();
		TaskQuery taskQuery = taskService.createTaskQuery();
		
		if (userId != null && userId != 0l) {
			taskQuery.taskAssignee(String.valueOf(userId));
		}
		
		taskQuery.processInstanceId(String.valueOf(workflowInstanceId));
		List<Task> result = new ArrayList<Task>(taskQuery.list());
		
		ProcessInstanceQuery processQuery = runtimeService.createProcessInstanceQuery();
		processQuery.active().superProcessInstanceId(String.valueOf(workflowInstanceId));
		
		for (ProcessInstance subProcess : processQuery.list()) {
			List<Task> subProcessTasks = getActiveTasksForProcessInstance(userId, Long.valueOf(subProcess.getProcessInstanceId())); 
			result.addAll(subProcessTasks);
		}
		
		return result;
	}
	
	private List<WorkflowTask> getWorkflowTasks(List<Task> list) {
		List<WorkflowTask> result = new ArrayList<WorkflowTask>();
		
		for (Task task : list) {
			try {
				result.add(getWorkflowTask(task));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	private WorkflowTask getWorkflowTask(Task task) throws WorkflowException {
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		
		ProcessDefinition processDef = workflowDefinitionManager.getProcessDefinition(task.getProcessDefinitionId());
		
		Map<String, Serializable> optionalAttributes = new HashMap<String, Serializable>();
		
		List<WorkflowTaskAssignee> workflowTaskAssignees = new ArrayList<WorkflowTaskAssignee>();
		String assigneeUserId = task.getAssignee();
		if (Validator.isNotNull(assigneeUserId)) {
			WorkflowTaskAssignee workflowTaskAssignee = new WorkflowTaskAssignee(User.class.getName(), Long.valueOf(assigneeUserId));
			workflowTaskAssignees.add(workflowTaskAssignee);
		} else {
			List<IdentityLink> participations = new ArrayList<IdentityLink>();
			try {
				participations = taskService.getIdentityLinksForTask(task.getId());
			} catch (Exception ex) {
				//nothing to do here
			}
			
			for (IdentityLink participation : participations) {
				if (Validator.isNotNull(participation.getGroupId())) {
					try {
						long companyId = Long.valueOf((String) taskService.getVariable(task.getId(), "companyId"));
						Role role = RoleLocalServiceUtil.getRole(companyId, participation.getGroupId()); //find role by name, not roleId!
						
						WorkflowTaskAssignee workflowTaskAssignee = new WorkflowTaskAssignee(Role.class.getName(), role.getRoleId());
						workflowTaskAssignees.add(workflowTaskAssignee);
					} catch (PortalException e) {
						e.printStackTrace();
					} catch (SystemException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		boolean asynchronous = true; //!task.isBlocking()
		
		CamundaWorkflowTask workflowTask = new CamundaWorkflowTask(null, task.getDueDate(), null, task.getName(), 
			task.getDescription(), asynchronous, optionalAttributes, Long.valueOf(processDef.getId()), processDef.getName(), processDef.getVersion(), 
			Long.valueOf(processInstanceId), workflowTaskAssignees, Long.valueOf(task.getId()));
		
		return workflowTask;
	}
	
	
	private WorkflowTask convertTask(long companyId, JSONObject task)
	{
		// retrieve definition if available
		JSONObject wfDef = null;
		String wfDefId = task.optString("processDefinitionId");
		try
		{
			wfDef = fCamundaUtil.getProcessDefinitionById(wfDefId);
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
		
		// retrieve process instance variable for Liferay-compatible ID, if available
		long instanceId = 0;
		
		boolean hasEntryName = false;
		try
		{
			// not available for non-Liferay workflows
			JSONObject instIdVar = fCamundaUtil.getProcessVariable(
				task.getString("processInstanceId"), "workflowId");
			instanceId = Long.parseLong(instIdVar.getString("value"));
			
			task.put("internal_processInstanceId", instanceId);
			
			JSONObject entryType = fCamundaUtil.getProcessVariable(
				task.getString("processInstanceId"), "Liferay_entryClassName");
			if(entryType.optString("value") != null)
			{
				// if there is no valid type, an exception will be thrown
				hasEntryName = true;
			}
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
		
		task.put("hasEntryName", hasEntryName);
		
		return new CamundaJsonTask(companyId, wfDef, instanceId, task);
	}
	
	private List<WorkflowTask> searchByUser(long companyId, long userId)
		throws WorkflowException
	{
		List<WorkflowTask> tasks = new ArrayList<WorkflowTask>();
		
		try
		{
			List<String> urlParams = new ArrayList<String>();
			
			// TODO: add parameters
			if(userId > 0)
			{
				// only tasks assigned to current user
				User assUser = UserLocalServiceUtil.fetchUser(userId);
				String assScreenName = assUser.getScreenName();
				urlParams.add("assignee=" + assScreenName);
			}
			
			JSONArray result = fCamundaUtil.getTasks(urlParams);
			
			// TODO: paging, sorting
			for(int i = 0; i < result.length(); ++i)
			{
				JSONObject task = result.getJSONObject(i);
				
				WorkflowTask taskInstance = convertTask(companyId, task);
				
				// skip tasks with no liferay workflow information
				if(task.has("internal_processInstanceId")
					&& task.getBoolean("hasEntryName"))
				{
					checkTaskId(task);
					
					System.err.println("### returning task with process ID: "
							+ task.get("internal_processInstanceId"));
					tasks.add(taskInstance);
				}
				else
				{
//					System.err.println("### not returning task: " + task.toString());
				}
			}
		}
		catch(Exception e)
		{
			throw new WorkflowException(e);
		}
		
		
		return tasks;
	}
	
	private void checkTaskId(JSONObject task) throws Exception
	{
		if(!task.has("taskId"))
		{
			String varVal = null;
			
			// try retrieving task ID separately
			try
			{
				JSONObject varRes = fCamundaUtil.getLocalTaskVariable(
					task.getString("id"), "taskId");
				varVal = varRes.optString("value");
				
				System.err.println("### task already has ID: " + varVal);
			}
			catch(Exception e)
			{
				// nop
			}
			
			if(varVal != null)
			{
				task.put("taskId", Long.parseLong(varVal));
				return;
			}
			
			
			// generate new ID if it is not set
			long id = fRandom.nextLong();
			
			task.put("taskId", id);
			
			// TODO: set on actual task
			System.err.println("### setting task ID: " + id);
			JSONObject modification = new JSONObject();
			
			modification.put("type", "String");
			modification.put("value", Long.toString(id));
			
			fCamundaUtil.setLocalTaskVariable(
				task.getString("id"), "taskId", modification);
		}
	}
	
	
	@Override
	public List<WorkflowTask> search(
		long companyId, long userId, String keywords, Boolean completed,
		Boolean searchByUserRoles, int start, int end,
		OrderByComparator orderByComparator)
		throws WorkflowException {
		
		System.err.println("!!!!!!!!! not properly implemented: WorkflowTask search 1");
		
		return searchByUser(companyId, userId);
	}
	
	@Override
	public List<WorkflowTask> search(
		long companyId, long userId, String taskName, String assetType,
		Long[] assetPrimaryKey, Date dueDateGT, Date dueDateLT,
		Boolean completed, Boolean searchByUserRoles, boolean andOperator,
		int start, int end, OrderByComparator orderByComparator)
		throws WorkflowException {
		
		System.err.println("!!!!!!!!! not properly implemented: WorkflowTask search 2");

		return searchByUser(companyId, userId);
	}
	
	@Override
	public List<WorkflowTask> search(
		long companyId, long userId, String keywords, String[] assetTypes,
		Boolean completed, Boolean searchByUserRoles, int start, int end,
		OrderByComparator orderByComparator)
		throws WorkflowException {
		
		System.err.println("!!!!!!!!! not properly implemented: WorkflowTask search 3");

		return searchByUser(companyId, userId);
	}
	
	@Override
	public int searchCount(
		long companyId, long userId, String keywords, Boolean completed,
		Boolean searchByUserRoles)
		throws WorkflowException {
		

		System.err.println("!!!!!!!!! not properly implemented: WorkflowTask searchCount");
	
		return search(companyId, userId, keywords, completed,
				searchByUserRoles, -1, -1, null).size();
	}
	
	@Override
	public int searchCount(
		long companyId, long userId, String taskName, String assetType,
		Long[] assetPrimaryKey, Date dueDateGT, Date dueDateLT,
		Boolean completed, Boolean searchByUserRoles, boolean andOperator)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not properly implemented: WorkflowTask searchCount");
		
		return search(companyId, userId, taskName, assetType, assetPrimaryKey,
				dueDateGT, dueDateLT, completed, searchByUserRoles, andOperator,
				-1, -1, null).size();
	}
	
	@Override
	public int searchCount(
		long companyId, long userId, String keywords, String[] assetTypes,
		Boolean completed, Boolean searchByUserRoles)
		throws WorkflowException {

		System.err.println("!!!!!!!!! not properly implemented: WorkflowTask searchCount");
		
		return search(companyId, userId, keywords, assetTypes, completed, searchByUserRoles,
				-1, -1, null).size();
	}
	
	@Override
	public WorkflowTask updateDueDate(
		long companyId, long userId, long workflowTaskId, String comment,
		Date dueDate)
		throws WorkflowException {
		
		System.err.println("!!!!!!!!! not implemented: updateDueDate");
	
		return null;
	}
	
	public ProcessEngine getProcessEngine() {
	
		return processEngine;
	}
	
	public RuntimeService getRuntimeService() {
	
		return runtimeService;
	}
	
	public TaskService getTaskService() {
	
		return taskService;
	}
	
	public IdentityService getIdentityService() {
	
		return identityService;
	}
	
	public void setProcessEngine(ProcessEngine processEngine) {
	
		this.processEngine = processEngine;
	}
	
	public void setRuntimeService(RuntimeService runtimeService) {
	
		this.runtimeService = runtimeService;
	}
	
	public void setTaskService(TaskService taskService) {
	
		this.taskService = taskService;
	}
	
	public void setIdentityService(IdentityService identityService) {
	
		this.identityService = identityService;
	}
	
	public HistoryService getHistoryService() {
	
		return historyService;
	}
	
	public void setHistoryService(HistoryService historyService) {
	
		this.historyService = historyService;
	}
}
