package cn.cl.bos.web.action.base.take_delivery;

import cn.cl.bos.domain.base.Promotion;
import cn.cl.bos.domain.take_delivery.WayBill;
import cn.cl.bos.service.take_delivery.base.WayBillService;
import cn.cl.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.lang.*;


@ParentPackage("json-default")
@Namespace("/")
@Actions
@Scope("prototype")
@Controller
public class WayBillActoin extends BaseAction<WayBill> {
    @Autowired
    private WayBillService wayBillService;

    @Action(value = "wayBill_save", results = @Result(name = SUCCESS, type = "json"))
    public String save() {
        Map<String, Object> result = new HashMap<>();
        try {
                if (model.getOrder() != null && (model.getOrder().getId() == null || model.getOrder().getId() == 0)) {
                model.setOrder(null);
            }
            wayBillService.save(model);
            //保存成功
            result.put("success", true);
            result.put("msg", "保存运单成功");

        } catch (java.lang.Exception e) {
            e.printStackTrace();
            //保存失败
            result.put("success", false);
            result.put("msg", "保存运单失败");
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    @Action(value = "wayBill_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String pageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows);

        Page<WayBill> pageData = wayBillService.findPageDate(pageable);

        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }

    @Action(value = "wayBill_findByWayBillNum", results = @Result(name = SUCCESS, type = "json"))
    public String findByWayBillNum() {
        WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
        Map<String, Object> result = new HashMap<>();
        if (wayBill == null) {
            //运单不存在
            result.put("success", false);
        } else {
            result.put("success", true);
            result.put("wayBillData", wayBill);
        }
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }
}
