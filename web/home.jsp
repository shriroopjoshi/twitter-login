<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tweet analyzer</title>
        <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    </head>
    <body>
        <div style="width: 100%; height: 20%; color: #ffff87">
            <center>
                <h3 style="color: #2c3c87">Tweet Analyzer</h3>
            </center>
        </div>
        <h5>Hello @${sessionScope.user.getUserName()}!</h5>
        <button id="signout_button">Sign out</button>
        <br>
        <br>
        <input type="text" id="query_text">
        <button id="search_button">search</button>
        <button id="analyze_button">analyze</button>
        <div id="img_div">
            <img src="" id="word_cloud"/>
        </div>
        <div id="results"></div>
        <script src="script.js"></script>
    </body>
</html>
