<@override name="title">Hello</@override>

<@override name="main">
<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th scope="col">任务ID</th>
            <th scope="col">任务名称</th>
            <th scope="col">流程实例ID</th>
            <th scope="col">任务创建时间</th>
            <th scope="col">办理人</th>
            <th scope="col">操作</th>
        </tr>
        </thead>
        <#list tasks as task>
            <tr>
                <td>${task.id}</td>
                <td>${task.name}</td>
                <td>${task.processInstanceId}</td>
                <td>${task.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                <td>${task.assignee!"无"}</td>
                <td>
                    <!--不为空-->
                    <#if task.assignee??>
                        <a class="btn" href="">签收</a>
                    <#else>
                        <a class="btn" href="">办理</a>
                    </#if>
                </td>
            </tr>
        </#list>
    </table>
</div>
</@override>
<@extends name="base.ftl"/>