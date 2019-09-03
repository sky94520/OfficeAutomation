
function myFunction() {
    //搜索表格中的用户
    // 声明变量
    var input, filter, table, tr, td, i;
    input = document.getElementById("myInput");
    filter = input.value;
    table = document.getElementById("myTable");
    tr = table.getElementsByTagName("tr");
    // 循环表格每一行，查找匹配项
    for (i = 1; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0].innerHTML + tr[i].getElementsByTagName("td")[1].innerHTML +tr[i].getElementsByTagName("td")[2].innerHTML+tr[i].getElementsByTagName("td")[3].innerHTML+tr[i].getElementsByTagName("td")[4].innerHTML;
        if (td) {
            if (td.indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}
function add_group() {
    //增加新的用户组
    //获取表单中的数据
    let id = $("#id").val();
    let name = $("#name").val();
    let type = $("#type").val();
    //对某些信息做出限制
    if(name==""){
        toggle_alert(false, "用户名不能为空！");
        return false;
    }
    if(id==""){
        toggle_alert(false,"ID不能为空！");
        return false;
    }
    if(type==""){
        toggle_alert(false, "类型不能为空！");
        return false;
    }

    let data = {"id":id, "name":name,"type": type};
    $.ajax({
        type: "post",
        url: "add-group",
        data: data,
        dataType: "json",
        success: function (response) {
            toggle_alert(response.success, response.message);
            $("#id").val("");
            $("#name").val("");
            $("#type").val("");
        },
        error: function(response){
            toggle_alert(response.success, response.message);
        }
    });
}

function update_user() {
    //更新用户信息
    //获取表单中的消息和用户id
    let id = $("#id").val();
    let name = $("#name").val();
    let tel_number = $("#tel_number").val();
    let email = $("#email").val();
    let school = $("#school").val();
    var  myselect=document.getElementById("type");
    var index=myselect.selectedIndex ;
    var type = myselect.options[index].text;
    //对某些信息做出限制
    if(name==""){
        toggle_alert("False", "", "用户名不能为空！");
        return false;
    }
    if(tel_number==""){
        toggle_alert("False", "", "联系电话不能为空！");
        return false;
    }
    var re = /^1\d{10}$/;
    if (!re.test(tel_number)) {
        toggle_alert("False", "", "请输入正确的联系号码！");
        return false;
    }
    if(email==""){
        toggle_alert("False", "", "用户邮箱不能为空！");
        return false;
    }
    var re =  /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    if(!re.test(email)){
        toggle_alert("False", "", "请输入正确的邮箱！");
        return false;
    }

    let data = {"id":id, "name": name, "tel_number": tel_number, "email": email, "school": school, "type": type}
    $.ajax({
        type: "post",
        url: "/update_user",
        data: data,
        dataType: "json",
        success: function (response) {
            toggle_alert(response.success,response.message);
            $("#name").val("");
            $("#tel_number").val("");
            $("#email").val("");
            $("#school").val("");
        },
        error: function(response){
            toggle_alert(response.success, "", response.message);
        }
    });
}

/**
 * 将用户的数据从左边的table中填写到右边的form表单中
 * @param e
 */
function edit(e) {
    let tds = e.parent().siblings();
    let id = tds[0].innerText;
    let name = tds[1].innerText;
    let type = tds[2].innerText;

    $("#id").val(id);
    $("#name").val(name);
    $("#type").val(type);

}
/**
 取得这一行的用户组ID，从而查询到这个群组中有哪些User，将User信息取出，列在模态框表格里
 */
function getMembers(e) {
    let tds = e.siblings();
    let id =  tds[0].innerText;
    let data = {"id":id};
    $.ajax({
        type:"post",
        url:"/get-member",
        data:data,
        dataType:"json",
        success:function (response) {
            getMembersModel(response);
        },
        error:function (response){
            toggle_alert(false,"获取成员信息失败");
        }
    })

}

/**
 * //将getMembers中获取到的信息填充到模态框中
 * @param membersInfo 获取到的群内成员信息
 */
function getMembersModel(membersInfo) {
    $(".modal-title").text("群组内成员");
    //清除模态框中原有内容
    $("#modelTbody").empty();
    //将成员信息填充到模态框里
    let id;
    for(id in membersInfo){
        $("#modelTbody").append("<tr><td></td><td>"+id+"</td><td>"+membersInfo[id]["firstName"]+"</td><td>"+membersInfo[id]["lastName"]+"</td><td>"+membersInfo[id]["email"]+"</td></tr>");
    }
}

/**
 * 实现模态框中左边元素放在右边，右边换到左边
 * @param obj1
 * @param obj2
 */
function moveOption(obj1, obj2)
{
    for(let i = obj1.options.length - 1 ; i >= 0 ; i--)
    {
        if(obj1.options[i].selected)
        {
            let opt = new Option(obj1.options[i].text,obj1.options[i].value);
            opt.selected = true;
            obj2.options.add(opt);
            obj1.remove(i);
        }
    }
}

/**
 * 将在用户组内的成员和不在用户组的成员ID分别填在对应的位置上
 */
function modifyGetMembers() {
    let id = $("#id").val();
    let data = {"id": id};
    $.ajax({
            url: "/modify-get-members",
            type: "POST",
            data: data,
            dataType: "json",
            success: function (response) {
                $("#left").empty();
                $("#right").empty();
                $(".modal-title").text("修改群组内成员");
                for(let id in response["exit"]){
                    $("#left").append("<option value=\"20\">"+id+"</option>");
                }
                for(let id in response["noExit"]){
                    $("#right").append("<option value=\"20\">"+id+"</option>");
                }
            },
            error: function (response) {
                toggle_alert(false, "获取成员信息失败");
            }

        }
    );
}

/**
 * 更新用户组中的用户
 */
function modifyMembersInfo() {
    let options = $("#left").children();
    let idStr = "";
    for(let i=0;i<options.length;i++){
        if(i==0){
            idStr += options[i].text;
        }else{
            idStr += (" "+options[i].text);
        }

    }
    let groupId = $("#id").val();
    let data = {idList:idStr,"groupId":groupId};
    console.log(data);
    $.ajax({
        type:"POST",
        url:"/modify-members",
        data:data,
        dataType:"json",
        traditional: true,
        success: function (response) {
            toggle_alert(response.success, response.message);
        },
        error: function (response) {
            toggle_alert(response.success, response.message);
        }

    })


}



function del_user(){
    //根据用户id删除某个用户
    if(confirm('确定要删除此用户吗?')) {
        let id = $("#id").val();
        let data = {"id":id}
        $.ajax({
            type: "post",
            url: "/del_user",
            data: data,
            dataType: "json",
            success: function (response) {
                toggle_alert(response.success, "", response.message);
            },
            error: function (response) {
                toggle_alert(response.success, "", response.message);
            }
        });
    }
    else{
        return false;
    }
}

//尝试在模态框关闭时将模态框表格中内容清空，失败，有缘再做
// $('modifyModal').on('hidden.bs.modal', function (e) {
//     $("#left").empty();
//     $("#right").empty();
// });