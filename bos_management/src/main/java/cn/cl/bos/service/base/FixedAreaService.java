package cn.cl.bos.service.base;

import cn.cl.bos.domain.base.FixedArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface FixedAreaService {
    void save(FixedArea model);

    Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable);

    void delete(String[] model);
}
