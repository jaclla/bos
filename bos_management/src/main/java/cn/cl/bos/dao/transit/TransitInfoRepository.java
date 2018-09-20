package cn.cl.bos.dao.transit;

import cn.cl.bos.domain.transit.TransitInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransitInfoRepository extends JpaRepository<TransitInfo,Integer> {
}
