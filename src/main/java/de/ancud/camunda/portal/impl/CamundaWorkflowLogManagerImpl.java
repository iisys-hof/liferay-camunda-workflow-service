package de.ancud.camunda.portal.impl;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.DefaultWorkflowLog;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowLog;
import com.liferay.portal.kernel.workflow.WorkflowLogManager;

import java.util.ArrayList;
import java.util.List;

import de.ancud.camunda.dto.CamundaWorkflowLog;
import org.camunda.bpm.engine.ProcessEngine;
import org.osgi.service.component.annotations.Component;

/**
 * @author bnmaxim.
 */
@Component(
	immediate = true, property = {"proxy.bean=false"},
	service = WorkflowLogManager.class
)
public class CamundaWorkflowLogManagerImpl implements WorkflowLogManager {
	
    private static Log log = LogFactoryUtil.getLog(CamundaWorkflowLogManagerImpl.class);
    private ProcessEngine processEngine;

    @Override
    public int getWorkflowLogCountByWorkflowInstance(long companyId, long workflowInstanceId, List<Integer> logTypes) throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowLogCountByWorkflowInstance");
    	
    	return 0;
    }

    @Override
    public int getWorkflowLogCountByWorkflowTask(long companyId, long workflowTaskId, List<Integer> logTypes) throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowLogCountByWorkflowTask");
    	
    	return 0;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

	@Override
	public List<WorkflowLog> getWorkflowLogsByWorkflowInstance(long companyId, long workflowInstanceId,
			List<Integer> logTypes, int start, int end, OrderByComparator<WorkflowLog> orderByComparator)
			throws WorkflowException {
		List<WorkflowLog> wfll = new ArrayList<WorkflowLog>();
		
		System.err.println("!!!!!!!!! not implemented: getWorkflowLogsByWorkflowInstance");
		
//		wfll.add(new CamundaWorkflowLog(0L,0L,0L,0L,0L,0L,1,"waiting","running"));
		
		return wfll;
	}

	@Override
	public List<WorkflowLog> getWorkflowLogsByWorkflowTask(long companyId, long workflowTaskId, List<Integer> logTypes,
			int start, int end, OrderByComparator<WorkflowLog> orderByComparator) throws WorkflowException {

		System.err.println("!!!!!!!!! not implemented: getWorkflowLogsByWorkflowInstance");
		
		return getWorkflowLogsByWorkflowInstance(0L,0L,null,0,0,null);
	}
}
