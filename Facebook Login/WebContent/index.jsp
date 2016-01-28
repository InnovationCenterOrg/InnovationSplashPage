<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	/*  FBConnection fbConnection = new FBConnection();*/
	System.out.println("Loading index.jsp");
%>

<% String base_grant_url = request.getParameter("base_grant_url");%>
<% String user_continue_url = request.getParameter("user_continue_url");%>
<% String node_id = request.getParameter("node_id");%>
<% String node_mac= request.getParameter("node_mac");%>
<% String gateway_id = request.getParameter("gateway_id");%>
<% String client_ip= request.getParameter("client_ip");%>
<% String client_mac= request.getParameter("client_mac");%>

<%
session.setAttribute("base_grant_url", base_grant_url);
session.setAttribute("user_continue_url", user_continue_url);
session.setAttribute("node_id", node_id);
session.setAttribute("node_mac", node_mac);
session.setAttribute("gateway_id", gateway_id);
session.setAttribute("client_ip", client_ip);
session.setAttribute("client_mac", client_mac);


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/login-splash.css" rel="stylesheet">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
<title>Authentication With Facebook</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12 text-center">
				<p></p>
				<h2>IBM Innovation</h2>
				<h2>Center WIFI</h2>
				<p></p>
			</div>
		</div>
		<div class="row">
			<div class="span12 text-center">
				<form method="post" action="testAction" class="form-signin">
					<button class="button" type="submit">Login with Facebook</button>
				</form>
			</div>
		</div>
		
	<div class="row" style="background-color: #F1EEEE;">
		<div class="span12 text-center">
			<p></p>
			<font color="gray">POWERED BY</font>
			<p></p>
		</div>
		<div class="span12 text-center">
			<img src="./img/cisco-meraki-ibm-gray.png" alt="Cisco meraki IBM">
			<p></p>
		</div>
		
	</div>
</div>
<script src="js/bootstrap.min.js"></script>

</body>
</html>		

