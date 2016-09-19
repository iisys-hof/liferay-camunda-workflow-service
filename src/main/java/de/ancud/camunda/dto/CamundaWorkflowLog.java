package de.ancud.camunda.dto;

import com.liferay.portal.kernel.workflow.DefaultWorkflowLog;

import java.util.Date;

/**
 * @author bnmaxim.
 */
public class CamundaWorkflowLog extends DefaultWorkflowLog {

    public CamundaWorkflowLog(long userId, long roleId, long workflowLogId,
                              long workflowtaskId, long prevRoleId, long prevUserId, int type, String state, String prevState) {
        super();
        this.setCreateDate(new Date());
        this.setAuditUserId(userId);
        this.setUserId(userId);
        this.setRoleId(roleId);
        this.setWorkflowLogId(workflowLogId);
        this.setWorkflowTaskId(workflowtaskId);
        this.setPreviousRoleId(prevRoleId);
        this.setPreviousState(prevState);
        this.setPreviousUserId(prevUserId);
        this.setState(state);
        this.setType(type);
    }


}
