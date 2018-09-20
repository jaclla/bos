package cn.cl.bos.web.action.transit;

import cn.cl.bos.domain.transit.InOutStorageInfo;
import cn.cl.bos.domain.transit.TransitInfo;
import cn.cl.bos.service.take_delivery.base.WayBillService;
import cn.cl.bos.service.transit.InOutStorageInfoService;
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

import java.util.HashMap;
import java.util.Map;

@ParentPackage("json-default")
@Actions
@Namespace("/")
@Controller
@Scope("prototype")
public class InOutStorageInfoAction extends BaseAction<InOutStorageInfo> {

    @Autowired
    private InOutStorageInfoService inOutStorageInfoService;


    private String transitInfoId;

    public void setTransitInfoId(String transitInfoId) {
        this.transitInfoId = transitInfoId;
    }

    @Action(value = "inoutstore_save", results = @Result(name = SUCCESS, type = "redirect",location = "pages/transit/transitinfo.html"))
    public String save() {
        inOutStorageInfoService.save(transitInfoId,model);

        return SUCCESS;
    }

}
