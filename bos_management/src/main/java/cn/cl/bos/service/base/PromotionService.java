package cn.cl.bos.service.base;

import cn.cl.bos.domain.base.PageBean;
import cn.cl.bos.domain.base.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public interface PromotionService {
    //保存宣传任务
    void save(Promotion model);

    //分页查询
    Page<Promotion> findPageDate(Pageable pageable);

    //    根据page和rows返回分页数据
    @Path("/pageQuery")
    @GET
    @Produces({"application/xml", "application/json"})
    PageBean<Promotion> findPageData(@QueryParam("page") int page, @QueryParam("rows") int rows);
}
