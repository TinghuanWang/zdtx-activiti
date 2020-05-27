<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>请假列表（业务数据）</title>

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
            <a class="btn btn-primary" href='/create?name=activiti&key=123456' role="button">添加申请</a>
            <p class="table-bordered">
            </p>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th width="5%">请假编号</th>
                    <th width="5%">请假类型</th>
                    <th width="10%">请假人</th>
                    <th width="10%">开始时间</th>
                    <th width="10%">截止时间</th>
                    <th width="10%">请假原因</th>
                    <th width="10%">流程实例ID（ProcessInstanceId）</th>
                    <th width="10%">当前状态名称</th>
                    <th width="30%">操作</th>
                </tr>
                </thead>
                <tbody>
	        <#list leavelist as leaveBill>
            <tr>
                <td width="5%">${leaveBill.id}</td>
                <td width="5%"><#if (leaveBill.leavetype)??>${leaveBill.leavetype}<#else> </#if></td>
                <td width="10%"><#if (leaveBill.username)??>${leaveBill.username}<#else> </#if></td>
                <td width="10%"><#if (leaveBill.startDate)??>${(leaveBill.startDate)?string("yyyy-MM-dd")}<#else> </#if></td>
                <td width="10%"><#if (leaveBill.endDate)??>${(leaveBill.endDate)?string("yyyy-MM-dd")}<#else> </#if></td>
                <td width="10%"><#if (leaveBill.reason)??>${leaveBill.reason}<#else> </#if></td>
                <td width="10%"><#if (leaveBill.processInstanceId)??>${leaveBill.processInstanceId}<#else> </#if></td>
                <td width="10%"><#if (leaveBill.status)??>${leaveBill.status}<#else>未启动</#if></td>
                <td width="30%">
                    <a class="btn btn-default" href='/leaveBill/submit?leaveId=${leaveBill.id}&userId=${userId}' role="button">提交申请</a>
                    <a class="btn btn-default" href='/leaveBill/edit?leaveId=${leaveBill.id}' role="button">编辑</a>
                    <a class="btn btn-default" href='/leaveBill/delete?leaveId=${leaveBill.id}' role="button">删除</a>
                    <br/>
                    <a class="btn btn-primary" href='/leaveBill/infos?modelId=${leaveBill.id}' role="button">表单数据</a>
                    <a class="btn btn-primary" href='/leaveBill/history?modelId=${leaveBill.id}' role="button">审核历史</a>
                    <a class="btn btn-primary" href='/leaveBill/process?modelId=${leaveBill.id}' role="button">进度查看</a>
                    <a class="btn btn-primary" href='/leaveBill/reback?modelId=${leaveBill.id}' role="button">撤销</a>
                    <a class="btn btn-primary" href='/leaveBill/suspend?modelId=${leaveBill.id}' role="button">挂起</a>
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