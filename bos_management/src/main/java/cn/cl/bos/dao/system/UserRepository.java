package cn.cl.bos.dao.system;

import cn.cl.bos.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
