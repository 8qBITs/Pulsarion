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
        <!-- Main Content -->
        <div class="col-md-9" style="padding-top: 10px; padding-left: 10px; padding-right: 10px;">
            <main>
                <!-- Form for Email and Minecraft Username -->
                <form action="/order" method="post">
                    <div class="mb-3">
                        <label for="email" class="form-label">Email address</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label for="minecraftUsername" class="form-label">Minecraft Username</label>
                        <input type="text" class="form-control" id="minecraftUsername" name="minecraftUsername" required>
                    </div>
                    <input type="hidden" type="number" name="product_id" value="${product_id}"> <!-- Replace 'presetValue' with your actual product ID -->

                    <!-- Terms of Service Agreement Text -->
                    <p class="terms-of-service">
                        By clicking 'Purchase', you agree to our
                        <a href="/tos" target="_blank">Terms of Service</a> and acknowledge that your donation is voluntary.
                    </p>

                    <button type="submit" class="btn btn-primary">Purchase</button>
                </form>
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