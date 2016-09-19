# liferay-camunda-workflow-service
Modified Kaleo package including Ancud and iisys implementations for Liferay to handle Camunda workflows - highly experimental and incomplete.

Contains modified version of the sources for com.liferay.portal.workflow.kaleo.runtime.impl-2.0.6.jar for Liferay 7 GA2

The original file can be found in osgi/marketplace/Liferay CE Forms and Workflow.lpkg

Configuration: /src/main/resources/camunda-workflows.properties

Building and Installing:

* Import into Liferay IDE as Liferay Module or possibly Gradle Project
* Execute build from the eclipse view "gradle tasks"
* this will generate a deployable jar file in build/libs/
* deploying this jar will permanently overwrite the original workflow service
* a restart might be necessary in some cases, but usually the OSGi module is replaced directly

Modify "my Tasks" JSP to redirect to Camunda:

    acquire a copy of com.liferay.portal.workflow.task.web-1.0.5.jar from Liferay CE Forms and Workflow.lpkg from your liferay installation
    modify /META-INF/resources/view.jsp to contain:
        <script type="text/javascript">
            window.location = "$CAMUNDA_TASKLIST_URL";
        </script>
    deploy the jar file
    restart Liferay