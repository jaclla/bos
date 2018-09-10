package cn.cl.bos.mq;



import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import cn.cl.bos.web.action.PromotionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("promotionConsumer")
public class PromotionConsumer implements MessageListener {

	private final Logger Log = LoggerFactory.getLogger(PromotionConsumer.class);
	
	@Override
	public void onMessage(Message message) {
		BytesMessage bytesMessage = (BytesMessage) message;
		if (bytesMessage != null) {
			PromotionAction.CREATE_HTML=false;
			Log.info("PromotionConsumer CREATE_HTML="+ PromotionAction.CREATE_HTML);
		}

	}
}
