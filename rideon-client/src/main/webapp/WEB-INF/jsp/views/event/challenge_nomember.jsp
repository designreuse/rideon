<%-- 
    Document   : Events
    Created on : 30-sep-2013, 8:54:50
    Author     : Fer
--%>
<%@include file="../../includes/includes.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<body>
    <div class="row">
        <ol class=" breadcrumb">
            <li><a href="<c:url value="/events"/>"><spring:message code="jsp.event.events"/></a></li>
            <li class="active">${event.name}</li>
        </ol>
        <div class="col-sm-7 col-md-8">
            <div class="well">
                <div class="row">
                    <div class="col-sm-4 col-md-3 col-lg-2">
                        <div style="max-width: 100px; margin:auto">
                            <a href="<c:url value="/events/${event.id}/image"/>">
                                <img src="<c:url value="/events/${event.id}/image"/>" class="img-thumbnail img-responsive"/>
                            </a>
                        </div>
                    </div>
                    <div class="col-sm-8 col-md-9 col-lg-10">
                        <h4>
                            <c:if test="${event.status eq 'CLOSE'}">
                                <span class="label label-danger"><i class="fa fa-lock"></i></span>
                                </c:if>
                                ${event.name}
                        </h4>
                        <blockquote class="">
                            <c:choose>
                                <c:when test="${not empty event.description}">
                                    ${event.description}
                                </c:when>
                                <c:otherwise>
                                    <em><spring:message code="jsp.form.nodescription"/></em>
                                </c:otherwise>
                            </c:choose>
                        </blockquote>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4" >
                        <h5><small><spring:message code="jsp.event.challenge.date"/></small></h5>
                        <span class="btn btn-sm btn-default"><i class="glyphicon glyphicon-calendar"></i> <fmt:formatDate type="both" value="${event.eventDate}" pattern="dd-MM-yyyy hh:mm" /></span>
                    </div>

                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <c:choose>
                        <c:when test="${event.routeId eq null and event.segmentId eq null}">
                            <div class="panel panel-default panel-body" style="text-align: center">
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div id="map" class="panel panel-default" style="height:300px;"></div>
                            <div id="practice-menu"  style="margin-top: 10px; margin-bottom: 5px; height: 35px; ">
                                <div class="btn-group pull-right " >
                                    <c:if test="${event.routeId ne null}">
                                        <a href="<c:url value="/routes/${event.routeId}"/>"  class="btn btn-sm btn-primary">
                                            <span class="fa fa-flag-checkered" style="margin-right: 5px;"></span>
                                            <spring:message code="jsp.route.viewroute"/></a>
                                        <a href="<c:url value="/routes/${event.routeId}.gpx"/>"  class="btn btn-sm btn-primary">
                                            <span class="fa fa-download" style="margin-right: 5px;"></span><spring:message code="jsp.form.downloadgpx"/>
                                        </a>
                                    </c:if>
                                    <c:if test="${event.segmentId ne null}">
                                        <a href="<c:url value="/segments/${event.segmentId}"/>"  class="btn btn-sm btn-primary">
                                            <span class="fa fa-flag-checkered" style="margin-right: 5px;"></span>
                                            <spring:message code="jsp.segment.viewsegment"/></a>
                                        <a href="<c:url value="/segments/${event.segmentId}.gpx"/>"  class="btn btn-sm btn-primary">
                                            <span class="fa fa-download" style="margin-right: 5px;"></span><spring:message code="jsp.form.downloadgpx"/>
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                            <div id="chart_div" class="panel panel-default col-lg-12"></div>

                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <h1><small><spring:message code="jsp.event.wall"/></small></h1>
            <div class="panel panel-default">
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${fn:length(messages) gt 0}">
                            <ul class="media-list">
                                <c:forEach var="message" items="${messages}">
                                    <li class="media list-group-item " style="overflow:visible;">
                                        <a class="pull-left" href="<c:url value='/${message.owner.username}'/>" >
                                            <img class="media-object img-thumbnail" style="margin:auto" src="<c:url value='/${message.owner.username}/thumbnail'/>" />
                                        </a>
                                        <div class="media-body" style="overflow:visible;">
                                            <div class="media-heading">
                                                <a class="btn btn-default btn-xs dropdown-toggle pull-right" data-mid="${message.id}"><span class="glyphicon glyphicon-trash"></span></a>
                                                <b><small><a href="<c:url value='/${message.owner.username}'/>">${message.owner.username} </a>- ${message.writtingDate}</small></b>
                                            </div>
                                            <p class="caption list-group-item-heading">${message.text}</p>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <em><spring:message code="jsp.message.nomessage"/></em>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class=" col-sm-5 col-md-4  ">
            <div class="panel panel-default hidden-xs">
                <div class="panel-heading">
                    <span class=""><spring:message code="jsp.event.members"/></span>
                </div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${fn:length(members) gt 0}">
                            <table class="table table-hover table-bordered">
                                <c:forEach var="member" items="${members}">
                                    <tr>
                                        <td>
                                            <a href="<c:url value='/${member.username}'/>" style="display:inline-block; width: 100%; height: 100%;">
                                                <img src="<c:url value='/${member.username}/thumbnail'/>" class="media img-thumbnail" style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                                ${member.fullName}
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:when>
                    </c:choose>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span><spring:message code="jsp.event.results"/></span>
                    

                </div>
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
                                            <c:if test="${loopCount.count gt 3}">
                                                <div class="col-xs-1 label label-default" style="background-color: #D2B48C; top: 7px; min-width: 25px;">
                                                    ${loopCount.count}
                                                </div>
                                            </c:if>
                                            <div class="col-xs-10" >
                                                <c:choose>
                                                <c:when test="${event.segmentId ne null}">
                                                    <a href="<c:url value='/practices/${result.id.practiceId}'/>" style="display:inline-block; width: 100%; height: 100%;">
                                                        <img src="<c:url value='/${result.owner.username}/thumbnail'/>" class="media img-thumbnail" style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                                        ${result.owner.fullName} (<span class="resultDuration" data-duration="${result.duration}"></span> )
                                                    </a>
                                                </c:when>
                                                    <c:otherwise>
                                                    <a href="<c:url value='/practices/${result.id}'/>" style="display:inline-block; width: 100%; height: 100%;">
                                                        <img src="<c:url value='/${result.owner.username}/thumbnail'/>" class="media img-thumbnail" style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                                        ${result.owner.fullName} (<span class="resultDuration" data-duration="${result.duration}"></span> )
                                                    </a>
                                                    </c:otherwise>
                                                </c:choose>
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
    </div>

    <script>
        if ('${event.routeId}' !== '' | '${event.segmentId}' !== '') {
            google.load('visualization', '1.0', {'packages': ['corechart']});
            google.setOnLoadCallback(function() {
                var url = '<c:url value="/events/${event.id}/routes.json"/>';
                loadPracticeInfo(url);
            });
        }
        $(".event-item").addClass("active");

        $("a[data-mid]").click(function(event) {
            event.preventDefault();
            var baseUrl = "<c:url value="/events/${event.id}/message/"/>";
            var fullUrl = baseUrl + $(this).data("mid");
            $.ajax({
                url: fullUrl,
                type: 'DELETE',
                // The type of data that is getting returned.
                dataType: "html",
                success: function(response) {
                    window.location.href = "<c:url value="/events/${event.id}" />";
                },
                error: function(e) {
                    alert("Some error ocurred. Please try it again later");
                    console.log(e);
                }
            });
        });

        $.each($(".resultDuration"), function(element) {
            var duration = $(this).data("duration");
            duration = parsePeriod(duration);
            $(this).html(duration);
        }
        );


        function loadPracticeInfo(url) {
            $("#map").html("");
            drawMapAndChar(url, "map", "chart_div");
        }

        $("#leave").click(function(e) {
            e.preventDefault();
            var url = '<c:url value='/events/${event.id}/members/${user.username}'/>';

            $.ajax({
                url: url,
                type: 'DELETE',
                // The type of data that is getting returned.
                dataType: "html",
                success: function(response) {
                    console.log("res" + response);
                    window.location.href = "<c:url value="/events/" />";
                },
                error: function(e) {
                    alert("Some error ocurred. Please try it again later");
                    console.log(e);
                }
            });
        });
    </script>
</body>