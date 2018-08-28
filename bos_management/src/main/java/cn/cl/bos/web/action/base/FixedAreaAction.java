package cn.cl.bos.web.action.base;

import cn.cl.bos.domain.base.FixedArea;
import cn.cl.bos.service.base.FixedAreaService;
import cn.cl.bos.web.action.base.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
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
    @Action(value = "fixedArea_save",results = @Result(name = SUCCESS,type = "redirect",location ="pages/base/fixed_area.html"))
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

    @Action(value = "fixedArea_pageQuery",results = @Result(name = SUCCESS, type = "json"))
    public String fixedArea_pageQuery(){
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
}
