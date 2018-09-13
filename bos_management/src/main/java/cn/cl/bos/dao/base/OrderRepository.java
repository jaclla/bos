package cn.cl.bos.dao.base;

import cn.cl.bos.domain.take_delivery.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order> {
    Order findByOrderNum(String orderNum);
}
