<!-- Vertical Navigation -->
<div class="col-md-2" style="padding-top: 15px;">
    <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
        <a class="nav-link <#if currentPage == 'home'>active</#if>" href="/admin"><i class="bi bi-house-fill"></i> Home</a>
        <a class="nav-link <#if currentPage == 'categories'>active</#if>" href="/admin/categories"><i class="bi bi-bookmark-fill"></i> Categories</a>
        <a class="nav-link <#if currentPage == 'products'>active</#if>" href="/admin/products"><i class="bi bi-box-fill"></i> Products</a>
        <a class="nav-link <#if currentPage == 'coupons'>active</#if> disabled" href="/admin/coupons"><i class="bi bi-ticket-perforated-fill"></i> Coupons</a>
        <a class="nav-link <#if currentPage == 'purchases'>active</#if>" href="/admin/purchases"><i class="bi bi-bag-check-fill"></i> Purchases</a>
    </div>
</div>