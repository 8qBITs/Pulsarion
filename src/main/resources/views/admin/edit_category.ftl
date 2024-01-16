<!DOCTYPE html>
<html>
<head>
    <title>Admin Area - Edit Category</title>
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
            <h2>Edit Category</h2>
            <!-- Edit Category Form -->
            <form action="/admin/categories/update" method="post">
                <input type="hidden" name="id" value="${category.id}">
                <div class="mb-3">
                    <label for="categoryName" class="form-label">Category Name</label>
                    <input type="text" class="form-control" id="categoryName" name="name" value="${category.name}" required>
                </div>
                <div class="mb-3">
                    <label for="categoryDescription" class="form-label">Description</label>
                    <textarea class="form-control" id="categoryDescription" name="description">${category.description}</textarea>
                </div>
                <button type="submit" class="btn btn-primary"><i class="bi bi-plus-circle-fill"></i> Update Category</button>
            </form>
        </div>
    </div>
</div>

<#include "modules/scripts.ftl">
</body>
</html>