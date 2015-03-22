<%-- 
    Document   : add_group
    Created on : 30-sep-2013, 9:14:56
    Author     : Fer
--%>
<%@include file="../../includes/includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
        <div class="row">
                    <ol class=" breadcrumb">
            <li><a href="<c:url value="/groups"/>"><spring:message code="jsp.group.groups"/></a></li>
            <li class="active"><spring:message code="jsp.group.newgroup"/></li>
        </ol>
            <div class="col-md-6">
                <form:form method="POST" commandName="group">
                    <div class="form-group">
                        <form:label class="control-label" path="name" ><spring:message code="jsp.group.name"/></form:label>
                        <form:input class="form-control" path="name"/>
                    </div>
                    <div class="form-group">
                        <form:label class="control-label" path="description"><spring:message code="jsp.group.description"/></form:label>
                        <form:textarea cols="50" rows="3" class="form-control" path="description"/>
                    </div>
                    <div class="form-group">
                        <form:label class="control-label" path="privacy"><spring:message code="jsp.event.privacy"/></form:label>
                        <form:select class="form-control" path="privacy">
                            <form:option value="PUBLIC"><spring:message code="jsp.event.public"/></form:option>
                            <form:option value="PRIVATE"><spring:message code="jsp.event.private"/></form:option>
                        </form:select>
                    </div>
                    <input class="btn btn-success" type="submit" align="center" value="<spring:message code="jsp.form.save"/>" />
                </form:form>
            </div>
        </div>
        <script>
            $(".group-item").addClass("active");
        </script>
    </body>
</html>
