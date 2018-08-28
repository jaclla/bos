package cn.cl.bos.service.base;

import cn.cl.bos.dao.base.FixedAreaRepository;
import cn.cl.bos.domain.base.FixedArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    @Override
    public void save(FixedArea model) {
        fixedAreaRepository.save(model);
    }

    @Override
    public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable) {
        return fixedAreaRepository.findAll(specification, pageable);
    }

    @Override
    public void delete(String[] model) {
        for (String s : model) {
            fixedAreaRepository.delete(s);
        }
    }


}
