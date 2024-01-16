<!DOCTYPE html>
<html>
<head>
    <title>Admin Area - Categories</title>
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
            <h2>Manage Categories</h2>

            </br>

            <!-- Trigger Modal Button -->
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addCategoryModal">
                <i class="bi bi-plus-circle-fill"></i> Add Category
            </button>

            <!-- Add Category Modal -->
            <div class="modal fade" id="addCategoryModal" tabindex="-1" aria-labelledby="addCategoryModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="addCategoryModalLabel">Add Category</h5>
                            <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <!-- Add Category Form -->
                            <form action="/admin/categories/add" method="post">
                                <div class="mb-3">
                                    <label for="categoryName" class="form-label">Category Name</label>
                                    <input type="text" class="form-control" id="categoryName" name="name" required>
                                </div>
                                <div class="mb-3">
                                    <label for="categoryDescription" class="form-label">Description</label>
                                    <textarea class="form-control" id="categoryDescription" name="description"></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary"><i class="bi bi-plus-circle-fill"></i> Add Category</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <hr>

            <!-- Categories List -->
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <#-- Iterate over categories -->
                <#list categories as category>
                    <tr>
                        <td>${category.id}</td>
                        <td>${category.name}</td>
                        <td>
                            <a href="/admin/categories/edit?id=${category.id}" class="btn btn-sm btn-info"><i class="bi bi-pencil-fill"></i> Edit</a>
                            <form action="/admin/categories/delete" method="post" style="display: inline;">
                                <input type="hidden" name="id" value="${category.id}">
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