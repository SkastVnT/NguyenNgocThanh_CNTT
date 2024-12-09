<%-- 
    Document   : main
    Created on : May 3, 2024, 9:00:44 AM
    Author     : PHT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <!-- Latest compiled and minified CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Latest compiled JavaScript -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <!-- Custom CSS -->
        <style>
            body {
                background-color: #2c3e50;
                color: #ecf0f1;
                font-family: 'Arial', sans-serif;
                margin: 0;
                padding: 0;
            }
            .container {
                background-color: #34495e;
                padding: 40px;
                border-radius: 15px;
                box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
                margin-top: 70px;
            }
            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 30px;
            }
            .header a {
                font-size: 22pt;
                font-weight: 700;
                color: #1abc9c;
                text-decoration: none;
            }
            .header a:hover {
                color: #16a085;
            }
            .header .btn {
                margin-left: 15px;
            }
            .header .float-end {
                display: flex;
                align-items: center;
            }
            h1 {
                margin-bottom: 25px;
                font-weight: 700;
                color: #1abc9c;
            }
            .btn-outline-light {
                border-color: #1abc9c;
                color: #1abc9c;
            }
            .btn-outline-light:hover {
                background-color: #1abc9c;
                color: #2c3e50;
            }
            footer {
                text-align: center;
                padding: 25px 0;
                background-color: #1abc9c;
                color: #2c3e50;
                margin-top: 40px;
                font-size: 16px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <a href="<c:url value='/home/index' />">Drinks Store</a>
                <span class="float-end">
                    <c:if test="${account == null}">
                        <a href="<c:url value='/account/register' />" class="btn btn-outline-light">Register</a>
                        <a href="<c:url value='/account/login' />" class="btn btn-outline-light">Login</a>
                    </c:if>
                    <c:if test="${account != null}">
                        <span class="btn btn-outline-light">Welcome ${account.fullName}</span>
                        <a href="<c:url value='/account/logout' />" class="btn btn-outline-light">Logout</a>
                    </c:if>
                    <a href="<c:url value='/cart/index' />" class="btn btn-outline-light">
                        Your cart: <fmt:formatNumber value="${empty cart ? 0 : cart.totalCost}" type="currency" />
                    </a>
                </span>
            </div>
            <hr/>
            <div class="row">
                <div class="col-12">
                    <%--insert view here--%>
                    <jsp:include page="/WEB-INF/views/${controller}/${action}.jsp" />
                </div>
            </div>
            <footer>
                Copyrights &copy; HSU Students 2024
            </footer>
        </div>
    </body>
</html>
