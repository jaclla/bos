package cn.cl.bos.service.base.Impl;

import cn.cl.bos.dao.base.StandardRepository;
import cn.cl.bos.domain.base.Standard;
import cn.cl.bos.service.base.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class StandardSerbiceImpl implements StandardService {
    @Autowired
    private StandardRepository standardRepository;

    @Override
    @CacheEvict(value = "standard", key = "#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<Standard> findPageData(Pageable pageable) {
        return standardRepository.findAll(pageable);
    }

    @Override
    @Cacheable("standard")
    public List<Standard> findAll() {
        return standardRepository.findAll();
    }

    @Override
    @CacheEvict(value = "standard", allEntries = true)
    public void delete(String[] id) {
        for (String s : id) {
            standardRepository.delete(Integer.parseInt(s));
        }
    }


    @Override
    @CacheEvict(value = "standard", allEntries = true)
    public void save(Standard standard) {
        standardRepository.save(standard);
    }
}
