
var userLocation="default";

	$(document).ready(function() {

		
		userLocation =  (RegExp("location" + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1];
		
		getList();		


		$("#submitButton").click(function() {

			addUser();

			$('#firstName').val("");
			$('#lastName').val("");
			$('#phoneNumber').val("");
		});
	});

	function workCustomer(uuid) {
		var url = "/WaitList/services/Service/serviceCustomer/" + userLocation + "/" + uuid;
		$.getJSON(url, function(data) {
			getList();
		});
	}

	function deleteCustomer(uuid) {
		var url = "/WaitList/services/Service/deleteCustomer/" + userLocation + "/" + uuid;
		$.getJSON(url, function(data) {
			getList();
		});
	}

	function isValidPhone(p) {
			console.log( "checking phone - " + p);
		  var phoneRe = /^[2-9]\d{2}[2-9]\d{2}\d{4}$/;
		  var digits = p.replace(/\D/g, "");
		  return phoneRe.test(digits);
	}
	
	function addUser() {
		$('#errorText').html("");
		if( isValidPhone( $('#phoneNumber').val() )) {
			console.log( "valid phone");
			var url = "/WaitList/services/Service/addCustomer?firstName="
					+ $('#firstName').val() + "&lastName=" + $('#lastName').val()
					+ "&phoneNumber=" + $('#phoneNumber').val()
					+ "&location=" + userLocation;
	
			$.getJSON(url, function(data) {
				getList();
			});
		} else {
			console.log( "bad phone")
			$('#errorText').html("Please provide a valid phone number");
		}
	}

	function getList() {
		url = "/WaitList/services/Service/customerList/" + userLocation
		$.getJSON(
			url,
			function(data) {
				var storeHtml = "";
				var listHtml = "";
				$.each(
					data,
					function(index, element) {
						var last = element.lastName.substring(0, 2);
						for (i = 1; i < (element.lastName.length - 1); i++) {
							last += "*";
						}
						var first = element.firstName;

						listHtml += first + " " + last + "<br/>";
						storeHtml += element.firstName + " " + element.lastName + " " + element.waitStartTime + " wait duration " + element.waitDuration + "  " + "<a id=\"workMeLink\" href=\"#\" onclick=\"workCustomer('" + element.id + "');return false;\">Click to service</a>" + "  " + "<a id=\"deleteMeLink\" href=\"#\" onclick=\"deleteCustomer('" + element.id + "');return false;\">Click to delete</a><br/>";
					});
							
							
					$('#storeView').html(storeHtml);
					$('#waitingList').html(listHtml);
				});
		
		url = "/WaitList/services/Service/servicedList/" + userLocation
			$.getJSON(
				url,
				function(data) {
					var servicedHtml="";
					$.each(
						data,
						function(index, element) {
							servicedHtml += element.firstName + " " + element.lastName + " " + element.waitStartTimeAndDate + " wait duration " + element.waitDuration	+ "  " 	+ element.disposition + "<br/>";
						});
			$('#servicedList').html(servicedHtml);

		
			setTimeout(getList, 10000);
		});
	}

