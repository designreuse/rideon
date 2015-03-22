<%-- 
    Document   : practices
    Created on : 07-oct-2013, 17:19:27
    Author     : Fer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../includes/includes.jsp" %>
<!DOCTYPE html>
<html>
    <body>
        <ol class=" breadcrumb">
            <li><a href="<c:url value="/events"/>"><spring:message code="jsp.event.events"/></a></li>
            <li class="active">${eventId}</li>
        </ol>
        <div class="row">
            <div class="col-sm-3">
                <div class=" panel panel-default">
                    <div class="panel-heading">Resultados
                    </div>
                    <div class="panel-body">
                        <div class="list-group" id="segmentList">
                            <em>Ningún segmento disponible</em>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div id="map" class="panel panel-default col-lg-12" style="height:400px;"></div>
            </div>
            <br>
        </div>
        <br>
        <div class="row">
            <div class="col-lg-12">
                <div class="pull-right">
                    <button id="saveSegment" class="btn btn-primary"><i class="fa fa-fw fa-save"></i> Guardar</button>
                </div>
            </div>
        </div>

        <script>
            $(".event-item").addClass("active");

            var map = new OpenLayers.Map("map");
            var mapnick = new OpenLayers.Layer.OSM();
            map.addLayer(mapnick);
            map.setCenter("", 1);
            var layerMarkers = new OpenLayers.Layer.Markers("Markers", {
                projection: new OpenLayers.Projection("EPSG:4326")
            });
            var currentMarkers = new OpenLayers.Layer.Markers("CurrentMarkers");
            map.addLayer(layerMarkers);

            var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
            var toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
//            var bounds = map.calculateBounds(map.getCenter()).toGeometry();
//            console.log(bounds);

            var url = "<c:url value="/search/segments"/>";


            var segmentUrl = '<c:url value="/segments/"/>';


            var segments = new OpenLayers.Layer.Vector("Segments", {
                style: {strokeColor: "red", strokeWidth: 2, strokeOpacity: 1},
                projection: new OpenLayers.Projection("EPSG:4326"),
                strategies: [
//                        new OpenLayers.Strategy.Fixed(),
                    new OpenLayers.Strategy.Cluster(),
                    new OpenLayers.Strategy.BBOX({resFactor: 1})
                ],
                protocol: new OpenLayers.Protocol.HTTP({
                    url: url,
                    format: new OpenLayers.Format.GeoJSON({
                        ignoreExtraDims: true
                    }),
                    parseFeatures: function(data) {
                        jsonString = data.responseText;
                        $("#segmentList").html("<tr><td><em>Ningún segmento disponible</em></td></tr>");
//                        console.log(jsonString);
                        return this.format.read(jsonString);
                    }

                }), eventListeners: {
                    "featuresadded": function() {
                        var layer = this.map.getLayersByName("Markers")[0];
                        layer.clearMarkers();
                        $("#segmentList").html("");
//                            console.log(this.features.length);
                        for (var i = 0; i < this.features.length; i++) {
                            var feature = this.features[i];
//                            console.log(feature);
                            var startPoint = feature.geometry;

                            var ob = feature.cluster[0];

                            var mark = new OpenLayers.LonLat(startPoint.x, startPoint.y);
                            var size = new OpenLayers.Size(40, 40);
                            var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
                            var start = new OpenLayers.Icon('<c:url value="/resources/marker_start.png"/>', size, offset);
                            var group = new OpenLayers.Icon('<c:url value="/resources/marker_group.png"/>', size, offset);
                            var marker;
                            if (feature.cluster.length === 1) {
                                marker = new OpenLayers.Marker(mark, start.clone());
                                marker.icon.imageDiv.id = ob.fid;
                                $("#segmentList").append("\
                                            <div class='checkbox list-group-item' id='" + ob.fid + "'>\n\
                                                <label>\n\
                                                    <input type='radio' name='optionsRadios' value='" + ob.fid + "'>\n\
                                                    Segmento " + ob.fid + "\n\
                                                </label>\n\
                                                <div class='pull-right action-buttons'>\n\
                                                    <a  href='" + segmentUrl + ob.fid + "'><span class='divider'></span><i class='fa fa-fw fa-lg fa-eye'></i></a>\n\
                                                </div>\n\
                                            </div>");

                                marker.events.register("click", marker, function() {
                                    var uri = '<c:url value="/segments/"/>';
                                    window.location.href = uri + this.icon.imageDiv.id;
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
//                                feature.cluster.length
                                $(marker.icon.imageDiv).append("<div style='position:relative; top: -36px; left:8px; width:22px; text-align:center;\n\
                                                                color:black; font-size:.9em' ><b>"
                                        + feature.cluster.length + "</b></div>");
                            }

                            layer.addMarker(marker);
                        }
                    }
                }
            });

            map.addLayer(segments);
            map.setLayerIndex(segments, 0);
            var markers = this.map.getLayersByName("Markers")[0];
            map.setLayerIndex(markers, 99);

            $("#saveSegment").click(function(e) {
                e.preventDefault();
                var id = $('input:checked').val();

                $.ajax({
                    type: 'POST',
                    url: '<c:url value="/events/${eventId}/segments"/>',
                    data: {'segmentId': id},
                    // The type of data that is getting returned.
//                    dataType: "html",
                    success: function(response) {
                        console.log("res" + response);
                        window.location.href = "<c:url value="/events/${eventId}" />";
                    },
                    error: function(e) {
                        alert("Some error ocurred. Please try it again later");
                        console.log(e);
                    }
                });

            });
        </script>
    </body>
</html>
