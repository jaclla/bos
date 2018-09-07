package cn.cl.bos.service.base;

import cn.cl.bos.domain.base.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
    void save(Promotion model);

    Page<Promotion> findPageDate(Pageable pageable);
}
