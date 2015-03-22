<%-- 
    Document   : add_group
    Created on : 30-sep-2013, 9:14:56
    Author     : Fer
--%>
<%@include file="../../includes/includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<body>
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="<c:url value="/groups"/>"><spring:message code="jsp.group.groups"/></a></li>
            <li><a href="<c:url value="/groups/${groupForm.id}"/>">${groupForm.name}</a></li>
            <li class="active"><spring:message code="jsp.group.admin"/></li>
        </ol>
        <div class="col-lg-12">
            <form:form method="POST" commandName="groupForm" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-md-5 col-sm-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <spring:message code="jsp.form.info"/>
                            </div>
                            <div class="panel-body">
                                <form:hidden class="form-control" path="id"/>
                                <div class="form-group">
                                    <form:label path="name"><spring:message code="jsp.group.name"/></form:label>
                                    <form:input class="form-control" path="name"/>
                                </div>
                                <div class="form-group">
                                    <form:label path="description"><spring:message code="jsp.group.description"/></form:label>
                                    <form:textarea cols="50" row="3" class="form-control" path="description"/>
                                </div>
                                <div class="form-group">
                                    <form:label class="control-label" path="privacy"><spring:message code="jsp.event.privacy"/></form:label>
                                    <form:select class="form-control" path="privacy">
                                        <form:option value="PUBLIC"><spring:message code="jsp.event.public"/></form:option>
                                        <form:option value="PRIVATE"><spring:message code="jsp.event.private"/></form:option>
                                    </form:select>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <spring:message code="jsp.form.image"/>
                            </div>
                            <div class="panel-body">

                                <div class="form-group">
                                    <div style="margin: auto; width: 200px;">
                                        <a style=" margin: auto; max-width: 200px;" class="pull-left" href="<c:url value="/groups/${groupForm.id}/image"/>">
                                            <img src="<c:url value="/groups/${groupForm.id}/image"/>" class="image-responsibe img-thumbnail">
                                        </a>
                                    </div>
                                    <form:label path="image"><spring:message code="jsp.form.image"/>
                                        <form:input type="file" path="image" />
                                    </form:label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-offset-1 col-md-5 col-sm-6">

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <spring:message code="jsp.group.groupmanager"/>
                            </div>
                            <div class="panel-body">
                                <div style="margin:auto; width: 150px;">
                                    <a  class="btn btn-danger" href="<c:url value='remove'/>"><spring:message code="jsp.group.deletegroup"/></a>
                                </div>
                                <br>
                                <p>Eliminando este grupo eliminará todos los registros asociados al mismo. Esta acción es irreversible.</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12 col-md-11">
                        <input class="btn btn-success pull-right" type="submit" align="center" value="<spring:message code="jsp.form.save"/>" />
                    </div>
                </div>
            </form:form>
        </div>
    </div>
    <script>
        $(".group-item").addClass("active");
    </script>
</body>
