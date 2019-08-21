<#macro render_nav_item endpoint title>
    <li>
        <a class="nav-item nav-link
        <#if endpoint == request.getRequestUri()>
        active
        </#if>" href="${endpoint}">${title}</a>
    </li>
</#macro>
