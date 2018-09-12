package cn.cl.crm.service;

import cn.cl.crm.domain.base.Customer;

import javax.ws.rs.*;
import java.util.List;

public interface CustomerService {

    //查询所有未关联客户列表
    @Path("/noassociationcustomers")
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Customer> findNoAssociationCustomers();

    //查询所有已关联客户列表
    @Path("/associationfixedareacustomers/{fixedareaid}")
    @GET
    @Produces({"application/xml", "application/json"})

    public List<Customer> findHasAssociationFixedAreaCustomers(
            @PathParam("fixedareaid") String fixedAreaId);

    //将客户关联到定区上，将所有客户id拼接成1，2，3
    @Path("/associationcustomerstofixedarea")
    @PUT
    public void associationCustomersToFixedArea(
            @QueryParam("customerIdStr") String customerIdStr,
            @QueryParam("fixedAreaId") String fixedAreaId);

    @Path("/customer")
    @POST
    @Consumes({"application/xml", "application/json"})
    public void regist(Customer customer);

    @Path("/customer/telephone/{telephone}")
    @GET
    @Produces({"application/xml", "application/json"})
    public Customer findByTelephone(@PathParam("telephone") String telephone);

    @Path("/customer/updatetype/{telephone}")
    @PUT
    public void updateType(@PathParam("telephone") String telephone);

    @Path("/customer/login")
    @GET
    @Consumes({"application/xml", "application/json"})
    public Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);

    @Path("/customer/findFixedAreaIdByAddress")
    @GET
    @Consumes({"application/xml", "application/json"})
    public String findFixedAreaIdByAddress(@QueryParam("address") String address);
}
