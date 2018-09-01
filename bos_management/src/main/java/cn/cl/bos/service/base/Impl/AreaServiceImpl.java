package cn.cl.bos.service.base.Impl;

import cn.cl.bos.dao.base.AreaRepository;
import cn.cl.bos.domain.base.Area;
import cn.cl.bos.service.base.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaRepository areaRepository;

    @Override
    public void saveBatch(ArrayList<Area> areas) {
        areaRepository.save(areas);
    }

    @Override
    public Page<Area> findPageData(Specification<Area> specification, Pageable pageable) {
        return areaRepository.findAll(specification,pageable);
    }

    @Override
    public void save(Area area) {
        areaRepository.save(area);
    }

    @Override
    public void del(String[] idArray) {
        for (String id : idArray) {
            areaRepository.delete(id);
        }
    }


}
