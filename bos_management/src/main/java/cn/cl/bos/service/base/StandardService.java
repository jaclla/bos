package cn.cl.bos.service.base;

import cn.cl.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StandardService {
    public void save(Standard standard);

    Page<Standard> findPageData(Pageable pageable);

    void delete(int id);

    List<Standard> findAll();
}
