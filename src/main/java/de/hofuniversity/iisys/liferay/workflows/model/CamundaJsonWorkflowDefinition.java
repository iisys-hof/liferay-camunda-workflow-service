package de.hofuniversity.iisys.liferay.workflows.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.liferay.portal.kernel.workflow.WorkflowDefinition;

public class CamundaJsonWorkflowDefinition implements WorkflowDefinition
{
	private final JSONObject fSource;
	
	public CamundaJsonWorkflowDefinition(JSONObject source)
	{
		fSource = source;
	}

	@Override
	public String getContent()
	{
		return fSource.toString();
	}

	@Override
	public InputStream getInputStream()
	{
		String json =  fSource.toString();
		return new ByteArrayInputStream(
				json.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public String getName()
	{
		return fSource.getString("name");
	}

	@Override
	public Map<String, Object> getOptionalAttributes()
	{
		Map<String, Object> values = new HashMap<String, Object>();
		
		for(String key : fSource.keySet())
		{
			values.put(key, fSource.get(key));
		}
		
		return values;
	}

	@Override
	public String getTitle()
	{
		return fSource.getString("name");
	}

	@Override
	public String getTitle(String languageId)
	{
		return fSource.getString("name");
	}

	@Override
	public int getVersion()
	{
		return fSource.getInt("version");
	}

	@Override
	public boolean isActive()
	{
		return !fSource.getBoolean("suspended");
	}

}
