<%-- 
    Document   : register
    Created on : Jun 21, 2024, 10:24:50 AM
    Author     : PHT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
    <div class="col-sm-12">
        <h2>Register</h2>
    </div>
</div>
<div class="row">    
    <div class="col-sm-6">
        <form action="<c:url value="/account/register_handler" />">   
            <div class="mb-3 mt-3">
                <label for="fullName" class="form-label">Full Name:</label>
                <input type="text" class="form-control" id="fullName" name="fullName" value="${param.fullName}" placeholder="Enter your full name">
            </div>
            <div class="mb-3 mt-3">
                <label for="email" class="form-label">Email:</label>
                <input type="email" class="form-control" id="email" name="email" value="${param.email}" placeholder="Enter your email">
            </div>
            <div class="mb-3 mt-3">
                <label for="password" class="form-label">Password:</label>
                <input type="password" class="form-control" id="password" name="password" value="${param.password}" placeholder="Enter your password">
            </div>
            <div style="text-align: right;">
                <button type="submit" class="btn btn-outline-success" name="op" value="register">Register</button>
                <a href="<c:url value="/home/index" />" class="btn btn-outline-success">Cancel</a>
            </div>
        </form>
    </div>
    <div class="col-sm-6">
        <img src="<c:url value="/images/create.png" />" style="width: 100%; height: 100%;"/>
    </div>
</div>
