<%-- 
    Document   : groups
    Created on : 30-sep-2013, 8:54:50
    Author     : Fer
--%>
<%@include file="../../includes/includes.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
        <h2><spring:message code="jsp.group.groups"/>
            <a class="btn btn-sm btn-success pull-right" href="<c:url value='/groups/add'/>">
                <span class="fa fa-plus" style="margin-right :5px"></span>
                <spring:message code="jsp.group.newgroup"/>
            </a>
        </h2>
        <hr>
        <div class="row">
            <div class="col-sm-5 col-md-4">
                <div class="panel panel-default">
                    <div class="panel-heading"><spring:message code="jsp.group.message.yourgroups"/></div>
                    <div class="panel-body">
                        <c:choose>
                            <c:when test="${fn:length(groups) gt 0}">
                                <table class="table table-hover table-bordered">
                                    <c:forEach items="${groups}" var="group">
                                        <tr>
                                            <td class="">
                                                <a style="display:inline-block; width: 100%; height: 100%;" href="<c:url value='/groups/${group.id}'/>">
                                                    <img  src="<c:url value='/groups/${group.id}/thumbnail'/>" class="media img-thumbnail" 
                                                          style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
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
            </div>
            <div class="col-sm-5 col-md-8">
                <div class="panel panel-default">
                    <div class="panel-heading"><spring:message code="jsp.group.message.searchgroups"/></div >
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
        </div>
        <script>
            $(".group-item").addClass("active");

            $("form#search-form").submit(function(event) {
                event.preventDefault();
                var value = $("#search-input").val()
                if (value != "") {
                    var query = {"query": value};
                    $.ajax({
                        data: query,
                        url: '<c:url value="/search/groups"/>',
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
                                    var baseUrl = '<c:url value="/groups/"/>';
                                    var groupUrl = baseUrl + this["id"];
                                    searchList = searchList + '<li class="list-group-item">\
                                        <a href="' + groupUrl + '"> \
                                            <img src="' + groupUrl + '/thumbnail' + '" class="img-thumbnail"  style="margin-right: 10px; max-width: 35px" /> \
                                            <span>' + this['name'] + '</span> \
                                        </a> \
                                    </li>';
                                });
                                searchList = searchList + '</ul>';
                                $("#search-result").html(searchList);
                                init();
                            }
                            else {
                                $("#search-result").html("<em>Ninguna coincidencia</em>");
                            }
                        }
                    });
                }
            });

        </script>

    </body>
</html>
