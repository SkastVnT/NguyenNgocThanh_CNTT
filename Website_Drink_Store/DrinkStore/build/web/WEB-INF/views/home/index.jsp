<%-- 
    Document   : index
    Created on : May 31, 2024, 9:04:24 AM
    Author     : PHT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Product Listing</title>
    <style>
        body {
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f0f0;
            color: #444;
            display: flex;
            flex-direction: column;
            align-items: center;
            background-image: url('https://w0.peakpx.com/wallpaper/1007/88/HD-wallpaper-mayurdhvajsinh-cocktail-cocktails.jpg');
            background-size: cover;
            background-repeat: no-repeat;
            background-position: center;
        }
        .container {
            padding: 60px 15px;
            max-width: 1200px;
            margin: 0 auto;
            background-color: rgba(255, 255, 255, 0.9);
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .card {
            background: linear-gradient(to bottom, #ffffff, #f9f9f9);
            border: none;
            border-radius: 12px;
            overflow: hidden;
            transition: transform 0.3s, box-shadow 0.3s;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .card:hover {
            transform: scale(1.02);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
        }
        .card img {
            width: 100%;
            height: 180px;
            object-fit: cover;
        }
        .card-body {
            padding: 15px;
            text-align: center;
        }
        .card-title {
            font-size: 1.5rem;
            font-weight: 700;
            color: #333;
        }
        .card-text {
            margin-top: 10px;
            margin-bottom: 20px;
            font-size: 1rem;
            color: #555;
        }
        .old-price {
            color: #999;
            text-decoration: line-through;
        }
        .new-price {
            color: #e74c3c;
            font-size: 1.2rem;
            font-weight: bold;
        }
        .card-footer {
            background-color: #f1f1f1;
            padding: 10px 15px;
            text-align: center;
        }
        .btn-primary {
            background-color: #3498db;
            border: none;
            color: #fff;
            padding: 10px 20px;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .btn-primary:hover {
            background-color: #2980b9;
        }
        .btn-cart i {
            margin-right: 5px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="row">
        <c:forEach var="product" items="${list}">
            <div class="col-md-4 col-sm-6 mb-4">
                <div class="card h-100">
                    <img src="<c:url value='/products/${product.id}.jpg' />" class="card-img-top" alt="${product.description}">
                    <div class="card-body">
                        <h5 class="card-title">Product ID: ${product.id}</h5>
                        <p class="card-text">${product.description}</p>
                        <p class="card-text">
                            <span class="old-price">Old Price: <fmt:formatNumber value="${product.price}" type="currency" /></span><br/>
                            Discount: <fmt:formatNumber value="${product.discount}" type="percent" /><br/>
                            <span class="new-price">New Price: <fmt:formatNumber value="${product.price * (1 - product.discount)}" type="currency" /></span>
                        </p>
                    </div>
                    <div class="card-footer">
                        <form action="<c:url value='/cart/add' />" method="post">
                            <input type="hidden" name="id" value="${product.id}" />
                            <button type="submit" class="btn btn-primary btn-cart"><i class="bi bi-cart-plus"></i> Add to Cart</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
