<%-- 
    Document   : add_members
    Created on : 14-oct-2013, 0:11:11
    Author     : Fer
--%>
<%@include file="../../includes/includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="row">
    <ol class="breadcrumb">
        <li><a href="<c:url value="/events"/>"><spring:message code="jsp.event.events"/></a></li>
        <li><a href="<c:url value="/events/${event.id}"/>">${event.name}</a></li>
        <li class="active"><spring:message code="jsp.event.adminmembers"/></li>
    </ol>
    <h1><small><spring:message code="jsp.event.members"/></small></h1>
    <hr>
    <div class="panel panel-default">
        <div class="panel-body">
            <c:choose>
                <c:when test="${fn:length(members) gt 0}">
                    <ul class="list-group">
                        <c:forEach var="member" items="${members}">
                            <li class="list-group-item">
                                <a data-username="${member.username}" href="<c:url value='/${member.username}'/>">
                                    <img src="<c:url value='/${member.username}/thumbnail'/>" class="img-thumbnail" style="margin-right: 10px; max-width: 35px" />
                                    <span >
                                        ${member.fullName}
                                    </span>
                                </a>
                                <c:if test="${event.owner.username != member.username}">
                                    <button class="pull-right btn btn-sm btn-danger" id="removeUser" data-userid="${member.username}"><i class="glyphicon glyphicon-trash"></i></button>
                                    </c:if>
                            </li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:otherwise>
                    <em>No members found</em>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<h4><spring:message code="jsp.event.addmembers"/></h4>
<hr>
<div class="row">
    <div class="col-sm-4">
        <div class="panel panel-default">
            <div class="panel-heading"><spring:message code="jsp.form.search"/></div >
            <div class="panel-body">
                <form id="search-form">
                    <div class="input-group">
                        <input id="search-input" type="text" class="form-control" placeholder="<spring:message code="jsp.form.search"/>">
                        <div class="input-group-btn">
                            <button type="submit" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
                        </div>
                    </div>
                </form>
                <br>
                <div id="search-result">

                </div>
            </div>
        </div>
    </div>
    <div class="col-sm-8 col-md-6 col-md-8">
        <legend>Añadir usuarios selecctionados</legend>
        <div id="users-selected" class="well">
            <em>No user selected</em>
        </div>
        <a href="#" id="apply" class="btn btn-success"><spring:message code="jsp.form.save"/></a>
        <a href="<c:url value="/events/${event.id}"/>" class="btn btn-danger"><spring:message code="jsp.form.cancel"/></a>
    </div>
</div>
<script>
    $(document).ready(function() {
        $(".event-item").addClass("active");

        $(function() {
            init();
            $("form#search-form").submit(function(event) {
                event.preventDefault();
                var query = {"query": $("#search-input").val()};
                $.ajax({
                    data: query,
                    url: '<c:url value="/search/user"/>',
                    type: 'GET',
                    // The type of data that is getting returned.
                    dataType: "html",
                    beforeSend: function() {
                        $("#search-result").html("Buscando, espere por favor...");
                    },
                    success: function(response) {
                        var searchList = '<ul class="list-group">';
                        var obj = $.parseJSON(response);
                        if (obj.length > 0) {
                            $.each(obj, function() {
                                if (this["username"] !== '${user.username}') {
                                    var baseUrl = <c:url value="/"/>;
                                    var userUrl = baseUrl + this["username"];
                                    searchList = searchList + '<li class="list-group-item">\
                                        <a data-username="' + this["username"] + '" href="' + userUrl + '"> \
                                            <img src="' + userUrl + '/thumbnail' + '" class="img-thumbnail"  style="margin-right: 10px; max-width: 35px" /> \
                                            <span>' + this['fullName'] + '</span> \
                                        </a> \
                                        <button class="pull-right btn btn-default" id="addUser"><i class="glyphicon glyphicon-plus"></i></button> \
                                    </li>';
                                }
                            });
                            searchList = searchList + '</ul>';
                            $("#search-result").html(searchList);
                            init();
                        }
                        else {
                            $("#search-result").html("<em>No users found</em>");
                        }
                    }
                });
            });
        });

        $("a#apply").click(function(event) {
            event.preventDefault();
            sendChanges();
        });
    });
    function init() {
        $("button#addUser").unbind();
        $("button#addUser").click(function(event) {
            event.preventDefault();
            if ($(this).find("i").hasClass("glyphicon-plus")) {
                $(this).find("i").removeClass("glyphicon-plus");
                $(this).find("i").addClass("glyphicon-check");

                if ($("#users-selected ul").length) {
                    addUser($(this));
                }
                else {
                    $("#users-selected").html("<ul class='list-group'></ul>");
                    addUser($(this));
                }
            }
        });

        $("button#unselectUser").click(function() {
            if ($(this).find("i").hasClass("glyphicon-minus")) {
                unselectUser($(this));
            }
            if ($("#users-selected ul li").length === 0) {
                $("#users-selected").html("<em>No user selected</em>");
            }
        });

        $("button#removeUser").unbind();
        $("button#removeUser").click(function() {
            removeUser($(this).data("userid"))
        });
    }

    function addUser(user) {
        var data = (user).parent().find("a").data("username");
        if ($("#users-selected ul a[data-username='" + data + "']").length === 0) {

            $("#users-selected ul").append('<li class="list-group-item">\
                            ' + $(user).parent().find("a").clone().wrap("<span></span>").parent().html() + '\
                            <button class="pull-right btn btn-default" id="unselectUser"><i class="glyphicon glyphicon-minus"></i></button>\
                            </li>'
                    );
            init();
        }
    }

    function unselectUser(user) {
        var data = (user).parent().find("a").data("username");
        var si = $("a[data-username='" + data + "']").parent().find("button");
        $.each(si, function() {
            $(this).find("i").removeClass("glyphicon-check");
            $(this).find("i").addClass("glyphicon-plus");
        });
        $(user).parent().remove();
    }

    function removeUser(user) {
        var baseUrl = "<c:url value="/events/${event.id}/members/"/>";
        var fullUrl = baseUrl + user;
        $.ajax({
            url: fullUrl,
            type: 'DELETE',
            // The type of data that is getting returned.
            dataType: "html",
            success: function(response) {
                console.log("res" + response);
                window.location.href = "<c:url value="/events/${event.id}/members" />";
            },
            error: function(e) {
                alert("Some error ocurred. Please try it again later");
                console.log(e);
            }
        });
    }

    function sendChanges() {
        var users = $("#users-selected").find("ul").find("a");
        var objectArray = [];
        $.each(users, function() {
            objectArray.push($(this).data("username"));
        });
        var jsonData = JSON.stringify(objectArray);
        if (objectArray.length !== 0) {
            $.ajax({
                type: 'POST',
                url: '<c:url value="/events/${event.id}/members/add"/>',
                data: jsonData,
                contentType: "application/json",
                // The type of data that is getting returned.
                dataType: "html",
                success: function(response) {
                    console.log("res" + response);
                    window.location.href = "<c:url value="/events/${event.id}" />";
                },
                error: function(e) {
                    alert("Some error ocurred. Please try it again later");
                    console.log(e);
                }
            });

        }
    }

</script>
