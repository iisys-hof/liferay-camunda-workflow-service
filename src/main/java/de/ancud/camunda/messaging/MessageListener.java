package de.ancud.camunda.messaging;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.proxy.ProxyMessageListener;

/**
 * Needed to set up cross context communication between Liferay and this context.
 *
 * @author bnmaxim.
 */
public class MessageListener  extends ProxyMessageListener{

    @Override
    public void receive(Message message) {
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        super.receive(message);
    }
}
