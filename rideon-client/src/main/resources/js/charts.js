/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function distanceBetweenPoints(lat0, lon0, lat1, lon1) {
    var Geographic = new OpenLayers.Projection("EPSG:4326");
    var Mercator = new OpenLayers.Projection("EPSG:900913");
//    var point0 = new OpenLayers.Geometry.Point(lon0, lat0).transform(Geographic, Mercator);
//    var point1 = new OpenLayers.Geometry.Point(lon1, lat1).transform(Geographic, Mercator);
//    return point0.distanceTo(point1);
    var point0 = new OpenLayers.Geometry.Point(lon0, lat0);
    var point1 = new OpenLayers.Geometry.Point(lon1, lat1);
    var line = new OpenLayers.Geometry.LineString([point0, point1]);
    return line.getGeodesicLength(new OpenLayers.Projection("EPSG:4326"));
}


function drawChart(xmlDoc, div, map) {
// Load the Visualization API and the piechart package.

// Set a callback to run when the Google Visualization API is loaded.
    var points = xmlDoc.getElementsByTagName("trkpt");
    var dist = 0;
    var incl = 0;
    var dataTable = new google.visualization.DataTable();
    dataTable.addColumn('number', 'Distance');
    dataTable.addColumn('number', 'Altitude');
    dataTable.addColumn({'type': 'string', 'role': 'tooltip', 'p': {'html': true}});
    for (index = 0; index < points.length; ++index) {
        var elevation = points[index].getElementsByTagName("ele")[0];
        var alt = parseFloat(elevation.firstChild.nodeValue);
        if (index > 0) {
            var lat0 = parseFloat(points[index - 1].getAttribute("lat"));
            var lat1 = parseFloat(points[index].getAttribute("lat"));
            var lon0 = parseFloat(points[index - 1].getAttribute("lon"));
            var lon1 = parseFloat(points[index].getAttribute("lon"));
            var distBet = distanceBetweenPoints(lat0, lon0, lat1, lon1);
            dist = dist + distBet;
            var elevation2 = points[index - 1].getElementsByTagName("ele")[0];
            var alt2 = parseFloat(elevation2.firstChild.nodeValue);
            incl = (alt - alt2) * 100 / distBet;
            incl = Math.round(incl * 100) / 100;
        }
        var kms = Math.round(dist / 1000 * 100) / 100;
//        dataTable.addRow([{v: kms, f: 'Distance : ' + kms + ' km <br>' + 'Inclination : ' + incl + ' %'}, {v: alt, f: alt + ' m', p: {lon: lon1, lat: lat1}}]);
        var tooltip = "<div style='width: 140px; padding:5px 5px 5px 5px;'>\n\
                        <table>\n\
                            <tr><td><b>Distancia:</b> " + kms + " km</li>\n\
                            <tr><td><b>Altitud:</b> " + alt + " m</td></tr>\n\
                            <tr><td><b>Pendiente</b>: " + incl + " %</td></tr>\n\
                            </table>\n\
                        </div>";
        dataTable.addRow([kms, {v: alt, p: {lon: lon1, lat: lat1}}, tooltip]);
    }
    // Set chart options
    var options = {'title': '',
        'width': '100%',
        'height': 200,
        vAxis: {title: 'Altitude (m)'},
        hAxis: {title: 'Distance (km)'},
        tooltip: {isHtml: true},
        legend: 'none'
    };


    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.AreaChart(document.getElementById(div));

    google.visualization.events.addListener(chart, 'onmouseover', function(rowColumn) {
        var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
        var toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection

        var point = dataTable.getProperties(rowColumn.row, rowColumn.column);
        var lonlat = new OpenLayers.LonLat(point.lon, point.lat).transform(fromProjection, toProjection);
        addCurrentMarker(map, lonlat);
    });
    google.visualization.events.addListener(chart, 'onmouseout', function(rowColumn) {
        removeCurrentMarker(map);
    });

    chart.draw(dataTable, options);
}

function drawChartFromJsonString(jsonString, div, map) {
// Load the Visualization API and the piechart package.

// Set a callback to run when the Google Visualization API is loaded.
    var geoJsonObject = jQuery.parseJSON(jsonString);
//    console.log(geoJsonObject);
    
    var points = geoJsonObject.geometry.coordinates;
//    console.log(points);
    
    var dist = 0;
    var incl = 0;
    var dataTable = new google.visualization.DataTable();
    dataTable.addColumn('number', 'Distance');
    dataTable.addColumn('number', 'Altitude');
    dataTable.addColumn({'type': 'string', 'role': 'tooltip', 'p': {'html': true}});
    
    for (index = 0; index < points.length; ++index) {
        var elevation = points[index][2];
        var alt = parseFloat(elevation);
        if (index > 0) {
            var lat0 = parseFloat(points[index - 1][1]);
            var lat1 = parseFloat(points[index][1]);
            var lon0 = parseFloat(points[index - 1][0]);
            var lon1 = parseFloat(points[index][0]);
            var distBet = distanceBetweenPoints(lat0, lon0, lat1, lon1);
            dist = dist + distBet;
            var elevation2 = points[index - 1][2];
            var alt2 = parseFloat(elevation2);
            incl = (alt - alt2) * 100 / distBet;
            incl = Math.round(incl * 100) / 100;
        }
        var kms = Math.round(dist / 1000 * 100) / 100;
//        dataTable.addRow([{v: kms, f: 'Distance : ' + kms + ' km <br>' + 'Inclination : ' + incl + ' %'}, {v: alt, f: alt + ' m', p: {lon: lon1, lat: lat1}}]);
        var tooltip = "<div style='width: 140px; padding:5px 5px 5px 5px;'>\n\
                        <table>\n\
                            <tr><td><b>Distancia:</b> " + kms + " km</li>\n\
                            <tr><td><b>Altitud:</b> " + alt + " m</td></tr>\n\
                            <tr><td><b>Pendiente</b>: " + incl + " %</td></tr>\n\
                            </table>\n\
                        </div>";
        dataTable.addRow([kms, {v: alt, p: {lon: lon1, lat: lat1}}, tooltip]);
    }
    // Set chart options
    var options = {'title': '',
        'width': '100%',
        'height': 200,
        vAxis: {title: 'Altitude (m)'},
        hAxis: {title: 'Distance (km)'},
        tooltip: {isHtml: true},
        legend: 'none'
    };


    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.AreaChart(document.getElementById(div));

    google.visualization.events.addListener(chart, 'onmouseover', function(rowColumn) {
        var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
        var toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection

        var point = dataTable.getProperties(rowColumn.row, rowColumn.column);
        var lonlat = new OpenLayers.LonLat(point.lon, point.lat).transform(fromProjection, toProjection);
        addCurrentMarker(map, lonlat);
    });
    google.visualization.events.addListener(chart, 'onmouseout', function(rowColumn) {
        removeCurrentMarker(map);
    });

    chart.draw(dataTable, options);
}