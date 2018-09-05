package cn.cl.crm.dao;

import cn.cl.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedareaid);

    @Query("update Customer set fixedAreaId =?1 where id =?2")
    @Modifying
    void updateFixedAreaId(String fixedAreaId, int id);

    @Query("update Customer set fixedAreaId = null where fixedAreaId=?1")
    @Modifying
    void clearFixedAreaId(String fixedAreaId);

    Customer findByTelephone(String telephone);

    @Query("update Customer set type = 1 where telephone=?1")
    @Modifying
    void updateType(String telephone);
}
