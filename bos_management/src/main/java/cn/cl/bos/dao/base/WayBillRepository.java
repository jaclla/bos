package cn.cl.bos.dao.base;

import cn.cl.bos.domain.take_delivery.WayBill;
import cn.cl.bos.domain.take_delivery.WorkBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WayBillRepository extends JpaRepository<WayBill,Integer> {
    @Query
    WayBill findByWayBillNum(String wayBillNum);
}
