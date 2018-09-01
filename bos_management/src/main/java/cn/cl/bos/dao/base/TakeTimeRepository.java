package cn.cl.bos.dao.base;

import cn.cl.bos.domain.base.TakeTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeTimeRepository extends JpaRepository<TakeTime,Integer> {
}
