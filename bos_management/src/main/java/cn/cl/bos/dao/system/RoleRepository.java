package cn.cl.bos.dao.system;

import cn.cl.bos.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    @Query("FROM Role r inner join fetch r.users u where  u.id =?1")
    List<Role> findByUser(Integer id);
}
