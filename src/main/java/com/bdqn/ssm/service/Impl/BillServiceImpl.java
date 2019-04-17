package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.BillDao;
import com.bdqn.ssm.entity.Bill;
import com.bdqn.ssm.entity.Provider;
import com.bdqn.ssm.service.BillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    @Resource
    private BillDao bd;
    @Override
    public List<Bill> getBillList(String productName, int providerId, int isPayment, int pageNo, int pageSize) {
        return bd.getBillList(productName,providerId,isPayment,pageNo,pageSize);
    }

    @Override
    public int getBillCount(String productName, int providerId, int isPayment) {
        return bd.getBillCount(productName,providerId,isPayment);
    }

    @Override
    public List<Provider> getProviderAll() {
        return bd.getProviderAll();
    }

    @Override
    public Bill getBillById(int id) {
        return bd.getBillById(id);
    }

    @Override
    public int updateBill(Bill bill) {
        return bd.updateBill(bill);
    }
}
