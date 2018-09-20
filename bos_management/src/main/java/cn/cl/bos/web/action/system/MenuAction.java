package cn.cl.bos.web.action.system;


import cn.cl.bos.domain.system.Menu;
import cn.cl.bos.domain.system.User;
import cn.cl.bos.service.system.MenuService;
import cn.cl.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
public class MenuAction extends BaseAction<Menu> {
    @Autowired
    private MenuService menuService;

    @Action(value = "menu_findAll", results = @Result(name = SUCCESS, type = "json"))
    public String findAll() {
        List<Menu> Menus = menuService.findAll();
        ActionContext.getContext().getValueStack().push(Menus);
        return SUCCESS;

    }

    @Action(value = "menu_add", results = @Result(name = SUCCESS, type = "redirect", location = "pages/system/menu.html"))
    public String add() {
        menuService.save(model);
        return SUCCESS;

    }

    @Action(value = "menu_showMenu", results = @Result(name = SUCCESS, type = "json"))
    public String showMenu() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        List<Menu> menus = menuService.findByUser(user);

        ActionContext.getContext().getValueStack().push(menus);

        return SUCCESS;
    }
}
