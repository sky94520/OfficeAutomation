<@override name="title">Hello</@override>

<@override name="main">
    <div class="container">
        <div class="row">
            <div class="col-4 card bg-light text-dark offset-4">
                <div class="card-body">
                    <div>
                        <h1 style="text-align: center" class="text-primary">登录</h1>
                    </div>
                    <div>
                        <form action="/validate" method="post">
                            <div class="form-group">
                                <label class="form-control-label" for="username">用户名</label>
                                <input type="text" class="form-control" id="username" name="username" required placeholder="请输入用户名">
                            </div>
                            <div class="form-group">
                                <label class="form-control-label" for="password">密码</label>
                                <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码" required>
                            </div>
                            <button type="submit" class="btn btn-primary btn-block">登录</button>
                        </form>
                    </div>
                    <hr>
                </div>
            </div>
        </div>
    </div>
</@override>
<@extends name="base.ftl"/>