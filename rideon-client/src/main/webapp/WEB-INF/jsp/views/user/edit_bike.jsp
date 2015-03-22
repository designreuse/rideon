<%-- 
    Document   : new_bike
    Created on : 02-sep-2013, 11:10:56
    Author     : Fer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../includes/includes.jsp" %>
<%--<%@include file="../includes/fileupload_heads.jsp" %>--%>

<div class="row">
    <ol class="breadcrumb">
        <li><a href="<c:url value="/${user.username}"/>">${user.username}</a></li>
        <li class="active"><spring:message code="jsp.bicycle.editBike"/></li>
    </ol>
    <div class="col-md-5">
        <%@include file="../../includes/bicycle_form.jsp" %>
    </div>
</div>
<script>
    $(".account-item").addClass("active");
</script>
