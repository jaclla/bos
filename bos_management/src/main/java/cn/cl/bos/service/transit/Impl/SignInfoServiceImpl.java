package cn.cl.bos.service.transit.Impl;


import cn.cl.bos.dao.base.WayBillRepository;
import cn.cl.bos.dao.transit.SignInfoRepository;
import cn.cl.bos.dao.transit.TransitInfoRepository;
import cn.cl.bos.domain.transit.DeliveryInfo;
import cn.cl.bos.domain.transit.SignInfo;
import cn.cl.bos.domain.transit.TransitInfo;
import cn.cl.bos.index.WayBillIndexRepository;
import cn.cl.bos.service.transit.DeliveryService;
import cn.cl.bos.service.transit.SignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService {
    @Autowired
    private SignInfoRepository signInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;
    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(String transitInfoId, SignInfo model) {
        //保存开始配送信息
        signInfoRepository.save(model);
        //查询运输配送对象
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));

        //关联运输
        transitInfo.setSignInfo(model);
        //更改状态
        if (model.getSignType().equals("正常")) {
            //正常签收
            transitInfo.setStatus("正常签收");
            //更改运单状态
            transitInfo.getWayBill().setSignStatus(3);
            //更改索引库
            wayBillIndexRepository.save(transitInfo.getWayBill());
        }else {
            //异常
            transitInfo.setStatus("异常");
            //更改运单状态
            transitInfo.getWayBill().setSignStatus(4);
            //更改索引库
            wayBillIndexRepository.save(transitInfo.getWayBill());
        }
    }
}
