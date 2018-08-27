package cn.cl.bos.web.action.base;

import cn.cl.bos.domain.base.Standard;
import cn.cl.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ParentPackage("json-default")
@Actions
@Namespace("/")
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

    @Autowired
    private StandardService standardService;
    //模型驱动
    private Standard standard = new Standard();
    //    属性驱动
    private int page;//页码
    private int rows; //行数

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public Standard getModel() {
        return standard;
    }

    //数据保存
    @Action(value = "standard_save", results = @Result(name = SUCCESS, type = "redirect", location = "/pages/base/standard.html"))
    public String save() {
        standardService.save(standard);
        return SUCCESS;
    }
    //数据删除
    @Action(value = "standard_delete", results = @Result(name = SUCCESS,type = "redirect", location = "/pages/base/standard.html"))
    public String delete() {
        int id = Integer.parseInt(ServletActionContext.getRequest().getParameter("id"));
        standardService.delete(id);
        return SUCCESS;
    }

    //分页列表查询
    @Action(value = "standard_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String pageQuery() {
        //调用业务层查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Standard> pageData = standardService.findPageData(pageable);
        //返回数据到客户端
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());
        //将map转换成json 使用struts2-json-plugin插件
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
    //快递员取派标准下拉框请求
    @Action(value = "standard_findAll", results = @Result(name = SUCCESS, type = "json"))
    public String findAll() {
        //调用业务层查询数据结果
        List<Standard> result = standardService.findAll();
        //返回数据到客户端
        //将map转换成json 使用struts2-json-plugin插件
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}
