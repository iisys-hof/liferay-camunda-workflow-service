package de.ancud.camunda.hook.listener;

import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Company;

/**
 * @author bnmaxim
 */
public class CompanyModelListener extends BaseModelListener<Company> {

    @Override
    public void onAfterRemove(Company company) throws ModelListenerException {
        /* TODO:
        try {
			PortalKaleoManager portalKaleoManager =
				PortalKaleoManagerUtil.getPortalKaleoManager();

			portalKaleoManager.deleteKaleoData(company);
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
        */
    }
}
