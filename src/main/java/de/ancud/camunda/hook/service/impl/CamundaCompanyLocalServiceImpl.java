package de.ancud.camunda.hook.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.CompanyLocalServiceWrapper;

import de.ancud.camunda.spring.CamundaEngineStaticBeanAccessor;
//import org.camunda.bpm.engine.ProcessEngine;

/**
 * @author bnmaxim
 */
public class CamundaCompanyLocalServiceImpl extends CompanyLocalServiceWrapper {

    public CamundaCompanyLocalServiceImpl(CompanyLocalService companyLocalService) {
        super(companyLocalService);
    }

    @Override
    public Company checkCompany(String webId, String mx) throws PortalException, SystemException {

        Company company = super.checkCompany(webId, mx);
//        ProcessEngine processEngine = CamundaEngineStaticBeanAccessor.getProcessEngine();
//        if (processEngine != null) {
//            System.out.println("Process Engine Name is " + processEngine.getName());
//        } else {
//            System.out.println("Process Engine is NULL");
//        }

        //TODO: deploy default configuration for LiferayCamunda, see KaleoCompanyLocalServiceImpl
        return company;
    }
}
