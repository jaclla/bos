package cn.cl.bos.service.transit.Impl;


import cn.cl.bos.dao.transit.DeliveryRepository;
import cn.cl.bos.dao.transit.TransitInfoRepository;
import cn.cl.bos.domain.transit.DeliveryInfo;
import cn.cl.bos.domain.transit.TransitInfo;
import cn.cl.bos.service.transit.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;
    @Override
    public void save(String transitInfoId, DeliveryInfo model) {
        //保存开始配送信息
        deliveryRepository.save(model);
        //查询运输配送对象
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));

        transitInfo.setDeliveryInfo(model);
        transitInfo.setStatus("开始配送");
    }
}
