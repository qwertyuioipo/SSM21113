package com.bdqn.ssm.controller;

import com.alibaba.fastjson.JSONArray;
import com.bdqn.ssm.entity.Bill;
import com.bdqn.ssm.entity.Page;
import com.bdqn.ssm.entity.Provider;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.BillService;
import com.bdqn.ssm.util.Constants;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {
    @Resource
    private BillService bs;
    private Logger logger=Logger.getLogger("BillController");
    @RequestMapping("/billlist.html")
    public String getBillList(Model model,
                              @RequestParam(value="queryProductName",required=false)String queryProductName,
                              @RequestParam(value="queryProviderId",required=false)String queryProviderId,
                              @RequestParam(value="queryIsPayment",required=false)String queryIsPayment,
                              @RequestParam(value = "pageIndex",required=false)String currPage){
        //获取当前页码
        int currentPageNo=1;
        int _queryProviderId=0;
        int _queryIsPayment=0;
        List<Bill> billList=null;
        List<Provider> proList=null;
        if(queryProviderId==null){
            queryProviderId="";
        }
        if(queryProviderId!=null&&!queryProviderId.equals("")){
            _queryProviderId= Integer.parseInt(queryProviderId);
        }
        if(queryIsPayment==null){
            queryIsPayment="";
        }
        if(queryIsPayment!=null&&!queryIsPayment.equals("")){
            _queryIsPayment= Integer.parseInt(queryIsPayment);
        }
        if(currPage!=null){
            currentPageNo= Integer.valueOf(currPage);
        }
        //获取账单总数量
        int totalCount = bs.getBillCount(queryProductName,_queryProviderId,_queryIsPayment);
        if(currPage==null||currPage==""){
            currPage="1";
        }
        int pageIndex= currentPageNo;
        //每页显示行数
        int pageSize=Constants.pageSize;
        //获取总页数
        Page page=new Page();
        page.setPageSize(pageSize);//页面大小
        page.setRecordCount(totalCount);//总记录数
        int totalPage=page.getTotalPageCount();
        //控制首页和末页
        if(pageIndex<1){
            pageIndex=1;
        }else if (pageIndex>totalPage){
            pageIndex=totalPage;
        }
        //设置当前页
        page.setCurrPageNo(pageIndex);

        billList=bs.getBillList(queryProductName,_queryProviderId,_queryIsPayment,(currentPageNo-1)*pageSize,pageSize);
        proList=bs.getProviderAll();
        model.addAttribute("billList",billList);
        model.addAttribute("providerList",proList);
        model.addAttribute("queryProductName",queryProductName);
        model.addAttribute("queryIsPayment",queryIsPayment);
        model.addAttribute("totalPageCount",totalPage);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "billlist";
    }
    @RequestMapping(value = "/billModify.html",method = RequestMethod.GET)
    public String updateBill(@RequestParam("billid") int billid,Model model){
        Bill bill=bs.getBillById(billid);
        model.addAttribute("bill",bill);
        return "billmodify";
    }
    @RequestMapping(value = "/billJSON",method = RequestMethod.GET,produces =
            {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object billJSON(){
        logger.debug("==================账单回显");
        List<Provider> getproviderlist=bs.getProviderAll();
        return JSONArray.toJSONString(getproviderlist);
    }

    @RequestMapping(value="/billmodifysave.html",method = RequestMethod.POST)
    public String updateBill(Bill bill, HttpSession session){
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        int count=bs.updateBill(bill);
        if(count>0){
            System.out.println("修改成功");
            return "redirect:/user/userlist.html";
        }
        return "billmodify";
    }
}
