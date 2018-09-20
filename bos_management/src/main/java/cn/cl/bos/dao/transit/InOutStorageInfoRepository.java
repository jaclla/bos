package cn.cl.bos.dao.transit;

import cn.cl.bos.domain.transit.InOutStorageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InOutStorageInfoRepository extends JpaRepository<InOutStorageInfo,Integer> {
}
