package cn.cl.bos.service.system.Impl;

import cn.cl.bos.dao.system.MenuRepository;
import cn.cl.bos.dao.system.PermissionRepository;
import cn.cl.bos.dao.system.RoleRepository;
import cn.cl.bos.domain.system.Menu;
import cn.cl.bos.domain.system.Permission;
import cn.cl.bos.domain.system.Role;
import cn.cl.bos.domain.system.User;
import cn.cl.bos.service.system.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Role> findByUser(User user) {
        if (user.getUsername().equals("admin")) {
            return roleRepository.findAll();
        } else {
            return roleRepository.findByUser(user.getId());
        }
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void add(Role model, String[] permissionIds, String menuIds) {
        //保存角色
        roleRepository.save(model);
        if (permissionIds != null) {
            for (String permissionId : permissionIds) {
                Permission permission = permissionRepository.findOne(Integer.parseInt(permissionId));
                model.getPermissions().add(permission);

            }
        }
        if (StringUtils.isNoneBlank(menuIds)) {
            String[] menuArray = menuIds.split(",");
            for (String menuId : menuArray) {
                Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
                model.getMenus().add(menu);
            }
        }
    }

}
