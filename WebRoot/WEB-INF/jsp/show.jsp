<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.json.JSONArray"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
%>
<%
	String type = (String) request.getAttribute("type");
	int begin = (Integer) request.getAttribute("begin");
	int end = (Integer) request.getAttribute("end");
	double dt = (Double) request.getAttribute("dt");
	double tt = (Double) request.getAttribute("tt");
	double rt = (Double) request.getAttribute("rt");
	JSONArray array = (JSONArray) request.getAttribute("array");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>MapFinger</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta name="viewport" content="initial-scale=1.0,user-scalable=no">

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
						<div id="submit"><input id="btn_set" type="button" value="GO" onclick="post()" style="width:30px;height:20px"/></div>
					</div>
					<div id="type_block">
						<div class="param">
							<div class="param_lab">TYPE</div>
							<div class="param_val">
								<select id="type" onchange="showParamBlock()">
									<option value="">--TYPE--</option>
									<option value="all">ALL</option>
									<option value="part">PART</option>
									<option value="key">KEY</option>
									<option value="skey">SKEY</option>
									<option value="ikey">IKEY</option>
									<option value="ekey">EKEY</option>
								</select>
							</div>
						</div>
					</div>
					<div class="param_block" name="param_block">
						<div class="param">
							<div class="param_lab">BEGIN</div>
							<div class="param_val"><input id="begin" type="text" value="<%=begin%>" size="5" /></div>
						</div>
						<div class="param">
							<div class="param_lab">END</div>
							<div class="param_val"><input id="end" type="text" value="<%=end%>" size="5" /></div>
						</div>
					</div>
					<div class="param_block" name="param_block">
						<div class="param">
							<div class="param_lab">DT</div>
							<div class="param_val"><input id="dtk" type="text" value="<%=dt%>" size="5" />m</div>
						</div>
						<div class="param">
							<div class="param_lab">TT</div>
							<div class="param_val"><input id="ttk" type="text" value="<%=tt%>" size="5" />s</div>
						</div>
					</div>
					<div class="param_block" name="param_block">
						<div class="param">
							<div class="param_lab">TT</div>
							<div class="param_val"><input id="tts" type="text" value="<%=tt%>" size="5" />s</div>
						</div>
					</div>
					<div class="param_block" name="param_block">
						<div class="param">
							<div class="param_lab">RT</div>
							<div class="param_val"><input id="rti" type="text" value="<%=rt%>" size="5" /></div>
						</div>
					</div>
					<div class="param_block" name="param_block">
						<div class="param">
							<div class="param_lab">DT</div>
							<div class="param_val"><input id="dte" type="text" value="<%=dt%>" size="5" />m</div>
						</div>
						<div class="param">
							<div class="param_lab">TT</div>
							<div class="param_val"><input id="tte" type="text" value="<%=tt%>" size="5" />s</div>
						</div>
						<div class="param">
							<div class="param_lab">RT</div>
							<div class="param_val"><input id="rte" type="text" value="<%=rt%>" size="5"/></div>
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