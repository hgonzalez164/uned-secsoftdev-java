<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
	<head>
		<title>ScoringExecutor</title>
		<script type="text/javascript" src="/js/jquery-1.11.2.js"></script>
	</head>
	<body>
		<h1>Bienvenido, ${user.username}</h1>
		<div id="execution-form-div">
			<h2>Sistema de ejecucion de scorings</h2>
			<c:choose>
				<c:when test="${config != null}">
					<p>Evento: ${config.event}</p>
					<p>Ejecutar Proceso</p>
					<form:form id="execution-form" action="/execute" method="POST" commandName="exec">
						<form:hidden path="csrfToken"/>
						<input id="event-input" type="hidden" name="event" value="${config.event}" />
						<label for="file-input">Fichero de entrada:</label>
						<br/>
						<form:select id="file-input" path="file" items="${dataFiles}" />
						<br/>
						<label for="model-args-input">Parametros del modelo:</label>
						<br/>
						<form:textarea id="model-args-input" path="modelArgs"/>
						<br/>
						<input type="submit" />
					</form:form>
				</c:when>
				<c:otherwise>
					No existe configuracion para el usuario
				</c:otherwise>
			</c:choose>
		</div>
		<div id="execution-info-div" style="display:none;">
			<p>Ejecutando proceso...</p>
			<img alt="loading" src="/img/loading.gif">
			<p>Prediccion del evento: <span id="event-span"></span></p>
			<p>Fichero de datos: <span id="file-span"></span></p>
		</div>
		<div id="execution-result-div" style="display:none;">
			<p>Resultado del proceso: <span id="result-span"></span></p>
			<p>Detalles: <span id="result-details-span"></span></p>
			<br/>
			<a href="/">Volver</a>
		</div>
		<br/>
		<hr/>
		<br/>
		<a href="/logout">Logout</a>
	</body>
	<script type="text/javascript">
		$(function() {
		    $("#execution-form").submit(function(e){
				e.preventDefault();
				$("#event-span").text($("#event-input").val());
				$("#file-span").text($("#file-input").val());
				$("#execution-form-div").hide();
				$("#execution-info-div").show();
				$.ajax({
					url: $("#execution-form").attr("action"),
					type: "POST",
					dataType: "json",
					data: $("#execution-form").serialize(),
					success: function(data) {
						$("#execution-info-div").hide();
						$("#result-span").text(data.status);
						$("#result-details-span").text(data.details);
						$("#execution-result-div").show();
					},
					error: function(data, status) {
						$("#execution-info-div").hide();
						$("#result-span").text("ERROR");
						$("#result-details-span").text("Error inesperado");
						$("#execution-result-div").show();
					}
	    	    });
		    });
		});
	</script>
</html>