<%-- 
    Document   : home
    Created on : 20-ago-2013, 13:09:39
    Author     : fer
--%>


<%@include file="../includes/includes.jsp" %>
<body>
    <div class="row">
        <h1>RideON</h1>
        <hr>
        <div class="col-md-5 ">
            <h4><spring:message code="jsp.home.lastroutes"/></h4>
            <hr>
            <c:forEach items="${routes}" var="route">
                <div class="row panel panel-default panel-body" style="margin: 0 0 35px 0;">
                    <div class="col-lg-12">
                        <div class="row">
                            <div id="map${route}" class="panel panel-default col-lg-12" style="height:220px;" ></div>
                        </div>
                        <div class="row">
                            <div class="btn-group" style="margin-top: 5px;">
                                <a href="<c:url value="/routes/${route}"/>"  class="btn btn-xs btn-primary">
                                    <span class="fa fa-flag-checkered" style="margin-right: 5px;"></span>
                                    Ver Ruta</a>
                                <a href="<c:url value="/routes/${route}.gpx"/>"  class="btn btn-xs btn-primary">
                                    <span class="fa fa-download" style="margin-right: 5px;"></span>Descargar GPX
                                </a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-4">
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.distance"/></small></h5>
                                        <span class="distance${route}"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.minaltitude"/></small></h5>
                                        <span class="minaltitude${route}"></span>
                                    </li>

                                </ul>
                            </div>
                            <div class="col-xs-4">
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.maxaltitude"/></small></h5>
                                        <span class="maxaltitude${route}"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.totaldownhill"/></small></h5>
                                        <span class="totaldownhill${route}"></span>
                                    </li>
                                </ul>
                            </div>
                            <div class="col-xs-4">
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.totalclimb"/></small></h5>
                                        <span class="totalclimb${route}"></span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>


        <div class="col-md-5 col-md-offset-1">
            <h4><spring:message code="jsp.home.lastsegments"/></h4>
            <hr>
            <c:forEach items="${segments}" var="segment">
                <div class="row panel panel-default panel-body" style="margin: 0 0 35px 0;">
                    <div class="col-lg-12">
                        <div class="row"  >
                            <div id="map${segment}" class="panel panel-default" style="height:180px; " ></div>
                        </div>
                        <div class="row"  >
                            <div class="btn-group" style="margin: 5px 0 5px 0;">
                                <a href="<c:url value="/segments/${segment}"/>"  class="btn btn-xs btn-primary">
                                    <span class="fa fa-flag-checkered" style="margin-right: 5px;"></span>
                                    Ver Segmento
                                </a>
                                <a href="<c:url value="/segments/${segment}.gpx"/>"  class="btn btn-xs btn-primary">
                                    <span class="fa fa-download" style="margin-right: 5px;"></span>Descargar GPX
                                </a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-4">
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.type"/></small></h5>
                                        <span class="type${segment}"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.minaltitude"/></small></h5>
                                        <span class="minaltitude${segment}"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.totalclimb"/></small></h5>
                                        <span class="totalclimb${segment}"></span>
                                    </li>
                                </ul>
                            </div>
                            <div class="col-xs-4" >
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.category"/></small></h5>
                                        <span class="category${segment}"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.maxaltitude"/></small></h5>
                                        <span class="maxaltitude${segment}"></span>
                                    </li>
                                </ul>
                            </div>
                            <div class="col-xs-4">
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.distance"/></small></h5>
                                        <span class="distance${segment}"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.totaldownhill"/></small></h5>
                                        <span class="totaldownhill${segment}"></span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>


    <script>
        var segments = new Array();
        <c:forEach items="${segments}" var="segment" varStatus="provinceStatus">
        segments.push(${segment});
        </c:forEach>

            var routes = new Array();
        <c:forEach items="${routes}" var="route" varStatus="provinceStatus">
            routes.push(${route});
        </c:forEach>

            var routeUrl = '<c:url value="/routes/"/>';
            for (var i = 0; i < routes.length; i++) {
                var id = routes[i];
                var url = routeUrl + id + ".json";
                var mapDiv = "map" + id;
                drawMapWithInfo(url, mapDiv, id);
            }
            var segmentUrl = '<c:url value="/segments/"/>';
            for (var i = 0; i < segments.length; i++) {
                var id = segments[i];
                var url = segmentUrl + id + ".json";
                var mapDiv = "map" + id;
                drawMapWithInfo(url, mapDiv, id);
            }
    </script>
</body>
