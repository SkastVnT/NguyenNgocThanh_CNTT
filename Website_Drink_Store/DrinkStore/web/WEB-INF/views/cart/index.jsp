<%-- 
    Document   : index
    Created on : May 31, 2024, 9:47:22 AM
    Author     : PHT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mt-5">
    <h2 class="mb-4">Your Cart</h2>
    <hr/>
    <table class="table table-bordered table-responsive-md">
        <thead class="bg-dark text-white">
            <tr>
                <th>#</th>
                <th>Id</th>
                <th>Image</th>
                <th>Description</th>
                <th class="text-end">Old Price</th>
                <th class="text-end">Discount</th>
                <th class="text-end">New Price</th>
                <th>Quantity</th>
                <th class="text-end">Cost</th>
                <th>Operations</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${cart.list}" varStatus="loop">
                <form action="<c:url value='/cart/update' />" method="post">
                    <tr>
                        <td>${loop.count}</td>
                        <td>
                            ${item.product.id}
                            <input type="hidden" name="id" value="${item.product.id}" />
                        </td>
                        <td><img src="<c:url value='/products/${item.product.id}.jpg' />" class="img-fluid" height="80"/></td>        
                        <td>${item.product.description}</td>
                        <td class="text-end text-danger">
                            <fmt:formatNumber value="${item.oldPrice}" type="currency"/>
                        </td>
                        <td class="text-end text-success">
                            <fmt:formatNumber value="${item.discount}" type="percent"/>
                        </td>
                        <td class="text-end text-warning">
                            <fmt:formatNumber value="${item.newPrice}" type="currency"/>
                        </td>
                        <td>
                            <input type="number" class="form-control bg-light text-dark border-dark" min="1" max="100" name="quantity" value="${item.quantity}" />
                        </td>
                        <td class="text-end text-primary">
                            <fmt:formatNumber value="${item.cost}" type="currency"/>
                        </td>
                        <td>
                            <div class="btn-group">
                                <button type="submit" class="btn btn-outline-secondary btn-sm">Update</button>
                                <a href="<c:url value='/cart/delete?id=${item.product.id}' />" class="btn btn-outline-danger btn-sm">Delete</a>
                            </div>
                        </td>
                    </tr>
                </form>
            </c:forEach>
            <tr class="bg-light text-dark">
                <th colspan="8" class="text-end">Total:</th>
                <th class="text-end text-primary"><fmt:formatNumber value="${cart.totalCost}" type="currency"/></th>
                <th><a href="<c:url value='/cart/empty' />" class="btn btn-outline-warning btn-sm">Empty</a></th>
            </tr>
        </tbody>
    </table>
</div>

<style>
    body {
        background-color: #2e2e2e;
        color: #ecf0f1;
        font-family: 'Verdana', sans-serif;
        margin: 0;
        padding: 0;
    }
    .container {
        background-color: #444;
        padding: 40px;
        border-radius: 15px;
        box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
        margin-top: 70px;
    }
    .table {
        color: #ecf0f1;
        margin-bottom: 0;
    }
    .table th, .table td {
        vertical-align: middle;
    }
    .table thead th {
        border-bottom: 2px solid #ecf0f1;
    }
    .btn-group .btn {
        margin-right: 5px;
    }
    .btn-outline-secondary {
        color: #ecf0f1;
        border-color: #ecf0f1;
    }
    .btn-outline-secondary:hover {
        background-color: #ecf0f1;
        color: #444;
    }
    .btn-outline-warning {
        color: #ecf0f1;
        border-color: #ecf0f1;
    }
    .btn-outline-warning:hover {
        background-color: #ecf0f1;
        color: #444;
    }
    .btn-outline-danger {
        color: #ecf0f1;
        border-color: #ecf0f1;
    }
    .btn-outline-danger:hover {
        background-color: #ecf0f1;
        color: #e74c3c;
    }
    .bg-dark {
        background-color: #2c3e50 !important;
    }
    .bg-light {
        background-color: #bdc3c7 !important;
    }
</style>
