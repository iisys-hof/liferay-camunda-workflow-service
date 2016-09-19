package de.hofuniversity.iisys.liferay.workflows.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class CamundaConfig
{
	public static final String CAMUNDA_URL = "camunda.url";
	
	private static final String PROPERTIES = "camunda-workflows";
	
	private static CamundaConfig fInstance;
	
	private final Map<String, String> fConfig;
	
	public static synchronized CamundaConfig getInstance()
	{
		if(fInstance == null)
		{
			fInstance = new CamundaConfig();
		}
		
		return fInstance;
	}
	
	public CamundaConfig()
	{
		fConfig = new HashMap<String, String>();
		
		readConfig();
	}
	
	private void readConfig()
	{
		try
		{
	        final ClassLoader loader = Thread.currentThread()
	            .getContextClassLoader();
	        ResourceBundle rb = ResourceBundle.getBundle(PROPERTIES,
	            Locale.getDefault(), loader);
	        
	        String key = null;
	        String value = null;
	        
	        Enumeration<String> keys = rb.getKeys();
	        while(keys.hasMoreElements())
	        {
	        	key = keys.nextElement();
	        	value = rb.getString(key);
	        	
	        	fConfig.put(key, value);
	        }
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, String> getConfiguration()
	{
		return fConfig;
	}
}
