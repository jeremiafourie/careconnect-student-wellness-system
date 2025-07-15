package model;

public class Student {
    
    //properties of student
    private final String name;
    private final String surname;
    private final String studentNumber;
    private final String course;
    private final String gender;
    private final String password;


    // Parameterized constructor
    public Student(String name, String surname, String studentNumber, String course, String gender, String password) {
        this.name = name;
        this.surname = surname;
        this.studentNumber = studentNumber;
        this.course = course;
        this.gender = gender;
        this.password = password;
    }

   

    // Optional: toString() method for easy display
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", course='" + course + '\'' +
                ", gender='" + gender + '\'' +
                ", password='[PROTECTED]'}";
    }
}
