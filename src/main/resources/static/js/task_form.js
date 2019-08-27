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
