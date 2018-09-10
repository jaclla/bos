/**
 * Copyright (C) 2015 O'er, Inc. All Rights Reserved.
 */

package cn.cl.bos.aop;

import cn.cl.bos.domain.base.Promotion;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @author xuming
 * @email 919094526@qq.com
 * 2017-12-11
 */
@Component
@Aspect
public class OperatorInterceptor implements Ordered {

    private final Logger Log = LoggerFactory.getLogger(OperatorInterceptor.class);

    @Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsQueueTemplate;
    
    @AfterReturning("execution(* cn.cl.bos.service.take_delivery.Impl.PromotionServiceImpl.save(..))")
    public void afterUpdatePromotion(JoinPoint joinPoint) {
        try {
            if (Log.isInfoEnabled()) {
                Log.info("afterUpdatePromotion");
            }
            Object obj = joinPoint.getArgs()[0];
            if (obj instanceof Promotion) {
            	final Integer promotionId = ((Promotion)obj).getId();
            	jmsQueueTemplate.send("update_promotion_detail", new MessageCreator() {
        			
        			@Override
        			public Message createMessage(Session session) throws JMSException {
        				BytesMessage bytesMessage =session.createBytesMessage();
        				bytesMessage.setIntProperty("promotionId", promotionId);
        				return bytesMessage;
        			}
        		});
            }
        } catch (Exception e) {
            Log.error("afterUpdatePromotion", e);
        }
    }
    
//    @Autowired
//    private WayBillService wayBillService;
//
//    @AfterReturning("execution(* cn.cl.bos.service.take_delivery.Impl.WayBillServiceImpl.save(..))")
//    public void afterUpdateWayBill(JoinPoint joinPoint) {
//        try {
//            if (Log.isInfoEnabled()) {
//                Log.info("afterUpdateWayBill");
//            }
//            Object obj = joinPoint.getArgs()[0];
//            if (obj instanceof WayBill) {
//            	wayBillService.updateWayBill(((WayBill)obj));
//            }
//        } catch (Exception e) {
//            Log.error("afterUpdateWayBill", e);
//        }
//    }
    

    @Override
    public int getOrder() {
        return 2;
    }

}
