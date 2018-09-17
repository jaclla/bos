package cn.cl.bos.service.system.Impl;

import cn.cl.bos.dao.system.MenuRepository;
import cn.cl.bos.domain.system.Menu;
import cn.cl.bos.domain.system.User;
import cn.cl.bos.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public void save(Menu model) {
        if (model.getParentMenu() != null && model.getParentMenu().getId() == 0) {
            model.setParentMenu(null);
        }
        menuRepository.save(model);
    }

    @Override
    public List<Menu> findByUser(User user) {
        if (user.getUsername().equals("admin")) {
            return menuRepository.findAll();
        } else {
            return menuRepository.findByUser(user.getId());
        }
    }
}
