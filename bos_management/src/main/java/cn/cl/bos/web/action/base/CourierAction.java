package cn.cl.bos.web.action.base;

import cn.cl.bos.domain.base.Courier;
import cn.cl.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("json-default")
@Actions
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
    //模型驱动
    private Courier courier = new Courier();
    //属性模型
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Autowired
    private CourierService courierService;

    @Override
    public Courier getModel() {
        return courier;
    }

    //数据添加
    @Action(value = "courier_save", results = @Result(name = SUCCESS, type = "redirect", location = "pages/base/courier.html"))
    public String save() {
        courierService.save(courier);
        return SUCCESS;
    }

    //分页查询
    @Action(value = "courier_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String pageQuery() {
        //调用业务层查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);
        //根据查询条件 构建条件查询对象
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //单表查询
                //快递员工号  精准查询
                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotBlank(courier.getCourierNum())) {
                    // courierNum =?
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
                    list.add(p1);
                }
                //快递公司 模糊查询
                if (StringUtils.isNotBlank(courier.getCompany())) {
                    // company like %?%
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
                    list.add(p2);
                }
                //快递员类型查询 精准查询
                if (StringUtils.isNotBlank(courier.getType())) {
                    //type = ?
                    Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
                    list.add(p3);
                }
//                多表查询
                //使用自己类关键目标类
//                查询快递员的收派标准 模糊查询
                Join<Object, Object> standard = root.join("standard", JoinType.INNER);
                if (courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {
                    //standardNum like $?$
                    Predicate p4 = cb.like(standard.get("name").as(String.class), "%" + courier.getStandard().getName() + "%");
                    list.add(p4);
                }

                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        Page<Courier> pageData = courierService.findPageData(specification, pageable);
        //返回数据到客户端
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());
        //将map转换成json 使用struts2-json-plugin插件
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    //作废快递员
    @Action(value = "courier_delBatch", results = @Result(name =SUCCESS,location ="pages/base/courier.html",type = "redirect"))
    public String delBatch() {
        //获取id数组然后按，分割
        String[] idArray = ids.split(",");
        courierService.delBath(idArray);
        return SUCCESS;

    }
//    恢复快递员
    @Action(value = "courier_restoreBatch", results = @Result(name =SUCCESS,location ="pages/base/courier.html",type = "redirect"))
    public String restoreBatch() {
        //获取id数组然后按，分割
        String[] idArray = ids.split(",");
        courierService.restoreBatch(idArray);
        return SUCCESS;

    }
}
