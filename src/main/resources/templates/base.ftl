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
    <link href="https://cdn.bootcss.com/mdui/0.4.2/css/mdui.min.css" rel="stylesheet">

    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <@block name="styles">
    </@block>
    <style>
        .hidden{
            display: none;
        }
        .alert{
            text-align: center;
        }
        #div1{
            float:left;
        }
        #div2{
            float:right
        }
    </style>
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
        <#if user??>
            <ul class="navbar-nav mr-auto">
                <@macro.render_nav_item endpoint="/process-list" title="部署流程"></@macro.render_nav_item>
                <li>
                    <a class="nav-item nav-link <#if "/task/list" == request.getRequestUri()>active</#if>"
                       href="/task/list">
                        待办任务
                        <#if pending_task??>
                            <span class="badge" style="background-color: red;color:white">${pending_task}</span>
                        </#if>
                    </a>
                </li>
            </ul>
            <!--操作-->
            <ul class="navbar-nav ml-auto">
                <li class="nav-item dropdown">
                    <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" role="button"
                       aria-haspopup="true" aria-expanded="false">
                        ${user.firstName}&nbsp;${user.lastName}
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" role="menu">
                        <a class="dropdown-item" href="/logout">
                            <i class="mdui-icon material-icons">&#xe8ac;</i> 注销
                        </a>
                    </div>
                </li>
            </ul>
        <#else>
            <ul class="navbar-nav ml-auto">
                <li class="clearfix">
                    <a class="nav-item nav-link active float-right" href="/login">
                        <i class="mdui-icon material-icons">&#xe7fd;</i>
                    </a>
                </li>
            </ul>
        </#if>
    </div>
</nav>

<div id="alert-box-success" class="alert alert-success hidden">
    <strong><span class="alert-message">操作成功</span></strong>
</div>
<div id="alert-box-danger" class="alert alert-danger hidden">
    <strong><span class="alert-message">操作失败，请稍后再试</span></strong>
</div>

<main>
    <#if message??>
        <div id="message" class="alert alert-${level!'info'} text-center">${message}</div>
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


<script src="/static/js/bootstrap_v4.min.js"></script>

<script>
    /**
     * 显示/隐藏提示框
     * @param {boolean} isSuccess
     * @param {string} message 用于显示的消息
     */
    function toggle_alert(isSuccess, message){
        var alert_success = $("#alert-box-success");
        var alert_error = $("#alert-box-danger");
        // 显示操作成功的提示框
        if(isSuccess){
            alert_error.hide();
            if(message){
                alert_success.find('.alert-message').text(message);
            }
            alert_success.show(1000);
            setTimeout(function () {
                alert_success.hide(2000);
            }, 2000);
        }else{
            alert_success.hide();
            if(message){
                alert_error.find('.alert-message').text(message);
            }
            alert_error.show(1000);
            setTimeout(function () {
                alert_error.hide(2000);
            }, 2000);
        }
    }
</script>

<@block name="scripts">
</@block>

</body>
</html>