package cn.cl.bos.service.take_delivery.base;

import cn.cl.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WayBillService {


    void save(WayBill model);

    Page<WayBill> findPageDate(WayBill model, Pageable pageable);

    WayBill findByWayBillNum(String wayBillNum);

    public void syncIndex();

}
