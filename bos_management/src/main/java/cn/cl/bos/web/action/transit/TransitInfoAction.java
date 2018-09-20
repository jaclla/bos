package cn.cl.bos.web.action.transit;

import cn.cl.bos.domain.take_delivery.WayBill;
import cn.cl.bos.domain.transit.TransitInfo;
import cn.cl.bos.service.take_delivery.base.WayBillService;
import cn.cl.bos.service.transit.TransitInfoService;
import cn.cl.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import javax.print.attribute.standard.PageRanges;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("json-default")
@Actions
@Namespace("/")
@Controller
@Scope("prototype")
public class TransitInfoAction extends BaseAction<TransitInfo> {

    @Autowired
    private TransitInfoService transitInfoService;

    @Autowired
    private WayBillService wayBillService;

    private String wayBillIds;

    public void setWayBillIds(String wayBillIds) {
        this.wayBillIds = wayBillIds;
    }

    @Action(value = "transit_create", results = @Result(name = SUCCESS, type = "json"))
    public String create() {
        Map<String, Object> result = new HashMap<>();

        try {
            transitInfoService.createTransits(wayBillIds);
            //成功
            result.put("success", true);
            result.put("msg", "开启中转配送成功！");
        } catch (Exception e) {
            //失败
            result.put("success", false);
            result.put("msg", "开启中转配送失败！异常：" + e.getMessage());
            e.printStackTrace();
        }
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }

    @Action(value = "transitInfo_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String pageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<TransitInfo> PageData = transitInfoService.findPageData(pageable);
        pushPageDataToValueStack(PageData);

        return SUCCESS;
    }
}
