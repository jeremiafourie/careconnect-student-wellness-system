<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - CareConnect</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="icon" type="image/png" href="Resources/careconnect_logo.png">
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="brand-section">
                <img src="Resources/bc_logo.png" alt="Belgium Campus Logo" class="careconnect-logo">
                <h2>Create Account</h2>
            </div>
            
            <c:if test="${not empty errors}">
                <div class="error-messages">
                    <ul>
                        <c:forEach items="${errors}" var="error">
                            <li>${error}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
            
            <form method="post" action="register">
                <div class="form-group">
                    <label for="studentNumber">Student Number *</label>
                    <input type="text" id="studentNumber" name="studentNumber" 
                           value="${registrationDto.studentNumber}" 
                           placeholder="123456" 
                           maxlength="6"
                           pattern="[0-9]{6}"
                           required>
                </div>
                
                <div class="form-group">
                    <label for="name">First Name *</label>
                    <input type="text" id="name" name="name" 
                           value="${registrationDto.name}" 
                           placeholder="John" 
                           required>
                </div>
                
                <div class="form-group">
                    <label for="surname">Last Name *</label>
                    <input type="text" id="surname" name="surname" 
                           value="${registrationDto.surname}" 
                           placeholder="Doe" 
                           required>
                </div>
                
                <div class="form-group">
                    <label for="email">Email Address *</label>
                    <input type="email" id="email" name="email" 
                           value="${registrationDto.email}" 
                           placeholder="john.doe@example.com" 
                           required>
                </div>
                
                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="tel" id="phone" name="phone" 
                           value="${registrationDto.phone}" 
                           placeholder="0652774945"
                           maxlength="10"
                           pattern="[0-9]{10}">
                </div>
                
                <div class="form-group">
                    <label for="password">Password *</label>
                    <input type="password" id="password" name="password" 
                           placeholder="Enter a strong password" 
                           required>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password *</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" 
                           placeholder="Confirm your password" 
                           required>
                </div>
                
                <button type="submit" class="btn">Create Account</button>
            </form>
            
            <div class="links">
                Already have an account? <a href="login">Sign in here</a><br>
                <a href="index.jsp">Back to Home</a>
            </div>
        </div>
    </div>
    
    <script>
        // Client-side validation
        document.querySelector('form').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Passwords do not match');
                return;
            }
            
            const studentNumber = document.getElementById('studentNumber').value;
            const studentNumberPattern = /^\d{6}$/;
            
            if (!studentNumberPattern.test(studentNumber)) {
                e.preventDefault();
                alert('Student number must be exactly 6 digits (e.g., 123456)');
                return;
            }
        });
        
        // Auto-format student number - only allow digits
        document.getElementById('studentNumber').addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    </script>
</body>
</html>