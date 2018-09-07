package cn.cl.bos.dao.base;

import cn.cl.bos.domain.base.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PromotionRepository extends JpaRepository<Promotion,Integer>, JpaSpecificationExecutor<Promotion> {
}
