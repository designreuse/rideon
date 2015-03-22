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
            <li><a href="<c:url value="/events"/>"><spring:message code="jsp.event.events"/></a></li>
            <li class="active"><spring:message code="jsp.event.newevent"/></li>
        </ol>
            <div class="col-sm-6">
                <form:form method="POST" commandName="event" id="eventform">
                    <div class="form-group">
                        <form:label class="control-label" path="name"><spring:message code="jsp.event.name"/>
                        </form:label>
                        <form:input class="form-control" path="name"/>
                    </div>
                    <div class="form-group">
                        <form:label class="control-label" path="description"><spring:message code="jsp.event.description"/>
                        </form:label>
                        <form:textarea cols="50" rows="3" class="form-control" path="description"/>
                    </div>
                    <div class="form-group date" >
                        <form:label class="control-label" path="eventDate" for="datetimepicker"><spring:message code="jsp.event.eventDate"/></form:label>
                        <form:input id="datetimepicker"  type="text" class="form-control" path="eventDate" />
                    </div>
                    <div class="form-group">
                        <form:label path="privacy"><spring:message code="jsp.event.privacy"/>
                        </form:label>
                        <form:select class="form-control" path="privacy">
                            <form:option value="PUBLIC"><spring:message code="jsp.event.public"/></form:option>
                            <form:option value="PRIVATE"><spring:message code="jsp.event.private"/></form:option>
                        </form:select>
                    </div>
                    <div class="form-group">
                        <form:label class="control-label" path="eventType"><spring:message code="jsp.event.eventtype"/>
                        </form:label>
                        <form:select class="form-control" path="eventType">
                            <form:option value="HANGOUT"><spring:message code="jsp.event.hangout"/></form:option>
                            <form:option value="CHALLENGE"><spring:message code="jsp.event.challenge"/></form:option>
                        </form:select>
                    </div>

                    <input class="btn btn-success" type="submit" align="center" value="<spring:message code="jsp.form.save"/>" />
                </form:form>
            </div>
        </div>
        <script>
            $(".event-item").addClass("active");
            setDateTimePicker();
            $("#eventform").submit(function(e) {
                var self = this;
                e.preventDefault();
                if ($("#datetimepicker").val() === "") {
                    $("#datetimepicker").parent().addClass("has-error");
                } else {
                    self.submit();
                }
            });
        </script>
    </body>
</html>
