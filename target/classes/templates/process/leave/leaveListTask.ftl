<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>待办列表（Task）</title>

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
            <p class="table-bordered">
            </p>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th width="15%">流程名称</th>
                    <th width="15%">关键描述</th>
                    <th width="5%">任务ID</th>
                    <th width="5%">业务数据ID</th>
                    <th width="10%">发起人</th>
                    <th width="10%">发起时间</th>
                    <th width="10%">流程实例ID</th>
                    <th width="30%">操作</th>
                </tr>
                </thead>
                <tbody>
	        <#list tasklist as task>

            <tr>
                <td width="10%"><#if (task.processName)??>${task.processName}<#else> </#if></td>
                <td width="10%"><#if (task.keyWord)??>${task.keyWord}<#else> </#if></td>
                <td width="10%"><#if (task.taskId)??>${task.taskId}<#else> </#if></td>
                <td width="10%"><#if (task.businessKey)??>${task.businessKey}<#else> </#if></td>
                <td width="10%"><#if (task.owner)??>${(task.owner)}<#else> </#if></td>
                <td width="10%"><#if (task.createtime)??>${task.createtime}<#else> </#if></td>
                <td width="10%"><#if (task.processInstanceId)??>${task.processInstanceId}<#else> </#if></td>
                <td width="30%">
                    <a class="btn btn-default"
                       href='/leaveBill/passed?taskId=${task.taskId}&businessKey=${task.businessKey}&userId=${userId}&status=1'
                       role="button">同意</a>
                    <a class="btn btn-default"
                       href='/leaveBill/passed?taskId=${task.taskId}&businessKey=${task.businessKey}&userId=${userId}&status=0'
                       role="button">驳回</a>
                    <br/>
                    <a class="btn btn-primary" href='/leaveBill/infos?modelId=${task.taskId}' role="button">表单数据</a>
                    <a class="btn btn-primary" href='/leaveBill/history?modelId=${task.taskId}' role="button">审核历史</a>
                <#--<a class="btn btn-primary" href='/leaveBill/process?modelId=${task.taskId}' role="button">进度查看</a>-->
                <#--<a class="btn btn-primary" href='/leaveBill/reback?modelId=${task.taskId}' role="button">撤销</a>-->
                <#--<a class="btn btn-primary" href='/leaveBill/suspend?modelId=${task.taskId}' role="button">挂起</a>-->
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