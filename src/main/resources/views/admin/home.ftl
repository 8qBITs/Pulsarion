<!DOCTYPE html>
<html>
<head>
    <title>Admin Area</title>
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
        <div class="col-md-10" style="padding-top: 10px">
            <div class="row" style="padding-bottom: 15px;">
                <!-- Total Earnings Card -->
                <div class="col-md-6">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">Total Earnings</h5>
                            <p class="card-text">$ ${totalEarnings}</p>
                        </div>
                    </div>
                </div>
                <!-- Earnings This Month Card -->
                <div class="col-md-6">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">Earnings This Month</h5>
                            <p class="card-text"></i> $ ${totalEarningsThisMonth}</p>
                        </div>
                    </div>
                </div>

            </div>
            <!-- Second Row for Statistics Cards -->
            <div class="row" style="padding-bottom: 15px;">
                <!-- Purchases Count Card -->
                <div class="col-md-6">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">Total Purchases</h5>
                            <p class="card-text">${purchaseCount}</p>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">Monthly Goal $ ${donationGoal}</h5>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar" style="width: ${donationGoalPercent}%;" aria-valuenow="${donationGoalPercent}" aria-valuemin="0" aria-valuemax="100">${donations}%</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="alert alert-success d-flex align-items-center" style="padding-bottom: 15px" role="alert">
                <i class="bi bi-check-circle-fill"></i>
                <div>
                    Your Pulsarion installation is current and up to date!
                </div>
            </div>

            <!-- Blog Section -->
            <div class="row mt-4">
                <div class="col-12">
                    <h3><i class="bi bi-award-fill"></i> Developer News:</h3>
                    <!-- Example Blog Post 1 -->
                    <div class="card mb-3">
                        <div class="card-body">
                            <h5 class="card-title">Blog Post Title 1</h5>
                            <h6 class="card-subtitle mb-2 text-muted">3 days ago</h6>
                            <p class="card-text">This is the full content of the blog post. It can be as long or as short as needed, and it's displayed directly here...</p>
                        </div>
                    </div>
                    <!-- Additional blog posts can be added in a similar structure -->
                </div>
            </div>
        </div>
    </div>
</div>
<#include "modules/scripts.ftl">
</body>
</html>