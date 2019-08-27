<@override name="title">任务办理</@override>
<@override name="styles">
    <link rel="stylesheet" href="/static/css/datepicker.css">
</@override>

<@override name="main">
<div class="container">
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
                <select name="priority" id="priority" style="display: none;width:50px;">
                    <option value="0">低</option>
                    <option value="50">中</option>
                    <option value="100">高</option>
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
    <hr>
    <form action="/task/complete/${taskId}" method="post">
        <input hidden id="taskId" value="${taskId}">
        <#if hasFormKey??>
            <input hidden id="processInstanceId" value="${task.processInstanceId}">
            ${taskFormData}
        <#else>
            <input hidden id="processInstanceId" value="${taskFormData.task.processInstanceId}">
            <#list taskFormData.formProperties as fp>
                <#assign disabled=fp.writable?string("", "disabled")>
                <#assign readonly=fp.writable?string("", "readonly")>
                <#assign required=fp.required?string("required", "")>
                <div class="form-group row">
                    <#--文本或者数字类型-->
                    <#if fp.type.name == "string" || fp.type.name == "long" || fp.type.name == "double">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="${fp.id}" name="${fp.id}"
                                   data-type="${fp.type.name}" value="${fp.value!''}" ${required} ${readonly}/>
                        </div>
                    <#--date-->
                    <#elseif fp.type.name == "date">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="${fp.id}" name="${fp.id}"
                                   data-type="${fp.type.name}" value="${fp.value!''}" ${required} ${readonly}/>
                        </div>
                    <#--大文本-->
                    <#elseif fp.type.name == "bigtext">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" id="${fp.id}" rows="5" name="${fp.id}"
                                      data-type="${fp.type.name}" ${required} ${readonly}>${fp.value}</textarea>
                        </div>
                    <#--下拉框-->
                    <#elseif fp.type.name == 'enum'>
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <select name="${fp.id}" id="${fp.id}" class="form-control" ${disabled} ${required}>
                                <#list fp.type.getInformation("values") as key, value>
                                    <option value="${key}">${value}</option>
                                </#list>
                            </select>
                        </div>
                    <#elseif fp.type.name == "javascript">
                        <script type="text/javascript">${fp.value}</script>
                    </#if>
                </div>
            </#list>
        </#if>
        <#--按钮-->
        <div class="form-group" style="text-align: center">
            <a href="" class="btn btn-info">返回列表</a>
            <#if task.assignee??>
                <button type="submit" class="btn btn-primary">完成任务</button>
            <#else>
                <a class="btn btn-primary" href="/task/claim/${task.id}?nextDo=handle">签收</a>
            </#if>
        </div>
    </form>
    <hr>
    <div class="row">
        <#--添加意见-->
        <div class="col-sm-6">
            <fieldset id="commentContainer">
                <legend>添加意见</legend>
                <textarea id="comment" class="form-control" rows="3"></textarea>
                <button id="saveComment" type="button" class="btn btn-outline-primary">保存意见</button>
            </fieldset>
        </div>
        <div class="col-sm-6">
            <fieldset id="commentList">
                <legend>意见列表</legend>
                <ol></ol>
            </fieldset>
        </div>
    </div>
</div>
</@override>
<@override name="scripts">
    <script type="text/javascript" src="/static/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="/static/js/task_form.js"></script>
</@override>

<@extends name="/base.ftl"/>