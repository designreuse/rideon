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
        <h2><spring:message code="jsp.segment.segments"/></h2>
        <hr>
        <div class="row">
            <div class="col-sm-4">
                <div class=" panel panel-default">
                    <div class="panel-heading"><spring:message code="jsp.form.results"/>
                    </div>
                    <div class="panel-body">
                        <div class="list-group" id="routeList">
                            <em><spring:message code="jsp.segment.nosegmentavailable"/></em>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-8">
                <div id="map" class="panel panel-default col-lg-12" style="height:400px;"></div>
            </div>
        </div>
        <script>
            $(".segment-item").addClass("active");
            var searchUrl = "<c:url value="/search/segments"/>";
            var destinationBaseUrl = '<c:url value="/segments/"/>';
            var resourcesBaseUrl = '<c:url value="/resources/"/>'
            var objType = "Segmento"
            drawSearchMap(searchUrl, destinationBaseUrl, resourcesBaseUrl, objType );   
        </script>
    </body>
</html>
