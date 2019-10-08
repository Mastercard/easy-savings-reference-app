$(document).ready(function() {
    // alert success
    $(".json").hide();
    $("#show-request").click(function() {
        $(".json").slideDown("1000");
        $("#response").hide();
        $("#request").show();
    });

    $("#show-response").click(function() {
        $(".json").slideDown("1000");
        $("#request").hide();
        $("#response").show();
    });

    $("#reload").click(function(){
        location.reload(true);
    });

    $("#close").click(function() {
        $(".json").slideUp("1000");
    });

    $("#reset").click(function() {
        location.reload(true);
    });
});