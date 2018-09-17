package cn.cl.bos.service.system.Impl;

import cn.cl.bos.dao.system.RoleRepository;
import cn.cl.bos.domain.system.Role;
import cn.cl.bos.domain.system.User;
import cn.cl.bos.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findByUser(User user) {
        if (user.getUsername().equals("admin")) {
            return roleRepository.findAll();
        } else {
            return roleRepository.findByUser(user.getId());
        }
    }
}
