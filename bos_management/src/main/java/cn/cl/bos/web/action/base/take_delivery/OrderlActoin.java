package cn.cl.bos.web.action.base.take_delivery;

import cn.cl.bos.domain.take_delivery.Order;
import cn.cl.bos.domain.take_delivery.WayBill;
import cn.cl.bos.service.take_delivery.base.OrderService;
import cn.cl.bos.service.take_delivery.base.WayBillService;
import cn.cl.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;


@ParentPackage("json-default")
@Namespace("/")
@Actions
@Scope("prototype")
@Controller
public class OrderlActoin extends BaseAction<Order> {
    @Autowired
    private OrderService orderService;

    @Action(value = "order_findByOrderNum", results = @Result(name = SUCCESS, type = "json"))
    public String findByOrderNum() {
        Order order=orderService.findByOrderNum(model.getOrderNum());
        Map<String, Object> result = new HashMap<>();
        if (order==null){
            result.put("success", false);
        }else {
            result.put("success", true);
            result.put("orderData", order);
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}
