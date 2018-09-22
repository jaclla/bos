package cn.cl.bos.service.transit;

import cn.cl.bos.domain.transit.DeliveryInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface DeliveryService {

    void save(String transitInfoId, DeliveryInfo model);
}
