package com.bdqn.ssm.dao;

import com.bdqn.ssm.entity.Bill;
import com.bdqn.ssm.entity.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillDao {
    //查询所有方法
    public List<Bill> getBillList(@Param("productName") String productName,@Param("providerId") int providerId, @Param("isPayment") int isPayment,@Param("pageNo") int currentPageNo,@Param("pageSize") int pageSize);
    //总页数
    public int getBillCount(@Param("productName")String productName,@Param("providerId") int providerId, @Param("isPayment") int isPayment);
    //查询所有供应商
    public List<Provider> getProviderAll();
    //根据id查询订单信息
    public Bill getBillById(@Param("id") int id);
    //修改用户商
    public int updateBill(Bill bill);
}
