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
                <form>
                    <div class="form-group">
                      <label for="studentName">Student Name</label>
                      <input type="text" class="form-control" id="studentName">
                    </div>

                    <div class="form-group">
                      <label for="studentSurname">Surname</label>
                      <input type="text" class="form-control" id="studentSurname" >
                    </div>

                    <div class="form-group">
                      <label for="studentNumber">Student Number</label>
                      <input type="text" class="form-control" id="studentNumber" >
                    </div>

                    <div class="form-group">
                      <label for="course">Course</label>
                      <input type="text" class="form-control" id="course">
                    </div>

                    <div class="form-group">
                      <label for="createPassword">Create Password</label>
                      <input type="password" class="form-control" id="createPassword" placeholder="Create password">
                    </div>

                    <div class="form-group">
                      <label for="confirmPassword">Confirm Password</label>
                      <input type="password" class="form-control" id="confirmPassword" placeholder="Confirm password">
                    </div>

                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>

            </div>
            
            <div class="content">
                
            </div>
        </div>
    </body>
</html>
