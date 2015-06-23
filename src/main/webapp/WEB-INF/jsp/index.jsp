<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>quartzTest</title>
<script type="text/javascript" src="static/dist/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="static/dist/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		$("#learnmore").click(function() {
			var param = $("#inputbox1").val();
			$.post("test.php",{param:param}, function(data) {
				alert(data);
			});
		});
		$("#changetime").click(function() {
			var param = $("#inputbox2").val();
			$.post("changetime.php",{param:param}, function(data) {
				alert(data);
			});
		});
	});
</script>
<link rel="stylesheet" href="static/dist/css/bootstrap.min.css" />
<link rel="stylesheet" href="static/dist/css/bootstrap-theme.min.css" />
<style type="text/css">
<!--
.chinese {
	font-family: "Microsoft YaHei", 微软雅黑, "MicrosoftJhengHei", 华文细黑, STHeiti,
		MingLiu
}
-->
</style>
</head>
<body>
	<div class="container">
		<div class="jumbotron">
			<h1 class="chinese">Hello</h1>
			<p class="chinese">这是一个quartz的测试</p>
			<p>
				<button id="addTask" class="btn btn-primary chinese">添加任务</button>
			</p>
			<div class="panel panel-default">
				<div class="panel-heading">
					<span class="panel-title chinese" >计划中任务</span>
				</div>
				<div class="panel-body">
				    <table class="table table-bordered">
				    	<thead>
				    		<tr>
				    			<th>任务名称</th>
				    			<th>任务组</th>
				    			<th>cron表达式</th>
				    			<th>状态</th>
				    			<th>备注</th>
				    			<th>操作</th>
				    		</tr>
				    	</thead>
				    	<tbody>
				    		<tr>
				    			<td>假的名称1</td>
				    			<td>假的任务组2</td>
				    			<td>假的表达式</td>
				    			<td>假的名称1</td>
				    			<td>假的任务组2</td>
				    			<td>
				    				<div class="btn-group" role="group" aria-label="buttonGroup">
										<button id="pause" type="button" class="btn btn-default chinese">暂停</button>
										<button id="delete" type="button" class="btn btn-default chinese">删除</button>
										<button id="modify" type="button" class="btn btn-default chinese">修改表达式</button>
										<button id="run" type="button" class="btn btn-default chinese">立即运行一次</button>
									</div>
				    			</td>
				    		</tr>
				    	</tbody>
				    	
					</table>
				</div>
			</div>
			
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title chinese">运行中任务</h3>
				</div>
				<div class="panel-body">
				    <table class="table table-bordered">
				    	<thead>
				    		<tr>
				    			<th>任务名称</th>
				    			<th>任务组</th>
				    			<th>cron表达式</th>
				    			<th>状态</th>
				    			<th>备注</th>
				    		</tr>
				    	</thead>
				    	<tbody>
				    		
				    	</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
