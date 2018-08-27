package cn.cl.bos.service.base;

import cn.cl.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CourierService  {
    void save(Courier courier);

    Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable);
}
