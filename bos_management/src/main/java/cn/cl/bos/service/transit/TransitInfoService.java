package cn.cl.bos.service.transit;

import cn.cl.bos.domain.transit.TransitInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransitInfoService {
    void createTransits(String wayBillIds);

    Page<TransitInfo> findPageData(Pageable pageable);

    TransitInfo findOne(int parseInt);
}
