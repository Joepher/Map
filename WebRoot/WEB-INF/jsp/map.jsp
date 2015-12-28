<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.json.JSONArray"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
%>
<%
	String longitude = (String)request.getAttribute("longitude");
	String latitude = (String)request.getAttribute("latitude");
	JSONArray array = (JSONArray)request.getAttribute("array");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>Insert title here</title>
	
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
					<div class="banner">
						<div class="title">Setting</div>
						<div id="submit"><input id="btn_set" type="button" value="GO" onclick="map_post()" style="width:30px;height:20px"/></div>
					</div>
					<div class="param_block" name="param_block">
						<div class="param">
							<div class="param_lab">lon</div>
							<div class="param_val"><input id="lon" type="text" value="<%=longitude%>" size="8" /></div>
						</div>
						<div class="param">
							<div class="param_lab">lat</div>
							<div class="param_val"><input id="lat" type="text" value="<%=latitude%>" size="8" /></div>
						</div>
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