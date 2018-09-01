package cn.cl.crm.service.Impl;

import cn.cl.crm.domain.Customer;
import cn.cl.crm.dao.CustomerRepository;
import cn.cl.crm.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    //    注入dao
    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public List<Customer> findNoAssociationCustomers() {
        List<Customer> byFixedAreaIdIsNull = customerRepository.findByFixedAreaIdIsNull();
        return byFixedAreaIdIsNull;

    }

    @Override
    public List<Customer> findHasAssociationFixedAreaCustomers(String fixedareaid) {
        return customerRepository.findByFixedAreaId(fixedareaid);
    }

    @Override
    public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
        //解除关联动作
        customerRepository.clearFixedAreaId(fixedAreaId);
        if (customerIdStr.equals("null")){
            return;
        }
//        拆分字符串
        String[] customerIdArray = customerIdStr.split(",");
        for (String idStr : customerIdArray) {
            int id = Integer.parseInt(idStr);
            customerRepository.updateFixedAreaId(fixedAreaId, id);
        }
    }
}