<@override name="title">待处理任务</@override>

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
                        <a class="btn btn-info" href="/task/getform/${task.id}">
                            <i class="mdui-icon material-icons">&#xe7fd;</i>
                            办理
                        </a>
                    <#else>
                        <a class="btn btn-info" href="/task/getform/${task.id}">
                            <i class="mdui-icon material-icons">&#xe417;</i>
                            查看
                        </a>
                        <a class="btn btn-info" href="/task/claim/${task.id}">
                            <i class="mdui-icon material-icons">&#xe3c9;</i>
                            签收
                        </a>
                    </#if>
                </td>
            </tr>
        </#list>
    </table>
</div>
</@override>
<@extends name="/base.ftl"/>