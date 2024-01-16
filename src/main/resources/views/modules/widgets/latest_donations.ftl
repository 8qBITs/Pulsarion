<!-- Latest Donations Widget -->
<div class="card">
    <div class="card-body">
        <h5 class="card-title">Latest Donations</h5>
        <ul class="list-group list-group-flush">
            <#-- Repeat this block for each donation -->
            <#list purchases as purchase>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    <img src="https://minotar.net/cube/${purchase.username}/100.png" alt="Donor" style="width: 30px; height: 30px; border-radius: 50%;">
                    <span>${purchase.username}</span>
                    <span>$ ${purchase.price}</span>
                </li>
            </#list>
            <!-- Repeat for more donations -->
        </ul>
    </div>
</div>