


	$(document).ready(function() {
		populateAdminLocations();
	});

	function populateAdminLocations(){
		$('#globalView').html("");
		var url = "/WaitList/services/Service/locationList"
		$.getJSON(
			url,
			function(data) {
				$.each( data,
					function(index, element) {
						getList( element );
					}
				);
			}
		);
		setTimeout(populateAdminLocations, 10000);
	}

	function getList(specificLocation) {

		
		var url = "/WaitList/services/Service/customerList/" + specificLocation
		var waitingHtml = "";
		var servicedHtml="";
		var corporateHtml = "";
		
		
		$.getJSON( url,
			function(data) {
				$.each(	data, function(index, element) {
									waitingHtml += element.firstName + " " + element.lastName + " " + element.waitStartTime + " wait duration " + element.waitDuration + "<br/>";
								}
				);
				corporateHtml += "<div class='waitingList'>Waiting list<br/>" + waitingHtml + "</div><br/>";;
				
				url = "/WaitList/services/Service/servicedList/" + specificLocation
				$.getJSON( url, function(data) {
							$.each(	data, function(index, element) {
												servicedHtml += element.firstName + " " + element.lastName + " " + element.waitStartTimeAndDate + " wait duration " + element.waitDuration	+ "  " 	+ element.disposition + "<br/>";
											}
							);
							corporateHtml += "<div class='servicedList'>Serviced or Deleted<br/>" + servicedHtml + "</div>";
							corporateHtml ="<div class='locationData'><span class='locationName'>"+specificLocation+"</span>"+corporateHtml+"</div><br/><br/>";		
							$('#globalView').html($('#globalView').html()+corporateHtml);

					}
				);
				
				
			}
		);
	}

