<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
	<head>
		<title>Login</title>
	</head>
	<body>
		<h1>Login</h1>
		<form:form action="/login" method="POST" commandName="auth">
			<form:errors htmlEscape="true" />
			<br/><br/>		
			<label for="username">Username</label>
			<form:input path="username" />
			<br/><br/>
			<label for="password">Password</label>
			<form:password path="password" />
			<br/><br/>
			<label for="rememberMe">Recordarme</label>
			<form:checkbox path="rememberMe" />
			<input type="submit" />
		</form:form>
	</body>
</html>