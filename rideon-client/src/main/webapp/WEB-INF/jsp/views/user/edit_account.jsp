<%--
    Document   : profile
    Created on : 01-sep-2013, 12:10:59
    Author     : Fer
--%>
<%@include file="../../includes/includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<body>
    <div class="row">
        <ol class="hidden-xs breadcrumb">
            <li><a href="<c:url value="/${userForm.username}"/>">${userForm.username}</a></li>
            <li class="active"><spring:message code="jsp.user.editprofile"/></li>
        </ol>
        <div class="col-lg-12">
            <form:form method="PUT" commandName="userForm" enctype="multipart/form-data" >
                <div class="row">
                    <div class="col-sm-6 col-md-5">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <spring:message code="jsp.form.info"/>
                            </div>
                            <div class="panel-body">
                                <form:hidden class="form-control" path="username"/>    
                                <div class="form-group">
                                    <form:label path="fullName"><spring:message code="jsp.user.fullName"/></form:label>
                                    <form:input class="form-control" path="fullName"/>
                                </div>
                                <div class="form-group">
                                    <form:label path="email"><spring:message code="jsp.form.email"/></form:label>
                                    <form:input class="form-control" path="email"/>
                                </div>
                                <div class="form-group">
                                    <form:label path="country"><spring:message code="jsp.user.country"/></form:label>
                                    <form:input class="form-control" path="country"/>
                                </div>
                                <div class="form-group date">
                                    <form:label path="bornDate"><spring:message code="jsp.user.bornDate"/></form:label>
                                    <form:input path="bornDate" id="datetimepicker" class="form-control"  type="text"/>
                                </div>
                                <div class="form-group">
                                    <form:label path="province"><spring:message code="jsp.user.province"/></form:label>
                                    <form:input class="form-control" path="province" />
                                </div>
                                <div class="form-group">
                                    <form:label path="town"><spring:message code="jsp.user.town"/></form:label>
                                    <form:input path="town" class="form-control" />
                                </div>

                            </div>
                        </div>

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                Nueva Contraseña
                            </div>
                            <div class="panel-body">
                                <div class="form-group">
                                    <form:label path="password">Contraseña</form:label>
                                    <form:password path="password" class="form-control" />
                                </div>
                                <div class="form-group">
                                    <form:label path="password">Repita la Contraseña</form:label>
                                    <form:password path="password" class="form-control" />
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class="col-md-offset-2 col-md-5 col-sm-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <spring:message code="jsp.user.image"/>
                            </div>
                            <div class="panel-body">
                                <div style="max-width: 160px; margin: auto;">
                                    <a href="<c:url value='/${userForm.username}/image'/>">
                                        <img src="<c:url value='/${userForm.username}/image'/>" class="img-responsive img-thumbnail" />
                                    </a>
                                </div>
                                <div class="form-group">
                                    <form:label path="image"><spring:message code="jsp.user.image"/>
                                        <form:input type="file" path="image" />
                                    </form:label>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading"><spring:message code="jsp.user.accountmanager"/></div>
                            <div class="panel-body">
                                <div style="margin:auto; width: 130px;">
                                    <a id="removeAccount" class="btn btn-danger">
                                        <i class="glyphicon glyphicon-trash"></i>
                                        <strong> <spring:message code="jsp.user.deleteaccount"/></strong>
                                    </a> 
                                </div>
                                <br>
                                <p>Eliminando esta cuenta eliminará toda la información asociada a la misma. Esta acción
                                    es irreversible.</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <input class="btn btn-success pull-right" type="submit" value="<spring:message code="jsp.form.save"/>" />
                    </div>
                </div>
            </form:form>
        </div>
    </div>
    <div class="modal fade" id="myModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Eliminar Usuario</h4>
                </div>
                <div class="modal-body">
                    <p>Al eliminar la cuenta de usuario se eliminarán todos los registros asociados a la misma. Esta acción es irreversible.</p>
                    <p><b>¿Estas seguro que quieres eliminar la cuenta?</b></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" data-dismiss="modal">No eliminar</button>
                    <button type="button" class="btn btn-danger"  id="confirmRemove">Eliminar</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <script>
        $(".account-item").addClass("active");

        $('#datetimepicker').datetimepicker({
            format: 'yyyy-mm-dd',
            autoclose: true,
            startView: 2,
            minView: 2,
            maxView: 4,
        });

        $("#removeAccount").click(function(event) {
            event.preventDefault();
            $('#myModal').modal('show')
        });

        $("#confirmRemove").click(function(event) {
            event.preventDefault();
            var url = "<c:url value="/${userForm.username}"/>"
            $.ajax({
                url: url,
                type: 'DELETE',
                // The type of data that is getting returned.
                dataType: "html",
                success: function(response) {
                    console.log("res" + response);
                    window.location.href = "<c:url value="/group/${group.id}/members" />";
                },
                error: function(e) {
                    alert("Some error ocurred. Please try it again later");
                    console.log(e);
                }
            });
        });
    </script>
</body>