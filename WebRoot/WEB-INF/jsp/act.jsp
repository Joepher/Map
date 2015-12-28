<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.json.JSONArray"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
%>
<%
	JSONArray positionArray = (JSONArray) request.getAttribute("positionArray");
	JSONArray pathArray = (JSONArray) request.getAttribute("pathArray");
	JSONArray curArray = (JSONArray) request.getAttribute("curArray");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>Act</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script type="text/javascript" src="script/json/json2.js"></script>
		<script type="text/javascript" src="script/act.js"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=sRbH11iRIGFPVthjnBGw5uMG"></script>
		<link rel="stylesheet" type="text/css" href="style/act.css">
	</head>
	
	<body onload=show(<%=positionArray%>,<%=pathArray%>,<%=curArray%>)>
		<div id="allmap"></div>
	</body>
</html>