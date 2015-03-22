<%-- 
    Document   : practices
    Created on : 07-oct-2013, 17:19:27
    Author     : Fer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../includes/includes.jsp" %>
<body>

    <!--<a href="#" id="showUploadForm" class="btn btn-sm btn-success pull-right"><i class="fa fa-fw fa-upload"></i> Añadir Práctica</a>-->

    <ol class=" breadcrumb">
        <li><a href="<c:url value="/practices"/>"><spring:message code="jsp.practice.practices"/></a></li>
        <li class="active"><fmt:formatDate pattern="hh:mm dd/MM/yyyy" value="${practice.practiceDate}"/></li>
    </ol>
    <div class="row">
        <div class="col-lg-12" >
            <div id="calendar" ></div>
        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-lg-12">
            <div class="row">
                <div class="col-md-4">
                    <div class="panel panel-default">
                        <div class="panel-body ">
                            <div class="row">
                                <div class="col-xs-5 pull-left">
                                    <ul class="list-unstyled">
                                        <li><h5><small><spring:message code="jsp.practice.distance"/></small></h5>
                                            <span class="distance"></span>
                                        </li>
                                        <li><h5><small><spring:message code="jsp.practice.mediumspeed"/></small></h5>
                                            <span class="mediumspeed"></span>
                                        </li>
                                        <li><h5><small><spring:message code="jsp.practice.minaltitude"/></small></h5>
                                            <span class="minaltitude"></span>
                                        </li>
                                        <li><h5><small><spring:message code="jsp.practice.totalclimb"/></small></h5>
                                            <span class="totalclimb"></span>
                                        </li>
                                    </ul>
                                </div>
                                <div class="col-xs-5 pull-right">
                                    <ul class="list-unstyled">
                                        <li><h5><small><spring:message code="jsp.practice.duration"/></small></h5>
                                            <span class="duration"></span>
                                        </li>
                                        <li><h5><small><spring:message code="jsp.practice.maxspeed"/></small></h5>
                                            <span class="maxspeed"></span>
                                        </li>
                                        <li><h5><small><spring:message code="jsp.practice.maxaltitude"/></small></h5>
                                            <span class="maxaltitude"></span>
                                        </li>
                                        <li><h5><small><spring:message code="jsp.practice.totaldownhill"/></small></h5>
                                            <span class="totaldownhill"></span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-12">
                                    <h4>Segmentos</h4>
                                    <div class="list-group" id="segments">
                                        <a href='#' class="list-group-item active" id='fullRoute'><spring:message code="jsp.route.fullRoute"/></a></li>
                                        <c:forEach var="segment" items='${practice.segmentsResults}' varStatus="loopCount">
                                            <a href='#' class='list-group-item segment' data-segmentid='${segment.id.segmentId}' 
                                               data-duration='${segment.duration}' data-maxspeed='${segment.maxspeed}' 
                                               data-mediumspeed='${segment.mediumspeed}'> 
                                                <spring:message code="jsp.segment.segment"/> ${loopCount.count}
                                            </a>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-8">
                    <div id="map" class="panel panel-default col-lg-12" style="height:400px;"></div>
                    <div id="practice-menu"  style="margin-top: 10px; margin-bottom: 5px; height: 35px; ">
                        <a class="hidden btn btn-sm btn-primary" id="viewSegment" href=''>
                            <span class='divider'></span><i class='fa fa-fw fa-lg fa-eye-slash'></i> <spring:message code="jsp.segment.viewsegment"/>
                        </a>
                        <div class="btn-group pull-right " >
                            <c:if test="${practice.privacy eq 'PRIVATE'}">
                                <a href="#"  id="unlockPractice" class="btn btn-sm btn-primary"><span class="fa fa-unlock" style="margin-right :5px">
                                    </span><spring:message code="jsp.practice.public"/></a>
                                </c:if>
                                <c:if test="${practice.privacy eq 'PUBLIC'}">
                                <a href="#" id="lockPractice" class="btn btn-sm btn-primary"><span class="fa fa-lock" style="margin-right :5px">
                                    </span><spring:message code="jsp.practice.unpublic"/></a>
                                </c:if>
                            <a href="<c:url value="/practices/${practice.id}.gpx"/>" class="btn btn-sm btn-primary"><span class="fa fa-download" style="margin-right :5px">

                                </span><spring:message code="jsp.practice.downloadgpx"/></a>
                            <a href="#" id="deletePractice" class="btn btn-sm btn-danger">
                                <span class="fa fa-trash-o" style="margin-right :5px">
                                </span><spring:message code="jsp.practice.removepractice"/></a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div id="chart_div" class="panel panel-default"></div>
                </div>
            </div>
        </div>
    </div>
    <script>
        $(".practice-item").addClass("active");
        google.load('visualization', '1.0', {'packages': ['corechart']});


        $("#showUploadForm").click(function(event) {
            event.preventDefault();
            $("#uploadForm").toggle("swing");
        });

        function setActive(element) {
            $("#segments").find(".active").removeClass("active");
            $(element).addClass("active");
        }

        function setActive(element, callback) {
            $("#segments").find(".active").removeClass("active");
            $(element).addClass("active", callback);
        }

        $("#practice-item").addClass("active");

        var y = '<fmt:formatDate   pattern="yyyy"  value="${practice.practiceDate}"></fmt:formatDate>';
        var m = <fmt:formatDate   pattern="MM"  value="${practice.practiceDate}"></fmt:formatDate>;
        m = m - 1;
        var d = '<fmt:formatDate   pattern="dd"  value="${practice.practiceDate}"></fmt:formatDate>';
        var url = '<c:url value="/search/practices"/>';
        url = url + '${userId}';
        $("#calendar").fullCalendar({
            height: 400,
            header: {left: '', center: 'title', right: 'today prev,next'},
            timeFormat: ' H(:mm) ',
            year: y,
            month: m,
            date: d,
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

        google.setOnLoadCallback(function() {

            loadPracticeInfo();

            $("#fullRoute").click(function(event) {
                event.preventDefault();
                $("#viewSegment").addClass("hidden");
                setActive($(this), loadPracticeInfo());
            });

            $(".segment").click(function(event) {
                event.preventDefault();
                setActive($(this));

                var url = '<spring:url value="/segments/"/>';
                url = url + $(this).data("segmentid");

                $("#viewSegment").removeClass("hidden");
                $("#viewSegment").attr("href", url);

                var id = $(this).data("segmentid");
                var duration = $(this).data("duration");
                var maxspeed = $(this).data("maxspeed");
                var mediumspeed = $(this).data("mediumspeed");
                $(".duration").html(parsePeriod(duration));
                $(".maxspeed").html(maxspeed + " km/h");
                $(".mediumspeed").html(mediumspeed + " km/h");

                $("#map").html("");
                url = '<c:url value='/segments/'/>';
                var url = url + id;
                url = url + ".json";
                drawMapAndChar(url, "map", "chart_div");
            });
        });

        function parsePeriod(miliseconds) {
            var seconds = parseInt(miliseconds) / 1000;
            var min = Math.floor((((seconds % 31536000) % 86400) % 3600) / 60);
            var hor = Math.floor(((seconds % 31536000) % 86400) / 3600);
            min = min % 60;
            return hor + "h:" + min + "m";
        }

        function loadPracticeInfo() {
            $(".duration").html(parsePeriod(${practice.duration}));
            $(".maxspeed").html(${practice.maxspeed} + " km/h");
            $(".mediumspeed").html(${practice.mediumspeed} + " km/h");


            $("#map").html("");
            var url = "<c:url value="/practices/${practice.id}/routes"/>";
            url = url + ".json";
            drawMapAndChar(url, "map", "chart_div");
        }

        $("#deletePractice").click(function(event) {
            event.preventDefault();
            url = '<c:url value='/practices/'/>';
            var url = url + ${practice.id};
            $.ajax({
                url: url,
                type: 'DELETE',
                success: function(response) {
                    window.location.href = "<c:url value="/practices" />";
                },
                error: function(e) {
                    console.log("Error " + e);
                }
            });
        });

        $("#unlockPractice").click(function(e) {
            e.preventDefault();
            updatePrivacy("PUBLIC")
        });
        $("#lockPractice").click(function(e) {
            e.preventDefault();
            updatePrivacy("PRIVATE")
        });

        function updatePrivacy(privacy) {

            url = '<c:url value='/practices/${practice.id}'/>';
            var practice = {
                id: ${practice.id},
                privacy: privacy
            }
            practice = JSON.stringify(practice);
            console.log(practice);
            $.ajax({
                url: url,
                type: 'POST',
                data: practice,
                contentType: "application/json; charset=utf-8",
                dataType: "html",
                success: function(response) {
                    console.log("ok");
                    window.location.href = url;
                },
                error: function(e) {
                    console.log("Error");
                    console.log(e);
                }
            });
        }
    </script>
</body>
