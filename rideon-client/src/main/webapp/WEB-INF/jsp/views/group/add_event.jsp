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
        <ol class=" breadcrumb">
            <li><a href="<c:url value="/groups"/>"><spring:message code="jsp.group.groups"/></a></li>
            <li class="active"><a href="<c:url value="/groups/${groupId}"/>">${groupId}</a></li>
            <li class="active"><spring:message code="jsp.event.newevent"/></li>
        </ol>
        <div class="row">
            <div class="col-sm-6">

                <form:form method="POST" commandName="eventForm" id="eventform">
                    <div class="form-group">
                        <form:label path="name"><spring:message code="jsp.event.name"/>
                        </form:label>
                        <form:input class="form-control" path="name"/>
                    </div>
                    <div class="form-group">
                        <form:label path="description"><spring:message code="jsp.event.description"/>
                        </form:label>
                        <form:textarea cols="50" rows="3" class="form-control" path="description"/>
                    </div>
                    <div class="form-group date" >
                        <form:label path="eventDate"><spring:message code="jsp.event.eventDate"/></form:label>
                        <form:input id="datetimepicker"  type="text" class="form-control" path="eventDate" />
                    </div>
                    <div class="form-group">
                        <form:label path="eventType"><spring:message code="jsp.event.eventtype"/>
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
                }else{
                self.submit();
                }
            });
        </script>
    </body>
</html>
