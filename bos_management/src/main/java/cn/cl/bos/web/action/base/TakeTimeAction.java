package cn.cl.bos.web.action.base;

import cn.cl.bos.dao.base.TakeTimeRepository;
import cn.cl.bos.domain.base.Courier;
import cn.cl.bos.domain.base.FixedArea;
import cn.cl.bos.domain.base.TakeTime;
import cn.cl.bos.service.base.CourierService;
import cn.cl.bos.service.base.TakeTimeService;
import cn.cl.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
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
public class TakeTimeAction extends BaseAction<TakeTime> {

    @Autowired
    private TakeTimeRepository takeTimeService;

    //    显示收派时间列表
    @Action(value = "taketime_findAll", results = @Result(name = SUCCESS, type = "json"))
    public String courier_findnoassociation() {
        //调用业务层，查询收派时间
        List<TakeTime> takeTime = takeTimeService.findAll();
        ActionContext.getContext().getValueStack().push(takeTime);
        return SUCCESS;

    }
    //    收派时间分页查询
    @Action(value = "taketime_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String taketime_pageQuery() {
        //获取分页数据
        Pageable pageable = new PageRequest(this.page - 1, rows);
        //调用业务层
        Page<TakeTime> pageData = takeTimeService.findAll(pageable);
        pushPageDataToValueStack(pageData);
        return SUCCESS;

    }

}
