package cn.cl.bos.web.action;


import cn.cl.bos.domain.constant.Constants;
import cn.cl.crm.domain.base.Customer;
import cn.cl.bos.utils.MailUtils;
import cn.cl.bos.web.action.common.BaseAction;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URLEncoder;
import java.lang.String;
import java.util.concurrent.TimeUnit;

@ParentPackage("json-default")
@Actions
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    //发送短信
    @Action(value = "customer_sendSms")
    public String sendSms() throws Exception {
        //生成验证码
        String randomCode = RandomStringUtils.randomNumeric(4);
        //将验证码保存到session
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
        String code = "#code#=" + randomCode;
        final String CheckCode = URLEncoder.encode(code, "utf-8");
        //mobile 接收短信的手机号码
        //tpl_id 短信模板ID
        //tpl_value 变量名和变量值对
        //key 应用APPKEY
        //dtype 返回数据的格式,xml或json，默认json
//        java.lang.String result = SmsUtils.getRequest2(telephone, "98465", CheckCode, "331c08249f76c9bbd5922742dc723dd2", "json");
        System.out.println("验证码为：" + randomCode);
//        System.out.println(result);

        //调用bos_sms发送短信
        jmsTemplate.send("smsQueue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage smsMap = session.createMapMessage();
                smsMap.setString("mobile", model.getTelephone());                 //mobile 接收短信的手机号码
                smsMap.setString("tpl_id", "98465");                           //tpl_id 短信模板ID
                smsMap.setString("tpl_value", CheckCode);                         //tpl_value 变量名和变量值对
                smsMap.setString("key", "331c08249f76c9bbd5922742dc723dd2");   //key 应用APPKEY
                smsMap.setString("dtype", "json");                             //dtype 返回数据的格式,xml或json，默认json
                return smsMap;
            }
        });


//        if (result.contains("fee")) {
//            //成功
//            return NONE;
//        } else {
////            失败
//            throw new RuntimeException("短信发送失败：信息码：" + result);
//        }
        return NONE;
    }

    //属性驱动
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    //客户注册
    @Action(value = "customer_regist", results = {
            @Result(name = SUCCESS, type = "redirect", location = "signup-success.html"),
            @Result(name = INPUT, type = "redirect", location = "signup.html")
    })
    public String regist() {
        //校验验证码时候正确
        String CheckCodeSession = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());

        System.out.println("输入的验证码：" + checkcode);

        if (CheckCodeSession == null || !CheckCodeSession.equals(checkcode)) {
            //验证码不正确或不存在
            return INPUT;
        }
        //验证码正确
        //调用WebService连接CRM创建用户
        WebClient.create("http://localhost:8088/crm_management/services/customerService/customer").type(MediaType.APPLICATION_JSON)
                .post(model);

        //注册成功后发送激活邮件
//        生成激活码
        String activeCode = RandomStringUtils.randomNumeric(32);
        //将激活码保存到redis 然后设置时间为24小时
        redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);
        //发送包含激活码路径的邮件
        String Content = "尊敬的用户您好，请于24小时之内进行邮件绑定,点击下面地址进行绑定：<br><a href='" + MailUtils.activeUrl + "?telephone=" + model.getTelephone() + "&activeCode=" + activeCode + "'>速运快递邮箱绑定地址</a>";
        MailUtils.sendMail("速运快递激活邮件", Content, model.getEmail());
        return SUCCESS;

    }

    //属性驱动
    private String activeCode;

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    //    校验邮箱是否存在，不存在就绑定，存在就报错
    //绑定邮箱
    @Action("customer_activeMail")
    public String activeMail() throws IOException {
        //设置写入数据库的字符编码集
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        //判断激活码是否有效
        String RedisActiveCode = redisTemplate.opsForValue().get(model.getTelephone());
        if (RedisActiveCode == null || !RedisActiveCode.equals(activeCode)) {
            //激活码错误
            ServletActionContext.getResponse().getWriter().println("激活码无效，请重新登陆！");
        } else {
            //激活码正确
            //确认是否已存在绑定邮箱
            Customer customer = WebClient.create("http://localhost:8088/crm_management/services/customerService/customer/telephone/" + model.getTelephone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
            if (customer.getType() == null || customer.getType() != 1) {
                //可以绑定
                WebClient.create("http://localhost:8088/crm_management/services/customerService/customer/updatetype/" + model.getTelephone()).put(null);
                ServletActionContext.getResponse().getWriter().println("邮箱绑定成功！");
            } else {
                //重复绑定
                ServletActionContext.getResponse().getWriter().println("邮箱已经绑定过了，请重新填写邮箱！");

            }


            //绑定成功删除激活码
            redisTemplate.delete(model.getTelephone());
        }
        return NONE;
    }


    //客户登陆
    @Action(value = "customer_login", results = {@Result(name = LOGIN, type = "redirect", location = "login.html"), @Result(name = SUCCESS, type = "redirect", location = "index.html#/myhome")})
    public String login() {

        //调用WebService登陆
        Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerService/customer/login?telephone=" + model.getTelephone() + "&password=" + model.getPassword()).type(MediaType.APPLICATION_JSON)
                .get(Customer.class);
        if (customer==null){
//            登录失败
            return LOGIN;
        }else {
//            登陆成功
            ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
            return SUCCESS;
        }
    }
}
