package de.ancud.camunda.portal.impl;

import com.liferay.portal.kernel.workflow.WorkflowEngineManager;

import java.util.Collections;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author bnmaxim.
 */
@Component(
	immediate = true, property = {"proxy.bean=false"},
	service = WorkflowEngineManager.class
)
public class CamundaWorkflowEngineManagerImpl implements WorkflowEngineManager {
    private static final Map<String, Object> OPTIONAL_ATTRIBUTES = Collections.emptyMap();
    private static final String KEY = "camunda";
    private static final String NAME = "Camunda REST Process Engine";
    private static final String VERSION = "7.4";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Map<String, Object> getOptionalAttributes() {
        return OPTIONAL_ATTRIBUTES;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public boolean isDeployed() {
        return true;
    }

}
