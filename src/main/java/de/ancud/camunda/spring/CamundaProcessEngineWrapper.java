package de.ancud.camunda.spring;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

import javax.sql.DataSource;

/**
 * @author bnmaxim.
 */
public class CamundaProcessEngineWrapper {
    private ProcessEngine processEngine;
    private DataSource dataSource;
    private String databaseType;

    private boolean jobExecutorActivate;
    private String processEngineName;
    private String history;

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

    public String getProcessEngineName() {
        return processEngineName;
    }

    public void setProcessEngineName(String processEngineName) {
        this.processEngineName = processEngineName;
    }

    public void initCamundaProcessEngine() throws Exception {

        ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();

        processEngine = engineConfiguration
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setDataSource(dataSource)
                .setDatabaseType(databaseType)
                .setJobExecutorActivate(jobExecutorActivate)
                .setProcessEngineName(processEngineName)
                .setHistory(history)
                .buildProcessEngine();

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isJobExecutorActivate() {
        return jobExecutorActivate;
    }

    public void setJobExecutorActivate(boolean jobExecutorActivate) {
        this.jobExecutorActivate = jobExecutorActivate;
    }

	public String getHistory() {
		return history;
	}
	
	public void setHistory(String history) {
		this.history = history;
	}

	
	public String getDatabaseType() {
	
		return databaseType;
	}

	
	public void setDatabaseType(String databaseType) {
	
		this.databaseType = databaseType;
	}

}
