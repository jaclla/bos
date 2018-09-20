package cn.cl.bos.service.transit;

import cn.cl.bos.domain.transit.InOutStorageInfo;

public interface InOutStorageInfoService {
    void save(String transitInfoId, InOutStorageInfo model);
}
