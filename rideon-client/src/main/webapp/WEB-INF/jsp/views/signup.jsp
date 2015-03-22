<%-- 
    Document   : register
    Created on : 27-ago-2013, 10:41:09
    Author     : Fer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../includes/includes.jsp" %>
<script type="text/javascript" src="<c:url value='/resources/js/md5.js'/>"></script>
<!DOCTYPE html>
<html style="height: 100%">
    <head>
        <%@include file="../includes/head.jsp" %>
        <title><spring:message code="jsp.form.signup"/></title>
    </head>
    <body style="height: 100%">
        <div class="hidden-xs" style="position: relative; height: 20%;"></div>
        <div class="container">
            <div class="row">
                <div class="well col-sm-6 col-md-4" style="margin: auto; float: none;">
                    <legend><spring:message code="jsp.form.signup"/></legend>
                    <form:form method="POST" commandName="signForm" id="signForm">
                        <div class="form-group">
                            <spring:message code="jsp.form.username"/>*
                            <form:input class="form-control" path="username" />
                            <form:errors path="username" cssClass="error"/>
                        </div>
                        <div class="form-group">
                            <spring:message code="jsp.form.email"/>
                            <form:input class="form-control" path="email" />
                            <form:errors path="email" cssClass="error"/>
                        </div>
                        <div class="form-group">
                            <spring:message code="jsp.form.fullName"/>
                            <form:input class="form-control" path="fullName" />
                            <form:errors path="fullName" cssClass="error"/>
                        </div>
                        <div class="form-group">
                            <div  id="password-match" class="alert alert-danger alert-dismissable hidden">
                                <a class="close" data-dismiss="alert" href="#">x</a>Las contraseñas no coinciden
                            </div>
                            <div  id="password-size" class="alert alert-danger alert-dismissable hidden">
                                <a class="close" data-dismiss="alert" href="#">x</a>Longitud mínima: 6 carácteres.
                            </div>
                            <spring:message code="jsp.form.password"/>*
                            <form:password id="password" class="form-control" path="password"/>
                            <form:errors path="password" cssClass="error"/>
                        </div>
                        <div class="form-group">
                            <spring:message code="jsp.form.repeatPassword"/>
                            <form:password id="repassword" class="form-control" path="confirmPassword"/>
                            <form:errors path="confirmPassword" cssClass="error"/>
                        </div>
                        <input type="submit" class="btn btn-success" align="center" value="<spring:message code="jsp.form.signup"/>" />
                    </form:form>
                </div>
            </div>
        </div>
        <script>
            $("#signForm").submit(function(e) {
                e.preventDefault();
                var self = this;
                $("#password-size").addClass("hidden");
                $("#password-match").addClass("hidden");

                if ($("#password").val().length < 6) {
                    $("#password-size").removeClass("hidden");
                } else if ($("#password").val() !== $("#repassword").val()) {
                    $("#password-match").removeClass("hidden");
                } else {
                    var password = $('#password').val();
                    var md5 = calcMD5(password);
                    $('#password').val(md5);
                    $('#repassword').val(md5);
                    self.submit();
                }
            });
        </script>
    </body>
</html>

