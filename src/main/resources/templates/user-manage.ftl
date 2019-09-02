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
                            <th>用户ID</th>
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
                                <td data-toggle="modal" data-target="#exampleModal" style="color: #005cbf;cursor: pointer" onclick="getGroups($(this))">查看所属群组</td><td></td>
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
                        <label for="name">用户ID</label>
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
                        <#--                        <label data-toggle="modal" data-target="#modifyModal" style="cursor: pointer;color: #005cbf;float: left" onclick="modifyGetMembers()">群组人员修改</label>-->
                        <label data-toggle="modal" data-target="#modifyModal" style="cursor: pointer;color: #005cbf;" onclick="modifyGetGroups()">所属群组修改</label>
                    </div>
                    <input id="id" name="id" style="display: none" type="text">
                    <div class="row">
<#--                        <div class="col-12 col-sm-4">-->
<#--                            <button type="button" class="btn btn-primary" onclick="update_user()">更新此用户-->
<#--                            </button>-->
<#--                        </div>-->
                        <div class="col-12 col-sm-4 tm-btn-right">
                            <button  type="button" class="btn btn-danger"  onclick="add_user()">新增用户
                            </button>
                        </div>
<#--                        <div class="col-12 col-sm-4 tm-btn-right">-->
<#--                            <button  type="button" class="btn btn-danger"  onclick="del_user()">删除此用户-->
<#--                            </button>-->
<#--                        </div>-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- 展示所属群组的模态框-->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog " role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel"></h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <table class="table" id="model-table">
                        <thead>
                        <tr>
                            <th scope="col"></th>
                            <th scope="col">群组ID</th>
                            <th scope="col">群组名称</th>
                            <th scope="col">类型</th>
                        </tr>
                        <tbody id="modelTbody">

                        </tbody>
                        </thead>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 修改所属群组的模态框（Modal） -->
    <div class="modal fade" id="modifyModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog bd-example-modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modifyModalLabel"></h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <table class="table" id="modify-model-table">
                        <tr>
                            <th scope="col" style="text-align: center;">该用户所在群组</th>
                            <th> </th>
                            <th scope="col" style="text-align: center;">该用户未在的群组</th>
                        </tr>
                        <tr>
                            <td width="40%">
                                <select   name="left" id="left" size="8">
                                    <!--<option value="20">测试数据一</option>-->
                                </select>
                            </td>
                            <td width="20%" align="center">
                                <input type="button" value=" >> "
                                       onclick="moveOption(document.getElementById('left'),document.getElementById('right'))"><br><br>
                                <input type="button" value=" << "
                                       onclick="moveOption(document.getElementById('right'), document.getElementById('left'))">
                            </td>
                            <td width="40%">
                                <select  multiple name="right" id="right" size="8">
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary " onclick="modifyMGroupsInfo()">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</@override>
<@extends name="base.ftl"/>