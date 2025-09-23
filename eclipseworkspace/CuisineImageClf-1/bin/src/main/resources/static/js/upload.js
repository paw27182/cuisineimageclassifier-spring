/*------------------------------
 src/main/resources/static/html
 submit.html - #btn_execute
------------------------------*/
$(function () {
	$('#btn_execute').off().on("click", function () {

		$("#area4Result").empty();  // clear result area

		var command = $("#selected_dropdown_item").val();  // Submit_an_image
		console.log("[appmain.js] command= ", command);

		// formData for POST
		var formData = new FormData($('#uploadForm')[0]);  //FormData object
		formData.append("command", command);

		// deactivate button
		$('#btn_execute').prop("disabled", true);
		$('#btn_execute').text("Executing...");

		$.ajax({
			url: '/upload',
			type: 'POST',
			data: formData,
			processData: false,
			contentType: false,
		})
			.done(function (output, status, xhr) {
//				$('#area4Result').html(output);
                $("#area4Result").html($("#area4Result", output).html());

				$('#area4Result').show();

				// activate button
				$('#btn_execute').prop("disabled", false);
				$('#btn_execute').text("Execute");

			})
			.fail(function (error) {
				console.log(error);
			});  // ajax
	});  // function
});  // function
