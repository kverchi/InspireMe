<html>
	<head>
	<%@ include file="include.jspf" %>
	<title>Home</title>
	<link href="<c:url value="/resources/css/jqvmap/jqvmap.css"/>" media="screen" rel="stylesheet" type="text/css" />
    
    <script src="<c:url value="/resources/js/jquery.js"/>"></script>
    <script src="<c:url value="/resources/js/jqvmap/jquery.vmap.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jqvmap/jquery.vmap.europe.js"/>" type="text/javascript"></script>
    
	<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('#vmap').vectorMap({
		    map: 'europe_en',
		    enableZoom: false,
		    showTooltip: true,
		    onRegionClick: function(element, code, region){
		    	//window.location.href = "http://stackoverflow.com";	
		    	window.location.replace("../sights/country?country_code="+code);
		    }
		});
	});
		
	
	</script>
	</head>
	
	<body>
		<div id="wrapper">
			<%@ include file="head.jspf" %>
			<div id="content" class="common_link">
				<p>It is a secret page. It's shown only for authorization users.</p>
			
			<div>
			<form action="../sights/country" method="get">
			<input type="text" list="searchCountry" name="country_code" autocomplete="off" placeholder="Double click to show list">
			<input type="submit" value="Go!">
			</form>
			</div>

			<datalist id="searchCountry">
				<c:forEach items="${countryList}" var="s">
					<option value="${s.getCountryCode()}" >${s.getCountryName()}</option>
				</c:forEach>
			</datalist>

			<div align="center" id="vmap" style="width: 680px; height: 520px; margin-left: 40px;"></div>
			
			</div>
			<%@ include file="footer.jspf" %>
		</div>
		
		
	</body>
</html>