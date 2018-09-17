package cn.cl.bos.web.action.system;


import cn.cl.bos.domain.system.Role;
import cn.cl.bos.service.system.RoleService;
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
public class RoleAction extends BaseAction<Role> {
    @Autowired
    private RoleService roleService;

    @Action(value = "role_findAll", results = @Result(name = SUCCESS, type = "json"))
    public String findAll() {
        List<Role> Menus = roleService.findAll();
        ActionContext.getContext().getValueStack().push(Menus);
        return SUCCESS;

    }

    private String[] permissionIds;
    private String menuIds;

    public void setPermissionIds(String[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    @Action(value = "role_add", results = @Result(name = SUCCESS, type = "redirect", location = "pages/system/role.html"))
    public String add() {
        roleService.add(model, permissionIds, menuIds);
        return SUCCESS;

    }
}
