package cn.cl.bos.mq;

import cn.cl.bos.utils.SmsUtils;
import org.springframework.stereotype.Service;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service("smsConsumer")
public class SmsConsumer implements MessageListener {
    @Override
    public void onMessage(Message message) {
        MapMessage map = (MapMessage) message;

        //调用sms服务发送短信
        try {
            String result = SmsUtils.getRequest2(map.getString("mobile"), map.getString("tpl_id"), map.getString("tpl_value"), map.getString("key"), map.getString("dtype"));
            if (result.contains("fee")) {
            //成功
        } else {
//            失败
            throw new RuntimeException("短信发送失败：信息码：" + result);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
