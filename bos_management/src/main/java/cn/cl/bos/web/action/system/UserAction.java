package cn.cl.bos.web.action.system;

import cn.cl.bos.domain.system.User;
import cn.cl.bos.service.system.UserService;
import cn.cl.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
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
public class UserAction extends BaseAction<User> {
    @Autowired
    private UserService userService;

    @Action(value = "user_login", results = {
            @Result(name = SUCCESS, type = "redirect", location = "index.html"),
            @Result(name = LOGIN, type = "redirect", location = "login.html")})
    public String login() {
        //基于shiro登陆
        Subject subject = SecurityUtils.getSubject();
        //用户名和密码
        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());


        try {
            subject.login(token);
            //登陆成功将用户信息保存到session
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            //登陆失败
            return LOGIN;
        }
    }

    @Action(value = "user_logout", results =
    @Result(name = SUCCESS, type = "redirect", location = "index.html"))
    public String logout() {
        //基于shiro退出
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return SUCCESS;
    }

    @Action(value = "user_findAll", results =
    @Result(name = SUCCESS, type = "json"))
    public String findAll() {
        List<User> users = userService.findAll();

        ActionContext.getContext().getValueStack().push(users);
        return SUCCESS;
    }


    private String[] roleIds;

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    @Action(value = "user_add", results = @Result(name = SUCCESS, type = "redirect", location = "pages/system/userlist.html"))
    public String add() {
        userService.saveUser(model, roleIds);
        return SUCCESS;
    }
}
