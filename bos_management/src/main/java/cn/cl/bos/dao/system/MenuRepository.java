package cn.cl.bos.dao.system;

import cn.cl.bos.domain.system.Menu;
import cn.cl.bos.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    @Query("from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id=?1 order by m.priority")
    List<Menu> findByUser(Integer id);
}
