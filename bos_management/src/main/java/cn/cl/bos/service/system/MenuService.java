package cn.cl.bos.service.system;

import cn.cl.bos.domain.system.Menu;
import cn.cl.bos.domain.system.User;

import java.util.List;

public interface MenuService {
    List<Menu> findAll();

    void save(Menu model);

    List<Menu> findByUser(User user);
}
