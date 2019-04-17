package com.bdqn.ssm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bdqn.ssm.entity.Page;
import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.UserService;
import com.bdqn.ssm.util.BaseController;
import com.bdqn.ssm.util.Constants;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService us;
    private Logger logger=Logger.getLogger(UserController.class);
    @RequestMapping(value ="/login.html",method = RequestMethod.GET)
    public String login(){
        logger.debug("进入登录页面");
        return "login";
    }
    @RequestMapping(value = "/dologin.html",method = RequestMethod.POST)
    public String doLogin(@RequestParam String userCode, @RequestParam String userPassword,
                          HttpServletRequest request, HttpSession session){
        User user=us.doLogin(userCode,userPassword);
        if(user==null){
            request.setAttribute("error","用户名密码错误");
            return "login";
        }else{
            session.setAttribute(Constants.USER_SESSION,user);
            return "redirect:/user/main.html";
        }
    }
    @RequestMapping("/main.html")
    public String main(HttpSession session){
        if (session.getAttribute(Constants.USER_SESSION)==null){
            return "redirect:/user/login.html";
        }else{
            return "frame";
        }
    }
    //SpringMVC异常处理
    @RequestMapping("/exlogin.html")
    public String exlogin(@RequestParam String userCode,@RequestParam String userPassword){
        logger.debug("进入exlogin方法");
        User user=us.doLogin(userCode,userPassword);
        if(user==null){
            throw new RuntimeException("用户名或密码不正确");

        }
        return "redirect:/user/main.html";
    }
    @ExceptionHandler(value=RuntimeException.class)
    public String handierException(RuntimeException e,HttpServletRequest request){
        request.setAttribute("e",e);
        return "error";
    }
    @RequestMapping("/userlist.html")
    public String getUserList(Model model,
                              @RequestParam(value="queryname",required=false)String queryUserName,
                              @RequestParam(value="queryUserRole",required=false)String queryUserRole,
                              @RequestParam(value = "pageIndex",required=false)String currPage){

        //获取当前页码
        int currentPageNo=1;
        int _queryUserRole=0;
        List<User> userList=null;
        List<Role> rolelist=null;
        if(queryUserRole==null){
            queryUserRole="";
        }
        if(queryUserRole!=null&&!queryUserRole.equals("")){
            _queryUserRole= Integer.parseInt(queryUserRole);
        }
        if(currPage!=null){
            currentPageNo= Integer.valueOf(currPage);
        }
        //获取账单总数量
        int totalCount = us.getUserCount(queryUserName,_queryUserRole);
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

        userList=us.getUserList(queryUserName,_queryUserRole,(currentPageNo-1)*pageSize,pageSize);
        rolelist=us.getRoleList();
        model.addAttribute("userList",userList);
        model.addAttribute("roleList",rolelist);
        model.addAttribute("queryUserRole",queryUserRole);
        model.addAttribute("queryUserName",queryUserName);
        model.addAttribute("totalPageCount",totalPage);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "userlist";
    }
    @RequestMapping(value = "/useradd.html",method = RequestMethod.GET)
    public String useradd(@ModelAttribute("user")User user){
        logger.debug("进入添加页面");
        return "useradd";
    }

    /**
     * 当请求表单中不指定action请求,该表单默认请求为转入该页面的请求
     * 处理添加请求,实现用户添加功能
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "/useradd.html",method = RequestMethod.POST)
    public String useradd(User user,HttpSession session){
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        if (us.addUser(user)>0){
            System.out.println("成功");
            return "redirect:/user/userlist.html";
        }
        System.out.println("失败");
        return "useradd";
    }
    @RequestMapping(value="/useraddsave.html",method=RequestMethod.POST)
    public  String adddUserSave(User user,HttpSession session,HttpServletRequest request,
                                @RequestParam(value ="a_idPicPath", required = false)
                                        MultipartFile[] attachs) {
        /**
         * 多文件上传
         */
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
            user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            try {
                if(us.addUser(user)>0){
                    return "redirect:/user/userlist.html";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "useradd";
    }
    //修改回显
    @RequestMapping(value = "/usermodify.html",method = RequestMethod.GET)
    public String updateUser(@RequestParam("userid") int userid,Model model){
        User user=us.getUserByid(userid);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(user.getBirthday());
        model.addAttribute("birthday",date);
        model.addAttribute("user",user);
        return "usermodify";
    }

    @RequestMapping(value = "/usermodifysave.html",method = RequestMethod.POST)
    public String usermodifysave(User user,HttpSession session){
        user.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        if(us.updateUser(user)>0){
            return "redirect:/user/userlist.html";
        }
        return "usermodify";
    }
    //使用REST风格通过id查询用户详情
    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public String view(@PathVariable String id, Model model){
        int userid= Integer.parseInt(id);
        User user=us.getUserByid(userid);
        model.addAttribute(user);
        return "userview";
    }
    @RequestMapping(value = "/ucexist.html")
    @ResponseBody
    public Object userCodeIsExit(@RequestParam String userCode){
        HashMap<String,String> resultMap=new HashMap<>();
        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode","exist");
        }else{
            User user=us.getLoginUser(userCode);
            if(null!=user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode","noexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }
    @RequestMapping(value="/view",method = RequestMethod.GET,produces ={"application/json;charset=UTF-8"})
    @ResponseBody
    public Object view(@RequestParam String id){
        String cjson="";
        if (null==id||"".equals(id)){
            return "nodata";
        }else{
            try {
                int userid= Integer.parseInt(id);
                User user=us.getUserByid(userid);
                cjson= JSON.toJSONString(user);
                logger.debug("cjson================="+cjson);
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }

        }
        return cjson;
    }
    //删除
    @RequestMapping(value = "/delete.html",method = RequestMethod.GET)
    @ResponseBody
    public Object delUser(@RequestParam int userid){
        //System.out.println("进入闪出去页面:+--------------------------------------------"+userid);
        HashMap<String,String> resultMap=new HashMap<String, String>();
        if(userid==0){
            resultMap.put("delResult","notexist");
            //System.out.println("进入成功页面:+--------------------------------------------"+userid);
        }else{
            int count=us.delUser(userid);
            if(count>0){
                //System.out.println("进入成功1页面:+--------------------------------------------"+userid);
                resultMap.put("delResult","true");
            }else{
                //System.out.println("进入失败页面:+--------------------------------------------"+userid);
                resultMap.put("delResult","false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }
    //退出
    @RequestMapping("/logout.html")
    public String logout(HttpSession session){
        session.removeAttribute(Constants.USER_SESSION);
        return "redirect:/user/login.html";
    }
}
