<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - CareConnect</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="icon" type="image/png" href="Resources/careconnect_logo.png">
</head>
<body>
    <div class="dashboard-header">
        <div class="header-brand">
            <img src="Resources/careconnect_logo.png" alt="CareConnect Logo" class="careconnect-logo">
            <h1>CareConnect</h1>
        </div>
        <div class="user-info">
            <span>${user.name} ${user.surname}</span>
            <span>ID: ${user.studentNumber}</span>
            <a href="logout" class="logout-btn">Logout</a>
        </div>
    </div>
    
    <div class="dashboard-content">
        <div class="dashboard-main">
            <h2>Welcome ${user.name}</h2>
            <p>Your CareConnect account is successfully set up. You can now access the CareConnect desktop platform to effectively manage your wellbeing.</p>
            
            <div class="features-grid">
                <div class="feature-card">
                    <h3>Profile Management</h3>
                    <p>Update your personal information, contact details, and wellness preferences.</p>
                </div>
                
                <div class="feature-card">
                    <h3>Health Tracking</h3>
                    <p>Monitor your wellness metrics, set health goals, and track your progress over time.</p>
                </div>
                
                <div class="feature-card">
                    <h3>Resource Center</h3>
                    <p>Access wellness resources, articles, and tools to support your health journey.</p>
                </div>
                
                <div class="feature-card">
                    <h3>Appointment Booking</h3>
                    <p>Schedule appointments with wellness professionals and manage your healthcare calendar.</p>
                </div>
            </div>
            
            <div style="margin-top: 40px; padding: 20px; background: #f8f9fa; border-radius: 8px; border-left: 4px solid #4ac1c1;">
                <h3 style="color: #333; margin-bottom: 10px;">Your Account Details</h3>
                <p><strong>Student Number:</strong> ${user.studentNumber}</p>
                <p><strong>Name:</strong> ${user.name} ${user.surname}</p>
                <p><strong>Email:</strong> ${user.email}</p>
                <c:if test="${not empty user.phone}">
                    <p><strong>Phone:</strong> ${user.phone}</p>
                </c:if>
                <p><strong>Account Created:</strong> ${user.createdAt}</p>
            </div>
        </div>
    </div>
</body>
</html>