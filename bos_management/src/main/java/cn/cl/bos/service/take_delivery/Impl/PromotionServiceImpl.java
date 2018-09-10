package cn.cl.bos.service.take_delivery.Impl;

import cn.cl.bos.dao.base.PromotionRepository;
import cn.cl.bos.domain.command.PageBean;
import cn.cl.bos.domain.base.Promotion;
import cn.cl.bos.service.take_delivery.base.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Override
    public PageBean<Promotion> findPageData(int page, int rows) {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Promotion> pageData = promotionRepository.findAll(pageable);

        PageBean<Promotion> pageBean = new PageBean<>();
        pageBean.setPageData(pageData.getContent());
        pageBean.setTotalCount(pageData.getTotalElements());
        return pageBean;
    }

    @Override
    public Promotion findPageData(Integer id) {
        return promotionRepository.findOne(id);
    }

    @Override
    public void updateStatus(Date date) {
        promotionRepository.updateStatus(date);
    }
}
