package cn.cl.bos.dao.system;

import cn.cl.bos.domain.system.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    @Query("FROM Permission p inner join fetch p.roles r inner join fetch r.users u where u.id=?1")
    List<Permission> findByUser(Integer id);
}
