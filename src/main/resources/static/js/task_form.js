/**
 * 获取所有的评论，并添加到ol标签之中
 */
function readComments() {
    $('#commentList ol').html('');
    //读取意见
    $.getJSON("/comment/list/" + processInstanceId, function (data) {
        for (var i = 0; i < data.comments.length; i++) {
            var value = data.comments[i];
            $('<li/>', {
                html: function () {
                    var content = value.fullMessage;
                    content += "<span style='margin-left:1em;'></span>" + value.userId + "(" + data.taskNames[value.taskId] + ")";
                    content += "<span style='margin-left: 1em;'></span>" + new Date(value.time).toLocaleString();

                    return content;
                }
            }).appendTo("#commentList ol");
        }
        console.log(data);
    });
}
//保存意见
$('#saveComment').click(function () {
    if (!$('#comment').val()) {
        return false;
    }
    $.ajax({
        url: '/comment/save',
        data: {
            taskId: taskId,
            processInstanceId: processInstanceId,
            message: $('#comment').val()
        },
        method: 'POST'
    }).done(function () {
        toggle_alert(true, "添加评论成功");
        //显示
        readComments();
    });
});

var processInstanceId = $('#processInstanceId').val();
var taskId = $('#taskId').val();
readComments();

//单击到期日属性可以编辑
$('.due-date').click(function () {
    $(this).hide();
    $('.due-date-input').show();
});
//点击时出现日期选择
$('.datepicker').datepicker({
    locale: 'zh-cn'
});
//更改到期日
$('.due-date-input').blur(function () {
    var ele = this;
    var value = $(this).val();
    if (value) {
        $(ele).hide();
        //发送post请求修改日期
        $.ajax({
            url: '/task/property/' + taskId,
            data: {
                propertyName: 'dueDate',
                value: value
            },
            method: 'POST'
        }).done(function () {
            toggle_alert(true, "截至日期修改成功");
            $('.due-date').show().text(value);
        });
    }
});

//更改任务优先级
$('.priority').click(function () {
    $(this).hide();
    $('#priority').show();
});
//更改任务优先级
$('#priority').change(function () {
    var ele = this;
    var value = $(this).val();
    if (value) {
        $(ele).hide();
        //发送post请求
        $.ajax({
            url: '/task/property/' + taskId,
            data: {
                propertyName: 'priority',
                value: value
            },
            method: 'POST'
        }).done(function () {
            toggle_alert(true, "优先级修改成功");
            $('.priority').show().text($('option:selected', ele).text());
        });
    }
});