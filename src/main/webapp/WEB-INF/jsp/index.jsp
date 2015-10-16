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
	html = '<div class="form-group">'+
	'<input type="text" class="urlInput form-control" placeholder="key" onfocus="insertUrlParam(this)" />'+
	'<input type="text" class="urlInput form-control" placeholder="value" onfocus="insertUrlParam(this)" />'+
	'<span class="trashParam glyphicon glyphicon-trash" aria-hidden="true" onclick="delUrlParam(this)"></span>' +
	'</div>';
	$(document).ready(function() {
		var button = $("#start");
		button.addClass("disabled");
		$.post("isInStandbyMode.php",function(data){
			
			if(data=='false'){
				button.text("调度器待命");
			}else if(data == 'true'){
				button.text("调度器开始");
			}else{
				return
			}
			button.removeClass("disabled");
		});
		//添加按钮
		$("#addTask").click(function(){
			//开始添加前的准备工作
			$("#reuqestParams").empty();
			$("#params").val("");
			$("#requestMethod").get(0).value = "GET";
			$("#addJobModal").modal('show');
			var form = $("#addForm");
			form.find("input[type='text']").each(function(){
				this.value= "";
			})
		});
		//添加提交按钮
		$("#submitAddTask").click(function(){
			// 拼接字段
			if($("#requestMethod").val() == 'POST'){
				var $divs = $("#reuqestParams>div");
				var array = new Array();
				$divs.each(function(){
					var key = $(this).find("input:eq(0)").val();
					var value = $(this).find("input:eq(1)").val();
					if(key != "" && value != ""){
						array.push(new Array(key,value));
					}
				});
				$("#params").attr("value",JSON.stringify(array));
			}
			var button = $(this);
			var param = $("#addForm").serialize();
			$.post("addJob.php",param,function(data){
				if(data=='success'){
					$("#addJobModal").modal('hide');
					window.location.reload();
				}else{
					alert(data);
				}
			});
		});
		$("#start").click(function(){
			$(this).addClass("disabled");
			var button = $(this);
			$.post("start.php",function(data){
				if(data=='start'){
					button.text("调度器停止");
				}else if(data=='shutdown'){
					button.text("调度器开始");
				}else{
					alert(data);
				}
				button.removeClass("disabled");
				window.location.reload();
			});
		});
		//get,post转化
		$("#requestMethod").change(function(){
			if(this.value=='GET'){
				$("#reuqestParams").text("");
			}
			else if(this.value=='POST'){
				var $params = $("#reuqestParams");
				$params.append(html);
			}
		});
	});
	function insertUrlParam(input){
		var $params = $("#reuqestParams");
		$params.append(html);
		$(input).parent().find("input").each(function(){
			$(this).attr("onfocus","");
		});
	}
	function delUrlParam(btn){
		if($("#reuqestParams>.form-group").length <= 1){
			return;
		}
		$(btn).parent().remove();
		var $params = $("#reuqestParams");
		$params.find("div:last>input").each(function(){
			$(this).bind("onfocus",function(){
				insertUrlParam(this);
			});
		});
	}
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
			}else {
				alert(data);
				return;
			}
		});
	}
	function del(btn,name,group){
		var button = $(btn);
		button.addClass("disabled");
		$.post("deleteJob.php",{name:name,group:group},function(data){
			if(data == 'success'){
				button.removeClass("disabled");
				window.location.reload();
			}else {
				alert(data);
				return;
			}
		});
	}
	
	function run(btn,name,group){
		var button = $(btn);
		button.addClass("disabled");
		$.post("runJobNow.php",{name:name,group:group},function(data){
			if(data=='success'){
				button.removeClass("disabled");
				alert("执行成功");
			}else return;
		});
	}
	
	function log(btn,name,group){
		var button = $(btn);
		button.addClass("disabled");
		$("#logContent").html('这是一条测试日志');
		button.removeClass("disabled");
		$("#logModal").modal('show');
	}
	
	function modify(name,group,cronExpress,url){
		var editModal = $("#editModal");
		if(cronExpress == "")
			editModal.find("input[name='time']").val("");
		else
			editModal.find("input[name='time']").val(cronExpress);
		if(url == "")
			editModal.find("input[name='url']").val("");
		else
			editModal.find("input[name='url']").val(url);
		
		editModal.find("input[name='name']").val(name);
		editModal.find("input[name='group']").val(group);
		editModal.modal({show:true,backdrop:false});
	}
	function submitEdit(){
		var editModal = $("#editModal");
		var param = editModal.find("form").serialize();
		$.post("editJob.php",param,function(data){
			if(data=='success'){
				editModal.modal('hide');
				window.location.reload();
			}else{
				alert(data);
			}
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
.box-shadow {
	box-shadow:0 0 25px 0 #aaa;
}
.text-shadow{
	text-shadow: #888 0 0 1px;
}
.urlInput{
	width: 40%;
	display: inline;
	margin-right: 10px;
}
.trashParam{
	font-size: 16px;
}
-->
</style>
</head>
<body>
	<div class="container text-shadow">
		<div class="jumbotron">
			<h1 class="chinese">703工作室URL调度平台</h1>
			<p class="chinese">V0.12@20151015</p>
			<p>
				<button id="start" type="button"  class="btn btn-primary chinese">全部开始</button>
				<button id="addTask" type="button"  class="btn btn-primary chinese">添加URL请求任务</button>
			</p>
			
			<div class="panel panel-default box-shadow">
				<div class="panel-heading">
					<span class="panel-title chinese" >计划中任务</span>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
					    <table class="table table-bordered">
					    	<thead>
					    		<tr>
					    			<th>任务名称</th>
					    			<th>任务组</th>
					    			<th>cron表达式</th>
					    			<th>url</th>
					    			<th>状态</th>
					    			<!--<th>备注</th>-->
					    			<th>操作</th>
					    		</tr>
					    	</thead>
					    	<tbody>
					    		<c:choose>
									<c:when test="${plist!=null and fn:length(plist) > 0}">
										<c:forEach var="map" items="${plist}">
											<tr>
												<td data-toggle="tooltip" data-placement="top" title="${map.jobName}"><c:out value="${map.jobName}"/></td>
												<td><c:out value="${map.jobGroup}"/></td>
												<td><c:out value="${map.cronExpression}"/></td>
												<td data-toggle="tooltip" data-placement="top" title="${map.url}"><c:out value="${fn:substring(map.url, 0, 10)}..."/></td>
												<td><c:out value="${map.jobStatus}"/></td>
												<!--<td><c:out value="${map.desc}"/></td>-->
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
														<button id="modify" type="button" class="btn btn-default chinese" onclick="modify('${map.jobName}','${map.jobGroup}','${map.cronExpression}','${map.url }')">修改</button>
														<button id="run" type="button" class="btn btn-default chinese" onclick="run(this,'${map.jobName}','${map.jobGroup}')">立即运行一次</button>
														<button id="log" type="button" class="btn btn-default chinese" onclick="log(this,'${map.jobName}','${map.jobGroup}')">查看日志</button>
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
			
			<div class="panel panel-primary box-shadow">
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
	<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModal">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="editModalTitle">修改</h4>
	      </div>
	      <div class="modal-body">
	        <form>
	          <div class="form-group">
	            <label for="time" class="control-label">输入新的表达式:</label>
	            <input type="text" class="form-control" name="time">
	          </div>
	          <div class="form-group">
	            <label for="url" class="control-label">输入新的URL:</label>
	            <div>
					<input type="text" class="form-control" name="url" style="display: inline;width: 80%" placeholder="url" />
					<select name="method" class="form-control" style="width: 18%;display: inline;">
						<option selected="selected">GET</option><option>POST</option>
					</select>
				</div>  
	          </div>
	          <div class="form-group">
	          	<textarea rows="" cols="" class="form-control" name="params"></textarea>
	          </div>
	          <input type="hidden" class="form-control" name="name">
	          <input type="hidden" class="form-control" name="group">
	        </form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" onclick="submitEdit()">修改</button>
	      </div>
	    </div>
	  </div>
	</div>
	<div class="modal fade" id="addJobModal" tabindex="-1" role="dialog" aria-labelledby="addJobModal">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="addJobModal">新建任务</h4>
	      </div>
	      <div class="modal-body">
	        <form id="addForm">
				<div class="form-group">
					<label for="name">任务名称</label>
					<input type="text" class="form-control" name="name" placeholder="name" />
				</div>
				<div class="form-group">
					<label for="group">任务组</label>
					<input type="text" class="form-control" name="group" placeholder="group" />
				</div>
				<div class="form-group">
					<label for="time">
						任务表达式
						<a href="./cron_readme.php" target="_Blank">
							<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
						</a>
					</label>
					<input type="text" class="form-control" name="time" placeholder="time" />
				</div>
				<div class="form-group">
					<label for="str">URL参数</label>
					<div>
					<input type="text" class="form-control" name="url" style="display: inline;width: 80%" placeholder="url" />
					<select id="requestMethod" name="method" class="form-control" style="width: 18%;display: inline;"><option selected="selected">GET</option><option>POST</option></select>
					</div>
				</div>
				<div id="reuqestParams" class="form-group"></div>
				<input id="params" type="hidden" name="params" />
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button id="submitAddTask" type="button" class="btn btn-primary chinese">添加任务</button>
	      </div>
	    </div>
	  </div>
	</div>
	<div class="modal fade" id="logModal" tabindex="-1" role="dialog" aria-labelledby="logModal">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">日志查看</h4>
	      </div>
	      <div class="modal-body" id="logContent">
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
</html>
