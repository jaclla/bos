package cn.cl.bos.service.transit;

import cn.cl.bos.domain.transit.SignInfo;

public interface SignInfoService {
    void save(String transitInfoId, SignInfo model);
}
