package cn.cl.bos.dao.base;

import cn.cl.bos.domain.base.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface PromotionRepository extends JpaRepository<Promotion,Integer>, JpaSpecificationExecutor<Promotion> {
    @Query("update Promotion set status='0' where endDate<?1 and status='1'")
    @Modifying
    void updateStatus(Date date);
}
