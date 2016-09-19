package de.hofuniversity.iisys.liferay.workflows;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowInstance;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.WorkflowEngine;

public class CamundaWorkflowEngine implements WorkflowEngine
{
	private final PrintWriter fLogger;
	
	public CamundaWorkflowEngine() throws Exception
	{
		File logFile = new File("/home/liferay/workflow-engine.log");
		
		if(!logFile.exists())
		{
			logFile.createNewFile();
		}
		
		fLogger = new PrintWriter(logFile);
		
		fLogger.println("camunda workflow engine initialized\n");
		fLogger.flush();
	}

	@Override
	public void deleteWorkflowDefinition(String name, int version, ServiceContext serviceContext)
			throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("deleteWorkflowDefinition\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public void deleteWorkflowInstance(long workflowInstanceId, ServiceContext serviceContext)
			throws WorkflowException {
		// TODO Auto-generated method stub

		fLogger.println("deleteWorkflowInstance\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public WorkflowDefinition deployWorkflowDefinition(String title, InputStream inputStream,
			ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub

		fLogger.println("deployWorkflowDefinition\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public ExecutionContext executeTimerWorkflowInstance(long kaleoTimerInstanceTokenId, ServiceContext serviceContext,
			Map<String, Serializable> workflowContext) throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("executeTimerWorkflowInstance\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public List<String> getNextTransitionNames(long workflowInstanceId, ServiceContext serviceContext)
			throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("getNextTransitionNames\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public WorkflowInstance getWorkflowInstance(long workflowInstanceId, ServiceContext serviceContext)
			throws WorkflowException {
		// TODO Auto-generated method stub
		

		fLogger.println("getWorkflowInstance\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public int getWorkflowInstanceCount(Long userId, String assetClassName, Long assetClassPK, Boolean completed,
			ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("getWorkflowInstanceCount\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public int getWorkflowInstanceCount(Long userId, String[] assetClassNames, Boolean completed,
			ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("getWorkflowInstanceCount\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public int getWorkflowInstanceCount(String workflowDefinitionName, int workflowDefinitionVersion, boolean completed,
			ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub

		fLogger.println("getWorkflowInstanceCount\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public List<WorkflowInstance> getWorkflowInstances(Long userId, String assetClassName, Long assetClassPK,
			Boolean completed, int start, int end, OrderByComparator<WorkflowInstance> orderByComparator,
			ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub
		

		fLogger.println("getWorkflowInstances\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public List<WorkflowInstance> getWorkflowInstances(Long userId, String[] assetClassNames, Boolean completed,
			int start, int end, OrderByComparator<WorkflowInstance> orderByComparator, ServiceContext serviceContext)
			throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("getWorkflowInstances\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public List<WorkflowInstance> getWorkflowInstances(String workflowDefinitionName, int workflowDefinitionVersion,
			boolean completed, int start, int end, OrderByComparator<WorkflowInstance> orderByComparator,
			ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("getWorkflowInstances\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public List<WorkflowInstance> search(Long userId, String assetClassName, String nodeName,
			String kaleoDefinitionName, Boolean completed, int start, int end,
			OrderByComparator<WorkflowInstance> orderByComparator, ServiceContext serviceContext)
			throws WorkflowException {
		// TODO Auto-generated method stub
		
		fLogger.println("search\n");
		fLogger.flush();
		
		throw new NotImplementedException();
	}

	@Override
	public int searchCount(Long userId, String assetClassName, String nodeName, String kaleoDefinitionName,
			Boolean completed, ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub
		

		fLogger.println("searchCount\n");
		fLogger.flush();

		
		throw new NotImplementedException();
	}

	@Override
	public WorkflowInstance signalWorkflowInstance(long workflowInstanceId, String transitionName,
			Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub


		fLogger.println("signalWorkflowInstance\n");
		fLogger.flush();

		
		throw new NotImplementedException();
	}

	@Override
	public WorkflowInstance startWorkflowInstance(String workflowDefinitionName, Integer workflowDefinitionVersion,
			String transitionName, Map<String, Serializable> workflowContext, ServiceContext serviceContext)
			throws WorkflowException {
		// TODO Auto-generated method stub

		fLogger.println("startWorkflowInstance\n");
		fLogger.flush();

		
		throw new NotImplementedException();
	}

	@Override
	public WorkflowInstance updateContext(long workflowInstanceId, Map<String, Serializable> workflowContext,
			ServiceContext serviceContext) throws WorkflowException {
		// TODO Auto-generated method stub

		fLogger.println("updateContext\n");
		fLogger.flush();

		
		throw new NotImplementedException();
	}

	@Override
	public void validateWorkflowDefinition(InputStream inputStream) throws WorkflowException {
		// TODO Auto-generated method stub
		

		fLogger.println("validateWorkflowDefinition\n");
		fLogger.flush();
		
		throw new NotImplementedException();
		
	}

}
