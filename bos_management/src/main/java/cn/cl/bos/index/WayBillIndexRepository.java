package cn.cl.bos.index;

import cn.cl.bos.domain.take_delivery.WayBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill,Integer> {
}
