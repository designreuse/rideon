<?xml version="1.0" encoding="UTF-8" ?>
<%@include file="../includes/includes.jsp" %>
<!DOCTYPE html>
<html style="height: 100%">
    <head>
        <%@include file="../includes/head.jsp" %>
        <script type="text/javascript" src="<c:url value='/resources/js/md5.js'/>"></script>
        <title>RideON - Login</title>
    </head>
    <body style="height: 100%">
        <div class="hidden-xs" style="position: relative; height: 20%;"></div>
        <div class="container">
            <div class="row">
                <div class="well col-sm-5 col-md-4" style="margin: auto; float: none;">
                    <c:if test="${loginFailed}">
                        <div  class="alert alert-danger alert-dismissable">
                            <a class="close" data-dismiss="alert" href="#">x</a><spring:message code="jsp.login.loginFailed"/>
                        </div>
                    </c:if>
                    <legend>RideON Login</legend>
                    <form id="loginForm" method=post action="j_spring_security_check" >
                        <div class="form-group">
                            <label for="j_username"><spring:message code="jsp.form.username"/></label>
                            <input class="form-control" id="username" type="text" name="j_username"/>
                        </div>
                        <div class="form-group">
                            <label for="j_password"><spring:message code="jsp.form.password"/></label>
                            <input class="form-control" id="password" type="password" name="j_password"/>
                        </div>
                        <input class="btn btn-success" type="submit" name="submit" value="Login">
                        <a class="btn btn-primary" href="<c:url value='/sign'/>"><spring:message code="jsp.form.signup"/></a>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <script>
        $("#loginForm").submit(function(e) {
            var self = this;
            if ($('#username').val().length > 0 && $('#password').val().length > 0) {
                var password = $('#password').val();
                var md5 = calcMD5(password);
                $('#password').val(md5);
//                return true;
            }
        });

    </script>
</html>
