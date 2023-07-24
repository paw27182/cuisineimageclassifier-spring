/*---------------
 initialization
---------------*/
window.onload = init;
function init() {
	$('#area4Result').hide();

    }


/*------------------------------
 load html
 topview.html - .dropdown-item
------------------------------*/
$(function () {
    $('.dropdown-item').off().on("click", function () {
        $("#home").hide();

        // get dropdown-menu and dropdown-item
        dropdown_menu = $(this).parent().attr("id");  // "Entry" or "Help"
        dropdown_item = $(this).attr("id");           // ex.) "Submit_an_image"
        console.log("[topview.js] dropdown_menu= ", dropdown_menu);
        console.log("[topview.js] dropdown_item= ", dropdown_item);

        // breadcrumb list
        str = dropdown_menu + " > " + dropdown_item;
        $("#label_request_area").text(str);

        // load html
        if (dropdown_menu == "Entry") {
            if (dropdown_item == "submit_an_image") {
                $("#selected_dropdown_item").val("submit_an_image");
                $('#area4Request #area1').load("html/submit.html");
            } else {
                // nothing to do
            };

            $('#label_request_area').show();
            $('#area4Request #area1').show();
            $("#area4Result").hide();

        } else if (dropdown_menu == "Help") {
            if (dropdown_item == "Contact") {
                $('#area4Result').load("html/contact.html");
            } else {
                // nothing to do
            };

            $('#label_request_area').show();
            $('#area4Request #area1').hide();
            $('#area4Result').show();

        } else {
            // nothing to do
        };
    });  // function
});  // function


/*-------------------
 show home tab again
 topview.html
--------------------*/
$(function () {
    $('#home_tab').off().on("click", function () {

        $('#home').show();

        $('#label_request_area').hide();
        $('#area4Request #area1').hide();
        $('#area4Result').hide();
    });
});
