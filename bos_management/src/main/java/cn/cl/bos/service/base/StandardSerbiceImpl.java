package cn.cl.bos.service.base;

import cn.cl.bos.dao.base.StandardRepository;
import cn.cl.bos.domain.base.Standard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class StandardSerbiceImpl implements StandardService {
    @Override
    public Page<Standard> findPageData(Pageable pageable) {
        return standardRepository.findAll(pageable);
    }

    @Autowired
    private StandardRepository standardRepository;


    @Override
    public void save(Standard standard) {
        standardRepository.save(standard);
    }
}
