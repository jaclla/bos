package cn.cl.bos.web.action;

import cn.cl.bos.domain.command.PageBean;
import cn.cl.bos.domain.base.Promotion;
import cn.cl.bos.domain.constant.Constants;
import cn.cl.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

@ParentPackage("json-default")
@Actions
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressAjWarnings("all")
public class PromotionAction extends BaseAction<Promotion> {

    public static boolean CREATE_HTML = false;

    @Action(value = "promotion_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String pageQuery() {

        PageBean<Promotion> pageBean = WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/promotionService/pageQuery?page=" + page + "&rows=" + rows)
                .accept(MediaType.APPLICATION_JSON).get(PageBean.class);
        ActionContext.getContext().getValueStack().push(pageBean);
        return SUCCESS;

    }

    @Action(value = "promotion_showDetail")
    public String showDetail() throws IOException, TemplateException {
//        先判断对应id的页面是否存在，如果存在就返回
        String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freemarker");

        File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
        if (!htmlFile.exists()) {
            //不存在，创建一个
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
            configuration.setDirectoryForTemplateLoading(new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarket_templates")));

            //获得模板对象
            Template template = configuration.getTemplate("promotion_detail.ftl");
            //获得动态数据
            Promotion promotion = WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/promotionService/promotion/" + model.getId())
                    .accept(MediaType.APPLICATION_JSON).get(Promotion.class);
            HashMap<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("promotion", promotion);
//            合并输出
            ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
            template.process(parameterMap, new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));

        }
        CREATE_HTML = false;

//            存在，返回
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
        return NONE;

    }
}
