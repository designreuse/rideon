<%-- 
    Document   : practices
    Created on : 07-oct-2013, 17:19:27
    Author     : Fer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../includes/includes.jsp" %>
<body>
    <h2><spring:message code="jsp.practice.practices"/>
        <button class="btn btn-success pull-right" data-toggle="modal" data-target="#myModal"><i class="fa fa-fw fa-upload"></i> 
            <spring:message code="jsp.practice.addpractice"/></button>
    </h2>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel"><spring:message code="jsp.practice.selectgpx"/></h4>
                </div>
                <div class="modal-body">
                    <form:form method="POST" commandName="gpxForm" enctype="multipart/form-data">
                        <form:input type="file" path="file" />
                        <div class="modal-footer">
                            <button class="btn btn-small btn-success" type="submit" data-loading-text="Subiendo..." id="submitGpx">
                                <span class="fa fa-upload" style="margin-right: 4px;"></span> <spring:message code="jsp.form.uploadfile"/></button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-lg-12" >
            <div id="calendar" ></div>
        </div>
    </div>
    <hr>
    <script>
        $("#submitGpx").click(function() {

        });
        $(".practice-item").addClass("active");

        var url = '<c:url value="/search/practices"/>';
        url = url + '${userId}';

        $("#calendar").fullCalendar({
            height: 400,
            header: {left: '', center: 'title', right: 'today prev,next'},
            timeFormat: ' H(:mm) ',
            events: {url: url,
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
                var url = "<c:url value="/practices/" />";
                url = url + calEvent.id;
                window.location.href = url;
            }
        });
    </script>
</body>
