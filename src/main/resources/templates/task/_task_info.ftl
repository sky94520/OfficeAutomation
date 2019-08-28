<h3>任务办理-[${task.name}],流程定义ID[${task.processDefinitionId}]</h3>
<div class="row">
    <#--任务到期日-->
    <div class="col">
            <span>
                <i class="mdui-icon material-icons">&#xe916;</i>到期日：
                <span class="due-date"><#if task.dueDate??>${task.dueDate?string("yyyy-mm-dd")}<#else>无到期日</#if></span>
        </span>
        <input type="text" style="display: none;" class="datepicker due-date-input" readonly data-date-format="yyyy-mm-dd">
    </div>
    <#--优先级-->
    <div class="col">
            <span>
                <i class="mdui-icon material-icons">&#xe153;</i>优先级：
                <span class="priority">
                <#if task.priority??>
                    <#if task.priority == 0>低
                    <#elseif task.priority <= 50>中
                    <#elseif task.priority <= 100>高
                    </#if>
                <#else>
                    无优先级
                </#if>
        </span>
        <#assign priorities=[0, 50, 100]>
        <select name="priority" id="priority" style="display: none;width:50px;">
            <#list priorities as priority>
                <#assign selected=(priority == task.priority)?string("selected", "")>
                <option value="${priority}" ${selected}>
                    <#if priority == 0>低
                    <#elseif priority == 50>中
                    <#elseif priority == 100>高
                    </#if>
                </option>
            </#list>
        </select>
        </span>
    </div>
    <#--创建日期-->
    <div class="col">
            <span>
                <i class="mdui-icon material-icons">&#xe916;</i>
                创建日期：${task.createTime?string("yyyy-MM-dd hh:mm:ss")}
            </span>
    </div>
</div>
<br>
<#--人员相关-->
<div class="row">
    <#--拥有人-->
    <div class="col-sm-4">
        <i class="mdui-icon material-icons">&#xe7fd;</i>拥有人：
        <span id="owner">${task.owner!'无'}</span>
        <#assign taskOwner=task.owner!''>
        <select id="ownerSelect" style="display: none;">
            <#list users as user>
                <#assign selected=(taskOwner == user.id)?string("selected", "")>
                <option value="${user.id}" ${selected}>
                    ${user.firstName} ${user.lastName}
                </option>
            </#list>
        </select>
    </div>
    <div class="col-sm-4">
        <i class="mdui-icon material-icons">&#xe7fd;</i>办理人：
        <span id="assignee">${task.assignee!'无'}</span>
    </div>
    <div class="col-sm-4">
        <i class="mdui-icon material-icons">&#xe7fd;</i>任务委派：
        <span id="delegateState">${task.delegationState!'无'}</span>
    </div>
</div>
<hr>
