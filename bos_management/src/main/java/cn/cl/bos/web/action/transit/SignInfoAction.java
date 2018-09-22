package cn.cl.bos.web.action.transit;

import cn.cl.bos.domain.transit.DeliveryInfo;
import cn.cl.bos.domain.transit.SignInfo;
import cn.cl.bos.service.transit.DeliveryService;
import cn.cl.bos.service.transit.SignInfoService;
import cn.cl.bos.web.action.base.common.BaseAction;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@ParentPackage("json-default")
@Actions
@Namespace("/")
@Controller
@Scope("prototype")
public class SignInfoAction extends BaseAction<SignInfo> {
    @Autowired
    private SignInfoService signInfoService;

    private String transitInfoId;

    public void setTransitInfoId(String transitInfoId) {
        this.transitInfoId = transitInfoId;
    }

    @Action(value = "sign_save", results = @Result(name = SUCCESS, type = "redirect", location = "pages/transit/transitinfo.html"))
    public String save() {
        signInfoService.save(transitInfoId,model);
        return SUCCESS;
    }
}
