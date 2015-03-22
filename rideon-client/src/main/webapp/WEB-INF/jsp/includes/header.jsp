<%--
    Document   : header
    Created on : 01-sep-2013, 12:03:28
    Author     : Fer
--%>
<%@include file="../includes/includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<sec:authentication property="principal" var="user"/>
<c:url value='/${user.username}' var="profileUrl"/>
<c:url value='/${user.username}/thumbnail' var="thumbnailUrl"/>
<div class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <button class="navbar-toggle" data-toggle="collapse" data-target=".menu-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <button style="color:white" class="notifications-button-mobile fa fa-lg fa-flag dropdown-toggle navbar-toggle visible-xs"  data-toggle="dropdown">
            </button>

            <ul class="dropdown-menu pull-right notifications-list" style="width: 100%; z-index: 1005" >
                <li class="disabled"><a><em>No notifications</em></a></li>
            </ul>
            <a class="navbar-brand" href="<c:url value='/home'/>"><spring:message code="jsp.header.home"/></a>
        </div>

        <nav  class="collapse navbar-collapse bs-navbar-collapse menu-collapse" role="navigation" >
            <ul class="nav navbar-nav">
                <li id="account-item" class="visible-xs">
                    <a href="${profileUrl}" >
                        <img class="thumbnail clearfix pull-left image-responsibe"  style="height: 30px; top:-8px; position:relative; margin-right: 5px;" src="${thumbnailUrl}" />
                        <b>${user.username}</b>
                    </a>
                </li>
                <li class="hidden-xs practice-item">
                    <a  href="<c:url value='/practices'/>">
                        <b><spring:message code="jsp.header.practices"/></b>
                    </a></li>
                <li class="hidden-xs route-item">
                    <a href="<c:url value='/routes'/>">
                        <b><spring:message code="jsp.header.routes"/></b>
                    </a></li>
                <li class="hidden-xs segment-item">
                    <a href="<c:url value='/segments'/>">
                        <b><spring:message code="jsp.header.segments"/></b>
                    </a></li>
                <li class="hidden-xs event-item">
                    <a href="<c:url value='/events'/>">
                        <b><spring:message code="jsp.header.events"/></b>
                    </a></li>
                <li class="hidden-xs group-item">
                    <a href="<c:url value='/groups'/>">
                        <b><spring:message code="jsp.header.groups"/></b>
                    </a></li>




                <li class="visible-xs practice-item"><a  href="<c:url value='/practices'/>">
                        <span class="fa-lg fa fa-map-marker" style="margin-right: 18px"></span>
                        <b><spring:message code="jsp.header.practices"/></b>
                    </a></li>
                <li class="visible-xs route-item">
                    <a href="<c:url value='/routes'/>"><span class="fa-lg fa fa-flag-checkered" style="margin-right: 10px"></span>
                        <b><spring:message code="jsp.header.routes"/></b>
                    </a></li>
                <li class="visible-xs segment-item">
                    <a href="<c:url value='/segments'/>"><span class="fa-lg fa fa-flag-checkered" style="margin-right: 10px"></span>
                        <b><spring:message code="jsp.header.segments"/></b>
                    </a></li>
                <li class="visible-xs event-item">
                    <a href="<c:url value='/events'/>"><span class="fa-lg fa fa-bell" style="margin-right: 12px"></span>
                        <b><spring:message code="jsp.header.events"/></b>
                    </a></li>
                <li class="visible-xs group-item">
                    <a href="<c:url value='/groups'/>"><span class="fa-lg fa fa-users" style="margin-right: 11px"></span>
                        <b><spring:message code="jsp.header.groups"/></b>
                    </a></li>
                <li class="visible-xs">
                    <a href="<c:url value='/j_spring_security_logout'/>"><span class="fa-lg glyphicon glyphicon-log-out" style="margin-right: 11px"></span>
                        <b><spring:message code="jsp.header.logout"/></b>
                    </a></li>

            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="visible-lg visible-md visible-sm" style="position: relative;">
                    <a style="position: relative;" 
                       href="#" class="notifications-button fa fa-2x fa-flag dropdown-toggle" data-toggle="dropdown">
                    </a>
                    <ul class="dropdown-menu notifications-list" id="desktop-list">
                        <li class="disabled"><a><em>No notifications</em></a></li>
                    </ul>
                </li>


                <li id="account-item" class="hidden-xs">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <img class="thumbnail clearfix pull-left image-responsibe"  style="height: 35px; top:-8px; position:relative; margin-right: 5px;" src="${thumbnailUrl}" />
                        <b> ${user.username}</b> <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="${profileUrl}"><span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;<spring:message code="jsp.header.profile"/></a>
                        </li>
                        <li>
                            <a href="<c:url value='/j_spring_security_logout'/>"><span class="glyphicon glyphicon-log-out"></span>&nbsp;&nbsp;<spring:message code="jsp.header.logout"/></a>
                        </li>
                    </ul>
                </li>

            </ul>
        </nav>
    </div>
</div>
<script>
    $("#search-dropdown").click(function(event) {
        event.stopPropagation();
    });
    $(document).ready(function() {
        $.ajax({
            url: '<c:url value="/${user.username}/notifications"/>',
            type: 'GET',
            // The type of data that is getting returned.
            dataType: "html",
            success: function(response) {
                var obj = $.parseJSON(response);
                if (obj.length > 0) {
                    $(".notifications-button").html('<span style="font-size: 11px; font-family:Arial; position: absolute;  top: 32px; left:30px; background-color:red" class="badge">' + obj.length + '</span>');
                    $(".notifications-button-mobile").html('<span style="font-size: 11px; position: absolute; background-color:red; top:22px; right: 0px;" class="badge">' + obj.length + '</span>');
                    notificationList = '<li class="dropdown-header">Peticiones de amistad\n\
                                            <button id="clean-notifications" style="font-size:12px;" class="pull-right btn btn-link btn-xs ">Limpiar</button>\n\
                                            </li>';
                    $.each(obj, function() {
                        var baseUrl = <c:url value="/"/>;
                        notificationList = notificationList + '<li style="z-index: 1005" class="notification" \n\
                                                                data-petitioner="' + this['request']['petitioner']['username'] + '" \n\
                                                                data-target="' + this['request']['target']['username'] + '" \n\
                                                                data-requestid="' + this["request"]["id"] + '" >\n\
                                                                   <a ';
                        if (this["request"]["requestStatus"] === "NOT_ANSWERED") {
                            var userUrl = baseUrl + "${user.username}" + "/friends";
                            var fullname = this["request"]["petitioner"]["fullName"];
                            var thumbnailUrl = baseUrl + "/" + this['request']['petitioner']['username'] + '/thumbnail';
                            notificationList = notificationList + 'href="' + userUrl + '">\n\
                                                            <img src="' + thumbnailUrl + '" class="img-thumbnail"  style=" max-width: 35px" /> ' + fullname + '\n\
                                                            <span style="margin-left:10px;" class="label label-warning"><small>Petición de amistad</small></span>';

                        }
                        if (this["request"]["requestStatus"] === "ACCEPTED") {
                            var userUrl = baseUrl + this["request"]["target"]["username"];
                            var fullname = this["request"]["target"]["fullName"];
                            var thumbnailUrl = baseUrl + "/" + this['request']['target']['username'] + '/thumbnail';
                            notificationList = notificationList + 'href="' + userUrl + '">\n\
                                                                    <img src="' + thumbnailUrl + '" class="img-thumbnail"  style=" max-width: 35px" /> ' + fullname + '\n\
                                                                    <span style="margin-left:10px;" class="label label-success"><small>Petición aceptada</small></span>';
                        }
                        notificationList = notificationList + '</a></li>';
                    });
                    $(".notifications-list").html(notificationList);
                }
                else {
                    resetNotificationList();
                }
                initHeaderElements();
            }
        });
        initHeaderElements();
    });

    function initHeaderElements() {
        $("ul.dropdown-menu").unbind('click');
        $("ul.dropdown-menu").click(function(e) {
            e.stopPropagation();
            cleanNotifications();
        });
    }

    function cleanNotifications() {
        var nots = $("ul#desktop-list li.notification");
        $.each(nots, function() {
            var jsonData = {"id": $(this).data("requestid"),
                "petitioner": {"username": $(this).data("petitioner")},
                "target": {"username": $(this).data("target")},
                "notificationStatus": "READED"};
            sendChanges(jsonData);
        });
//        resetNotificationList();
    }

    function resetNotificationList() {
        $(".notifications-list").html('<li class="disabled"><a><em>No notifications</em></a></li>');
        $(".notifications-button").html("");
        $("a#notifications-button").html("");
    }

    function sendChanges(data) {
        var jsonData = JSON.stringify(data);
        $.ajax({
            type: 'PUT',
            url: '<c:url value="/${user.username}/notifications"/>',
            data: jsonData,
            async: false,
            contentType: "application/json",
            // The type of data that is getting returned.
            dataType: "html",
            success: function(response) {
                console.log("res" + response);
            },
            error: function(e) {
                alert("Some error ocurred. Please try it again later");
                console.log(e);
            }
        });

    }


</script>

