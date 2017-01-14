
	$(document).ready(function() {

		getList();		


		$("#submitButton").click(function() {

			addUser();

			$('#firstName').val("");
			$('#lastName').val("");
			$('#phoneNumber').val("");
		});
	});

	function workCustomer(uuid) {
		var url = "/rest/WaitList/Service/serviceCustomer/" + uuid;
		$.getJSON(url, function(data) {
			getList();
		});
	}

	function deleteCustomer(uuid) {
		var url = "/rest/WaitList/Service/deleteCustomer/" + uuid;
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
		if( isValidPhone( $('#phoneNumber').val() )) {
			console.log( "valid phone");
			var url = "/rest/WaitList/Service/addCustomer?firstName="
					+ $('#firstName').val() + "&lastName=" + $('#lastName').val()
					+ "&phoneNumber=" + $('#phoneNumber').val();
	
			$.getJSON(url, function(data) {
				getList();
			});
		} else {
			console.log( "bad phone")
			$('#errorText').html("Please provide a valid phone number");
		}
	}

	function getList() {
		url = "/rest/WaitList/Service/customerList"
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
						var first = element.firstName.substring(0, 2);
						for (i = 1; i < (element.firstName.length - 1); i++) {
							first += "*";
						}

						listHtml += first + " " + last + "<br/>";
						storeHtml += element.firstName + " " + element.lastName + " " + element.waitStartTime + " wait duration " + element.waitDuration + "  " + "<a id=\"workMeLink\" href=\"#\" onclick=\"workCustomer('" + element.id + "');return false;\">Click to service</a>" + "  " + "<a id=\"deleteMeLink\" href=\"#\" onclick=\"deleteCustomer('" + element.id + "');return false;\">Click to delete</a><br/>";
					});
							
							
					$('#storeView').html(storeHtml);
					$('#waitingList').html(listHtml);
				});
		
		url = "/rest/WaitList/Service/servicedList"
			$.getJSON(
				url,
				function(data) {
					var servicedHtml="";
					$.each(
						data,
						function(index, element) {
							servicedHtml += element.firstName + " " + element.lastName + " " + element.waitStartTime + " wait duration " + element.waitDuration	+ "  " 	+ element.disposition + "<br/>";
						});
			$('#servicedList').html(servicedHtml);

		
			setTimeout(getList, 10000);
		});
	}

