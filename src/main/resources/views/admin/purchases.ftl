<!DOCTYPE html>
<html>
<head>
    <title>Admin Area - Purchases</title>
    <!-- Bootstrap CSS -->
    <#include "modules/head.ftl">
</head>
<body>
<div class="container">
    <!-- Navbar -->
    <#include "modules/navbar.ftl">

    <div class="row flex-nowrap">
        <#include "modules/vertical_navbar.ftl">

        <!-- Main Area -->
        <div class="col-md-10" style="padding-top: 15px">
            <h2>Manage Purchases</h2>
            <!-- Add Purchase Form -->

            </br>

            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addPurchaseModal">
                <i class="bi bi-plus-circle-fill"></i> Add Purchase
            </button>

            <!-- Add Purchase Modal -->
            <div class="modal fade" id="addPurchaseModal" tabindex="-1" aria-labelledby="addPurchaseModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="addPurchaseModalLabel">Add Purchase</h5>
                            <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <!-- Add Purchase Form -->
                            <form action="/admin/purchases/add" method="post">
                                <div class="mb-3">
                                    <label for="purchaseEmail" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="purchaseEmail" name="email" required>
                                </div>
                                <div class="mb-3">
                                    <label for="purchaseProduct" class="form-label">Product</label>
                                    <select class="form-control" id="purchaseProduct" name="product" required>
                                        <#-- Iterate over products to create dropdown options -->
                                        <#list products as product>
                                            <option value="${product.id}">${product.name}</option>
                                        </#list>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="purchaseUsername" class="form-label">Username</label>
                                    <input type="text" class="form-control" id="purchaseUsername" name="username" required>
                                </div>
                                <div class="mb-3">
                                    <label for="purchasePrice" class="form-label">Price</label>
                                    <input type="number" step="0.01" class="form-control" id="purchasePrice" name="price" required>
                                </div>
                                <button type="submit" class="btn btn-primary"><i class="bi bi-plus-circle-fill"></i> Add Purchase</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Purchases List -->
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Product ID</th>
                    <th>User ID</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <#-- Iterate over purchases -->
                <#list purchases as purchase>
                    <tr>
                        <td>${purchase.id}</td>
                        <td>${purchase.product}</td> <!-- Corrected from purchase.productId to purchase.product -->
                        <td>${purchase.username}</td> <!-- Assuming you have a username field -->
                        <td>

                            <a href="/admin/purchases/complete?id=${purchase.id}" class="btn btn-sm btn-info"><i class="bi bi-play-circle-fill"></i> Run Action</a>
                            <form action="/admin/purchases/delete" method="post" style="display: inline;">
                                <input type="hidden" name="id" value="${purchase.id}">
                                <button type="submit" class="btn btn-sm btn-danger"><i class="bi bi-x-circle-fill"></i> Delete</button>
                            </form>
                        </td>
                    </tr>
                </#list>
                </tbody>

            </table>
        </div>
    </div>

</div>
<#include "modules/scripts.ftl">
</body>
</html>

