/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

        function drawMapWithInfo(url, mapDiv, id) {
            map = new OpenLayers.Map(mapDiv);
            var mapnick = new OpenLayers.Layer.OSM();
            map.addLayer(mapnick);
            var layerMarkers = new OpenLayers.Layer.Markers("Markers");
            var currentMarkers = new OpenLayers.Layer.Markers("CurrentMarkers");
            map.addLayer(layerMarkers);
            map.addLayer(currentMarkers);
            map.setCenter("", 1);

            var jsonString;
            var test = new OpenLayers.Layer.Vector("test", {
                style: {strokeColor: "red", strokeWidth: 2, strokeOpacity: 1},
                projection: new OpenLayers.Projection("EPSG:4326"),
                strategies: [new OpenLayers.Strategy.Fixed()],
                protocol: new OpenLayers.Protocol.HTTP({
                    url: url,
                    format: new OpenLayers.Format.GeoJSON({
                        ignoreExtraDims: true
                    }),
                    parseFeatures: function(data) {
                        jsonString = data.responseText;
                        return this.format.read(data.responseText);
                    }
                }),
                eventListeners: {
                    "featuresadded": function() {
                        this.map.zoomToExtent(this.getDataExtent());
                        var startPoint = this.features[0].geometry.components[0];

                        var mark = new OpenLayers.LonLat(startPoint.x, startPoint.y);
                        var size = new OpenLayers.Size(21, 25);
                        var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
                        var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png', size, offset);

                        var layer = this.map.getLayersByName("Markers")[0];
                        layer.addMarker(new OpenLayers.Marker(mark, icon));

                        var feature = this.features[0];
                        var ob = feature.data;
                        $(".minaltitude" + id).html(ob.minaltitude + " m");
                        $(".maxaltitude" + id).html(ob.maxaltitude + " m");
                        $(".totaldownhill" + id).html(Math.round(ob.totaldownhill / 1000 * 10) / 10 + " km");
                        $(".totalclimb" + id).html(Math.round(ob.totalclimb / 1000 * 10) / 10 + " km");
                        $(".distance" + id).html(Math.round(ob.distance / 1000 * 10) / 10 + " km");
                        $(".type" + id).html(getSegmentType(ob.segmentType));
                        $(".category" + id).html(getCategory(ob.category));
                    }
                }
            });
            map.addLayer(test);
        }

function drawMapAndChar(url, mapDiv, charDiv) {
    map = new OpenLayers.Map(mapDiv);
    var mapnick = new OpenLayers.Layer.OSM();
    map.addLayer(mapnick);
    var layerMarkers = new OpenLayers.Layer.Markers("Markers");
    var currentMarkers = new OpenLayers.Layer.Markers("CurrentMarkers");
    map.addLayer(layerMarkers);
    map.addLayer(currentMarkers);
    map.setCenter("", 1);

    var jsonString;
    var test = new OpenLayers.Layer.Vector("test", {
        style: {strokeColor: "red", strokeWidth: 2, strokeOpacity: 1},
        projection: new OpenLayers.Projection("EPSG:4326"),
        strategies: [new OpenLayers.Strategy.Fixed()],
        protocol: new OpenLayers.Protocol.HTTP({
            url: url,
            format: new OpenLayers.Format.GeoJSON({
                ignoreExtraDims: true
            }),
            parseFeatures: function(data) {
                jsonString = data.responseText;
                return this.format.read(data.responseText);
            }
        }),
        eventListeners: {
            "featuresadded": function() {
                this.map.zoomToExtent(this.getDataExtent());
                var startPoint = this.features[0].geometry.components[0];

                var mark = new OpenLayers.LonLat(startPoint.x, startPoint.y);
                var size = new OpenLayers.Size(21, 25);
                var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
                var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png', size, offset);

                var layer = this.map.getLayersByName("Markers")[0];
                layer.addMarker(new OpenLayers.Marker(mark, icon));
                for (var i = 0; i < this.features.length; i++) {
                    var feature = this.features[i];
                    var ob = feature.data;
                    $("#name").html(ob.name);
                    if (ob.description != null) {
//                        $("#description").html(ob.description);
                    }
                    $(".minaltitude").html(ob.minaltitude + " m");
                    $(".maxaltitude").html(ob.maxaltitude + " m");
                    $(".totaldownhill").html(Math.round(ob.totaldownhill / 1000 * 10) / 10 + " km");
                    $(".totalclimb").html(Math.round(ob.totalclimb / 1000 * 10) / 10 + " km");
                    $(".distance").html(Math.round(ob.distance / 1000 * 10) / 10 + " km");

                    $(".type").html(getSegmentType(ob.segmentType));
                    $(".category").html(getCategory(ob.category));
                }
                drawChartFromJsonString(jsonString, charDiv, this.map);
            }
        }
    });
    map.addLayer(test);
}

function getSegmentType(type) {
    if (type === "FLAT") {
        return "Paseo";
    }
    if (type === "DOWNHILL") {
        return "Descenso";
    }
    if (type === "CLIMB") {
        return "Ascenso";
    }
}

function getCategory(type) {
    if (type === "FIRST") {
        return "1º Categoría";
    }
    if (type === "SECOND") {
        return "2º Categoría";
    }
    if (type === "THIRD") {
        return "3º Categoría";
    }
    if (type === "FOURTH") {
        return "4º Categoría";
    }
}

function getCategoryClass(type) {
    if (type === "FIRST") {
        return "label-danger";
    }
    if (type === "SECOND") {
        return "label-warning";
    }
    if (type === "THIRD") {
        return "label-primary";
    }
    if (type === "FOURTH") {
        return "label-info";
    }
}


function drawSearchMap(searchUrl, destinationBaseUrl, resourcesBaseUrl, objType) {
    var map = new OpenLayers.Map("map");
    var mapnick = new OpenLayers.Layer.OSM();
    map.addLayer(mapnick);
    map.setCenter("", 1);
    var layerMarkers = new OpenLayers.Layer.Markers("Markers", {
        projection: new OpenLayers.Projection("EPSG:4326")
    });
    map.addLayer(layerMarkers);




    var routes = new OpenLayers.Layer.Vector("Routes", {
        style: {strokeColor: "red", strokeWidth: 2, strokeOpacity: 1},
        projection: new OpenLayers.Projection("EPSG:4326"),
        strategies: [
//                        new OpenLayers.Strategy.Fixed(),
            new OpenLayers.Strategy.Cluster(),
            new OpenLayers.Strategy.BBOX({resFactor: 1})
        ],
        protocol: new OpenLayers.Protocol.HTTP({
            url: searchUrl,
            format: new OpenLayers.Format.GeoJSON({
                ignoreExtraDims: true
            }),
            parseFeatures: function(data) {
                jsonString = data.responseText;
                $("#routeList").html("<tr><td><em>Ninguna ruta disponible</em></td></tr>");
                return this.format.read(jsonString);
            }
        }), eventListeners: {
            "featuresadded": function() {
                var layer = map.getLayersByName("Markers")[0];
                layer.clearMarkers();
                $("#routeList").html("");
//                            console.log(this.features.length);
                for (var i = 0; i < this.features.length; i++) {
                    var feature = this.features[i];
//                            console.log(feature);
                    var startPoint = feature.geometry;

                    var ob = feature.cluster[0];

                    var mark = new OpenLayers.LonLat(startPoint.x, startPoint.y);
                    var size = new OpenLayers.Size(40, 40);
                    var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
                    var start = new OpenLayers.Icon(resourcesBaseUrl + "marker_start.png", size, offset);
                    var group = new OpenLayers.Icon(resourcesBaseUrl + 'marker_group.png', size, offset);
                    var marker;
                    if (feature.cluster.length === 1) {
                        marker = new OpenLayers.Marker(mark, start.clone());
                        marker.icon.imageDiv.id = ob.fid;
                        if (objType === "Ruta") {
                            if (ob.data.name !== null) {
                                $("#routeList").append("<a class='list-group-item geometry' style='z-index:100' id=" + ob.fid + " href='" + destinationBaseUrl + ob.fid + "'>\n\
                                                            <p class='list-group-item-heading'><b>" + objType + " " + ob.fid + "</b></p>\n\
                                                        <p class='list-group-item-text'><small>" + ob.data.name + "</small></p></a>");
                            } else {
                                $("#routeList").append("<a class='list-group-item geometry' style='z-index:100' id=" + ob.fid + " href='" + destinationBaseUrl + ob.fid + "'>\n\
                                                            <p class='list-group-item-heading'><b>" + objType + " " + ob.fid + "</b></p></a>");
                            }
                        }
                        if (objType === "Segmento") {
                            var line = "<a class='list-group-item geometry' style='z-index:100' id=" + ob.fid + " href='" + destinationBaseUrl + ob.fid + "'>\n\
                                                            <p class='list-group-item-heading'><b>" + objType + " " + ob.fid + "</b>";
                            if (ob.data.segmentType === "FLAT") {
                                line = line + "<span class='label label-default pull-right'  style='top:5px; position:relative'><small>PASEO</small></span>";
                            }
                            else {
                                line = line + "<span class='label label-default pull-right' style='top:-5px; position:relative'><small>" + getSegmentType(ob.data.segmentType) + "</small></span>";
                                line = line + "<br><span class='label " + getCategoryClass(ob.data.category) + " pull-right' style='top:-5px; position:relative'><small>" + getCategory(ob.data.category) + "</small></span>";
                            }
                            var line = line + "</p></a>";
                            $("#routeList").append(line);
                        }
                        marker.events.register("click", marker, function() {

                            window.location.href = destinationBaseUrl + this.icon.imageDiv.id;
                        });
                        marker.events.register("mouseover", marker, function() {
                            $("#" + this.icon.imageDiv.id).addClass("active");
                            $(this.icon.imageDiv).css('cursor', 'pointer');

                        });
                        marker.events.register("mouseout", marker, function() {
                            $("#" + this.icon.imageDiv.id).removeClass("active");
                        });
                    } else {
                        marker = new OpenLayers.Marker(mark, group.clone());
                        $(marker.icon.imageDiv).append("<div style='position:relative; top: -36px; left:8px; width:22px; text-align:center;\n\
                                                                color:black; font-size:.9em' ><b>"
                                + feature.cluster.length + "</b></div>");
                    }

                    layer.addMarker(marker);
                }
            }
        }


    });
    $("a.geometry").click(function(e) {
        e.preventDefault();
        console.log("asdfs")
    });
    $("a.geometry").mouseenter(
            function() {
                console.log("this.id");
            }
    );
    map.addLayer(routes);
    map.setLayerIndex(routes, 0);
    var markers = map.getLayersByName("Markers")[0];
    map.setLayerIndex(markers, 99);
}

function addCurrentMarker(map, lonlat) {
    var size = new OpenLayers.Size(21, 25);
    var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
    var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png', size, offset);
    var layer = map.getLayersByName("CurrentMarkers")[0];
    layer.addMarker(new OpenLayers.Marker(lonlat, icon.clone()));
}

function removeCurrentMarker(map, marker) {
    var layer = map.getLayersByName("CurrentMarkers")[0];
    layer.clearMarkers();
}

function addMarker(map, name, lonlat) {
    var size = new OpenLayers.Size(21, 25);
    var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
    var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png', size, offset);

    var layer = map.getLayersByName("Markers")[0];
    layer.addMarker(new OpenLayers.Marker(lonlat, icon));
}
