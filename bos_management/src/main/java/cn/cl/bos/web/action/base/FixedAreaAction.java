package cn.cl.bos.web.action.base;

import cn.cl.bos.domain.base.FixedArea;
import cn.cl.bos.service.base.FixedAreaService;
import cn.cl.bos.web.action.base.common.BaseAction;
import cn.cl.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Actions
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {
    //注入service
    @Autowired
    private FixedAreaService fixedAreaService;

    //保存修改
    @Action(value = "fixedArea_save", results = @Result(name = SUCCESS, type = "redirect", location = "pages/base/fixed_area.html"))
    public String fixedArea_save() {
        fixedAreaService.save(model);
        return SUCCESS;
    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    //数据删除
    @Action(value = "fixedArea_delBatch", results = @Result(name = SUCCESS, type = "redirect", location = "pages/base/fixed_area.html"))
    public String fixedArea_delBatch() {
        //获取id数组然后按，分割
        String[] idArray = ids.split(",");
        fixedAreaService.delete(idArray);
        return SUCCESS;
    }

    //分页查询和显示
    @Action(value = "fixedArea_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String fixedArea_pageQuery() {
        //获取分页数据
        Pageable pageable = new PageRequest(this.page - 1, rows);
        //根据查询条件 构建条件查询对象
        Specification<FixedArea> specification = new Specification<FixedArea>() {
            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //单表查询
                //快递员工号  精准查询
                List<Predicate> list = new ArrayList<>();
                //添加查询条件

//                根据id 等值查询
                if (StringUtils.isNotBlank(model.getId())) {
                    Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
                    list.add(p1);
                }
//                根据公司 模糊查询
                if (StringUtils.isNotBlank(model.getCompany())) {
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
                    list.add(p2);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };

        //调用业务层
        Page<FixedArea> pageData = fixedAreaService.findPageData(specification, pageable);
        //返回数据到客户端
//        Map<String, Object> result = new HashMap<>();
//        result.put("total", pageData.getTotalElements());
//        result.put("rows", pageData.getContent());
//        //将map转换成json 使用struts2-json-plugin插件
//        ActionContext.getContext().getValueStack().push(result);
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }

    //    属性驱动
    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    //    绑定定区和客户
    @Action(value = "decidedzone_assigncustomerstodecidedzone", results = @Result(name = SUCCESS, type = "redirect", location = "/pages/base/fixed_area.html"))
    public String decidedzone_assigncustomerstodecidedzone() {
        String customerIdStr = StringUtils.join(customerIds, ",");
        WebClient.create("http://localhost:8088/crm_management/services/customerService/associationcustomerstofixedarea?customerIdStr=" + customerIdStr + "&fixedAreaId=" + model.getId())
                .put(null);
        return SUCCESS;
    }


    //属性驱动
    private Integer courierId;
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    //    绑定定区和快递员和分派时间
    @Action(value = "fixedArea_associationCourierToFixedArea", results = @Result(name = SUCCESS, type = "redirect", location = "/pages/base/fixed_area.html"))
    public String fixedArea_associationCourierToFixedArea() {
        fixedAreaService.fixedArea_associationCourierToFixedArea(model,courierId,takeTimeId);

        return SUCCESS;
    }

    //查询没有分配定区的客户信息
    @Action(value = "fixedArea_findNoAssociationCustomers", results = @Result(name = SUCCESS, type = "json"))
    public String fixedArea_findNoAssociationCustomers() {
        //使用webClient调用webService接口
        Collection<? extends Customer> collection = WebClient
                .create("http://localhost:8088/crm_management/services/customerService/noassociationcustomers")
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);

        return SUCCESS;
    }

    //查询已经分配定区的客户信息
    @Action(value = "fixedArea_findHasAssociationFixedAreaCustomers", results = @Result(name = SUCCESS, type = "json"))
    public String fixedArea_findHasAssociationFixedAreaCustomers() {

        //使用webClient调用webService接口
        Collection<? extends Customer> collection = WebClient
                .create("http://localhost:8088/crm_management/services/customerService/associationfixedareacustomers/" + model.getId())
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);

        return SUCCESS;
    }


}
