package cn.cl.bos.dao.base;

import cn.cl.bos.domain.base.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AreaRepository extends JpaRepository<Area,String>, JpaSpecificationExecutor<Area> {
    Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}
