//请求数据
$.getJSON("/user/list", function (data) {
    console.log(data);
    $('#userModal select').html('');
    for (var key in data) {
        var users = data[key];
        var $opt = $('<optgroup/>', {
            label: key
        }).appendTo('#userModal select');
        for (var i in users) {
            var user = users[i];
            $('<option/>', {
                value: user.id,
                text: user.firstName + ' ' + user.lastName + '(' + user.id + ')'
            }).appendTo($opt);
        }
    }
});
//点击users回调函数
$('.users').on('click', function () {
    $('body').data('usersEle', this);
    $('#userModal').modal('show');
})
$('#userModal .ok').click(function () {
    var users = new Array();
    $('#userModal select option:selected').each(function () {
        users.push($(this).val());
    });
    $('#users').val(users);
    $('#userModal').modal('hide');
});
