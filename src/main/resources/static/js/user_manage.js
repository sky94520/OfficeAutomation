
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
function add_user() {
    //增加新的用户
    //获取表单中的数据
    let name = $("#name").val();
    let first_name = $("#first-name").val();
    let last_name = $("#last-name").val();
    let email = $("#email").val();
    let password = $("#password").val();

    //对某些信息做出限制
    if(name==""){
        toggle_alert(false,  "用户名不能为空！");
        return false;
    }
    if(first_name==""){
        toggle_alert(false,  "用户名不能为空！");
        return false;
    }
    if(last_name==""){
        toggle_alert(false,  "用户名不能为空！");
        return false;
    }

    var re =  /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    if(!re.test(email)){
        toggle_alert(false,  "请输入正确的邮箱！");
        return false;
    }

    let data = {"id": name, "first_name":first_name, "last_name":last_name, "email": email, "password": password,};
    $.ajax({
    type: "post",
    url: "/add-user",
    data: data,
    dataType: "json",
    success: function (response) {
         toggle_alert(response.success, response.message);
         $("#name").val("");
         $("#first-name").val("");
         $("#last-name").val("");
         $("#email").val("");
         $("#password").val("");
    },
    error: function(response){
         toggle_alert(response.success,  response.message);
    }
});
}

function getGroups(e) {
    let tds = e.siblings();
    let id = tds[0].innerText;
    let data = {"id":id};
    $.ajax({
        type:"post",
        url:"/get-group",
        data:data,
        dataType:"json",
        success:function (response) {
            $(".modal-title").text("所属群组");
            //清除模态框中原有内容
            $("#modelTbody").empty();
            for(let id in response){
                $("#modelTbody").append("<tr><td></td><td>"+id+"</td><td>"+response[id]["name"]+"</td><td>"+response[id]["type"]+"</td></tr>");
            }
        },
        error:function (response) {
            toggle_alert(false,"获取群组信息失败")
        }
    })

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
        toggle_alert(response.success, "", response.message);
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

function edit(e) {
    //将用户的数据从左边的table中填写到右边的form表单中
    tds = e.parent().siblings();
    let name = tds[0].innerText;
    let first_name = tds[1].innerText;
    let last_name = tds[2].innerText;
    let email = tds[3].innerText;
    let password = tds[4].innerText;
    let group = tds[5].innerText;
    $("#name").val(name);
    $("#first-name").val(first_name);
    $("#last-name").val(last_name);
    $("#email").val(email);
    $("#password").val(password);
    myselect.value=group
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
                console.log(response);
                toggle_alert(response.success,  response.message);
            },
            error: function (response) {
                console.log(response);
                toggle_alert(response.success, response.message);
            }
        });
    }
    else{
        return false;
    }
}

function modifyGetGroups() {
    let id = $("#name").val();

    let data = {"id": id};
    $.ajax({
            url: "/modify-get-groups",
            type: "POST",
            data: data,
            dataType: "json",
            success: function (response) {
                $("#left").empty();
                $("#right").empty();
                $(".modal-title").text("所属群组修改");
                for(let id in response["exit"]){
                    $("#left").append("<option value=\"20\">"+response["exit"][id]+"</option>");
                }
                for(let id in response["noExit"]){
                    $("#right").append("<option value=\"20\">"+response["noExit"][id]+"</option>");
                }

            },

        }
    );
}

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
 * 更新所属用户组
 */
function modifyMGroupsInfo() {
    let options = $("#left").children();
    let nameStr = "";
    for(let i=0;i<options.length;i++){
        if(i==0){
            nameStr += options[i].text;
        }else{
            nameStr += (" "+options[i].text);
        }

    }
    let userId = $("#name").val();
    console.log(nameStr)
    let data = {nameList:nameStr,"userId":userId};
    console.log(data);
    $.ajax({
        type:"POST",
        url:"/modify-groups",
        data:data,
        dataType:"json",
        traditional: true,
        success: function (response) {
            toggle_alert(response.success, response.message);
        },


    })


}