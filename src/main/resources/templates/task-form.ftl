<@override name="title">任务办理</@override>

<@override name="main">
<div class="container">
    <h3>
        <#if hasFormKey??>
            任务办理-[${task.name}],流程定义ID[${task.processDefinitionId}]
        <#else>
            任务办理-[${taskFormData.task.name}],流程定义ID[${taskFormData.task.processDefinitionId}]
        </#if>
    </h3>
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
                    <!--文本或者数字类型-->
                    <#if fp.type.name == "string" || fp.type.name == "long">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="${fp.id}" name="${fp.id}"
                                   data-type="${fp.type.name}" value="${fp.value}" ${required} ${readonly}/>
                        </div>
                    <!--date-->
                    <#elseif fp.type.name == "date">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="${fp.id}" name="${fp.id}"
                                   data-type="${fp.type.name}" value="${fp.value!''}" ${required} ${readonly}/>
                        </div>
                    <!--下拉框-->
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
        <!--按钮-->
        <div class="form-group" style="text-align: center">
            <a href="" class="btn btn-info">返回列表</a>
            <button type="submit" class="btn btn-primary">完成任务</button>
        </div>
    </form>
    <hr>
    <div class="row">
        <!--添加意见-->
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
    <script type="text/javascript" src="/static/js/task_form.js"></script>
</@override>

<@extends name="base.ftl"/>