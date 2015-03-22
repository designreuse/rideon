<%-- 
    Document   : friends
    Created on : 23-oct-2013, 11:26:52
    Author     : Fer
--%>

<%@include file="../../includes/includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="row">
    <ol class="breadcrumb">
        <li><a href="<c:url value="/${userId}"/>">${userId}</a></li>
        <li class="active"><spring:message code="jsp.user.friends"/></li>
    </ol>
    <div class="col-sm-6 col-md-6">
        <div class="panel panel-default">
            <div class="panel-heading">Peticiones de amistad</div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${fn:length(requests) gt 0}">
                        <ul class="list-group">
                            <c:forEach var="request" items="${requests}">
                                <li class="list-group-item">
                                    <div>
                                        <a href="<c:url value='/${request.petitioner.username}'/>">
                                            <img src="<c:url value='/${request.petitioner.username}/thumbnail'/>" class="img-thumbnail" style="height: 35px; margin-right: 10px;" />
                                            <span class="caption list-group-item-heading">${request.petitioner.fullName}</span>
                                        </a>
                                        <div class="pull-right" style="position: relative; top:5px;">
                                            <button class="btn btn-success btn-xs" id="acceptRequest" data-petitionerid="${request.petitioner.username}" data-requestid="${request.id}"><i class="glyphicon glyphicon-ok"></i></button>
                                            <button class="btn btn-danger btn-xs" id="rejectRequest" data-requestid="${request.id}"><i class="glyphicon glyphicon-remove"></i></button>
                                        </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <em>Ninguna petición pendiente</em>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

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
    <div class="col-sm-6 col-md-6">
        <div class="panel panel-default">
            <div class="panel-heading"><spring:message code="jsp.friend.friends"/></div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${fn:length(friends) gt 0}">
                        <ul class="list-group">
                            <c:forEach var="friend" items="${friends}">
                                <li class="list-group-item">
                                    <a href="<c:url value='/${friend.username}'/>">
                                        <img src="<c:url value='/${friend.username}/thumbnail'/>" class="img-thumbnail" style="height: 35px; margin-right: 10px;" />
                                        <spam>${friend.fullName}</spam>
                                    </a>
                                    <div class="btn-group pull-right">
                                        <button class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>
                                        <ul  class="dropdown-menu" role="menu">
                                            <li><a href="#" id="removeFriend" data-friendid="${friend.username}"><i class="fa fa-fw fa-trash-o"></i> Eliminar de mis amigos</a></li>
                                        </ul>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <em><spring:message code="jsp.message.nofriendsfound"/></em>
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

    $(function() {
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

    function init() {
        $("button#addUser").unbind();
        $("button#acceptRequest").unbind();
        $("button#rejectRequest").unbind();
        $("button#addUser").click(function(event) {
            event.preventDefault();
            addFriend($(this));
        });

        $("button#acceptRequest").click(function(event) {
            event.preventDefault();
            acceptRequest($(this));
        });
        $("button#rejectRequest").click(function(event) {
            event.preventDefault();
            rejectRequest($(this));
        });

        $("a#removeFriend").click(function(event) {
            event.preventDefault();
            removeFriend($(this))
        });
    }

    function removeFriend(link) {
        var friendid = $(link).data("friendid");
        var url = "<c:url value="/${user.username}/friends/"/>";
        url = url + friendid;
        $.ajax({
            type: 'DELETE',
            url: url,
            dataType: "html",
            success: function(response) {
                window.location.href = "<c:url value="/${userId}/friends" />";
            },
            error: function(e) {
                alert("Some error ocurred. Please try it again later");
                console.log(e);
            }
        });

    }

    function addFriend(button) {
        var username = $(button).parent().find("a").data("username")
        var jsonData = username;
        console.log(jsonData);
        var url = "<c:url value="/${user.username}/friends"/>";
        $.ajax({
            type: 'POST',
            url: url,
            data: jsonData,
            contentType: "application/json",
            // The type of data that is getting returned.
//                dataType: "html",
            success: function(response) {
                button.html("Peticion enviada");
            },
            error: function(e) {
                alert("Some error ocurred. Please try it again later");
                console.log(e);
            }
        });
    }
    function acceptRequest(button) {
        var req = {"id": $(button).data("requestid"),
            "petitioner": {"username": $(button).data("petitionerid")},
            "target": {"username": "${user.username}"},
            "requestStatus": "ACCEPTED"};
        var jsonData = JSON.stringify(req);
        console.log(jsonData);
        if (updateRequest(jsonData)) {
            $(this).parent().html('<button class="btn btn-defaul btn-xs">ok\
                                    </button>');
        }
        else {

        }
    }

    function rejectRequest(button) {
        var req = {"id": $(button).data("requestid"),
            "petitioner": {"username": $(button).data("petitionerid")},
            "target": {"username": "${user.username}"},
            "requestStatus": "DENIED"};

        var jsonData = JSON.stringify(req);
        updateRequest(jsonData);
    }

    function updateRequest(jsonData) {
        $.ajax({
            type: 'PUT',
            url: '<c:url value="/${user.username}/friends"/>',
            data: jsonData,
            contentType: "application/json",
            // The type of data that is getting returned.
            dataType: "html",
            success: function(response) {
                window.location.href = "<c:url value="/${userId}/friends" />";
                return true;
            },
            error: function(e) {
                alert("Some error ocurred. Please try it again later");
                console.log(e);
                return false;
            }
        });
    }

</script>