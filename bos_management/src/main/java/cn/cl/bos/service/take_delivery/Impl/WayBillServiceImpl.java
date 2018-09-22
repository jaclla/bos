package cn.cl.bos.service.take_delivery.Impl;

import cn.cl.bos.dao.base.WayBillRepository;
import cn.cl.bos.index.WayBillIndexRepository;
import cn.cl.bos.domain.take_delivery.WayBill;
import cn.cl.bos.service.take_delivery.base.WayBillService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
    @Autowired
    private WayBillRepository wayBillRepository;
    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(WayBill wayBill) {
        WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
        if (persistWayBill == null || persistWayBill.getId() == null) {
            // 运单不存在
            wayBill.setSignStatus(1); // 待发货
            wayBillRepository.save(wayBill);
            //保存索引
            wayBillIndexRepository.save(wayBill);
        } else {
            try {
                if (persistWayBill.getSignStatus() == 1) {
                    Integer id = persistWayBill.getId();
                    BeanUtils.copyProperties(persistWayBill, wayBill);
                    persistWayBill.setId(id);
                    persistWayBill.setSignStatus(1); // 待发货
                    //保存索引
                    wayBillIndexRepository.save(persistWayBill);
                    syncIndex();
                } else {
                    throw new RuntimeException("运单已经发出，无法修改保存！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Page<WayBill> findPageDate(WayBill wayBill, Pageable pageable) {

        //判断是否是条件查询
        if (StringUtils.isBlank(wayBill.getWayBillNum())
                && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress())
                && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
            //不是条件查询
            return wayBillRepository.findAll(pageable);
        } else {
            //是条件查询
            //most 条件必须成立
            //most not 条件必须不成立
            // should 条件可以成立
            BoolQueryBuilder query = new BoolQueryBuilder();
            if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
                //运单号查询
                QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                query.must(queryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
                //发货地模糊查询
                //情况一：单个词条查询
                QueryBuilder wildcardQuery = new WildcardQueryBuilder("sendAddress", "*" + wayBill.getSendAddress() + "*");

                //情况二：多个词条查询
                QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress()).field("sendAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);

                //两种情况 or
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQuery);
                boolQueryBuilder.should(queryStringQueryBuilder);
                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
                //收货地模糊查询
                //情况一：单个词条查询
                QueryBuilder wildcardQuery = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");

                //情况二：多个词条查询
                QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress()).field("recAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);

                //两种情况 or
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQuery);
                boolQueryBuilder.should(queryStringQueryBuilder);
                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                //运单类型 等值查询
                QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                query.must(queryBuilder);
            }
            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                //签收状态类型 等值查询
                QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
                query.must(queryBuilder);
            }
            SearchQuery searchQuery = new NativeSearchQuery(query);
            searchQuery.setPageable(pageable);//分页效果
            //有条件查询
            return wayBillIndexRepository.search(searchQuery);
        }
    }

    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }

    @Override
    public void syncIndex() {
        List<WayBill> wayBills = wayBillRepository.findAll();
        wayBillIndexRepository.save(wayBills);
    }

    @Override
    public List<WayBill> findWayBills(WayBill wayBill) {
        //判断是否是条件查询
        if (StringUtils.isBlank(wayBill.getWayBillNum())
                && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress())
                && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
            //不是条件查询
            return wayBillRepository.findAll();
        } else {
            //是条件查询
            //most 条件必须成立
            //most not 条件必须不成立
            // should 条件可以成立
            BoolQueryBuilder query = new BoolQueryBuilder();
            if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
                //运单号查询
                QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                query.must(queryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
                //发货地模糊查询
                //情况一：单个词条查询
                QueryBuilder wildcardQuery = new WildcardQueryBuilder("sendAddress", "*" + wayBill.getSendAddress() + "*");

                //情况二：多个词条查询
                QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress()).field("sendAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);

                //两种情况 or
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQuery);
                boolQueryBuilder.should(queryStringQueryBuilder);
                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
                //收货地模糊查询
                //情况一：单个词条查询
                QueryBuilder wildcardQuery = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");

                //情况二：多个词条查询
                QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress()).field("recAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);

                //两种情况 or
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQuery);
                boolQueryBuilder.should(queryStringQueryBuilder);
                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                //运单类型 等值查询
                QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                query.must(queryBuilder);
            }
            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                //签收状态类型 等值查询
                QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
                query.must(queryBuilder);
            }
            SearchQuery searchQuery = new NativeSearchQuery(query);
            Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);
            //有条件查询
            return wayBillIndexRepository.search(searchQuery).getContent();
        }
    }

}
