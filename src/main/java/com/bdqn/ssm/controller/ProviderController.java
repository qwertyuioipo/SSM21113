package com.bdqn.ssm.controller;

import com.bdqn.ssm.entity.Page;
import com.bdqn.ssm.entity.Provider;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.ProviderService;
import com.bdqn.ssm.util.Constants;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("pro")
public class ProviderController {
    @Resource
    private ProviderService ps;
    private Logger logger=Logger.getLogger(ProviderController.class);
    @RequestMapping(value = "/provider.html")
    public String getProviderList(Model model,
                                  @RequestParam(value="queryProCode",required=false)String queryProCode,
                                  @RequestParam(value="queryProName",required=false)String queryProName,
                                  @RequestParam(value = "pageIndex",required=false)String currPage){
        //获取当前页码
        int currentPageNo=1;
        List<Provider> providerList=null;
        if(currPage!=null){
            currentPageNo= Integer.valueOf(currPage);
        }
        //获取账单总数量
        int totalCount = ps.getProviderCount(queryProCode,queryProName);
        if(currPage==null||currPage==""){
            currPage="1";
        }
        int pageIndex= currentPageNo;
        //每页显示行数
        int pageSize= Constants.pageSize;
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

        providerList=ps.getProviderList(queryProCode,queryProName,(currentPageNo-1)*pageSize,pageSize);
        model.addAttribute("providerList",providerList);
        model.addAttribute("queryProCode",queryProCode);
        model.addAttribute("queryProName",queryProName);
        model.addAttribute("totalPageCount",totalPage);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "providerlist";
    }
    //进入添加页面
    @RequestMapping(value = "/provideradd.html",method = RequestMethod.GET)
    public String addProviderList(@ModelAttribute("provider") Provider provider){
        return "provideradd";
    }
    //实现添加功能
    @RequestMapping(value = "/provideraddsave.html",method = RequestMethod.POST)
    public  String addProvider(Provider provider,HttpSession session){
        provider.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        int count=ps.addProvider(provider);
        if(count>0){
            return "redirect:/pro/providerlist.html";
        }
        return "provideradd";
    }
    /*@RequestMapping(value = "/provideraddsave.html",method = RequestMethod.POST)
    public String addProvider(Provider provider, HttpSession session, HttpServletRequest request,
                              @RequestParam(value ="a_orgCodePicPath", required = false)
                                      MultipartFile[] attachs){
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
        logger.info("uploadFile path ============== > "+path);
        for(int i = 0;i < attachs.length ;i++){
            MultipartFile attach = attachs[i];
            if(!attach.isEmpty()){
                if(i == 0){
                    errorInfo = "uploadFileError";
                }else if(i == 1){
                    errorInfo = "uploadWpError";
                }
                String oldFileName = attach.getOriginalFilename();//原文件名
                logger.info("uploadFile oldFileName ============== > "+oldFileName);
                String prefix= FilenameUtils.getExtension(oldFileName);//原文件后缀
                logger.debug("uploadFile prefix============> " + prefix);
                int filesize = 500000;
                logger.debug("uploadFile size============> " + attach.getSize());
                if(attach.getSize() >  filesize){//上传大小不得超过 500k
                    request.setAttribute("e", " * 上传大小不得超过 500k");
                    flag = false;
                }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                    String fileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000000)+"_Personal.jpg";
                    logger.debug("new fileName======== " + attach.getName());
                    File targetFile = new File(path, fileName);
                    if(!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    //保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    if(i == 0){
                        idPicPath = path+File.separator+fileName;
                    }else if(i == 1){
                        workPicPath = path+File.separator+fileName;
                    }
                    logger.debug("idPicPath: " + idPicPath);
                    logger.debug("workPicPath: " + workPicPath);

                }else{
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if(flag){
            //provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            provider.setCreationDate(new Date());
            provider.setCompanyLicPicPath(idPicPath);
            provider.setOrgCodePicPath(workPicPath);
            try {
                if(ps.addProvider(provider)>0){
                    return "redirect:/pro/providerlist.html";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("1111111111111111111111111111111111"+provider.getProCode()+provider.getProName());
        return "provideradd";
    }*/
}
