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
<link rel="stylesheet" href="static/dist/css/bootstrap.min.css" />
<link rel="stylesheet" href="static/dist/css/bootstrap-theme.min.css" />
<style type="text/css">
<!--
.chinese {
	font-family: "Microsoft YaHei", 微软雅黑, "MicrosoftJhengHei", 华文细黑, STHeiti,
		MingLiu;
}

.box-shadow {
	box-shadow: 0 0 25px 0 #aaa;
}

.text-shadow {
	text-shadow: #888 0 0 1px;
}

-->
</style>
<script type="text/javascript">
window.onresize=function(){  
	var main = $(".jumbotron p");
	var width = document.body.clientWidth;
	if(width < 1377){
		main.each(function(){
			this.style.fontSize = "14.5px";
		})
	}
	else{
		main.each(function(){
			this.style.fontSize = (width - 1377)*0.02 + 14.5 + "px";
		})
	}
}
</script>
</head>
<body>
	<div class="container text-shadow">
		<div id="main" class="jumbotron chinese">
			<h1>Cron Expressions</h1>
			<p>cron的表达式被用来配置CronTrigger实例。
				cron的表达式是字符串，实际上是由七子表达式，描述个别细节的时间表。这些子表达式是分开的空白，代表：</p>
			<pre># 文件格式说明
#  ——分钟 (0 - 59)
# |  ——小时 (0 - 23)
# | |  ——日   (1 - 31)
# | | |  ——月   (1 - 12)
# | | | |  ——星期 (0 - 7)（星期日=0或7）
# | | | | |
# * * * * * 被执行的命令</pre>
			
			<h2>
				<li>Cron表达式的格式：秒 分 时 日 月 周 年(可选)。</li>
			</h2>
			<table class="table">
				<thead>
					<tr>
						<th>字段名</th>
						<th>允许的值</th>
						<th>允许的特殊字符</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>秒</td>
						<td>0-59</td>
						<td>, - * /</td>
					</tr>
					<tr>
						<td>分</td>
						<td>0-59</td>
						<td>, - * /</td>
					</tr>
					<tr></tr>
					<tr>
						<td>小时</td>
						<td>0-23</td>
						<td>, - * /</td>
					</tr>
					<tr>
						<td>日</td>
						<td>1-31</td>
						<td>, - * ? / L W C</td>
					</tr>
					<tr>
						<td>月</td>
						<td>1-12 or JAN-DEC</td>
						<td>, - * /</td>
					</tr>
					<tr>
						<td>周几</td>
						<td>1-7 or SUN-SAT</td>
						<td>, - * ? / L C #</td>
					</tr>
					<tr>
						<td>年 (可选字段)</td>
						<td>empty, 1970-2099</td>
						<td>, - * /</td>
					</tr>
				</tbody>
			</table>
			<pre>“?”字符：表示不确定的值
“,”字符：指定数个值
“-”字符：指定一个值的范围
“/”字符：指定一个值的增加幅度。n/m表示从n开始，每次增加m
“L”字符：用在日表示一个月中的最后一天，用在周表示该月最后一个星期X
“W”字符：指定离给定日期最近的工作日(周一到周五)
“#”字符：表示该月第几个周X。6#3表示该月第3个周五</pre>
			<h2>
				<li>Cron表达式范例：</li>
			</h2>
			<pre>
每隔5秒执行一次：*/5 * * * * ?
每隔1分钟执行一次： 0 */1 * * * ?
每天23点执行一次：0 0 23 * * ?
每天凌晨1点执行一次：0 0 1 * * ?
每月1号凌晨1点执行一次：0 0 1 1 * ?
每月最后一天23点执行一次：0 0 23 L * ?
每周星期天凌晨1点实行一次：0 0 1 ? * L
在26分、29分、33分执行一次：0 26,29,33 * * * ?
每天的0点、13点、18点、21点都执行一次：0 0 0,13,18,21 * * ?
			</pre>
		</div>
	</div>
</body>
</html>
