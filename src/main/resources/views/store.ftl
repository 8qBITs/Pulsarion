<!DOCTYPE html>
<html>
<head>
    <title>Home Page</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>
<div class="container mt-4 d-flex flex-column min-vh-100">
    <#include "modules/header.ftl">
    </br>
    <div class="row">
        <!-- Category Navigation -->
        <div class="col-12 mb-3">
            <ul class="nav nav-tabs">
                <#list categories as category>
                    <li class="nav-item">
                        <a class="nav-link <#if view?? && (view?string == category.id?string)>active</#if>" style="color: black; text-transform: uppercase; font-weight: bold; padding: 10px 20px; font-size: 1.2em;" href="/store?view=${category.id}">${category.name}</a>
                    </li>
                </#list>
            </ul>
        </div>

        <!-- Main Content -->
        <div class="col-md-9">
            <main>
                <#if products?? && category??>
                    <#if products?size == 0>
                        <div class="alert alert-warning" role="alert">
                            There are no products in this category.
                        </div>
                    <#else>
                        <div class="row">
                            <h2>${category.name}</h2>
                            <p>${category.description}</p>
                            <hr>
                            <#list products as product>
                                <div class="col-md-4 mb-4">
                                    <div class="card">
                                        <img src="/img/${product.imageUrl}" class="card-img-top" alt="${product.name}" width="250" height="250">
                                        <hr class="m-0">
                                        <div class="card-body d-flex">
                                            <div class="flex-grow-1">
                                                <h5 class="card-title font-weight-bold">${product.name}</h5>
                                                <p class="mb-2">Price: $${product.price}</p>
                                            </div>
                                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#productModal${product.id}">
                                                More info
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <!-- Modal for Product -->
                                <div class="modal fade" id="productModal${product.id}" tabindex="-1" role="dialog" aria-labelledby="productModalLabel${product.id}" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="productModalLabel${product.id}">${product.name}</h5>
                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                ${product.description}
                                            </div>
                                            <div class="modal-footer">
                                                <hr>
                                                <form action="/order?pid=${product.id}" method="get">
                                                    <input type="hidden" name="id" value="${product.id}">
                                                    <button type="submit" class="btn btn-primary">Purchase</button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </#if>
                <#else>
                    <div class="alert alert-info" role="alert">
                        Please select a category to view specific products.
                    </div>

                    <!-- Display Featured Products -->
                    <#if featured?? && (featured?size > 0)>
                        <div class="row">
                            <h2>Featured Products</h2>
                            <hr>
                            <#list featured as product>
                                <div class="col-md-4 mb-4">
                                    <div class="card">
                                        <img src="/img/${product.imageUrl}" class="card-img-top" alt="${product.name}" width="250" height="250">
                                        <hr class="m-0">
                                        <div class="card-body d-flex">
                                            <div class="flex-grow-1">
                                                <h5 class="card-title font-weight-bold">${product.name}</h5>
                                                <p class="mb-2">Price: $${product.price}</p>
                                            </div>
                                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#productModal${product.id}">
                                                More info
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <!-- Modal for Product -->
                                <div class="modal fade" id="productModal${product.id}" tabindex="-1" role="dialog" aria-labelledby="productModalLabel${product.id}" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="productModalLabel${product.id}">${product.name}</h5>
                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                ${product.description}
                                            </div>
                                            <div class="modal-footer">
                                                <hr>
                                                <form action="/order" method="get">
                                                    <input type="hidden" name="id" value="${product.id}">
                                                    <button type="submit" class="btn btn-primary">Purchase</button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <div class="alert alert-info" role="alert">
                            No featured products available.
                        </div>
                    </#if>
                </#if>
            </main>
        </div>

        <!-- Sidebar Widgets -->
        <div class="col-md-3">
            <#include "modules/widgets/donation_goal.ftl">
            <#include "modules/widgets/top_donator.ftl">
            <#include "modules/widgets/latest_donations.ftl">
        </div>
    </div>
    <#include "modules/footer.ftl">
</div>
<#include "modules/scripts.ftl">
</body>
</html>