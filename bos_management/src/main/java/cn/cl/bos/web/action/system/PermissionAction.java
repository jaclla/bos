package cn.cl.bos.web.action.system;


import cn.cl.bos.domain.system.Menu;
import cn.cl.bos.domain.system.Permission;
import cn.cl.bos.service.system.MenuService;
import cn.cl.bos.service.system.PermissionService;
import cn.cl.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

@ParentPackage("json-default")
@Actions
@Namespace("/")
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission> {
    @Autowired
    private PermissionService permissionService;

    @Action(value = "permission_findAll", results = @Result(name = SUCCESS, type = "json"))
    public String findAll() {
        List<Permission> Menus = permissionService.findAll();
        ActionContext.getContext().getValueStack().push(Menus);
        return SUCCESS;

    }

    @Action(value = "permission_add", results = @Result(name = SUCCESS, type = "redirect",location = "pages/system/permission.html"))
    public String add() {
        permissionService.save(model);
        return SUCCESS;

    }
}
