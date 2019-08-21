<@override name="title">已部署流程定义列表</@override>

<@override name="main">
<div class="container">
    <fieldset id="deployFieldset">
        <legend>部署流程资源</legend>
        <span class="alert alert-info"><b>支持文件格式：</b>zip、bar、bpmn、bpmn20.xml</span>
        <form action="/deploy" method="post" enctype="multipart/form-data" style="margin-top:1em;">
            <input type="file" name="file" />
            <input type="submit" value="Submit" class="btn" />
        </form>
        <hr class="soften" />
    </fieldset>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">程定义ID</th>
            <th scope="col">部署ID</th>
            <th scope="col">流程定义名称</th>
            <th scope="col">流程定义KEY</th>
            <th scope="col">版本号</th>
            <th scope="col">XML资源名称</th>
            <th scope="col">图片资源名称</th>
            <th scope="col">操作</th>
        </tr>
        </thead>
        <#list processDefinitionList as pd>
            <tr>
                <td>${pd.id }</td>
                <td>${pd.deploymentId }</td>
                <td>${pd.name }</td>
                <td>${pd.key }</td>
                <td>${pd.version }</td>
                <td><a target="_blank" href="/read-resource?pdid=${pd.id}&resourceName=${pd.resourceName}">bpmn文件</a> </td>
                <#if pd.diagramResourceName??>
                    <td><a target="_blank" href="/read-resource?pdid=${pd.id}&resourceName=${pd.diagramResourceName}">流程图</a> </td>
                <#else>
                    <td>无流程图</td>
                </#if>
                <td>
                    <a target="_blank" class="btn btn-danger" href="/delete-deployment?deploymentId=${pd.deploymentId}">删除</a>
                    <a class="btn btn-primary" href="/getform/start/${pd.id}">启动</a>
                </td>
            </tr>
        </#list>
    </table>
</div>
</@override>
<@extends name="base.ftl"/>
