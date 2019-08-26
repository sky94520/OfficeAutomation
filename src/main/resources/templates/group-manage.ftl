<@override name="title">user-manage</@override>
<@override name="scripts">
    <script src="https://cdn.staticfile.org/popper.js/1.12.5/umd/popper.min.js"></script>
    <script src="../static/js/group_manage.js"></script>
</@override>

<@override name="main">
    <div class="container-fluid">
        <div class="row mt-4">
            <div class="col-sm-6 offset-1" >
                <!--头-->
                <div class="row">
                    <div class="col-12">
                        <h2 class="tm-block-title d-inline-block">用户组列表</h2>
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
                            <th>群组ID</th>
                            <th>群组名</th>
                            <th>群组类型</th>
                            <th>组内人员</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list groupList as gl>
                            <tr>
                                <td>${gl.id}</td>
                                <td>${gl.name}</td>
                                <td>${gl.type}</td>
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
                        <label for="id">群组ID</label>
                        <input  id="id" name="id" type="text" class="form-control validate" required>
                    </div>
                    <div class="form-group">
                        <label for="name">群组名称</label>
                        <input id="name" name="name" type="text" class="form-control validate" required>
                    </div>
                    <div class="form-group">
                        <label for="type">群组类型</label>
                        <input  id="type" name="type" type="text" class="form-control validate" required>
                    </div>
                    <div class="form-group">
                        <label for="members">组内人员</label>
                        <input id="members" name="members" type="text" class="form-control validate" required>
                    </div>

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