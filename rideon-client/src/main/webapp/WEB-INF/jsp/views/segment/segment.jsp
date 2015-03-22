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
                <li><a href="<c:url value="/segments"/>"><spring:message code="jsp.segment.segments"/></a></li>
                <li class="active"><spring:message code="jsp.segment.segment"/> ${segmentId}</li>
            </ol>
            <div class="col-lg-12">
                <h4>
                    <span id="name"><em><spring:message code="jsp.form.noname"/></em></span>
                </h4>
            </div>
        </div>
        <br>
        <div class="row">
            <div class="col-sm-4 col-md-4">
                <div class="panel panel-default">
                    <div class=" panel-body">
                        <div class="row">
                            <div class="col-xs-5 pull-left">
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.type"/></small></h5>
                                        <span class="type"></span>
                                    </li>
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
                            <div class="col-xs-5 pull-right" style="left:-15px">
                                <ul class="list-unstyled">
                                    <li><h5><small><spring:message code="jsp.practice.category"/></small></h5>
                                        <span class="category"><em><spring:message code="jsp.segment.nocategory"/></em></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.maxaltitude"/></small></h5>
                                        <span class="maxaltitude"></span>
                                    </li>
                                    <li><h5><small><spring:message code="jsp.practice.totaldownhill"/></small></h5>
                                        <span class="totaldownhill"></span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-12">
                    <a href="<c:url value="/segments/${segmentId}.gpx"/>" class="btn btn-primary col-lg-12">
                        <i class="fa fa-download fa-lg"></i> <spring:message code="jsp.segment.downloadsegment"/>
                    </a>
                </div>
                <br>
                <div class="panel panel-default">
                    <div class="panel-heading"><spring:message code="jsp.form.bestresults"/></div>
                    <div class="panel-body">
                        <c:choose>
                            <c:when test="${fn:length(results) gt 0}">
                                <table class="table table-hover table-bordered">
                                    <c:forEach var="result" items="${results}"  varStatus="loopCount">
                                        <tr>
                                            <td>
                                                <c:if test="${loopCount.count eq 1}">
                                                    <div class="col-xs-1 label label-warning" style="top: 7px; min-width: 25px;">
                                                        <i class="fa fa-lg fa-trophy "></i>
                                                    </div>
                                                </c:if>
                                                <c:if test="${loopCount.count eq 2}">
                                                    <div class="col-xs-1 label" style="background-color: #C0C0C0; ;top: 7px; min-width: 25px;">
                                                        <i class="fa fa-lg fa-trophy "></i>
                                                    </div>
                                                </c:if>
                                                <c:if test="${loopCount.count eq 3}">
                                                    <div class="col-xs-1 label label-default" style="background-color: #D2B48C; top: 7px; min-width: 25px;">
                                                        <i class="fa fa-lg fa-trophy "></i>
                                                    </div>
                                                </c:if>
                                                <div class="col-xs-10" >
                                                    <a href="<c:url value='/practices/${result.id.practiceId}'/>" style="display:inline-block; width: 100%; height: 100%;">
                                                        <img src="<c:url value='/${result.owner.username}/thumbnail'/>" class="media img-thumbnail" style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                                        ${result.owner.fullName} (<span class="resultDuration" data-duration="${result.duration}"></span> )
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <em><spring:message code="jsp.segment.message.noresultssfound"/></em>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </div>
            <div class="col-sm-8 col-md-8">
                <div id="map" class="panel panel-default col-lg-12" style="height:400px;"></div>
                <br>
                <div id="chart_div" class="panel panel-default col-lg-12" ></div>
                <br>
                <div class="panel panel-default">
                    <div class="panel-body">
                        <h4><spring:message code="jsp.form.description"/></h4>
                        <p id="description" ><em><spring:message code="jsp.form.nodescription"/></em></p>
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(".segment-item").addClass("active");
            google.load('visualization', '1.0', {'packages': ['corechart']});
            google.setOnLoadCallback(function() {
                var url = "<c:url value="/segments/${segmentId}.json"/>";
                loadPracticeInfo(url);
            });

            function loadPracticeInfo(url) {
                $("#map").html("");
                drawMapAndChar(url, "map", "chart_div");
            }
            $.each($(".resultDuration"), function(element) {
                var duration = $(this).data("duration");
                duration = parsePeriod(duration);
                $(this).html(duration);
            }
            );
            function parsePeriod(miliseconds) {
                var seconds = parseInt(miliseconds) / 1000;
                var min = Math.floor((((seconds % 31536000) % 86400) % 3600) / 60);
                var hor = Math.floor(((seconds % 31536000) % 86400) / 3600);
                min = min % 60;
                return hor + "h:" + min + "m";
            }
        </script>
    </body>
</html>
