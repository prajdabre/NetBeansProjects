<%-- 
    Document   : index
    Created on : 31 Jul, 2013, 11:39:28 PM
    Author     : RAJ
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--

Design by Free CSS Templates
http://www.freecsstemplates.org
Released for free under a Creative Commons Attribution 2.5 License

Title      : Voyage
Version    : 1.0
Released   : 20090118
Description: A two-column, fixed-width and lightweight template ideal for 1024x768 resolutions. Suitable for blogs and small websites.

-->
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <title>Voyage by Free Css Templates</title>
        <meta name="keywords" content="" />
        <meta name="description" content="" />
        <link href="default.css" rel="stylesheet" type="text/css" />
        <script src="http://code.jquery.com/jquery-latest.js">
        </script>
        <script>
            $(document).ready(function() {
                $('#hitme').click(function(event) {

                    
                    //$('#post').text($("#itemtype").val());
                    // $.post("SearchResults", { type: $("#itemtype").val(), query: $("#query") }, function(responseText) {
                    //    $('#post').html(responseText);
                    //} );
                    $.ajax({
                        type : "POST",
                        url : "SearchResults1",
                        data : "type=" + $("#type").val() + "&query=" + $("#query").val(),
                        success : function(data) {
                            $("#post").html(data);
                        }
                    });
                       
                   
                });

                $('#about').click(function(event) {

                    $.get('aboutus',function(responseText) {
                        $('#post').html(responseText);
                    });
                });

                $('#contact').click(function(event) {

                    $.get('contactus',function(responseText) {
                        $('#post').html(responseText);
                    });
                });
            });
        </script>
    </head>
    <body>
        <!-- <h1 id="welcometext">Hello World!</h1>
         <button id="submit">Try this</button> -->
        <!-- start header -->
        <div id="header">
            <div id="menu">
                <ul>
                    <li class="current_page_item"><a href="#">Homepage</a></li>
                    <li id="about"><a href="#" >About</a></li>
                    <li class="last" id="contact" ><a href="#" >Contact</a></li>
                </ul>
            </div>
        </div>
        <!-- end header -->
        <div id="logo">
            <h1><a href="#">QUIKQUOTE </a></h1><br/>
            <h2>One for all, all in one</h2>
        </div>

        <div id="banner"></div>

        <!-- start page -->
        <div id="page">
            <!-- start content -->
            <div id="content">
                <h2 class="title">Welcome to our Website </h2>
                <div id="post" class="post">

                    Watch The Magic Happen Here!


                </div>

            </div>
            <!-- end content -->
            <!-- start sidebar -->
            <div id="sidebar">
                <ul>
                    <li id="search">
                        <h2>Search</h2>

                        
                        <input name="query" type="text" id="query" value="" />
                        <button type="submit" id="hitme"> HIT ME </button><br/>
                        <select id="type" name="type">
                            <option value="Shoes">Shoes</option>
                            <option value="Mobile">Mobiles</option>
                            <option value="Watche">Watches</option>
                            <option value="Clothes">Clothes</option>
                        </select>


                    </li>

                </ul>
            </div>
            <!-- end sidebar -->
            <div style="clear: both;">&nbsp;</div>
        </div>
        <!-- end page -->
        <!-- start footer -->
        <div id="footer">
            <p id="legal">( c ) 2008. All Rights Reserved. <a href="http://www.freecsstemplates.org/">Voyage</a> designed by <a href="http://www.freecsstemplates.org/">Free CSS Templates</a>.</p>
        </div>
        <!-- end footer -->
        <div align=center>This template  downloaded form <a href='http://all-free-download.com/free-website-templates/'>free website templates</a></div></body>
</html>
