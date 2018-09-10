package cn.cl.bos.quartz;

import java.util.Date;

import cn.cl.bos.service.take_delivery.base.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时设置宣传任务 状态
 *
 * @author itcast
 */
@Service("promotionBean")
public class PromotionBean {

//    private final Logger Log = LoggerFactory.getLogger(PromotionBean.class);

    @Autowired
    private PromotionService promotionService;

    public void updateStatus() {
        // 当前时间 大于 promotion数据表 endDate， 活动已经过期，设置status='0'
        promotionService.updateStatus(new Date());
//        Log.info("PromotionBean updateStatus活动过期处理程序执行....");
    }
}
