<!DOCTYPE html>
<html>
<head>
    <title>Admin Area - Products</title>
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
            <h2>Manage Products</h2>

            </br>

            <!-- Trigger Modal Button for Add Product -->
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addProductModal">
                <i class="bi bi-plus-circle-fill"></i> Add Product
            </button>

            <!-- Add Product Modal -->
            <div class="modal fade" id="addProductModal" tabindex="-1" aria-labelledby="addProductModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="addProductModalLabel">Add Product</h5>
                            <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <!-- Add Product Form -->
                            <form action="/admin/products/add" method="post" enctype="multipart/form-data">
                                <div class="mb-3">
                                    <label for="productName" class="form-label">Product Name</label>
                                    <input type="text" class="form-control" id="productName" name="name" required>
                                </div>
                                <div class="mb-3">
                                    <label for="productDescription" class="form-label">Description</label>
                                    <textarea class="form-control" id="productDescription" name="description"></textarea>
                                </div>
                                <div class="mb-3">
                                    <label for="productPrice" class="form-label">Price</label>
                                    <input type="number" step="0.01" class="form-control" id="productPrice" name="price" required>
                                </div>
                                <div class="mb-3">
                                    <label for="productCategory" class="form-label">Category</label>
                                    <select class="form-select" id="productCategory" name="category">
                                        <#list categories as category>
                                            <option value="${category.id}">${category.name}</option>
                                        </#list>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="productCommand" class="form-label">Command</label>
                                    <label for="productCommand" class="form-label" style="color:red">Available variables: {player}</label>
                                    <input type="text" class="form-control" id="productCommand" name="command">
                                </div>

                                <div class="mb-3">
                                    <label for="productFeatured" class="form-label">Featured Product</label>
                                    <select class="form-select" id="productFeatured" name="featured">
                                        <option value="true">True</option>
                                        <option value="false" selected>False</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="productImage" class="form-label">Product Image</label>
                                    <input type="file" class="form-control" id="productImage" name="image">
                                </div>

                                <button type="submit" class="btn btn-primary"><i class="bi bi-plus-circle-fill"></i> Add Product</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <hr>

            <!-- Products List -->
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Category</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <#-- Iterate over products -->
                <#list products as product>
                    <tr>
                        <td>${product.id}</td>
                        <td>${product.name}</td>
                        <td>$ ${product.price}</td>
                        <td>
                            <#-- Find and display the category name -->
                            <#list categories as category>
                                <#if category.id == product.category>
                                    ${category.name}
                                </#if>
                            </#list>
                        </td>
                        <td>
                            <a href="/admin/products/edit?id=${product.id}" class="btn btn-sm btn-info"><i class="bi bi-pencil-fill"></i> Edit</a>
                            <form action="/admin/products/delete" method="post" style="display: inline;">
                                <input type="hidden" name="id" value="${product.id}">
                                <button type="submit" class="btn btn-sm btn-danger"><i class="bi bi-x-circle-fill"></i> Delete</button>
                            </form>
                            <#-- Check if product is featured -->
                            <#if product.featured>
                                <span class="badge bg-success">Featured</span>
                            </#if>
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