package de.hofuniversity.iisys.liferay.workflows;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.workflow.kaleo.model.KaleoNode;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.graph.GraphWalker;
import com.liferay.portal.workflow.kaleo.runtime.graph.PathElement;

public class CamundaGraphWalker implements GraphWalker
{

	@Override
	public void follow(KaleoNode sourceKaleoNode, KaleoNode targetKaleoNode, List<PathElement> remainingPathElements,
			ExecutionContext executionContext) throws PortalException {
		// TODO Auto-generated method stub
		
		throw new NotImplementedException();
		
	}

}
