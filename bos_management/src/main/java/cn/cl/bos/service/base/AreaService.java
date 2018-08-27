package cn.cl.bos.service.base;

import cn.cl.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public interface AreaService {
    void saveBatch(ArrayList<Area> areas);

    Page<Area> findPageData(Specification<Area> specification, Pageable pageable);

    void save(Area area);

    void del(String[] idArray);
}
