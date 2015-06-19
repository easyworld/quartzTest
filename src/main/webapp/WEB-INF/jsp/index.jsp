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
	});
</script>
<link rel="stylesheet" href="static/dist/css/bootstrap.min.css" />
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
			<div class="form-group">
				<label for="input">input</label> 
				<input type="text" class="form-control" id="inputbox1"	placeholder="please input" />
			</div>
			<a id="learnmore" class="btn btn-primary btn-lg chinese" href="#" role="button">开始！</a>
		</div>
	</div>
</body>
</html>
