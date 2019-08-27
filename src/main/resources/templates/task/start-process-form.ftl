<@override name="title">启动流程</@override>

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
                    <#if fp.type.name == "string" || fp.type.name == "long" || fp.type.name == "double">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value=""/>
                        </div>
                    <!--大文本-->
                    <#elseif fp.type.name == "bigtext">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" id="${fp.id}" rows="5" name="${fp.id}" data-type="${fp.type.name}"></textarea>
                        </div>
                    <!--date-->
                    <#elseif fp.type.name == "date">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}"/>
                        </div>
                    <!--javascript-->
                    <#elseif fp.type.name == "javascript">
                        <script type="text/javascript">${fp.value}</script>
                    <!--users-->
                    <#elseif fp.type.name == "users">
                        <label class="col-sm-2 col-form-label" for="${fp.id}">${fp.name}</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control users" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" readonly/>
                        </div>
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
    <div id="userModal" class="modal hide fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5>选择人员</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                </div>
                <div class="modal-body">
                    <select multiple="multiple" class="form-control"></select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" data-miss="modal" aria-hidden="true">关闭</button>
                    <button type="button" class="btn btn-primary ok">确定</button>
                </div>
            </div>
        </div>
    </div>
</div>
</@override>

<@override name="scripts">
    <script src="/static/js/bootstrap_v4.min.js"></script>
    <script type="text/javascript" src="/static/js/start_process_form.js"></script>
</@override>
<@extends name="/base.ftl"/>