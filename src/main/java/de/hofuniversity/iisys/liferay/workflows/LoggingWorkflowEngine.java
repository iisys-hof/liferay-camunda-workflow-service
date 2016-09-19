/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package de.hofuniversity.iisys.liferay.workflows;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowInstance;
import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.portal.workflow.kaleo.KaleoWorkflowModelConverter;
import com.liferay.portal.workflow.kaleo.definition.Definition;
import com.liferay.portal.workflow.kaleo.definition.deployment.WorkflowDeployer;
import com.liferay.portal.workflow.kaleo.definition.parser.WorkflowModelParser;
import com.liferay.portal.workflow.kaleo.definition.parser.WorkflowValidator;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinition;
import com.liferay.portal.workflow.kaleo.model.KaleoInstance;
import com.liferay.portal.workflow.kaleo.model.KaleoInstanceToken;
import com.liferay.portal.workflow.kaleo.model.KaleoNode;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceToken;
import com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceToken;
import com.liferay.portal.workflow.kaleo.model.KaleoTransition;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.KaleoSignaler;
import com.liferay.portal.workflow.kaleo.runtime.WorkflowEngine;
import com.liferay.portal.workflow.kaleo.runtime.internal.BaseKaleoBean;
import com.liferay.portal.workflow.kaleo.runtime.internal.node.NodeExecutorFactory;
import com.liferay.portal.workflow.kaleo.runtime.internal.util.comparator.KaleoInstanceOrderByComparator;
import com.liferay.portal.workflow.kaleo.runtime.node.NodeExecutor;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

/**
 * @author Michael C. Han
 */
@Transactional(
	isolation = Isolation.PORTAL, propagation = Propagation.REQUIRED,
	rollbackFor = {Exception.class}
)
public class LoggingWorkflowEngine
	extends BaseKaleoBean implements WorkflowEngine {

	private final PrintWriter fLogger;
	
	public LoggingWorkflowEngine() throws Exception
	{
		File logFile = new File("/home/liferay/workflow-engine.log");
		
		if(!logFile.exists())
		{
			logFile.createNewFile();
		}
		
		fLogger = new PrintWriter(logFile);
		
		fLogger.println("logging workflow engine initialized\n");
		fLogger.flush();
	}
	
	private void logMap(Map<String, Serializable> map, String indent)
	{
		if(map != null)
		{
			for(Entry<String, Serializable> entry : map.entrySet())
			{
				fLogger.println(indent + entry.getKey() + ": " + entry.getValue());
			}
		}
	}
	
	private void logServiceContext(ServiceContext serviceContext, String indent)
	{
		if(serviceContext != null)
		{
			fLogger.println(indent + "command: " + serviceContext.getCommand());
			try {
				fLogger.println(indent + "guestOrUserId: " + serviceContext.getGuestOrUserId());
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fLogger.println(indent + "workflowAction: " + serviceContext.getWorkflowAction());
			fLogger.println(indent + "attributes:" );
			logMap(serviceContext.getAttributes(), indent + "\t");
		}
	}
	
	private void logExecContext(ExecutionContext ec, String indent)
	{
		if(ec != null)
		{
			fLogger.println(indent + "transitionName: " + ec.getTransitionName());
			fLogger.println(indent + "serviceContext:");
			logServiceContext(ec.getServiceContext(), indent + "\t");

			fLogger.println(indent + "workflowContext:");
			logMap(ec.getWorkflowContext(), indent + "\t");
		}
	}
	
	private void logWfInstance(WorkflowInstance wf, String indent)
	{
		if(wf != null)
		{
			fLogger.println(indent + "ChildrenWorkflowInstanceCount: " + wf.getChildrenWorkflowInstanceCount());
			fLogger.println(indent + "ParentWorkflowInstanceId: " + wf.getParentWorkflowInstanceId());
			fLogger.println(indent + "state: " + wf.getState());
			fLogger.println(indent + "WorkflowDefinitionName: " + wf.getWorkflowDefinitionName());
			fLogger.println(indent + "WorkflowInstanceId: " + wf.getWorkflowInstanceId());
			fLogger.println(indent + "WorkflowContext:");
			logMap(wf.getWorkflowContext(), indent + "\t");
		}
	}

	@Override
	public void deleteWorkflowDefinition(
			String name, int version, ServiceContext serviceContext)
		throws WorkflowException {

		// TODO
		fLogger.println("WorkflowEngine.deleteWorkflowDefinition");
		fLogger.println("\tTODO");
		fLogger.flush();
		
		try {
			kaleoDefinitionLocalService.deleteKaleoDefinition(
				name, version, serviceContext);
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public void deleteWorkflowInstance(
			long workflowInstanceId, ServiceContext serviceContext)
		throws WorkflowException {

		try {
			fLogger.println("WorkflowEngine.deleteWorkflowInstance");
			fLogger.println("\tworkflowInstanceId: " + workflowInstanceId);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			fLogger.flush();
			
			kaleoInstanceLocalService.deleteKaleoInstance(workflowInstanceId);
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public WorkflowDefinition deployWorkflowDefinition(
			String title, InputStream inputStream,
			ServiceContext serviceContext)
		throws WorkflowException {

		try {
			fLogger.println("WorkflowEngine.deleteWorkflowDefinition");
			fLogger.println("\tTODO");
			fLogger.flush();
			
			Definition definition = _workflowModelParser.parse(inputStream);

			if (_workflowValidator != null) {
				_workflowValidator.validate(definition);
			}

			WorkflowDefinition workflowDefinition = _workflowDeployer.deploy(
				title, definition, serviceContext);

			return workflowDefinition;
		}
		catch (WorkflowException we) {
			throw we;
		}
		catch (PortalException pe) {
			throw new WorkflowException(pe);
		}
	}

	@Override
	public ExecutionContext executeTimerWorkflowInstance(
			long kaleoTimerInstanceTokenId, ServiceContext serviceContext,
			Map<String, Serializable> workflowContext)
		throws WorkflowException {

		try {
			fLogger.println("WorkflowEngine.executeTimerWorkflowInstance");
			fLogger.println("\tkaleoTimerInstanceTokenId: " + kaleoTimerInstanceTokenId);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			fLogger.println("\tworkflowContext:");
			logMap(workflowContext, "\t\t");
			
			
			KaleoTimerInstanceToken kaleoTimerInstanceToken =
				kaleoTimerInstanceTokenLocalService.getKaleoTimerInstanceToken(
					kaleoTimerInstanceTokenId);

			KaleoInstanceToken kaleoInstanceToken =
				kaleoTimerInstanceToken.getKaleoInstanceToken();

			final ExecutionContext executionContext = new ExecutionContext(
				kaleoInstanceToken, kaleoTimerInstanceToken, workflowContext,
				serviceContext);

			KaleoTaskInstanceToken kaleoTaskInstanceToken =
				kaleoTimerInstanceToken.getKaleoTaskInstanceToken();

			executionContext.setKaleoTaskInstanceToken(kaleoTaskInstanceToken);

			final KaleoNode currentKaleoNode =
				kaleoInstanceToken.getCurrentKaleoNode();

			NodeExecutor nodeExecutor = _nodeExecutorFactory.getNodeExecutor(
				currentKaleoNode.getType());

			nodeExecutor.executeTimer(currentKaleoNode, executionContext);

			TransactionCommitCallbackUtil.registerCallback(
				new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						_kaleoSignaler.signalExecute(
							currentKaleoNode, executionContext);

						return null;
					}

				});
			

			fLogger.println("\tRETURN executionContext:");
			logExecContext(executionContext, "\t\t");
			fLogger.flush();

			return executionContext;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public List<String> getNextTransitionNames(
			long workflowInstanceId, ServiceContext serviceContext)
		throws WorkflowException {

		try {
			fLogger.println("WorkflowEngine.getNextTransitionNames");
			fLogger.println("\tworkflowInstanceId: " + workflowInstanceId);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			KaleoInstance kaleoInstance =
				kaleoInstanceLocalService.getKaleoInstance(workflowInstanceId);

			KaleoInstanceToken rootKaleoInstanceToken =
				kaleoInstance.getRootKaleoInstanceToken(null, serviceContext);

			List<String> transitionNames = new ArrayList<>();

			getNextTransitionNames(rootKaleoInstanceToken, transitionNames);

			if(transitionNames != null)
			{
				fLogger.println("\tRETURN transitionNames:");
				for(String name : transitionNames)
				{
					fLogger.println("\t\t" + name);
				}
			}
			
			fLogger.flush();
			
			return transitionNames;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public WorkflowInstance getWorkflowInstance(
			long workflowInstanceId, ServiceContext serviceContext)
		throws WorkflowException {

		try {
			fLogger.println("WorkflowEngine.getWorkflowInstance");
			fLogger.println("\tworkflowInstanceId: " + workflowInstanceId);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			KaleoInstance kaleoInstance =
				kaleoInstanceLocalService.getKaleoInstance(workflowInstanceId);

			KaleoInstanceToken rootKaleoInstanceToken =
				kaleoInstance.getRootKaleoInstanceToken(serviceContext);

			WorkflowInstance instance = _kaleoWorkflowModelConverter.toWorkflowInstance(
				kaleoInstance, rootKaleoInstanceToken);
			
			
			if(instance != null)
			{
				fLogger.println("\tRETURN WorkflowInstance:");
				
				logWfInstance(instance, "\t\t");
			}
			
			fLogger.flush();
			
			
			return instance;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public int getWorkflowInstanceCount(
			Long userId, String assetClassName, Long assetClassPK,
			Boolean completed, ServiceContext serviceContext)
		throws WorkflowException {

		try {
			
			fLogger.println("WorkflowEngine.getWorkflowInstanceCount");
			fLogger.println("\tuserId: " + userId);
			fLogger.println("\tassetClassName: " + assetClassName);
			fLogger.println("\tassetClassPK: " + assetClassPK);
			fLogger.println("\tcompleted: " + completed);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			
			int count = kaleoInstanceLocalService.getKaleoInstancesCount(
				userId, assetClassName, assetClassPK, completed,
				serviceContext);
			
			fLogger.println("\tRETURN count: " + count);
			fLogger.flush();
			
			return count;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public int getWorkflowInstanceCount(
			Long userId, String[] assetClassNames, Boolean completed,
			ServiceContext serviceContext)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.getWorkflowInstanceCount");
			fLogger.println("\tuserId: " + userId);
			fLogger.println("\tassetClassNames: " + assetClassNames);
			fLogger.println("\tcompleted: " + completed);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			
			int count = kaleoInstanceLocalService.getKaleoInstancesCount(
				userId, assetClassNames, completed, serviceContext);
			
			fLogger.println("\tRETURN count: " + count);
			fLogger.flush();
			
			return count;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public int getWorkflowInstanceCount(
			String workflowDefinitionName, int workflowDefinitionVersion,
			boolean completed, ServiceContext serviceContext)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.getWorkflowInstanceCount");
			fLogger.println("\tworkflowDefinitionName: " + workflowDefinitionName);
			fLogger.println("\tworkflowDefinitionVersion: " + workflowDefinitionVersion);
			fLogger.println("\tcompleted: " + completed);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			
			int count = kaleoInstanceLocalService.getKaleoInstancesCount(
				workflowDefinitionName, workflowDefinitionVersion, completed,
				serviceContext);
			
			fLogger.println("\tRETURN count: " + count);
			fLogger.flush();
			
			return count;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public List<WorkflowInstance> getWorkflowInstances(
			Long userId, String assetClassName, Long assetClassPK,
			Boolean completed, int start, int end,
			OrderByComparator<WorkflowInstance> orderByComparator,
			ServiceContext serviceContext)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.getWorkflowInstances");
			fLogger.println("\tuserId: " + userId);
			fLogger.println("\tassetClassName: " + assetClassName);
			fLogger.println("\tassetClassPK: " + assetClassPK);
			fLogger.println("\tcompleted: " + completed);
			fLogger.println("\tstart: " + start);
			fLogger.println("\tend: " + end);
			fLogger.println("\torderByComparator: " + orderByComparator);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			
			List<KaleoInstance> kaleoInstances =
				kaleoInstanceLocalService.getKaleoInstances(
					userId, assetClassName, assetClassPK, completed, start, end,
					KaleoInstanceOrderByComparator.getOrderByComparator(
						orderByComparator, _kaleoWorkflowModelConverter,
						serviceContext),
					serviceContext);

			List<WorkflowInstance> instances = toWorkflowInstances(kaleoInstances, serviceContext);
			
			if(instances != null)
			{
				fLogger.println("\tRETURN instances:");
				for(WorkflowInstance wfi : instances)
				{
					fLogger.println("\t\tworkflow instance:");
					logWfInstance(wfi, "\t\t\t");
				}
			}
			fLogger.flush();
			
			
			return instances;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public List<WorkflowInstance> getWorkflowInstances(
			Long userId, String[] assetClassNames, Boolean completed, int start,
			int end, OrderByComparator<WorkflowInstance> orderByComparator,
			ServiceContext serviceContext)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.getWorkflowInstances");
			fLogger.println("\tuserId: " + userId);
			fLogger.println("\tassetClassNames: " + assetClassNames);
			fLogger.println("\tcompleted: " + completed);
			fLogger.println("\tstart: " + start);
			fLogger.println("\tend: " + end);
			fLogger.println("\torderByComparator: " + orderByComparator);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			
			List<KaleoInstance> kaleoInstances =
				kaleoInstanceLocalService.getKaleoInstances(
					userId, assetClassNames, completed, start, end,
					KaleoInstanceOrderByComparator.getOrderByComparator(
						orderByComparator, _kaleoWorkflowModelConverter,
						serviceContext),
					serviceContext);

			List<WorkflowInstance> instances = toWorkflowInstances(kaleoInstances, serviceContext);
			
			if(instances != null)
			{
				fLogger.println("\tRETURN instances:");
				for(WorkflowInstance wfi : instances)
				{
					fLogger.println("\t\tworkflow instance:");
					logWfInstance(wfi, "\t\t\t");
				}
			}
			
			fLogger.flush();
			
			
			return instances;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public List<WorkflowInstance> getWorkflowInstances(
			String workflowDefinitionName, int workflowDefinitionVersion,
			boolean completed, int start, int end,
			OrderByComparator<WorkflowInstance> orderByComparator,
			ServiceContext serviceContext)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.getWorkflowInstances");
			fLogger.println("\tworkflowDefinitionName: " + workflowDefinitionName);
			fLogger.println("\tworkflowDefinitionVersion: " + workflowDefinitionVersion);
			fLogger.println("\tcompleted: " + completed);
			fLogger.println("\tstart: " + start);
			fLogger.println("\tend: " + end);
			fLogger.println("\torderByComparator: " + orderByComparator);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			
			List<KaleoInstance> kaleoInstances =
				kaleoInstanceLocalService.getKaleoInstances(
					workflowDefinitionName, workflowDefinitionVersion,
					completed, start, end,
					KaleoInstanceOrderByComparator.getOrderByComparator(
						orderByComparator, _kaleoWorkflowModelConverter,
						serviceContext),
					serviceContext);

			List<WorkflowInstance> instances = toWorkflowInstances(kaleoInstances, serviceContext);
			
			if(instances != null)
			{
				fLogger.println("\tRETURN instances:");
				for(WorkflowInstance wfi : instances)
				{
					fLogger.println("\t\tworkflow instance:");
					logWfInstance(wfi, "\t\t\t");
				}
			}
			
			fLogger.flush();
			
			return instances;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public List<WorkflowInstance> search(
			Long userId, String assetType, String nodeName,
			String kaleoDefinitionName, Boolean completed, int start, int end,
			OrderByComparator<WorkflowInstance> orderByComparator,
			ServiceContext serviceContext)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.search");
			fLogger.println("\tuserId: " + userId);
			fLogger.println("\tassetType: " + assetType);
			fLogger.println("\tnodeName: " + nodeName);
			fLogger.println("\tkaleoDefinitionName: " + kaleoDefinitionName);
			fLogger.println("\tcompleted: " + completed);
			fLogger.println("\tstart: " + start);
			fLogger.println("\tend: " + end);
			fLogger.println("\torderByComparator: " + orderByComparator);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			
			List<KaleoInstance> kaleoInstances =
				kaleoInstanceLocalService.search(
					userId, assetType, nodeName, kaleoDefinitionName, completed,
					start, end,
					KaleoInstanceOrderByComparator.getOrderByComparator(
						orderByComparator, _kaleoWorkflowModelConverter,
						serviceContext),
					serviceContext);

			List<WorkflowInstance> instances = toWorkflowInstances(kaleoInstances, serviceContext);
			

			if(instances != null)
			{
				fLogger.println("\tRETURN instances:");
				for(WorkflowInstance wfi : instances)
				{
					fLogger.println("\t\tworkflow instance:");
					logWfInstance(wfi, "\t\t\t");
				}
			}
			
			fLogger.flush();
			
			return instances;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public int searchCount(
			Long userId, String assetType, String nodeName,
			String kaleoDefinitionName, Boolean completed,
			ServiceContext serviceContext)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.searchCount");
			fLogger.println("\tuserId: " + userId);
			fLogger.println("\tassetType: " + assetType);
			fLogger.println("\tnodeName: " + nodeName);
			fLogger.println("\tkaleoDefinitionName: " + kaleoDefinitionName);
			fLogger.println("\tcompleted: " + completed);
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			
			int count = kaleoInstanceLocalService.searchCount(
				userId, assetType, nodeName, kaleoDefinitionName, completed,
				serviceContext);
			
			fLogger.println("\tRETURN count: " + count);
			
			fLogger.flush();
			
			return count;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	public void setKaleoSignaler(KaleoSignaler kaleoSignaler) {
		_kaleoSignaler = kaleoSignaler;
	}

	@Override
	public WorkflowInstance signalWorkflowInstance(
			long workflowInstanceId, final String transitionName,
			Map<String, Serializable> workflowContext,
			ServiceContext serviceContext)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.signalWorkflowInstance");
			fLogger.println("\tworkflowInstanceId: " + workflowInstanceId);
			fLogger.println("\ttransitionName: " + transitionName);
			fLogger.println("\tworkflowContext:");
			logMap(workflowContext, "\t\t");
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			KaleoInstance kaleoInstance = doUpdateContext(
				workflowInstanceId, workflowContext, serviceContext);

			KaleoInstanceToken kaleoInstanceToken =
				kaleoInstance.getRootKaleoInstanceToken(serviceContext);

			if (Validator.isNotNull(transitionName)) {

				// Validate that the transition actually exists before moving
				// forward

				KaleoNode currentKaleoNode =
					kaleoInstanceToken.getCurrentKaleoNode();

				currentKaleoNode.getKaleoTransition(transitionName);
			}

			serviceContext.setScopeGroupId(kaleoInstanceToken.getGroupId());

			final ExecutionContext executionContext = new ExecutionContext(
				kaleoInstanceToken, workflowContext, serviceContext);

			TransactionCommitCallbackUtil.registerCallback(
				new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						try {
							_kaleoSignaler.signalExit(
								transitionName, executionContext);
						}
						catch (Exception e) {
							throw new WorkflowException(
								"Unable to signal next transition", e);
						}

						return null;
					}

				});

			WorkflowInstance instance = _kaleoWorkflowModelConverter.toWorkflowInstance(
				kaleoInstance, kaleoInstanceToken, workflowContext);
			
			fLogger.println("\tRETURN instance:");
			logWfInstance(instance, "\t\t");
			
			fLogger.flush();
			
			return instance;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public WorkflowInstance startWorkflowInstance(
			String workflowDefinitionName, Integer workflowDefinitionVersion,
			final String transitionName,
			Map<String, Serializable> workflowContext,
			ServiceContext serviceContext)
		throws WorkflowException {

		fLogger.println("WorkflowEngine.startWorkflowInstance");
		fLogger.println("\tworkflowDefinitionName: " + workflowDefinitionName);
		fLogger.println("\tworkflowDefinitionVersion: " + workflowDefinitionVersion);
		fLogger.println("\ttransitionName: " + transitionName);
		fLogger.println("\tworkflowContext:");
		logMap(workflowContext, "\t\t");
		fLogger.println("\tserviceContext:");
		logServiceContext(serviceContext, "\t\t");

		try {
			KaleoDefinition kaleoDefinition =
				kaleoDefinitionLocalService.getKaleoDefinition(
					workflowDefinitionName, workflowDefinitionVersion,
					serviceContext);

			if (!kaleoDefinition.isActive()) {
				throw new WorkflowException(
					"Inactive workflow definition with name " +
						workflowDefinitionName + " and version " +
							workflowDefinitionVersion);
			}

			KaleoNode kaleoStartNode = kaleoDefinition.getKaleoStartNode();

			if (Validator.isNotNull(transitionName)) {

				// Validate that the transition actually exists before moving
				// forward

				kaleoStartNode.getKaleoTransition(transitionName);
			}

			long scopeGroupId = serviceContext.getScopeGroupId();

			if (scopeGroupId != WorkflowConstants.DEFAULT_GROUP_ID) {
				Group group = _groupLocalService.getGroup(scopeGroupId);

				if (group.isLayout()) {
					group = _groupLocalService.getGroup(
						group.getParentGroupId());

					serviceContext.setScopeGroupId(group.getGroupId());
				}
			}

			KaleoInstance kaleoInstance =
				kaleoInstanceLocalService.addKaleoInstance(
					kaleoDefinition.getKaleoDefinitionId(),
					kaleoDefinition.getName(), kaleoDefinition.getVersion(),
					workflowContext, serviceContext);

			KaleoInstanceToken rootKaleoInstanceToken =
				kaleoInstance.getRootKaleoInstanceToken(
					workflowContext, serviceContext);

			rootKaleoInstanceToken.setCurrentKaleoNode(kaleoStartNode);

			kaleoLogLocalService.addWorkflowInstanceStartKaleoLog(
				rootKaleoInstanceToken, serviceContext);

			final ExecutionContext executionContext = new ExecutionContext(
				rootKaleoInstanceToken, workflowContext, serviceContext);

			TransactionCommitCallbackUtil.registerCallback(
				new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						try {
							_kaleoSignaler.signalEntry(
								transitionName, executionContext);
						}
						catch (Exception e) {
							throw new WorkflowException(
								"Unable to start workflow", e);
						}

						return null;
					}

				});

			WorkflowInstance instance = _kaleoWorkflowModelConverter.toWorkflowInstance(
				kaleoInstance, rootKaleoInstanceToken, workflowContext);
			
			fLogger.println("\tRETURN instance:");
			logWfInstance(instance, "\t\t");
			
			fLogger.flush();
			
			return instance;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public WorkflowInstance updateContext(
			long workflowInstanceId, Map<String, Serializable> workflowContext,
			ServiceContext serviceContext)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.updateContext");
			fLogger.println("\tworkflowInstanceId: " + workflowInstanceId);
			fLogger.println("\tworkflowContext:");
			logMap(workflowContext, "\t\t");
			fLogger.println("\tserviceContext:");
			logServiceContext(serviceContext, "\t\t");
			
			
			KaleoInstance kaleoInstance = doUpdateContext(
				workflowInstanceId, workflowContext, serviceContext);

			KaleoInstanceToken rootKaleoInstanceToken =
				kaleoInstance.getRootKaleoInstanceToken(serviceContext);

			WorkflowInstance instance = _kaleoWorkflowModelConverter.toWorkflowInstance(
				kaleoInstance, rootKaleoInstanceToken);
			
			

			fLogger.println("\tRETURN instance:");
			logWfInstance(instance, "\t\t");
			
			fLogger.flush();
			
			return instance;
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	@Override
	public void validateWorkflowDefinition(InputStream inputStream)
		throws WorkflowException {

		try {

			fLogger.println("WorkflowEngine.updateContext");
			
			Definition definition = _workflowModelParser.parse(inputStream);

			fLogger.println("\tdefinition.name" + definition.getName());
			fLogger.println("\tdefinition.content" + definition.getContent());
			fLogger.println("\tdefinition.initialState" + definition.getInitialState());
			
			fLogger.flush();
			
			if (_workflowValidator != null) {
				_workflowValidator.validate(definition);
			}
		}
		catch (Exception e) {
			throw new WorkflowException(e);
		}
	}

	protected KaleoInstance doUpdateContext(
			long workflowInstanceId, Map<String, Serializable> workflowContext,
			ServiceContext serviceContext)
		throws Exception {

		return kaleoInstanceLocalService.updateKaleoInstance(
			workflowInstanceId, workflowContext, serviceContext);
	}

	protected void getNextTransitionNames(
			KaleoInstanceToken kaleoInstanceToken, List<String> transitionNames)
		throws Exception {

		if (kaleoInstanceToken.hasIncompleteChildrenKaleoInstanceToken()) {
			List<KaleoInstanceToken> incompleteChildrenKaleoInstanceTokens =
				kaleoInstanceToken.getIncompleteChildrenKaleoInstanceTokens();

			for (KaleoInstanceToken incompleteChildrenKaleoInstanceToken :
					incompleteChildrenKaleoInstanceTokens) {

				getNextTransitionNames(
					incompleteChildrenKaleoInstanceToken, transitionNames);
			}
		}
		else {
			KaleoNode kaleoNode = kaleoInstanceToken.getCurrentKaleoNode();

			List<KaleoTransition> kaleoTransitions =
				kaleoNode.getKaleoTransitions();

			for (KaleoTransition kaleoTransition : kaleoTransitions) {
				transitionNames.add(kaleoTransition.getName());
			}
		}
	}

	protected List<WorkflowInstance> toWorkflowInstances(
			List<KaleoInstance> kaleoInstances, ServiceContext serviceContext)
		throws PortalException {

		List<WorkflowInstance> workflowInstances = new ArrayList<>(
			kaleoInstances.size());

		for (KaleoInstance kaleoInstance : kaleoInstances) {
			KaleoInstanceToken rootKaleoInstanceToken =
				kaleoInstance.getRootKaleoInstanceToken(serviceContext);

			workflowInstances.add(
				_kaleoWorkflowModelConverter.toWorkflowInstance(
					kaleoInstance, rootKaleoInstanceToken));
		}

		return workflowInstances;
	}

	@ServiceReference(type = GroupLocalService.class)
	private GroupLocalService _groupLocalService;

	private KaleoSignaler _kaleoSignaler;

	@ServiceReference(type = KaleoWorkflowModelConverter.class)
	private KaleoWorkflowModelConverter _kaleoWorkflowModelConverter;

	@ServiceReference(type = NodeExecutorFactory.class)
	private NodeExecutorFactory _nodeExecutorFactory;

	@ServiceReference(type = WorkflowDeployer.class)
	private WorkflowDeployer _workflowDeployer;

	@ServiceReference(type = WorkflowModelParser.class)
	private WorkflowModelParser _workflowModelParser;

	@ServiceReference(type = WorkflowValidator.class)
	private WorkflowValidator _workflowValidator;

}