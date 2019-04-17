<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2019/3/29
  Time: 14:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fm" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/statics/js/useradd.js"></script>
</head>
<body>
<fm:form name="myform" method="post" modelAttribute="user">
    <fm:errors path="userCode"/><br>
    用户编码:<fm:input path="userCode"/><br>
    <fm:errors path="userName"/><br>
    用户名称:<fm:input path="userName"/><br>
    <fm:errors path="userPassword"/><br>
    用户密码:<fm:input path="userPassword"/><br>
    <fm:errors path="birthday"/><br>
    用户生日:<fm:input path="birthday" Class="Wdate" readonly="readonly"
                   onchange="WdatePicker();" cssClass="Wdate"/> <br>
    用户地址:<fm:input path="address"/><br>
    联系电话:<fm:input path="phone"/><br>
    用户角色:<fm:radiobutton path="userRole" value="1"/>系统管理员
    <fm:radiobutton path="userRole" value="2"/>经理
    <fm:radiobutton path="userRole" value="3" checked="checked"/>普通用户
    <br>
    <input type="submit" value="保存">
</fm:form>
</body>
</html>
