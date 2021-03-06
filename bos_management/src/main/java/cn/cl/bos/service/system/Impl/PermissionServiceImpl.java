package cn.cl.bos.service.system.Impl;

import cn.cl.bos.dao.system.PermissionRepository;
import cn.cl.bos.domain.system.Permission;
import cn.cl.bos.domain.system.User;
import cn.cl.bos.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    @Override
    public List<Permission> findByUser(User user) {
        if (user.getUsername().equals("admin")) {
            return permissionRepository.findAll();
        } else {
            return permissionRepository.findByUser(user.getId());
        }
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public void save(Permission model) {
        permissionRepository.save(model);
    }
}
