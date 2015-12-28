<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.json.JSONArray"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
%>
<%
	JSONArray array = (JSONArray)request.getAttribute("array");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>List</title>
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<script type="text/javascript" src="script/json/json2.js"></script>
		<script type="text/javascript" src="script/loadmap.js"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=sRbH11iRIGFPVthjnBGw5uMG"></script>
		
		<link rel="stylesheet" type="text/css" href="style/styles.css">
	</head>
	
	<body onload=parse(<%=array%>)>
		<div id="box">
			<div id="panel">
				<div id="setting">
					<div class="param_block" name="param_block">
					</div>
				</div>
				<div id="point">
					<div class="banner">
						<div class="title">Key Points</div>
					</div>
					<div id="list"></div>
				</div>
			</div>
			<div id="allmap"></div>
		</div>
	</body>
</html>