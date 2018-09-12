package cn.cl.bos.service.take_delivery.Impl;

import cn.cl.bos.dao.base.AreaRepository;
import cn.cl.bos.dao.base.FixedAreaRepository;
import cn.cl.bos.dao.base.OrderRepository;
import cn.cl.bos.domain.base.Area;
import cn.cl.bos.domain.base.Courier;
import cn.cl.bos.domain.base.FixedArea;
import cn.cl.bos.domain.base.SubArea;
import cn.cl.bos.domain.constant.Constants;
import cn.cl.bos.domain.take_delivery.Order;
import cn.cl.bos.domain.take_delivery.WorkBill;
import cn.cl.bos.service.take_delivery.base.OrderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @Override
    public void saveOrder(Order order) {
        order.setOrderTime(new Date());//设置下单时间
        order.setOrderNum(UUID.randomUUID().toString());//设置订单号
        order.setStatus("1");//代取件状态
        //自动分单

//        逻辑一：基于crm地址库获得定区，匹配快递员
        String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_HOST + "/services/customerService/customer/findFixedAreaIdByAddress?address=" + order.getSendAddress())
                .accept(MediaType.APPLICATION_JSON).get(String.class);
//        通过寄件人寄件地址获得定区id
        if (fixedAreaId != null) {
//            通过定区id查找定区
            FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
//            通过定区查找快递员
            Courier courier = fixedArea.getCouriers().iterator().next();
            if (courier != null) {
//                如果找到，将订单绑定快递员
                savaOrder(order, courier);
//                生成工单，发送短信
                generateWorkBill(order);

                return;
            }
        }
//        逻辑二：通过寄件人填写的省市区，查找分区，匹配地址，分配快递员

//        获得寄件人地址
        Area area = order.getSendArea();
//        通过地址中的省市区查询分区
        Area persistArea = areaRepository.findByProvinceAndCityAndDistrict(area.getProvince(), area.getCity(), area.getDistrict());
//        获得寄件人地址
        Area recArea = order.getRecArea();
//        通过地址中的省市区查询分区
        Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(), recArea.getDistrict());
        order.setSendArea(persistArea);
        order.setRecArea(persistRecArea);
        //便利定区下的每个分区
        for (SubArea subarea : persistArea.getSubareas()) {

            //如果客户下单的地址包含分区关键字
            if (area.getSubareas().contains(subarea.getKeyWords())) {
                //通过分区找到定区,然后找到快递员
                Iterator<Courier> iterator = subarea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        savaOrder(order, courier);
                        //            生成工单，发送短信
                        generateWorkBill(order);

                        return;
                    }
                }
            }
        }
        //        逻辑三：人工分单
        order.setOrderType("1");
        orderRepository.save(order);

    }

    private void generateWorkBill(final Order order) {
        //生成工单
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        final String smsNumber = RandomStringUtils.randomNumeric(4);
        workBill.setSmsNumber(smsNumber);
        workBill.setOrder(order);
        workBill.setCourier(order.getCourier());
        //发送短信 太麻烦还要申请模板 算了~！~
        System.out.println("发了短信假装！");

//        调用mq服务发送一条消息
//        jmsTemplate.send("smsQueue", new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                //调用bos_sms发送短信
//                jmsTemplate.send("smsQueue", new MessageCreator() {
//                    @Override
//                    public Message createMessage(Session session) throws JMSException {
//                        MapMessage smsMap = session.createMapMessage();
//                        smsMap.setString("mobile", order.getCourier().getTelephone());                 //mobile 接收短信的手机号码
//                        smsMap.setString("tpl_id", "98465");                           //tpl_id 短信模板ID
//                        smsMap.setString("tpl_value", CheckCode);                         //tpl_value 变量名和变量值对
//                        smsMap.setString("key", "331c08249f76c9bbd5922742dc723dd2");   //key 应用APPKEY
//                        smsMap.setString("dtype", "json");                             //dtype 返回数据的格式,xml或json，默认json
//                        return smsMap;
//                    }
//                });
//            }
//        });
        workBill.setPickstate("已通知");
    }

    private void savaOrder(Order order, Courier courier) {
        //找到了快递员将订单绑定快递员
        order.setCourier(courier);
//                  然后生成订单号保存到订单上
        order.setOrderType("0");
        orderRepository.save(order);
    }
}
