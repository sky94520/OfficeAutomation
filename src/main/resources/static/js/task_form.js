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
    });
}
/**
 * 改变任务的属性
 * @param $element
 * @param key 要修改的键名
 * @param callback 发送成功的回调函数
 */
function changeTaskProperty($element, key, callback) {
    var value = $element.val();
    if (value) {
        $element.hide();
        //发送post请求
        $.ajax({
            url: '/task/property/' + taskId,
            data: {
                propertyName: key,
                value: value
            },
            method: 'POST'
        }).done(callback);
    }
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

//------------------------更改日期优先级---------------------------
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
var $dueDate = $('.due-date-input');
$dueDate.blur(function () {
    changeTaskProperty($dueDate, "dueDate", function () {
        toggle_alert(true, "到期日修改成功");
        $('.due-date').show().text($dueDate.val());
    })
});
//------------------------更改任务优先级---------------------------
$('.priority').click(function () {
    $(this).hide();
    $('#priority').show();
});
var $priority = $('#priority');
$priority.change(function () {
    changeTaskProperty($priority, "priority", function () {
        toggle_alert(true, "优先级修改成功");
        $('.priority').show().text($('option:selected', $priority).text());
    })
});
//------------------------------拥有人---------------------------
$('#owner').click(function () {
    $(this).hide();
    $('#ownerSelect').show();
});
//更改拥有人
var $ownerSelect = $('#ownerSelect');
$ownerSelect.change(function () {
    changeTaskProperty($ownerSelect, "owner", function () {
        toggle_alert(true, "拥有人修改成功");
        $('#owner').show().text($('option:selected', $ownerSelect).val());
    })
});
