package cn.cl.bos.service.system;

import cn.cl.bos.domain.system.User;

public interface UserService {
    User findByUsername(String username);
}
