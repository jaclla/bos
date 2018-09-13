package cn.cl.bos.service.take_delivery.Impl;

import cn.cl.bos.dao.base.WayBillRepository;
import cn.cl.bos.domain.take_delivery.WayBill;
import cn.cl.bos.service.take_delivery.base.WayBillService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
    @Autowired
    private WayBillRepository wayBillRepository;

    @Override
    public void save(WayBill wayBill) {
        WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
        if (persistWayBill == null || persistWayBill.getId() == null) {
            // 运单不存在
            wayBill.setSignStatus(1); // 待发货
            wayBillRepository.save(wayBill);
        } else {
            try {
                Integer id = persistWayBill.getId();
                BeanUtils.copyProperties(persistWayBill, wayBill);
                persistWayBill.setId(id);
                persistWayBill.setSignStatus(1); // 待发货
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public Page<WayBill> findPageDate(Pageable pageable) {
        return wayBillRepository.findAll(pageable);
    }

    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }
}
