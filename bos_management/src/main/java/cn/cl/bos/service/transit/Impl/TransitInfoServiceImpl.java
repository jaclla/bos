package cn.cl.bos.service.transit.Impl;

import cn.cl.bos.dao.base.WayBillRepository;
import cn.cl.bos.dao.transit.TransitInfoRepository;
import cn.cl.bos.domain.take_delivery.WayBill;
import cn.cl.bos.domain.transit.TransitInfo;
import cn.cl.bos.service.transit.TransitInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {
    @Autowired
    private TransitInfoRepository transitInfoRepository;
    @Autowired
    private WayBillRepository wayBillRepository;

    @Override
    public void createTransits(String wayBillIds) {
        if (StringUtils.isNoneBlank(wayBillIds)) {
            String[] split = wayBillIds.split(",");
            for (String id : split) {
                WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(id));
                //判断运单是否为代发货状态
                if (wayBill.getSignStatus() == 1) {
                    //生成运输信息
                    TransitInfo transitInfo = new TransitInfo();
                    transitInfo.setWayBill(wayBill);
                    transitInfo.setStatus("出入库中转");
                    transitInfoRepository.save(transitInfo);
                    //更改运单状态
                    wayBill.setSignStatus(2);
                }
            }

        }
    }

    @Override
    public Page<TransitInfo> findPageData(Pageable pageable) {
        return transitInfoRepository.findAll(pageable);
    }

    @Override
    public TransitInfo findOne(int parseInt) {
        return transitInfoRepository.findOne(parseInt);
    }
}
