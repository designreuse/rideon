<form:form method="PUT" commandName="bicycleForm" enctype="multipart/form-data" >
    <form:hidden path="id"/>
    <div class="form-group">
        <form:label path="brand"><spring:message code="jsp.bicycle.brand"/></form:label>
        <form:input class="form-control" path="brand"/>
    </div>
    <div class="form-group">
        <form:label path="model"><spring:message code="jsp.bicycle.model"/></form:label>
        <form:input class="form-control" path="model"/>
    </div>
    <div class="form-group date">
        <form:label path="buyDate"><spring:message code="jsp.bicycle.buyDate"/></form:label>
        <form:input path="buyDate" id="datetimepicker" class="form-control add-on" data-format="yyyy-MM-dd" type="text"/>
    </div>
    <div class="form-group">
        <form:label path="kilometers"><spring:message code="jsp.bicycle.kilometers"/></form:label>
        <form:input class="form-control" type="" path="kilometers" />
    </div>
    <div class="form-group checkbox">
        <form:checkbox path="isPrincipal" id="isPrincipal" />
        <form:label path="isPrincipal" for="isPrincipal"><b><spring:message code="jsp.bicycle.isPrincipal"/></b></form:label>
    </div>
    <div class="form-group">
        <form:label path="image"><spring:message code="jsp.bicycle.image"/>
            <form:input type="file" path="image" />
        </form:label>
    </div>

    <input class="btn btn-success" type="submit" value="<spring:message code="jsp.form.save"/>" />
</form:form>
<script>
    setDatePicker();
</script>
