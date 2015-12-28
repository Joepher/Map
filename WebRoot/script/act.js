function createMap() {
	var map = new BMap.Map("allmap");

	var point = new BMap.Point(116.404, 39.915); // ����������
	map.centerAndZoom(point, 15); // ��ʼ����ͼ���������ĵ�����͵�ͼ����
	map.addControl(new BMap.MapTypeControl()); // ��ӵ�ͼ���Ϳؼ�
	map.setCurrentCity("����"); // ���õ�ͼ��ʾ�ĳ��� �����Ǳ������õ�
	map.enableScrollWheelZoom(true); // ��������������

	return map;
}

function show(positionArray, pathArray, curArray) {
	var map = createMap();
	//addPositionMarker(positionArray, map);
	//addPathMarker(pathArray, map);
	addCurMarker(curArray, map);
}

function addPositionMarker(positionArray, map) {
	if (positionArray != null) {
		if (positionArray.length == 2) {
			var start = new BMap.Point(positionArray[1].longitude,
					positionArray[1].latitude);
			var sicon = new BMap.Icon(
					"http://222.201.139.216/Map/img/icon/start.gif",
					new BMap.Size(50, 50));
			var smarker = new BMap.Marker(start, {
				icon : sicon
			});
			map.addOverlay(smarker);

			var end = new BMap.Point(positionArray[0].longitude,
					positionArray[0].latitude);
			var eicon = new BMap.Icon(
					"http://222.201.139.216/Map/img/icon/end.gif",
					new BMap.Size(50, 50));
			var emarker = new BMap.Marker(end, {
				icon : eicon
			});
			map.addOverlay(emarker);
		} else {
			alert("invalid position");
		}
	} else {
		alert("empty position");
	}
}

function addPathMarker(pathArray, map) {
	if (pathArray != null) {
		for (var i = 0; i < pathArray.length; ++i) {
			var point = new BMap.Point(pathArray[i].longitude,
					pathArray[i].latitude);
			addMarker(map, point);
		}
	} else {
		alert("empty path");
	}
}

function addCurMarker(curArray, map) {
	if (curArray != null) {
		var cpoint = new BMap.Point(curArray[0].longitude, curArray[0].latitude);
		map.centerAndZoom(cpoint, 16);
		var cicon = new BMap.Icon(
				"http://222.201.139.216/Map/img/icon/cur.gif", new BMap.Size(
						23, 42));
		var cmarker = new BMap.Marker(cpoint, {
			icon : cicon
		});
		map.addOverlay(cmarker);
	} else {
		alert("empty cur");
	}
}

function addMarker(map, point) {
	var marker = new BMap.Marker(point);
	map.addOverlay(marker);
}