package cn.cl.bos.dao.transit;

import cn.cl.bos.domain.transit.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<DeliveryInfo,Integer> {
}
