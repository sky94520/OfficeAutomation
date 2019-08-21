<@override name="title">Hello</@override>

<@override name="main">
<div class="container">
    <h3>
        启动流程一
        <#if hasStartFormKey>
            [${processDefinition.name}]，版本号:${processDefinition.version}
        <#else>
            [${startFormData.processDefinition.name}]，版本号:${startFormData.processDefinition.version}
        </#if>
    </h3>
    <hr>
    <form action="/process-instance/start/${processDefinitionId}" method="post">
        <#if hasStartFormKey>
            ${startFormData}
        <#else>
            <#list startFormData.formProperties as fp>
                <div class="form-group row">
                    <!--文本或者数字类型-->
                    <#if fp.type.name == "string" || fp.type.name == "long">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value=""/>
                        </div>
                    <!--date-->
                    <#elseif fp.type.name == "date">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}"/>
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
            <button type="submit" class="btn btn-primary">启动流程</button>
        </div>
    </form>
</div>
</@override>
<@extends name="base.ftl"/>