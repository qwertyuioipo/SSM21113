package com.bdqn.ssm.service;

import com.bdqn.ssm.entity.Bill;
import com.bdqn.ssm.entity.Provider;

import java.util.List;

public interface BillService {
    //查询所有方法
    public List<Bill> getBillList(String productName, int providerId,int isPayment, int pageNo, int pageSize);
    //总页数
    public int getBillCount(String productName, int providerId,int isPayment);
    //查询所有供应商
    public List<Provider> getProviderAll();
    //根据id查询订单信息
    public Bill getBillById(int id);
    //修改用户商
    public int updateBill(Bill bill);
}
