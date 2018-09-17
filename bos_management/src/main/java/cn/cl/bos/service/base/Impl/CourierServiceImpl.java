package cn.cl.bos.service.base.Impl;

import cn.cl.bos.dao.base.CourierRepository;
import cn.cl.bos.domain.base.Courier;
import cn.cl.bos.service.base.CourierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
    @Autowired
    private CourierRepository courierRepository;

    @Override
    public Page<Courier> findPageData(Specification<Courier> specification,Pageable pageable) {
        return courierRepository.findAll(specification,pageable);
    }

    @Override
    public void delBath(String[] ids) {
        for (String idStr : ids) {
            Integer id = Integer.parseInt(idStr);
            courierRepository.updateDelTag1(id);
        }
    }

    @Override
    public void restoreBatch(String[] idArray) {
        for (String idStr : idArray) {
            Integer id = Integer.parseInt(idStr);
            courierRepository.updateDelTag0(id);
        }
    }

    @Override
    public List<Courier> findNoAssociation() {
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.isEmpty(root.get("fixedAreas").as(Set.class));
            }
        };
        return courierRepository.findAll(specification);
    }

    @Override
    @RequiresPermissions("courier:add")
    public void save(Courier courier) {
        courierRepository.save(courier);
    }
}
