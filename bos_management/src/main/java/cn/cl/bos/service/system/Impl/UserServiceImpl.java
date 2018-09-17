package cn.cl.bos.service.system.Impl;

import cn.cl.bos.dao.system.RoleRepository;
import cn.cl.bos.dao.system.UserRepository;
import cn.cl.bos.domain.system.Role;
import cn.cl.bos.domain.system.User;
import cn.cl.bos.service.system.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User model, String[] roleIds) {
        userRepository.save(model);
        if (StringUtils.isNoneBlank(roleIds)) {
            for (String roleId : roleIds) {
                Role role = roleRepository.findOne(Integer.parseInt(roleId));
                model.getRoles().add(role);
            }
        }

    }

}
