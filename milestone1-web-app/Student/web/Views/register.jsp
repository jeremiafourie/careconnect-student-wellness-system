<%-- 
    Document   : register
    Created on : 14 Jul 2025, 23:48:32
    Author     : mosam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="../Styles/register.css" />
    </head>
    <body>
        <div class="form-grid">
            <div class="form-styles">
                <h2>Register</h2>
                <form method="post" action="/Student/RegisterServlet">
                    <div class="form-group">
                      <label for="studentName">Student Name</label>
                      <input type="text" class="form-control" id="studentName" name="studentName" required>
                    </div>

                    <div class="form-group">
                      <label for="studentSurname">Surname</label>
                      <input type="text" class="form-control" id="studentSurname" name="studentSurname" required>
                    </div>

                    <div class="form-group">
                      <label for="emailAddress">Email Address</label>
                      <input type="email" class="form-control" id="emailAddress" name="emailAddress" required>
                    </div>

                    <div class="form-group">
                      <label for="course">Course</label>
                      <select class="form-control" id="course" name="course" required>
                        <option value="">-- Select a course --</option>
                        <option value="Bachelor of Computing">Bachelor of Computing</option>
                        <option value="Bachelor of IT">Bachelor of IT</option>
                        <option value="Diploma">Diploma</option>
                        <option value="Higher Certificate">Higher Certificate</option>
                      </select>
                    </div>

                    <div class="form-group">
                      <label for="createPassword">Create Password</label>
                      <input type="password" class="form-control" id="createPassword" name="createPassword" placeholder="Create password" required>
                    </div>

                    <div class="form-group">
                      <label for="confirmPassword">Confirm Password</label>
                      <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Confirm password" required>
                    </div>

                    <button type="submit" class="btn btn-primary">Submit</button>
                    <% String msg = (String) request.getAttribute("message"); %>
                    <% if (msg != null) { %>
                        <div class="alert alert-info"><%= msg %></div>
                    <% } %>

                </form>


            </div>
            
            <div class="content">
                
            </div>
        </div>
    </body>
</html>
