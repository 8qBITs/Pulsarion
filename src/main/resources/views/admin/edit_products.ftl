<!DOCTYPE html>
<html>
<head>
    <title>Admin Area - Edit Product</title>
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
            <h2>Edit Product</h2>
            <!-- Edit Product Form -->
            <form action="/admin/products/update" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${product.id}">

                <div class="mb-3">
                    <label for="productName" class="form-label">Product Name</label>
                    <input type="text" class="form-control" id="productName" name="name" value="${product.name}" required>
                </div>

                <div class="mb-3">
                    <label for="productDescription" class="form-label">Description</label>
                    <textarea class="form-control" id="productDescription" name="description">${product.description}</textarea>
                </div>

                <div class="mb-3">
                    <label for="productPrice" class="form-label">Price</label>
                    <input type="number" step="0.01" class="form-control" id="productPrice" name="price" value="${product.price}" required>
                </div>

                <div class="mb-3">
                    <label for="productCategory" class="form-label">Category</label>
                    <select class="form-select" id="productCategory" name="category">
                        <#list categories as category>
                            <option value="${category.id}" <#if product.category == category.id>selected</#if>>${category.name}</option>
                        </#list>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="productCommand" class="form-label">Command</label>
                    <label for="productCommand" class="form-label" style="color:red">Available variables: {player}</label>
                    <input type="text" class="form-control" id="productCommand" name="command" value="${product.command}" required>
                </div>

                <div class="mb-3">
                    <label for="productFeatured" class="form-label">Featured Product</label>
                    <select class="form-select" id="productFeatured" name="featured">
                        <option value="true" <#if product.featured>selected</#if>>True</option>
                        <option value="false" <#if !product.featured>selected</#if>>False</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="productImage" class="form-label">Product Image</label>
                    <br>
                    <!-- Display current product image -->
                    <img src="/img/${product.imageUrl}" alt="${product.name}" style="max-width: 200px; max-height: 200px;">
                    <br><br>
                    <!-- Input for new image upload -->
                    <input type="file" class="form-control" id="productImage" name="image">
                    <small class="form-text text-muted">Upload a new image to change the current one.</small>
                </div>

                <button type="submit" class="btn btn-primary"><i class="bi bi-plus-circle-fill"></i> Update Product</button>
            </form>
        </div>
    </div>
</div>

<#include "modules/scripts.ftl">
</body>
</html>
