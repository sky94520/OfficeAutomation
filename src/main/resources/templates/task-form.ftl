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
        <#if hasFormKey??>
            ${taskFormData}
        <#else>
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
</div>
</@override>
<@extends name="base.ftl"/>