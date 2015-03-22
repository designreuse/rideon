<%-- 
    Document   : practices
    Created on : 07-oct-2013, 17:19:27
    Author     : Fer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../includes/includes.jsp" %>
<!DOCTYPE html>
<body>
    <h2><spring:message code="jsp.event.events"/>
        <a class="btn btn-sm btn-success pull-right" href="<c:url value='events/new'/>">
            <span class="fa fa-plus" style="margin-right :5px"></span>
            <spring:message code="jsp.event.newevent"/></a>
    </h2>
    <hr>
    <div class="row">
        <div class="col-sm-5 col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading"><spring:message code="jsp.event.yourevents"/></div>
                <div class="panel-body">
                    <h4><spring:message code="jsp.event.hangouts"/></h4>
                    <c:choose>
                        <c:when test="${fn:length(events) gt 0}">
                            <table class="table table-hover table-bordered">
                                <c:forEach items="${events}" var="event">
                                    <c:if test="${event.eventType == 'HANGOUT'}">
                                        <tr>
                                            <td>
                                                <a style="display:inline-block; width: 100%; height: 100%;" href="<c:url value='/events/${event.id}'/>">
                                                    <img  src="<c:url value='/events/${event.id}/thumbnail'/>" class="media img-thumbnail" 
                                                          style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                                    ${event.name}
                                                </a>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="well well-large">
                                <em><spring:message code="jsp.events.noeventsfound"/></em>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <h4><spring:message code="jsp.event.challenges"/></h4>
                    <c:choose>
                        <c:when test="${fn:length(events) gt 0}">
                            <table class="table table-hover table-bordered">
                                <c:forEach items="${events}" var="event">
                                    <c:if test="${event.eventType == 'CHALLENGE'}">
                                        <tr>
                                            <td class="">
                                                <a style="display:inline-block; width: 100%; height: 100%;" href="<c:url value='/events/${event.id}'/>">
                                                    <img  src="<c:url value='/events/${event.id}/thumbnail'/>" 
                                                          class="media img-thumbnail" style="width: 30px; height: 30px; margin-right: 5px; position: relative" />
                                                    ${event.name}
                                                </a>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="well well-large">
                                <em><spring:message code="No hay eventos disponibles"/></em>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="col-sm-5 col-md-8">
            <div id="calendar"></div>
            <br>
            <div class="panel panel-default">
                <div class="panel-heading"><spring:message code="jsp.event.search"/></div >
                <div class="panel-body">
                    <form id="search-form">
                        <div class="input-group">
                            <input id="search-input" type="text" class="form-control" placeholder="<spring:message code="jsp.form.search"/>">
                            <div class="input-group-btn">
                                <button type="submit" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
                            </div>
                        </div>
                    </form>
                    <br>
                    <div id="search-result">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        $(".event-item").addClass("active");

        $("#calendar").fullCalendar({
            height: 400,
            header: {left: '', center: 'title', right: 'today prev,next'},
            timeFormat: ' H:mm ',
            events: {url: '<c:url value="/search/events"/>',
                cache: true},
            eventColor: '#378006',
            eventBackgroundColor: '#378006',
            monthNames: ['<spring:message code="jsp.calendar.january"/>',
                '<spring:message code="jsp.calendar.february"/>',
                '<spring:message code="jsp.calendar.march"/>',
                '<spring:message code="jsp.calendar.april"/>',
                '<spring:message code="jsp.calendar.may"/>',
                '<spring:message code="jsp.calendar.june"/>',
                '<spring:message code="jsp.calendar.july"/>',
                '<spring:message code="jsp.calendar.august"/>',
                '<spring:message code="jsp.calendar.september"/>',
                '<spring:message code="jsp.calendar.october"/>',
                '<spring:message code="jsp.calendar.november"/>',
                '<spring:message code="jsp.calendar.december"/>'
            ],
            dayNames: ['<spring:message code="jsp.calendar.sunday"/>',
                '<spring:message code="jsp.calendar.monday"/>',
                '<spring:message code="jsp.calendar.tuesday"/>',
                '<spring:message code="jsp.calendar.wednsday"/>',
                '<spring:message code="jsp.calendar.thursday"/>',
                '<spring:message code="jsp.calendar.friday"/>',
                '<spring:message code="jsp.calendar.saturday"/>'
            ],
            dayNamesShort: ['<spring:message code="jsp.calendar.sun"/>',
                '<spring:message code="jsp.calendar.mon"/>',
                '<spring:message code="jsp.calendar.tue"/>',
                '<spring:message code="jsp.calendar.wed"/>',
                '<spring:message code="jsp.calendar.thu"/>',
                '<spring:message code="jsp.calendar.fri"/>',
                '<spring:message code="jsp.calendar.sat"/>'
            ],
            eventClick: function(calEvent, jsEvent, view) {
                var url = "<c:url value="/events/" />";
                url = url + calEvent.id;
                window.location.href = url;
            }
        });

        $("form#search-form").submit(function(event) {
            event.preventDefault();
            var value = $("#search-input").val()
            if (value != "") {
                var query = {"query": value};
                $.ajax({
                    data: query,
                    url: '<c:url value="/search/events"/>',
                    type: 'GET',
                    // The type of data that is getting returned.
                    dataType: "html",
                    beforeSend: function() {
                        $("#search-result").html("Buscando, espere por favor...");
                    },
                    success: function(response) {
                        var searchList = '<ul class="list-group">';
                        var obj = $.parseJSON(response);
                        if (obj.length > 0) {
                            $.each(obj, function() {
        console.log(this);                        
        var baseUrl = '<c:url value="/events/"/>';
                                var eventUrl = baseUrl + this["id"];
                                searchList = searchList + '<li class="list-group-item">\
                                        <a href="' + eventUrl + '"> \
                                            <img src="' + eventUrl + '/thumbnail' + '" class="img-thumbnail"  style="margin-right: 10px; max-width: 35px" /> \
                                            <span>' + this['title'] + '</span> \
                                        </a> \
                                    </li>';
                            });
                            searchList = searchList + '</ul>';
                            $("#search-result").html(searchList);
                        }
                        else {
                            $("#search-result").html("<em>Ninguna coincidencia</em>");
                        }
                    }
                });
            }
        });
    </script>

</body>
</html>

