<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Activiti6流程设计器Demo</title>

</head>
<!-- Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
<body>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <a class="btn btn-primary" href='/modeler/create?name=activiti6&key=20200515' role="button">绘制流程</a>
            <p class="table-bordered">
            </p>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th width="10%">模型编号</th>
                    <th width="10%">版本</th>
                    <th width="20%">模型名称</th>
                    <th width="20%">模型key</th>
                    <th width="40%">操作</th>
                </tr>
                </thead>
                <tbody>
	        <#list modelList as model>

            <tr>
                <td width="10%">${model.id}</td>
                <td width="10%">${model.version}</td>
                <td width="20%"><#if (model.name)??>${model.name}<#else> </#if></td>
                <td width="20%"><#if (model.key)??>${model.key}<#else> </#if></td>
                <td width="40%">
                    <a class="btn btn-primary" href='/editor?modelId=${model.id}' role="button">编辑</a>
                    <a class="btn btn-primary" href='/modeler/publish?modelId=${model.id}' role="button">发布</a>
                    <a class="btn btn-primary" href='/modeler/revokePublish?modelId=${model.id}' role="button">撤销</a>
                    <a class="btn btn-primary" href='/modeler/delete?modelId=${model.id}' role="button">删除</a>
                    <a class="btn btn-primary" href='/modeler/suspend?modelId=${model.id}' role="button">挂起</a>
                    <a class="btn btn-primary" href='/modeler/activate?modelId=${model.id}' role="button">激活</a>
                </td>
            </tr>
            </#list>
                </tbody>
            </table>
            <p class="table-bordered">
            </p>
        </div>
    </div>
</div>
</body>
</html>