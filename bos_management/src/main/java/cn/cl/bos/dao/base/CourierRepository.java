package cn.cl.bos.dao.base;

import cn.cl.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourierRepository extends JpaRepository<Courier,Integer> , JpaSpecificationExecutor<Courier> {
}
