package cn.cl.bos.service.take_delivery.base;

import cn.cl.bos.domain.command.PageBean;
import cn.cl.bos.domain.base.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.util.Date;

@Service
public interface PromotionService {
    //保存宣传任务
    void save(Promotion model);

    //分页查询
    Page<Promotion> findPageDate(Pageable pageable);

    //    根据page和rows返回分页数据
    @Path("/pageQuery")
    @GET
    @Produces({"application/xml", "application/json"})
    PageBean<Promotion> findPageData(@QueryParam("page") int page, @QueryParam("rows") int rows);//    根据page和rows返回分页数据

    @Path("/promotion/{id}")
    @GET
    @Produces({"application/xml", "application/json"})
    Promotion findPageData(@PathParam("id") Integer id);

    void updateStatus(Date date);
}
