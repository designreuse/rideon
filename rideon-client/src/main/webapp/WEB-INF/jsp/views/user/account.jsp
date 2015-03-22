<%--
    Document   : profile
    Created on : 01-sep-2013, 12:10:59
    Author     : Fer
--%>
<%@include file="../../includes/includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<h1><small>${user.fullName}</small></h1>
<hr>
<div class="row">
    <div class="col-sm-7 col-md-6">

        <div class="row">
            <div class="col-xs-4 col-sm-5" >
                <div style="max-width: 160px; margin: auto;">
                    <a href="<c:url value='${user.username}/image'/>">
                        <img src="<c:url value='${user.username}/image'/>" class="img-responsive img-thumbnail" />
                    </a>
                </div>
            </div>

            <div class="col-xs-8 col-sm-7" >
                <blockquote><p>${user.fullName}</p>
                    <small>${user.username}</small>
                    <div class="hidden-xs">
                        <i class="glyphicon glyphicon-envelope"></i>   ${user.email}<br>
                        <i class="glyphicon glyphicon-calendar"></i>   <fmt:formatDate pattern="dd/MM/yyyy" value="${user.bornDate}"/><br>
                        <c:choose>
                            <c:when test="${not empty user.town or not empty user.province or not empty user.country}">
                                <i class="glyphicon glyphicon-home"></i>
                                <c:if test="${not empty user.town}">
                                    ${user.town},
                                </c:if>
                                <c:if test="${not empty user.province}">
                                    ${user.province}
                                </c:if>
                                <c:if test="${not empty user.country}">
                                    ${user.country}
                                </c:if>
                            </c:when>
                        </c:choose>
                        <br><br>
                        <a class="btn btn-primary btn-sm" href="<c:url value='${user.username}/edit'/>">
                            <i class="glyphicon glyphicon-edit"></i>
                            <spring:message code="jsp.user.editprofile"/>
                        </a>
                    </div>
                </blockquote>
            </div>
            <div class="visible-xs">
                <blockquote>
                    <i class="glyphicon glyphicon-envelope"></i>   ${user.email}<br>
                    <i class="glyphicon glyphicon-calendar"></i>   <fmt:formatDate pattern="dd/MM/yyyy" value="${user.bornDate}"/><br>
                    <i class="glyphicon glyphicon-home"></i>   ${user.town}, ${user.province}, ${user.country}<br><br>
                </blockquote>
            </div>
        </div>
        <div class="visible-xs" style="text-align: center">
            <div class="btn-group">
                <a class="btn btn-primary" href="<c:url value='/practices'/>"><spring:message code="jsp.practice.practices"/></a>
                <a class="btn btn-primary" href="<c:url value='/events'/>"><spring:message code="jsp.event.events"/></a>
                <a class="btn btn-primary" href="<c:url value='/groups'/>"><spring:message code="jsp.group.groups"/></a>
                <div class="btn-group" style="text-align: left">
                    <button class="btn btn-primary dropdown-toggle" data-toggle="dropdown">Más <span class="caret"></span></button>
                    <ul class="dropdown-menu pull-right" style="left:auto">
                        <li><a  href="<c:url value='${user.username}/friends'/>"><spring:message code="jsp.user.friends"/></a></li>
                        <li><a href="<c:url value='${user.username}/edit'/>"><spring:message code="jsp.user.editprofile"/></a></li>
                    </ul>
                </div>
            </div>
        </div>
        <br>
        <br>

        <div class="row">
            <div class="col-sm-12">
                <legend>
                    <spring:message code="jsp.user.yourbikes"/>
                    <a  class="btn btn-success btn-xs pull-right" href="<c:url value='${user.username}/bike'/>">
                        <span class="fa fa-plus" style="margin-right :5px"></span>
                        <spring:message code="jsp.bicycle.addBike"/>
                    </a>
                </legend>

                <c:choose>
                    <c:when test="${fn:length(bicycles) gt 0}">
                        <c:forEach var="bike" items="${bicycles}">
                            <c:if test="${bike.isPrincipal}">
                                <div class=" well well-sm">
                                    <div class="row">
                                        <div class="col-xs-5 col-md-5" >
                                            <div style="max-width: 200px; margin: auto;">
                                                <a href="<c:url value='${user.username}/bike/${bicycles[0].id}/image'/>">
                                                    <img src="<c:url value='${user.username}/bike/${bicycles[0].id}/image'/>" class="img-responsive img-thumbnail"/>
                                                </a>
                                            </div> 
                                        </div> 
                                        <div class="col-xs-7 col-md-6" >
                                            <blockquote>
                                                <strong><spring:message code="jsp.bicycle.brand"/>: </strong>${bike.brand}<br>
                                                <strong><spring:message code="jsp.bicycle.model"/>: </strong>${bike.model}<br>

                                                <strong><spring:message code="jsp.bicycle.buyDate"/>: </strong>
                                                <fmt:parseDate pattern="yyyy-MM-dd" value="${bike.buyDate}" var="parsedStatusDate" />
                                                <fmt:formatDate pattern="dd/MM/yyyy" value="${parsedStatusDate}"/><br>
                                                <strong><spring:message code="jsp.bicycle.kilometers"/>: </strong>${bike.kilometers / 1000} Km
                                            </blockquote>
                                            <div class="pull-right">
                                                <a class="btn btn-primary btn-xs" href="<c:url value='${user.username}/bike/${bike.id}'/>">
                                                    <i class="glyphicon glyphicon-edit"></i>
                                                    <spring:message code="jsp.form.edit"/>
                                                </a>
                                                <a class="btn btn-xs btn-danger" id="removeBike" data-bikeid="${bike.id}" href="#" >
                                                    <i class="glyphicon glyphicon-trash"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                            </c:if>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${fn:length(bicycles) gt 0}">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <table class="table table-condensed">
                                            <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>
                                                        <spring:message code="jsp.bicycle.brand"/>
                                                    </th>
                                                    <th>
                                                        <spring:message code="jsp.bicycle.model"/>
                                                    </th>
                                                    <th>
                                                        <spring:message code="jsp.bicycle.buyDate"/>
                                                    </th>
                                                    <th>
                                                        <span class="hidden-xs"><spring:message code="jsp.bicycle.kilometers"/></span>
                                                        <span class="visible-xs">Km</span>
                                                    </th>
                                                </tr>
                                            </thead>
                                            <c:forEach var="bike" items="${bicycles}">
                                                <c:if test="${not bike.isPrincipal}">
                                                    <tr>
                                                        <td>                                                            
                                                            <div class="btn-group">
                                                                <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
                                                                    <img src="<c:url value='${user.username}/bike/${bicycles[0].id}/image'/>" class="" style="max-width: 20px"/>
                                                                    <span class="caret"></span>
                                                                </button>
                                                                <ul class="dropdown-menu" role="menu">
                                                                    <li>
                                                                        <a href="<c:url value='${user.username}/bike/${bike.id}'/>">
                                                                            <i class="glyphicon glyphicon-edit"></i> 
                                                                            <spring:message code="jsp.form.edit"/>
                                                                        </a>
                                                                    </li>
                                                                    <li>
                                                                        <a id="makePrincipal" data-bikeid="${bike.id}" href="#">
                                                                            <i class="glyphicon glyphicon-check"></i> Hacer Principal
                                                                        </a>
                                                                    </li>
                                                                    <li>
                                                                        <a id="removeBike" data-bikeid="${bike.id}" href="#" >
                                                                            <i class="glyphicon glyphicon-trash"></i> <spring:message code="jsp.form.delete"/>
                                                                        </a>
                                                                    </li>
                                                                </ul>
                                                            </div>

                                                        </td>
                                                        <td>
                                                            ${bike.brand}
                                                        </td>
                                                        <td>
                                                            ${bike.model}
                                                        </td>
                                                        <td>
                                                            <fmt:parseDate pattern="yyyy-MM-dd" value="${bike.buyDate}" var="parsedStatusDate" />
                                                            <fmt:formatDate pattern="dd/MM/yyyy" value="${parsedStatusDate}"/>
                                                        </td>
                                                        <td>
                                                            ${bike.kilometers}
                                                        </td>

                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <div class="col-lg-12">
                            <em><spring:message code="jsp.profile.noBikePresent"/></em>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>
    <div class=" hidden-xs col-sm-5 col-md-4 col-lg-4  col-md-offset-2 col-lg-offset-2">
        <div class="panel panel-default">
            <div class="panel-heading">
                <span class=""><spring:message code="jsp.user.friends"/></span>
                <a class="btn btn-xs btn-primary pull-right" href="<c:url value='/${user.username}/friends'/>"><spring:message code="jsp.user.viewfriends"/></a>
            </div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${fn:length(friends) gt 0}">
                        <table class="table table-hover table-bordered">
                            <c:forEach var="friend" items="${friends}">
                                <tr>
                                    <td>
                                        <a style="display:inline-block; width: 100%; height: 100%;" href="<c:url value='/${friend.username}'/>">
                                            <img src="<c:url value='/${friend.username}/thumbnail'/>" class="media img-thumbnail" style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                            ${friend.fullName}
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <em><spring:message code="jsp.message.nofriendsfound"/></em>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">Prácticas Recientes
                <a class="btn btn-xs btn-primary pull-right" href="<c:url value='/practices'/>">Ver Prácticas</a>
            </div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${fn:length(practices) gt 0}">
                        <table class="table table-hover table-bordered">
                            <c:forEach items="${practices}" var="practice">
                                <tr>
                                    <td class="">
                                        <a style="display:inline-block; width: 100%; height: 100%;" href="<c:url value='/practices/${practice.id}'/>">
                                            <fmt:formatDate pattern="dd/MM/yyyy" value="${practice.practiceDate}"/> - <span class="practiceDuration" data-duration="${practice.duration}"></span>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="well well-large">
                            <em>No hay prácticas recientes</em>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading"><spring:message code="jsp.group.groups"/></div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${fn:length(groups) gt 0}">
                        <table class="table table-hover table-bordered">
                            <c:forEach items="${groups}" var="group">
                                <tr>
                                    <td class="">
                                        <a style="display:inline-block; width: 100%; height: 100%;" href="<c:url value='/groups/${group.id}'/>">
                                            <img  src="<c:url value='/groups/${group.id}/thumbnail'/>" class="media img-thumbnail" style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                            ${group.name}
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="well well-large">
                            <em><spring:message code="jsp.group.message.nogroupsfound"/></em>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading"><spring:message code="jsp.event.events"/></div>
            <div class="panel-body">
                <h4><spring:message code="jsp.event.hangouts"/></h4>
                <c:choose>
                    <c:when test="${fn:length(events) gt 0}">
                        <table class="table table-hover table-bordered">
                            <c:forEach items="${events}" var="event">
                                <c:if test="${event.eventType == 'HANGOUT'}">
                                    <tr>
                                        <td>
                                            <a style="display:inline-block; width: 100%; height: 100%;" href="<c:url value='/events/${event.id}'/>">
                                                <img  src="<c:url value='/events/${event.id}/thumbnail'/>" class="media img-thumbnail" style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                                ${event.name}
                                            </a>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="well well-large">
                            <em><spring:message code="jsp.events.noeventsfound"/></em>
                        </div>
                    </c:otherwise>
                </c:choose>
                <h4><spring:message code="jsp.event.challenges"/></h4>
                <c:choose>
                    <c:when test="${fn:length(events) gt 0}">
                        <table class="table table-hover table-bordered">
                            <c:forEach items="${events}" var="event">
                                <c:if test="${event.eventType == 'CHALLENGE'}">
                                    <tr>
                                        <td class="">
                                            <a style="display:inline-block; width: 100%; height: 100%;" href="<c:url value='/events/${event.id}'/>">
                                                <img  src="<c:url value='/events/${event.id}/thumbnail'/>" class="media img-thumbnail" style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                                ${event.name}
                                            </a>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="well well-large">
                            <em><spring:message code="No hay eventos disponibles"/></em>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        $(".account-item").addClass("active");
        init();
    });

    $.each($(".practiceDuration"), function(element) {
        var duration = $(this).data("duration");
        duration = parsePeriod(duration);
        $(this).html(duration);
    }
    );

    function init() {
        $("a#removeBike").click(function(event) {
            event.preventDefault();
            var url = "<c:url value="/${user.username}/bike/"/>";
            url = url + $(this).data("bikeid");
            $.ajax({
                type: 'DELETE',
                url: url,
                dataType: "html",
                success: function(response) {
                    window.location.href = "<c:url value="/${user.username}" />";
                },
                error: function(e) {
                    alert("Some error ocurred. Please try it again later");
                    console.log(e);
                }
            });
        });

        $("a#makePrincipal").click(function(event) {
            event.preventDefault();
            var bikeid = $(this).data("bikeid");
            bicycle = {"id": bikeid,
                "isPrincipal": "true"};
            var jsonData = JSON.stringify(bicycle);
            var url = '<c:url value="/${user.username}/bike/"/>';
            url = url + bikeid;
            $.ajax({
                type: 'PUT',
                url: url,
                data: jsonData,
                contentType: "application/json",
                // The type of data that is getting returned.
                dataType: "html",
                success: function(response) {
                    console.log("res" + response);
                    window.location.href = "<c:url value="/${user.username}" />";
                },
                error: function(e) {
                    alert("Some error ocurred. Please try it again later");
                    console.log(e);
                }
            });
        });
    }
</script>
