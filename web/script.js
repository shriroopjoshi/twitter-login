//$(window).load(function () {
//    $('#analyze_button').hide();
//    $('#img_div').hide();
//});

$("#signin_button").click(function () {
    window.location.href = "twitter-login";
});

$('#signout_button').click(function () {
    window.location.href = "twitter-logout";
});

$('#search_button').click(function () {
    if ($('#query_text').val().length > 0) {
        $('#results').html('fetching tweets');
        $.ajax({
            url: "fetch_tweets",
            data: $('#query_text').val(),
            success: function (data) {
                $('#results').html(data);
                $('#analyze_button').show();
            },
            error: function (data) {
                $('#results').html('Unable to find tweets');
                console.log(data);
            }
        });
    }
});

$('#analyze_button').click(function () {
    $.ajax({
        url: "analyze_tweets",
        success: function (data) {
            var res = data.split(":");
            if (res[0] === "success") {
                $('#word_cloud').attr('src', 'img/output.png');
                $('#img_div').show();
            } else {
                $('#img_div').html("Please wait a little while");
            }
        },
        error: function (data) {

        }
    });
});


