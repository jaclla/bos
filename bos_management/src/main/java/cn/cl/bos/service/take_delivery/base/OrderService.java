package cn.cl.bos.service.take_delivery.base;

import cn.cl.bos.domain.take_delivery.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public interface OrderService {


    @Path("/order")
    @POST
    @Consumes({"application/xml", "application/json"})
    public void saveOrder(Order order);

}
