package cn.cl.bos.dao.base;

import cn.cl.bos.domain.take_delivery.WorkBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkBillRepository extends JpaRepository<WorkBill,Integer> {
}
