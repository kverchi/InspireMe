$( document ).ready(function() {
	userSearch();
});

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