<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>My JSP 'index.jsp' starting page</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	</head>

	<body>
		<select id="wupin_id" name="wupin_id" onclick="saveLast()" onchange="changeForm(this.value)">
			<option value="0" selected>请选择您要使用的设备类型</option>
			<option value="001">惠普扫描仪G5590</option>
			<option value="002">惠普扫描仪G2410</option>
			<option value="003">惠普扫描仪G3110</option>
		</select>

		<script type="text/javascript">
			var lastIndex, lastValue;

			function saveLast() {
				var select = document.getElementById("wupin_id");
				lastIndex = select.selectedIndex;
				var midValue = select.options[lastIndex].value;
				//var text = select.options[index].text;
				if (midValue != "0") {
					lastIndex = select.selectedIndex;
					lastValue = select.options[lastIndex].value;
				}
			}
			
			function changeForm(val) {
				var obj = document.getElementById("wupin_id");
				var tr01 = document.getElementById("tr01").value;
				var flag = document.getElementById("saverk").disabled;
				var flag2 = document.getElementById("tr01").disabled;
				if (val != "0" && flag2 == true) {
					document.getElementById("tr01").disabled = false;
					document.getElementById("tr01").focus();
				}
				if (val != "0" && tr01 != "" && flag != true) {
					alert("请先保存入库，再进行其他型号物品的扫描录入！");
					obj.options[lastIndex].selected = true; //加载用户改变以前的选中的值
					document.getElementById("saverk").focus();
				}
			}
		</script>

	</body>
</html>