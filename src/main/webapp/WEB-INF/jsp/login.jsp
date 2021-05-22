<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Log in with your account</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
</head>

<body>

<div class="container">
    <form method="POST" action="${contextPath}/login" class="form-signin">
        <h2 class="form-heading">Log in</h2>
        <%-- ${error} and ${message} correnspond the code in UserAccountController.java }--%>
        <%-- Also can be done this way:       --%>
        <%--        <c:if test="${param.error != null}">--%>
        <%--            <p>Invalid username or password!</p>--%>
        <%--        </c:if>--%>
        <div class="form-group ${error != null ? 'has-error' : ''}">
            <span style="color: lawngreen">${message}</span>
            <span style="color: red">${error}</span><br>
            <input name="username" type="text" class="form-control" placeholder="Email"
                   autofocus="true"/><br>
            <input name="password" type="password" class="form-control" placeholder="Password"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <br>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
            <h4 class="text-center"><a href="${contextPath}/registration">Create an account</a></h4>
        </div>
    </form>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>