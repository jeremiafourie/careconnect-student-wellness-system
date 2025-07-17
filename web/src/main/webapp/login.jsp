<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - CareConnect</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="icon" type="image/png" href="Resources/careconnect_logo.png">
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="brand-section">
                <img src="Resources/bc_logo.png" alt="Belgium Campus Logo" class="careconnect-logo">
                <h2>Welcome Back</h2>
            </div>
            
            <c:if test="${not empty successMessage}">
                <div class="success-message">
                    ${successMessage}
                </div>
            </c:if>
            
            <c:if test="${not empty errors}">
                <div class="error-messages">
                    <ul>
                        <c:forEach items="${errors}" var="error">
                            <li>${error}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
            
            <form method="post" action="login">
                <div class="form-group">
                    <label for="identifier">Student Number or Email</label>
                    <input type="text" id="identifier" name="identifier" 
                           value="${loginDto.identifier}" 
                           placeholder="123456 or email@example.com" 
                           required>
                </div>
                
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" 
                           placeholder="Enter your password" 
                           required>
                </div>
                
                <button type="submit" class="btn">Sign In</button>
            </form>
            
            <div class="links">
                Don't have an account? <a href="register">Create one here</a><br>
                <a href="index.jsp">Back to Home</a>
            </div>
        </div>
    </div>
    
    <script>
        // Auto-format student number if it looks like one
        document.getElementById('identifier').addEventListener('input', function(e) {
            const value = this.value.trim();
            // If it's all digits and 6 characters or less, treat it as a student number
            if (value.length <= 6 && /^\d+$/.test(value)) {
                this.value = value;
            }
        });
    </script>
</body>
</html>