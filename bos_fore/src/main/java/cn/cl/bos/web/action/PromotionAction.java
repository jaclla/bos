package cn.cl.bos.web.action;

import cn.cl.bos.domain.base.PageBean;
import cn.cl.bos.domain.base.Promotion;
import cn.cl.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;

@ParentPackage("json-default")
@Actions
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {

    @Action(value = "promotion_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String pageQuery() {

        PageBean<Promotion> pageBean = WebClient.create("http://localhost:8085/bos_management/services/promotionService/pageQuery?page=" + page + "&rows=" + rows)
                .accept(MediaType.APPLICATION_JSON).get(PageBean.class);
        ActionContext.getContext().getValueStack().push(pageBean);
        return SUCCESS;

    }
}
