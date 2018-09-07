package cn.cl.bos.service.base.Impl;

import cn.cl.bos.dao.base.PromotionRepository;
import cn.cl.bos.domain.base.Promotion;
import cn.cl.bos.service.base.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public void save(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    @Override
    public Page<Promotion> findPageDate(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }
}
