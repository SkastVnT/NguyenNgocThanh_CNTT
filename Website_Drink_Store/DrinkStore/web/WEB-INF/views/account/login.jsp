<%-- 
    Document   : login
    Created on : Jun 21, 2024, 9:09:52 AM
    Author     : PHT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
    <div class="col-sm-12">
        <h2>Login</h2>
        <hr/>
    </div>
</div>
<div class="row">
    <div class="col-sm-6">
        <form action="<c:url value="/account/login_handler" />">
            <div class="mb-3 mt-3">
                <label for="email" class="form-label">Email:</label>
                <input type="text" class="form-control" id="email" placeholder="Enter email" name="email" value="${param.email}">
            </div>
            <div class="mb-3 mt-3">
                <label for="password" class="form-label">Password:</label>
                <input type="password" class="form-control" id="password" placeholder="Enter password" name="password"  value="${param.password}">
            </div>                       
            <button type="submit" class="btn btn-primary">Login</button>
        </form>
        <i style="color:red;">${errorMessage}</i>
    </div>
    <div class="col-sm-6">
        <img src="<c:url value="/images/create.png" />" width="100%" />
    </div>
</div>
