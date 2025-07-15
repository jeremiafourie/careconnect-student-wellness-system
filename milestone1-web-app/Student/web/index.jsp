<%-- 
    Document   : index.jsp
    Created on : 14 Jul 2025, 20:29:23
    Author     : mosam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="Styles/index.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    </head>
    <body>
        <header>
            <div class="nav">
                <div class="logo">
                    <img src="Media/logo-w.png" alt="alt"/>
                </div>

                <ul class="links">
                    <li>Home</li>
                    <li><a href="/Student/Views/register.jsp" class="btn btn-primary">Register</a></li>
                    <li><a href="/Student/Views/login.jsp" class="btn btn-primary">Login</a></li>
                </ul>
            </div>
            
            <div class="intro-message">
                <h2>Wellness is not a luxury — it's the foundation of your potential. Take care of your mind and body, because your future starts with you.</h2>
                <a class="btn appointment">Book appointment</a>
               
            </div>

        </header>
        
        <div class="student-wellness">
            <h2>Your Wellness Matters </h2>
            <p class="message">
                Personal wellness and academic success are deeply connected. When students prioritize their well-being, they are better equipped to face academic challenges with confidence.
                A healthy student can manage stress effectively, adapt to change, bounce back from setbacks, think critically when solving problems, and build strong, respectful relationships. 
                They’re also more capable of handling conflict, regulating emotions, and practicing compassion—for themselves and others. These skills are essential for maintaining a balance between academics,
                extracurricular commitments, and personal life.
            </p>
            <h2 style="text-align: center; margin-top: 35px;">Key Benefits of Taking Care of Your Well-Being</h2>
            <div class="wellness-tips row">
                <div class="tip col-6 mb-3">
                    <h3>Improved Academic Performance</h3>
                    <div class="img-frame">
                        <img src="Media/tips/improved.jpg" alt="alt"/>
                    </div>
                    <p>A well-balanced student is more focused, motivated, and better able to retain information. Good sleep, nutrition, and emotional stability directly support memory, concentration, and critical thinking.</p>
                </div>
                <div class="tip col-6 mb-3">
                    <h3>Effective Stress Management</h3>
                    <div class="img-frame">
                        <img src="Media/tips/effective.jpg" alt="alt"/>
                        <p>Personal wellness helps students cope with academic and social pressures. Techniques like exercise, mindfulness, and time management reduce anxiety and prevent burnout.</p>
                    </div>
                </div>
           
                <div class="tip col-6">
                  <h3>Improved Academic Performance</h3>
                    <div class="img-frame">
                        <img src="Media/tips/unity.jpg" alt="alt"/>
                    </div>
                    <p>A well-balanced student is more focused, motivated, and better able to retain information. Good sleep, nutrition, and emotional stability directly support memory, concentration, and critical thinking.</p>
                </div>
                
                <div class="tip col-6">
                  <h3>Improved Academic Performance</h3>
                    <div class="img-frame">
                        <img src="Media/tips/resiliance.jpg" alt="alt"/>
                    </div>
                  <p>Wellness habits build emotional strength. When setbacks occur—like poor grades or personal disappointments—students with strong wellness routines are more likely to bounce back positively.</p>
                 </div>  
                 
            </div>
               
           
         </div>
    </body>
</html>
