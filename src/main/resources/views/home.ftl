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
                <div class="text-center">
                    <h2>Support Our Minecraft Server</h2>
                    <p>Thank you for considering a donation to our Minecraft server! Your support helps us keep the server running and provides a better experience for all players.</p>
                    <img src="img/cat.png" class="img-fluid" alt="Support Us">
                    <p>We accept donations through <strong>PayPal</strong>, ensuring a secure and easy transaction. Your contributions go directly towards server maintenance and upgrades.</p>
                    <p>Please note that purchases may take up to <strong>10 minutes</strong> to process and be applied in-game. We appreciate your patience and understanding.</p>
                    <p><strong>Thank you</strong> for any kind donations. Every bit helps us maintain and improve our Minecraft community!</p>
                </div>
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