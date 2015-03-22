function setDateTimePicker() {

currentTime = getCurrentDateTime();

    $('#datetimepicker').datetimepicker({
        format: 'yyyy-mm-dd hh:ii',
        autoclose: true,
        todayBtn: true,
        startDate: currentTime,
        language: 'es',
//            viewSelect: 4
    });
}

function setDatePicker() {

    $('#datetimepicker').datetimepicker({
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: true,
        language: 'es',
        maxView: 4,
        minView: 2,
    });
}

function getCurrentDateTime(){
        var currentTime = new Date();
    var dd = currentTime.getDate();
    var MM = currentTime.getMonth() + 1; //January is 0!
    var yyyy = currentTime.getFullYear();
    var hh = currentTime.getHours()
    var ii = currentTime.getMinutes();
    if (dd < 10) {
        dd = '0' + dd
    }
    if (MM < 10) {
        MM = '0' + MM
    }
    if (ii < 10) {
        ii = '0' + ii
    }

    currentTime = yyyy + "-" + MM + "-" + dd + " " + hh + ":" + ii;
    return currentTime;
}

function parsePeriod(miliseconds) {
    var seconds = parseInt(miliseconds) / 1000;
    var min = Math.floor((((seconds % 31536000) % 86400) % 3600) / 60);
    var hor = Math.floor(((seconds % 31536000) % 86400) / 3600);
    min = min % 60;
    return hor + "h:" + min + "m";
}