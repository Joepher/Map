function initCompoment() {
	var box_width = document.getElementById("box").offsetWidth;
	var panel_width = document.getElementById("panel").offsetWidth;
	document.getElementById("allmap").style.width = box_width - panel_width
			+ "px";
}
function initMap(point) {
	var map = new BMap.Map("allmap"); // 创建地图实例
	
	if (point != null) {
		map.centerAndZoom(point, 15);
		map.addControl(new BMap.MapTypeControl()); // 添加地图类型控件
		map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放
	} else {
		var point = new BMap.Point(116.404, 39.915); // 创建点坐标
		map.centerAndZoom(point, 15); // 初始化地图，设置中心点坐标和地图级别
		map.addControl(new BMap.MapTypeControl()); // 添加地图类型控件
		map.setCurrentCity("北京"); // 设置地图显示的城市 此项是必须设置的
		map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放
	}
	
	return map;
}
function parse(array) {
	initCompoment();
	
	var map;
	
	if (array != null) {
		for ( var i = 0; i < array.length; i++) {
			var point = new BMap.Point(array[i].longitude, array[i].latitude);
			
			if (i == 0) {
				map = initMap(point);
			}
			
			addMarker(map, point);
		}
	} else {
		alert("null array");
		map = initMap(null);
	}
}
function addMarker(map, point) {
	var marker = new BMap.Marker(point);
	map.addOverlay(marker);
}
function post() {
	var type, begin, end, dt, tt, rt, param, uri = "show.action";
	
	var typeSelect = document.getElementById("type");
	var curIndex = typeSelect.selectedIndex;
	
	type = typeSelect.options[curIndex].value;
	
	if (curIndex == 2) {// part
		begin = document.getElementById("begin").value;
		end = document.getElementById("end").value;
		
		if (begin < 0 || end <= 0 || (begin - end) > 0) {
			var msg = "";
			if (begin < 0) {
				msg += "begin cannot below 0\r";
			}
			if (end <= 0) {
				msg += "end must above 0\r";
			}
			if ((begin - end) > 0 && begin >= 0 && end > 0) {
				msg += "begin cannot above end";
			}
			alert(msg);
			return;
		}
		param = "type=" + type + "&begin=" + begin + "&end=" + end;
	} else if (curIndex == 3) {// key
		dt = document.getElementById("dtk").value;
		tt = document.getElementById("ttk").value;
		param = "type=" + type + "&dt=" + dt + "&tt=" + tt;
	} else if (curIndex == 4) {// skey
		tt = document.getElementById("tts").value;
		param = "type=" + type + "&tt=" + tt;
	} else if (curIndex == 5) {// ikey
		rt = document.getElementById("rti").value;
		param = "type=" + type + "&rt=" + rt;
	} else if (curIndex == 6) {// ekey
		dt = document.getElementById("dte").value;
		tt = document.getElementById("tte").value;
		rt = document.getElementById("rte").value;
		param = "type=" + type + "&dt=" + dt + "&tt=" + tt + "&rt=" + rt;
	} else {// default all
		param = "type=" + type;
	}
	
	sendAjaxRequest(uri, param);
}
function createHttpRequest() {
	var httpRequest;
	
	if (window.XMLHttpRequest) {
		httpRequest = new XMLHttpRequest();
	} else {
		if (window.ActiveXObject) {
			try {
				httpRequest = new ActiveXObject("Msxml12.XMLHTTP");
			}
			catch (e) {
				try {
					httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
				}
				catch (e) {
					alert("浏览器不支持Ajax");
				}
			}
		}
	}
	
	return httpRequest;
}
function sendAjaxRequest(uri, param) {
	var httpRequest = createHttpRequest();
	
	if (httpRequest != null) {
		httpRequest.open("POST", uri, true);
		httpRequest.setRequestHeader('Content-type',
				'application/x-www-form-urlencoded');
		httpRequest.setRequestHeader('User-Agent', 'XMLHTTP');
		httpRequest.send(param);
		
		httpRequest.onreadystatechange = function() {
			var responseText = httpRequest.responseText;
			var begin = responseText.indexOf("onload");
			var end = responseText.indexOf("<div id=\"box\">");
			var response = responseText.substring(begin + 13, end - 6);
			var array = eval('(' + response + ')');
			parse(array);
		}
		httpRequest.send(null);
	} else {
		alert("create httpRequest failed");
	}
}
function showParamBlock() {
	var param_block = document.getElementsByName("param_block");
	var curIndex = document.getElementById("type").selectedIndex;
	// type: default-all-part-key-skey-ikey-ekey
	// param_block: part-key-skey-ikey-ekey
	for ( var i = 0; i < 7; i++) {
		if (i == 0 || i == 1) {
			continue;
		} else if (i == curIndex) {
			param_block[i - 2].style.display = "block";
		} else {
			param_block[i - 2].style.display = "none";
		}
	}
}
