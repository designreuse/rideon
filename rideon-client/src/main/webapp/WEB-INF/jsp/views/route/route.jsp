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
        <div class="row">
            <ol class=" breadcrumb">
                <li><a href="<c:url value="/routes"/>"><spring:message code="jsp.route.routes"/></a></li>
                <li class="active"><spring:message code="jsp.route.route"/> ${routeId}</li>
            </ol>
            <div class="col-lg-12">
                <h4>
                    <span id="name"><em><spring:message code="jsp.form.noname"/></em></span>
                </h4>
            </div>
        </div>
        <br>
        <div class="row">
            <div class="col-sm-4 col-md-3">
                <div class="panel panel-default">
                    <div class=" panel-body">
                        <div class="row">
                            <div class="col-xs-5 pull-left">
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.distance"/></small></h5>
                                        <span class="distance"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.minaltitude"/></small></h5>
                                        <span class="minaltitude"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.totalclimb"/></small></h5>
                                        <span class="totalclimb"></span>
                                    </li>
                                </ul>
                            </div>
                            <div class="col-xs-5 pull-right">
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.maxaltitude"/></small></h5>
                                        <span class="maxaltitude"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.totaldownhill"/></small></h5>
                                        <span class="totaldownhill"></span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h4>Segmentos</h4>
                                <div class="list-group" id="segments">
                                    <c:choose>
                                        <c:when test="${fn:length(segments.features) gt 0}">
                                            <c:forEach var="segment" items='${segments.features}' varStatus="loopCount">
                                                <a href='<c:url value="/segments/${segment.id}"/>' class='list-group-item segment'><spring:message code="jsp.segment.segment"/> ${segment.id}</a>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <em><spring:message code="jsp.route.nosegments"/></em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-12">
                    <a href="<c:url value="/routes/${routeId}.gpx"/>" class="btn btn-primary btn-lg col-lg-12" style="display: inline-block">
                        <i class="fa fa-download fa-lg"></i> <spring:message code="jsp.route.downloadroute"/>
                    </a>
                </div>

            </div>
            <div class="col-sm-8 col-md-9">
                <div id="map" class="panel panel-default col-lg-12" style="height:400px;"></div>
                <br>
                <div id="chart_div" class="panel panel-default col-lg-12" ></div>
                <br>
                <div class="panel panel-default">
                    <div class="panel-body">
                        <h4><spring:message code="jsp.form.description"/>:</h4>
                        <p id="description" ><em><spring:message code="jsp.form.nodescription"/></em></p>
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(".route-item").addClass("active");
            google.load('visualization', '1.0', {'packages': ['corechart']});
            google.setOnLoadCallback(function() {
                var url = "<c:url value="/routes/${routeId}.json"/>";
                loadPracticeInfo(url);
            });

            function loadPracticeInfo(url) {
                $("#map").html("");
                drawMapAndChar(url, "map", "chart_div");
            }

        </script>
    </body>
</html>
