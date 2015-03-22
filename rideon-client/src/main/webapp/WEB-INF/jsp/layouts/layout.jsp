<%-- 
    Document   : layout
    Created on : 27-ago-2013, 9:30:57
    Author     : Fer
--%>

<?xml version="1.0" encoding="UTF-8" ?>
<%@include file="../includes/includes.jsp" %>
<!--<link rel="shortcut icon" href="favicon.ico" />-->
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%@include file="../includes/head.jsp" %>
        <title>RideON - Keep riding</title>
    </head>
    <body>
        <header>
            <jsp:include page="../includes/header.jsp"/>
        </header>
        <div class="container">
             <decorator:body />
    </div>  
    <footer>
        <div class="container">
            <jsp:include page="../includes/footer.jsp"/>
        </div>
    </footer>
</body>
</html>