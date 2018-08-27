package cn.cl.bos.dao.base;

import cn.cl.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {
    @Query(value = "update Courier set deltag='1' where id=?1")
    @Modifying
    void updateDelTag1(Integer id);

    @Query(value = "update Courier set deltag='0' where id=?1")
    @Modifying
    void updateDelTag0(Integer id);
}
