<@override name="title">user-manage</@override>
<@override name="scripts">
    <script src="https://cdn.staticfile.org/popper.js/1.12.5/umd/popper.min.js"></script>
    <script src="../static/js/user_manage.js"></script>
</@override>


<@override name="main">
    <div class="container-fluid">
        <div class="row mt-4">
            <div class="col-sm-6 offset-1" >
                <!--头-->
                <div class="row">
                    <div class="col-12">
                        <h2 class="tm-block-title d-inline-block">用户列表</h2>
                        <div class="input-group mb-3" style="width: 40%;float:right">
                            <input type="text" class="form-control"  id="myInput" name="content">
                            <div class="input-group-append">
                                <button type="submit" class="btn btn-success" onclick="myFunction()">搜索</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--用户列表-->
                <div class="row">
                    <table id="myTable" class="table table-striped">
                        <thead>
                        <tr>
                            <th>用户名</th>
                            <th>FIRSTNAME</th>
                            <th>LASTNAME</th>
                            <th>邮箱</th>
                            <th>密码 </th>
                            <th>所属群组</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list userList as ul>
                            <tr>
                                <td>${ul.id}</td>
                                <td>${ul.firstName}</td>
                                <td>${ul.lastName}</td>
                                <td>${ul.email}</td>
                                <td>${ul.password!}</td>
                                <td></td>
                                <td><button type="button" class="btn btn-info" onclick="edit($(this))">编辑</button></td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
            <!--用户编辑-->
            <div class="col-sm-4 offset-1">
                <div class="bg-white tm-block">
                    <h2 class="tm-block-title">用户编辑</h2>
                </div>

                <form class="tm-signup-form" id="myform">
                    <div class="form-group">
                        <label for="name">用户名</label>
                        <input  id="name" name="name" type="text" class="form-control validate" required>
                    </div>
                    <div class="form-group">
                        <label for="tel_number">FIRST_NAME</label>
                        <input id="first-name" name="tel_number" type="text" class="form-control validate" required>
                    </div>
                    <div class="form-group">
                        <label for="email">LAST_NAME</label>
                        <input  id="last-name" name="email" type="email" class="form-control validate" required>
                    </div>
                    <div class="form-group">
                        <label for="school">邮箱</label>
                        <input id="email" name="school" type="text" class="form-control validate" required>
                    </div>
                    <div class="form-group">
                        <label for="school">密码</label>
                        <input id="password" name="school" type="text" class="form-control validate" required>
                    </div>
                    <div class="form-group">
                        <label for="type">所属群组</label>
                        <select id="type" class="form-control">
                            <option>普通员工</option>
                            <option>首席运营官</option>
                            <option>首席技术官</option>
                        </select>
                    </div>
                    <input id="id" name="id" style="display: none" type="text">
                    <div class="row">
                        <div class="col-12 col-sm-4">
                            <button type="button" class="btn btn-primary" onclick="update_user()">更新此用户
                            </button>
                        </div>
                        <div class="col-12 col-sm-4 tm-btn-right">
                            <button  type="button" class="btn btn-danger"  onclick="add_user()">新增用户
                            </button>
                        </div>
                        <div class="col-12 col-sm-4 tm-btn-right">
                            <button  type="button" class="btn btn-danger"  onclick="del_user()">删除此用户
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@override>
<@extends name="base.ftl"/>