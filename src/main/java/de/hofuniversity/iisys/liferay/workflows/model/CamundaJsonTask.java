package de.hofuniversity.iisys.liferay.workflows.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskAssignee;

public class CamundaJsonTask implements WorkflowTask
{
	private final JSONObject fSource;
	
	private final JSONObject fDefinition;

	private final long fAssigneeId;
	
	private final long fWfInstanceId;
	
	public CamundaJsonTask(long companyId, JSONObject definition,
			long wfInstanceId, JSONObject source)
	{
		fSource = source;
		
		fDefinition = definition;
		
		fWfInstanceId = wfInstanceId;
		
		// translate LDAP ID to Liferay ID
		String screenName = source.optString("assignee");
		if(screenName != null)
		{
			User assUser = UserLocalServiceUtil.fetchUserByScreenName(
					companyId, screenName);
			fAssigneeId = assUser.getUserId();
		}
		else
		{
			fAssigneeId = 0;
		}
	}

	@Override
	public long getAssigneeUserId()
	{
		return fAssigneeId;
	}

	@Override
	public Date getCompletionDate()
	{
		// TODO: not available - only for historic tasks
		return new Date();
	}

	@Override
	public Date getCreateDate()
	{
		// TODO Auto-generated method stub
		// created - "2016-06-01T10:18:19"
		return new Date();
	}

	@Override
	public String getDescription()
	{
		String desc = "";
		
		try
		{
			desc = fSource.optString("description");
		}
		catch(Exception e)
		{
			//nop
		}
		
		return desc;
	}

	@Override
	public Date getDueDate()
	{
		// TODO Auto-generated method stub
		// due - "2016-06-01T10:18:19"
		return new Date();
	}

	@Override
	public String getName()
	{
		return fSource.getString("name");
	}

	@Override
	public Map<String, Serializable> getOptionalAttributes()
	{
		// TODO Auto-generated method stub
		// TODO: use all directly?
		
		Map<String, Serializable> atts = new HashMap<String, Serializable>();
		
		for(String key : fSource.keySet())
		{
			atts.put(key, (Serializable) fSource.get(key));
		}
		
		return atts;
	}

	@Override
	public long getWorkflowDefinitionId()
	{
		// TODO: does not exist
		return 0;
	}

	@Override
	public String getWorkflowDefinitionName()
	{
		if(fDefinition != null)
		{
			return fDefinition.getString("name");
		}
		else
		{
			return "";
		}
	}

	@Override
	public int getWorkflowDefinitionVersion()
	{
		return fSource.getInt("version");
	}

	@Override
	public long getWorkflowInstanceId()
	{
		return fWfInstanceId;
	}

	@Override
	public List<WorkflowTaskAssignee> getWorkflowTaskAssignees()
	{
		// TODO: groups/roles/multiple users?
		
		List<WorkflowTaskAssignee> assUsers = new ArrayList<>();
		
		WorkflowTaskAssignee ass = new WorkflowTaskAssignee(
			User.class.getName(), fAssigneeId);
		assUsers.add(ass);
		
		return assUsers;
	}

	@Override
	public long getWorkflowTaskId()
	{
		return fSource.optLong("taskId");
	}

	@Override
	public boolean isAssignedToSingleUser()
	{
		// TODO Auto-generated method stub
		// TODO: determine if assigned to group or user? how?
		return true;
	}

	@Override
	public boolean isAsynchronous()
	{
		// TODO: ???
		return true;
	}

	@Override
	public boolean isCompleted()
	{
		// only uncompleted tasks are returned
		return false;
	}
}
