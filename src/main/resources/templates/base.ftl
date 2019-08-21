<#import "macros.ftl" as macro>

<!DOCTYPE html>
<html lang="en">
<head>
<@block name="head">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title><@block name="title">Base title</@block></title>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="/static/css/bootstrap_v4.min.css">

    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <@block name="styles"></@block>
</@block>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="navbar-header">
        <a class="navbar-brand thumbnail" href="/">Activiti Explorer</a>
    </div>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarColor01" aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarColor01">
        <ul class="navbar-nav mr-auto">
            <@macro.render_nav_item endpoint="/process-list" title="部署流程"></@macro.render_nav_item>
            <@macro.render_nav_item endpoint="/task/list" title="待办任务"></@macro.render_nav_item>
        </ul>
    </div>
</nav>
<main>
    <#if message??>
        <div id="message" class="alert alert-${level!'info'}">${message}</div>
        <!--自动隐藏-->
        <script type="text/javascript">
            setTimeout(function () {
                $('#message').hide('slow');
            }, 5000);
        </script>
    </#if>
    <@block name="main">
    </@block>
</main>

<@block name="scripts">
    <script src="/static/js/bootstrap_v4.min.js"></script>
</@block>
</body>
</html>