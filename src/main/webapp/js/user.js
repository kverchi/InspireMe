$(function() {
	$("body").on("focus", "#login", function() {
		if(!$.isEmptyObject($("#loginError"))) 
			$("#loginError").empty();
	});
	$("body").on("blur", "#login", function() {
		validName();
	});
	$("#register").click(function(event) {

		event.preventDefault();
		addUser();
	});
});
function addUser() {
	var formData = new FormData(document.getElementById("user"));
	$.ajax({
		url: contexPath + "/main/addUser", 
		data: formData,
		processData: false,
		contentType: false,
		type: 'POST',
		success: function(response){
			if(response.status == "SUCCESS"){
				window.location.href = contexPath + "/main/" + response.result;
			}else{
				$.each(response.result, function(field, msg) {
					var errorField = $('#' + field + "Error");
					errorField.html(msg);
				});
				refreshCaptcha();
			}	      
		},  
		error: function(e){  
			alert('Error: ' + e);  
		}  
});
}
function refreshCaptcha() {
	var image = document.getElementById("captchaImg");
    image.src = contexPath+"/jcaptcha.jpg?"+Math.floor(Math.random()*100)           
}
	function validName() {
		var login = $("#login").val();
		$.ajax({
			type: "GET",  
		    url: contexPath + "/main/validName",  
		    data: "login=" + login,
		    success: function(response){
		    	if(response == "FALSE")
		    		$("#loginError").append("<p>This name is busy. </p>");
		    },
			error: function(error) {
				alert("In valid name error: " + error);
			}
		});
	}
	
	function userSearch() {
		var login = $("#searchUserField").val();
		$.ajax({
			type: "POST",  
		    url: contexPath + "/main/peopleSearch",  
		    data: "fragment=" + login,
		    success: function(response){
		    	$("#users").empty();
		    	for(var r in response){
		    		var linkText="";
		    		if(response[r].second==0){
		    			linkText = "<p>Already in your friends/requests</p>";
		    		}else linkText = '<a href="addFriend?id='+response[r].first.userId+'">Add to friends</a>';
		    		
		    		$("#users").append("<div class='user'>"+"<h3>"+response[r].first.username+"</h3>"
		    				+ linkText
		    				+"</div>");
		    	}
		    	
		    },
			error: function(error) {
				alert("In valid name error: " + error);
			}
		});
	}
	