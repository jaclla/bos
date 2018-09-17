package cn.cl.bos.service.system.Impl;

import cn.cl.bos.dao.system.UserRepository;
import cn.cl.bos.domain.system.User;
import cn.cl.bos.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
