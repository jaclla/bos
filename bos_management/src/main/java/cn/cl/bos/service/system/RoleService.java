package cn.cl.bos.service.system;

import cn.cl.bos.domain.system.Role;
import cn.cl.bos.domain.system.User;

import java.util.List;

public interface RoleService {
    List<Role> findByUser(User user);

    List<Role> findAll();

    void add(Role model, String[] permissionIds, String menuIds);
}
