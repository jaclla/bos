package cn.cl.bos.service.take_delivery.base;

import cn.cl.bos.domain.take_delivery.Order;
import cn.cl.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface WayBillService {


    void save(WayBill model);

    Page<WayBill> findPageDate(Pageable pageable);

    WayBill findByWayBillNum(String wayBillNum);
}
