package onlineExaminationsystem;



public class Validation {

    // NAME VALIDATION
    public static boolean isValidName(String name) {
        return name != null
                && !name.trim().isEmpty()
                && name.matches("[A-Za-z ]+");
    }

    // EMAIL VALIDATION
    public static boolean isValidEmail(String email) {
        return email != null
                && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // PHONE VALIDATION
    public static boolean isValidPhone(String phone) {
        return phone != null
                && phone.matches("[0-9]{10}");
    }

    // ID VALIDATION
    public static boolean isValidId(int id) {
        return id > 0;
    }

    // EXAM NAME VALIDATION
    public static boolean isValidExamName(String examName) {
        return examName != null
                && !examName.trim().isEmpty()
                && examName.matches("[A-Za-z0-9 ]+");
    }

    // QUESTION VALIDATION
    public static boolean isValidQuestion(String question) {
        return question != null
                && !question.trim().isEmpty();
    }

    // OPTION VALIDATION
    public static boolean isValidOption(String option) {
        return option != null
                && !option.trim().isEmpty();
    }

    // ANSWER VALIDATION
    public static boolean isValidAnswer(String answer) {
        if (answer == null) {
            return false;
        }

        return answer.equalsIgnoreCase("A")
                || answer.equalsIgnoreCase("B")
                || answer.equalsIgnoreCase("C")
                || answer.equalsIgnoreCase("D");
    }

    // MARKS VALIDATION
    public static boolean isValidMarks(int marks) {
        return marks >= 0;
    }
}
