package cn.cl.bos.service.take_delivery.base;

import cn.cl.bos.domain.take_delivery.Order;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Service
public interface OrderService {


    @Path("/order")
    @POST
    @Consumes({"application/xml", "application/json"})
    public void saveOrder(Order order);

    Order findByOrderNum(String orderNum);
}
