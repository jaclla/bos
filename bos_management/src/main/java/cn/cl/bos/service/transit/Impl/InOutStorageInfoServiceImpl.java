package cn.cl.bos.service.transit.Impl;

import cn.cl.bos.dao.transit.InOutStorageInfoRepository;
import cn.cl.bos.domain.transit.InOutStorageInfo;
import cn.cl.bos.domain.transit.TransitInfo;
import cn.cl.bos.service.transit.InOutStorageInfoService;
import cn.cl.bos.service.transit.TransitInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService {
    @Autowired
    private InOutStorageInfoRepository inOutStorageInfoRepository;
    @Autowired
    private TransitInfoService transitInfoService;

    @Override
    public void save(String transitInfoId, InOutStorageInfo model) {
        //保存出入库信息
        inOutStorageInfoRepository.save(model);
        //查询配送信息
        TransitInfo transitInfo = transitInfoService.findOne(Integer.parseInt(transitInfoId));
        //关联出入库信息到配送信息中
        transitInfo.getInOutStorageInfos().add(model);
        //修改状态
        if (model.getOperation().equals("到达网点")) {
            transitInfo.setStatus("到达网点");
            //更新网点，显示配送路径
            transitInfo.setOutletAddress(model.getAddress());
        }
    }
}
