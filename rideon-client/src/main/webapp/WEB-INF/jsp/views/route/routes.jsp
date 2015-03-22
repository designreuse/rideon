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
        <h2><spring:message code="jsp.route.routes"/></h2>
        <hr>
        <div class="row">
            <div class="col-sm-3">
                <div class=" panel panel-default">
                    <div class="panel-heading"><spring:message code="jsp.form.results"/>
                    </div>
                    <div class="panel-body">
                        <div class="list-group" id="routeList">
                            <em><spring:message code="jsp.route.norouteavailable"/></em>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div id="map" class="panel panel-default col-lg-12" style="height:400px;"></div>
            </div>
        </div>
        <script>
            $(".route-item").addClass("active");

            var searchUrl = "<c:url value="/search/routes"/>";
            var destinationBaseUrl = '<c:url value="/routes/"/>';
            var resourcesBaseUrl = '<c:url value="/resources/"/>'
            var objType = "Ruta"
            drawSearchMap(searchUrl, destinationBaseUrl, resourcesBaseUrl, objType);   

        </script>
    </body>
</html>
