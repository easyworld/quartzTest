<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>quartzTest</title>
<script type="text/javascript" src="static/dist/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="static/dist/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var button = $("#start");
		button.addClass("disabled");
		$.post("isStart.php",function(data){
			
			if(data=='true'){
				button.text("全部停止");
			}else if(data == 'false'){
				button.text("全部开始");
			}else{
				return
			}
			button.removeClass("disabled");
		});
		//添加按钮
		$("#addTask").click(function(){
			$(this).addClass("disabled");
			var button = $(this);
			var param = $("#addForm").serialize();
			$.post("addJob.php",param,function(data){
				alert(data);
				button.removeClass("disabled");
				window.location.reload();
			});
		});
		$("#start").click(function(){
			$(this).addClass("disabled");
			var button = $(this);
			$.post("start.php",function(data){
				if(data=='start'){
					button.text("全部停止");
				}else if(data=='shutdown'){
					button.text("全部开始");
				}else{
					alert(data);
				}
				button.removeClass("disabled");
			});
		});
	});
	function pause(btn,name,group){
		var button = $(btn);
		button.addClass("disabled");
		$.post("pauseJob.php",{name:name,group:group},function(data){
			if(data == 'success'){
				button.get(0).onclick = "resume(this,'"+name+"','"+group+"')";
				button.text("继续");
				button.removeClass("disabled");
				window.location.reload();
			}else return
		});
	}
	function resume(btn,name,group){
		var button = $(btn);
		button.addClass("disabled");
		$.post("resumeJob.php",{name:name,group:group},function(data){
			if(data == 'success'){
				button.get(0).onclick = "pause(this,'"+name+"','"+group+"')";
				button.text("暂停");
				button.removeClass("disabled");
				window.location.reload();
			}else return
		});
	}
	function del(btn,name,group){
		var button = $(btn);
		button.addClass("disabled");
		$.post("deleteJob.php",{name:name,group:group},function(data){
			if(data == 'success'){
				button.removeClass("disabled");
				window.location.reload();
			}else return
		});
	}
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
			<p><button id="start" class="btn btn-primary chinese">全部开始</button></p>
			<p>
				<form id="addForm" class="form-inline">
					<div class="form-group">
						<label for="name">任务名称</label>
						<input type="text" class="form-control" name="name" placeholder="name" />
					</div>
					<div class="form-group">
						<label for="group">任务组</label>
						<input type="text" class="form-control" name="group" placeholder="group" />
					</div>
					<div class="form-group">
						<label for="time">任务表达式</label>
						<input type="text" class="form-control" name="time" placeholder="time" />
					</div>
					<div class="form-group">
						<label for="str">任务参数</label>
						<input type="text" class="form-control" name="str" placeholder="str" />
					</div>
					<div class="form-group">
						<button id="addTask" class="btn btn-primary chinese">添加任务</button>
					</div>
				</form>
			</p>
			<div class="panel panel-default">
				<div class="panel-heading">
					<span class="panel-title chinese" >计划中任务</span>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
					    <table class="table table-bordered">
					    	<thead>
					    		<tr>
					    			<th style="width:80px">任务名称</th>
					    			<th style="width:80px">任务组</th>
					    			<th style="width:95px">cron表达式</th>
					    			<th style="width:60px">状态</th>
					    			<th style="width:90px">备注</th>
					    			<th>操作</th>
					    		</tr>
					    	</thead>
					    	<tbody>
					    		<c:choose>
									<c:when test="${plist!=null and fn:length(plist) > 0}">
										<c:forEach var="map" items="${plist}">
											<tr>
												<td><c:out value="${map.jobName}"/></td>
												<td><c:out value="${map.jobGroup}"/></td>
												<td><c:out value="${map.cronExpression}"/></td>
												<td><c:out value="${map.jobStatus}"/></td>
												<td><c:out value="${map.desc}"/></td>
												<td>
								    				<div class="btn-group" role="group" aria-label="buttonGroup">
								    					<c:choose>
									    					<c:when test="${map.jobStatus == 'PAUSED'}">
									    						<button id="resume" type="button" class="btn btn-default chinese" onclick="resume(this,'${map.jobName}','${map.jobGroup}')">继续</button>
									    					</c:when>
									    					<c:otherwise>
									    						<button id="pause" type="button" class="btn btn-default chinese" onclick="pause(this,'${map.jobName}','${map.jobGroup}')">暂停</button>
									    					</c:otherwise>
								    					</c:choose>
														<button id="delete" type="button" class="btn btn-default chinese" onclick="del(this,'${map.jobName}','${map.jobGroup}')">删除</button>
														<button id="modify" type="button" class="btn btn-default chinese">修改表达式</button>
														<button id="run" type="button" class="btn btn-default chinese">立即运行一次</button>
													</div>
								    			</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr><td colspan="6">暂无数据</td></tr>
									</c:otherwise>
								</c:choose>
					    	</tbody>
						</table>
					</div>
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
				    		<c:choose>
								<c:when test="${rlist!=null and fn:length(rlist)>0}">
						    		<c:forEach var="map" items="${rlist}">
										<tr>
											<td><c:out value="${map.jobName}"/></td>
											<td><c:out value="${map.jobGroup}"/></td>
											<td><c:out value="${map.cronExpression}"/></td>
											<td><c:out value="${map.jobStatus}"/></td>
											<td><c:out value="${map.desc}"/></td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr><td colspan="5">暂无数据</td></tr>
								</c:otherwise>
							</c:choose>
				    	</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
