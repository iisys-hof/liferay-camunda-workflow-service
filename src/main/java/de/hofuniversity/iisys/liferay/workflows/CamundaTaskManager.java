package de.hofuniversity.iisys.liferay.workflows;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.workflow.kaleo.runtime.TaskManager;

public class CamundaTaskManager implements TaskManager
{
	private final PrintWriter fLogger;
	
	public CamundaTaskManager() throws Exception
	{
		File logFile = new File("/home/liferay/workflow-task-manager.log");
		
		if(!logFile.exists())
		{
			logFile.createNewFile();
		}
		
		fLogger = new PrintWriter(logFile);
		
		fLogger.println("camunda workflow task manager initialized\n");
		fLogger.flush();
	}

	@Override
	public WorkflowTask assignWorkflowTaskToRole(long workflowTaskId, long roleId, String comment, Date dueDate,
			Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub

		fLogger.println("assignWorkflowTaskToRole\n");
		fLogger.flush();

		throw new NotImplementedException();
	}

	@Override
	public WorkflowTask assignWorkflowTaskToUser(long workflowTaskId, long assigneeUserId, String comment, Date dueDate,
			Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub

		fLogger.println("assignWorkflowTaskToUser\n");
		fLogger.flush();

		throw new NotImplementedException();
	}

	@Override
	public WorkflowTask completeWorkflowTask(long workflowTaskId, String transitionName, String comment,
			Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("completeWorkflowTask\n");
		fLogger.flush();

		throw new NotImplementedException();
	}

	@Override
	public WorkflowTask updateDueDate(long workflowTaskId, String comment, Date dueDate, ServiceContext serviceContext)
			throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("updateDueDate\n");
		fLogger.flush();

		throw new NotImplementedException();
	}

}
