import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class test_validation {
    public static void main(String[] args) {
        try {
            // Load the shared module classes
            Path sharedTarget = Paths.get("shared/target/classes");
            if (!Files.exists(sharedTarget)) {
                System.out.println("❌ Shared module not compiled. Please run 'mvn compile' first.");
                return;
            }
            
            // Create class loader for shared classes
            URLClassLoader classLoader = new URLClassLoader(new URL[]{sharedTarget.toUri().toURL()});
            
            // Load ValidationUtils class
            Class<?> validationUtils = classLoader.loadClass("com.bc.shared.utils.ValidationUtils");
            
            // Test student number validation
            Method isValidStudentNumber = validationUtils.getMethod("isValidStudentNumber", String.class);
            System.out.println("🧪 Testing ValidationUtils.isValidStudentNumber()");
            System.out.println("   A12345678: " + isValidStudentNumber.invoke(null, "A12345678"));
            System.out.println("   B98765432: " + isValidStudentNumber.invoke(null, "B98765432"));
            System.out.println("   invalid123: " + isValidStudentNumber.invoke(null, "invalid123"));
            System.out.println("   A1234567: " + isValidStudentNumber.invoke(null, "A1234567"));
            
            // Test email validation
            Method isValidEmail = validationUtils.getMethod("isValidEmail", String.class);
            System.out.println("\n🧪 Testing ValidationUtils.isValidEmail()");
            System.out.println("   test@example.com: " + isValidEmail.invoke(null, "test@example.com"));
            System.out.println("   student@bc.ca: " + isValidEmail.invoke(null, "student@bc.ca"));
            System.out.println("   invalid-email: " + isValidEmail.invoke(null, "invalid-email"));
            
            // Test password validation
            Method isValidPassword = validationUtils.getMethod("isValidPassword", String.class);
            System.out.println("\n🧪 Testing ValidationUtils.isValidPassword()");
            System.out.println("   Password123!: " + isValidPassword.invoke(null, "Password123!"));
            System.out.println("   weakpass: " + isValidPassword.invoke(null, "weakpass"));
            System.out.println("   NoNumbers!: " + isValidPassword.invoke(null, "NoNumbers!"));
            
            // Test name validation
            Method isValidName = validationUtils.getMethod("isValidName", String.class);
            System.out.println("\n🧪 Testing ValidationUtils.isValidName()");
            System.out.println("   John: " + isValidName.invoke(null, "John"));
            System.out.println("   Mary-Jane: " + isValidName.invoke(null, "Mary-Jane"));
            System.out.println("   O'Connor: " + isValidName.invoke(null, "O'Connor"));
            System.out.println("   123Invalid: " + isValidName.invoke(null, "123Invalid"));
            
            // Test phone validation
            Method isValidPhone = validationUtils.getMethod("isValidPhone", String.class);
            System.out.println("\n🧪 Testing ValidationUtils.isValidPhone()");
            System.out.println("   1234567890: " + isValidPhone.invoke(null, "1234567890"));
            System.out.println("   +1234567890123: " + isValidPhone.invoke(null, "+1234567890123"));
            System.out.println("   invalid-phone: " + isValidPhone.invoke(null, "invalid-phone"));
            System.out.println("   (empty): " + isValidPhone.invoke(null, ""));
            
            // Load BCrypt JAR for password testing
            Path bcryptJar = Paths.get("/home/jeremia/.m2/repository/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar");
            if (Files.exists(bcryptJar)) {
                URLClassLoader bcryptClassLoader = new URLClassLoader(new URL[]{
                    sharedTarget.toUri().toURL(),
                    bcryptJar.toUri().toURL()
                });
                
                Class<?> passwordUtils = bcryptClassLoader.loadClass("com.bc.shared.utils.PasswordUtils");
                
                // Test password hashing
                Method hashPassword = passwordUtils.getMethod("hashPassword", String.class);
                Method verifyPassword = passwordUtils.getMethod("verifyPassword", String.class, String.class);
                
                System.out.println("\n🧪 Testing PasswordUtils.hashPassword() and verifyPassword()");
                String plainPassword = "TestPassword123!";
                String hashedPassword = (String) hashPassword.invoke(null, plainPassword);
                System.out.println("   Original: " + plainPassword);
                System.out.println("   Hashed: " + hashedPassword);
                System.out.println("   Verify correct: " + verifyPassword.invoke(null, plainPassword, hashedPassword));
                System.out.println("   Verify incorrect: " + verifyPassword.invoke(null, "WrongPassword", hashedPassword));
            }
            
            System.out.println("\n✅ All validation tests completed successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Test error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}