package onlineExaminationsystem;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

public class Tablescreation {

    // =====================================================
    // CREATE TABLES
    // =====================================================

    public static void createTables(Connection con) {

        String students = """
        CREATE TABLE IF NOT EXISTS students (student_id INT AUTO_INCREMENT PRIMARY KEY, 
        name VARCHAR(50) NOT NULL, 
        email VARCHAR(100) NOT NULL UNIQUE, 
        phone VARCHAR(10) NOT NULL UNIQUE, 
        password VARCHAR(50) NOT NULL)
        """;

        String exams = """
        CREATE TABLE IF NOT EXISTS exams (exam_id INT AUTO_INCREMENT PRIMARY KEY, 
        exam_name VARCHAR(100) NOT NULL, 
        subject VARCHAR(50) NOT NULL, 
        total_marks INT NOT NULL, 
        duration INT NOT NULL)
        """;

        String questions = """
        CREATE TABLE IF NOT EXISTS questions (question_id INT AUTO_INCREMENT PRIMARY KEY, 
        exam_id INT NOT NULL, 
        question_text VARCHAR(500) NOT NULL, 
        correct_answer VARCHAR(1) NOT NULL, 
        marks INT NOT NULL, 
        CONSTRAINT fk_question_exam FOREIGN KEY (exam_id) REFERENCES exams(exam_id))
        """;

        String results = """
        CREATE TABLE IF NOT EXISTS results (result_id INT AUTO_INCREMENT PRIMARY KEY, 
        student_id INT NOT NULL, 
        exam_id INT NOT NULL, 
        marks_obtained INT NOT NULL, 
        result_status VARCHAR(20) NOT NULL, 
        CONSTRAINT fk_result_student FOREIGN KEY (student_id) REFERENCES students(student_id), CONSTRAINT fk_result_exam FOREIGN KEY (exam_id) REFERENCES exams(exam_id))
        """;

        try (Statement stmt = con.createStatement()) {

            stmt.executeUpdate(students);
            stmt.executeUpdate(exams);
            stmt.executeUpdate(questions);
            stmt.executeUpdate(results);

            System.out.println("All tables created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // ADD STUDENT
    // =====================================================

    public static void addStudent(Connection con, Scanner sc) {

        String sql = """
        INSERT INTO students (name, email, phone, password) VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            if (!Validation.isValidName(name)) {
                System.out.println("Invalid name.");
                return;
            }

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            if (!Validation.isValidEmail(email)) {
                System.out.println("Invalid email.");
                return;
            }

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

            if (!Validation.isValidPhone(phone)) {
                System.out.println("Phone must contain 10 digits.");
                return;
            }

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            if (password.trim().isEmpty()) {
                System.out.println("Password cannot be empty.");
                return;
            }

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                System.out.println("Student added successfully.");
                System.out.println("Student ID: " + rs.getInt(1));
            }

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println("Email or phone already exists.");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // VIEW STUDENTS
    // =====================================================

    public static void viewStudents(Connection con) {

        String sql = """
        SELECT student_id, name, email, phone, password FROM students
        """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== STUDENTS ==========");

            while (rs.next()) {

                System.out.println("Student ID : " + rs.getInt("student_id"));
                System.out.println("Name       : " + rs.getString("name"));
                System.out.println("Email      : " + rs.getString("email"));
                System.out.println("Phone      : " + rs.getString("phone"));
                System.out.println("--------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // SEARCH STUDENT
    // =====================================================

    public static void searchStudent(Connection con, Scanner sc) {

        String sql = """
        SELECT student_id, name, email, phone FROM students WHERE student_id = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(sc.nextLine());

            if (!Validation.isValidId(id)) {
                System.out.println("Invalid ID.");
                return;
            }

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\n========== STUDENT DETAILS ==========");
                System.out.println("ID    : " + rs.getInt("student_id"));
                System.out.println("Name  : " + rs.getString("name"));
                System.out.println("Email : " + rs.getString("email"));
                System.out.println("Phone : " + rs.getString("phone"));

            } else {

                System.out.println("Student not found.");
            }

        } catch (Exception e) {
            System.out.println("Enter a valid Student ID.");
        }
    }

    // =====================================================
    // UPDATE STUDENT
    // =====================================================

    public static void updateStudent(Connection con, Scanner sc) {

        String sql = """
        UPDATE students SET name = ?, email = ?, phone = ?, password = ? WHERE student_id = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter New Name: ");
            String name = sc.nextLine();

            System.out.print("Enter New Email: ");
            String email = sc.nextLine();

            System.out.print("Enter New Phone: ");
            String phone = sc.nextLine();

            System.out.print("Enter New Password: ");
            String password = sc.nextLine();

            if (!Validation.isValidName(name)
                    || !Validation.isValidEmail(email)
                    || !Validation.isValidPhone(phone)
                    || password.trim().isEmpty()) {

                System.out.println("Invalid student details.");
                return;
            }

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student not found.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println("Email or phone already exists.");

        } catch (Exception e) {

            System.out.println("Invalid input.");
        }
    }

    // =====================================================
    // DELETE STUDENT
    // =====================================================

    public static void deleteStudent(Connection con, Scanner sc) {

        String deleteResults = """
        DELETE FROM results WHERE student_id = ?
        """;

        String deleteStudent = """
        DELETE FROM students WHERE student_id = ?
        """;

        try {

            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(sc.nextLine());

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(deleteResults)) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(deleteStudent)) {

                ps.setInt(1, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {

                    con.commit();

                    System.out.println("Student and related results deleted successfully.");

                } else {

                    con.rollback();

                    System.out.println("Student not found.");
                }
            }

        } catch (Exception e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // STUDENT LOGIN
    // =====================================================

    public static void studentLogin(Connection con, Scanner sc) {

        String sql = """
        SELECT student_id, name FROM students WHERE email = ? AND password = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\nLogin successful.");
                System.out.println("Welcome " + rs.getString("name") + "!");

            } else {

                System.out.println("Invalid email or password.");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // ADD EXAM
    // =====================================================

    public static void addExam(Connection con, Scanner sc) {

        String sql = """
        INSERT INTO exams (exam_name, subject, total_marks, duration) VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            System.out.print("Enter Exam Name: ");
            String examName = sc.nextLine();

            if (!Validation.isValidExamName(examName)) {
                System.out.println("Invalid exam name.");
                return;
            }

            System.out.print("Enter Subject: ");
            String subject = sc.nextLine();

            if (subject.trim().isEmpty()) {
                System.out.println("Subject cannot be empty.");
                return;
            }

            System.out.print("Enter Total Marks: ");
            int totalMarks = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Duration in Minutes: ");
            int duration = Integer.parseInt(sc.nextLine());

            if (totalMarks <= 0 || duration <= 0) {
                System.out.println("Marks and duration must be greater than 0.");
                return;
            }

            ps.setString(1, examName);
            ps.setString(2, subject);
            ps.setInt(3, totalMarks);
            ps.setInt(4, duration);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {

                System.out.println("Exam added successfully.");
                System.out.println("Exam ID: " + rs.getInt(1));
            }

        } catch (Exception e) {

            System.out.println("Invalid exam details.");
        }
    }

    // =====================================================
    // VIEW EXAMS
    // =====================================================

    public static void viewExams(Connection con) {

        String sql = """
        SELECT exam_id, exam_name, subject, total_marks, duration FROM exams
        """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== EXAMS ==========");

            while (rs.next()) {

                System.out.println("Exam ID     : " + rs.getInt("exam_id"));
                System.out.println("Exam Name   : " + rs.getString("exam_name"));
                System.out.println("Subject     : " + rs.getString("subject"));
                System.out.println("Total Marks : " + rs.getInt("total_marks"));
                System.out.println("Duration    : " + rs.getInt("duration"));
                System.out.println("--------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // UPDATE EXAM
    // =====================================================

    public static void updateExam(Connection con, Scanner sc) {

        String sql = """
        UPDATE exams SET exam_name = ?, subject = ?, total_marks = ?, duration = ? WHERE exam_id = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Exam ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter New Exam Name: ");
            String examName = sc.nextLine();

            System.out.print("Enter New Subject: ");
            String subject = sc.nextLine();

            System.out.print("Enter New Total Marks: ");
            int totalMarks = Integer.parseInt(sc.nextLine());

            System.out.print("Enter New Duration: ");
            int duration = Integer.parseInt(sc.nextLine());

            if (!Validation.isValidExamName(examName)
                    || subject.trim().isEmpty()
                    || totalMarks <= 0
                    || duration <= 0) {

                System.out.println("Invalid exam details.");
                return;
            }

            ps.setString(1, examName);
            ps.setString(2, subject);
            ps.setInt(3, totalMarks);
            ps.setInt(4, duration);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Exam updated successfully.");
            } else {
                System.out.println("Exam not found.");
            }

        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    // =====================================================
    // DELETE EXAM
    // =====================================================

    public static void deleteExam(Connection con, Scanner sc) {

        String deleteQuestions = """
        DELETE FROM questions WHERE exam_id = ?
        """;

        String deleteResults = """
        DELETE FROM results WHERE exam_id = ?
        """;

        String deleteExam = """
        DELETE FROM exams WHERE exam_id = ?
        """;

        try {

            System.out.print("Enter Exam ID: ");
            int id = Integer.parseInt(sc.nextLine());

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(deleteQuestions)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(deleteResults)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(deleteExam)) {

                ps.setInt(1, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {

                    con.commit();

                    System.out.println("Exam and related records deleted successfully.");

                } else {

                    con.rollback();

                    System.out.println("Exam not found.");
                }
            }

        } catch (Exception e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // ADD QUESTION
    // =====================================================

    public static void addQuestion(Connection con, Scanner sc) {

        String checkExam = """
        SELECT exam_id FROM exams WHERE exam_id = ?
        """;

        String sql = """
        INSERT INTO questions (exam_id, question_text, correct_answer, marks) VALUES (?, ?, ?, ?)
        """;

        try {

            System.out.print("Enter Exam ID: ");
            int examId = Integer.parseInt(sc.nextLine());

            try (PreparedStatement check = con.prepareStatement(checkExam)) {

                check.setInt(1, examId);

                ResultSet rs = check.executeQuery();

                if (!rs.next()) {

                    System.out.println("Exam does not exist. Add the exam first.");

                    return;
                }
            }

            System.out.print("Enter Question: ");
            String question = sc.nextLine();

            if (!Validation.isValidQuestion(question)) {
                System.out.println("Question cannot be empty.");
                return;
            }

            System.out.print("Enter Correct Answer (A/B/C/D): ");
            String answer = sc.nextLine();

            if (!Validation.isValidAnswer(answer)) {
                System.out.println("Answer must be A, B, C or D.");
                return;
            }

            System.out.print("Enter Marks: ");
            int marks = Integer.parseInt(sc.nextLine());

            if (!Validation.isValidMarks(marks) || marks == 0) {
                System.out.println("Marks must be greater than 0.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, examId);
                ps.setString(2, question);
                ps.setString(3, answer.toUpperCase());
                ps.setInt(4, marks);

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    System.out.println("Question added successfully.");
                    System.out.println("Question ID: " + rs.getInt(1));
                }
            }

        } catch (Exception e) {

            System.out.println("Invalid question details.");
        }
    }

    // =====================================================
    // VIEW QUESTIONS
    // =====================================================

    public static void viewQuestions(Connection con) {

        String sql = """
        SELECT q.question_id, e.exam_name, q.question_text, q.correct_answer, q.marks FROM questions q JOIN exams e ON q.exam_id = e.exam_id
        """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== QUESTIONS ==========");

            while (rs.next()) {

                System.out.println("Question ID : " + rs.getInt("question_id"));
                System.out.println("Exam       : " + rs.getString("exam_name"));
                System.out.println("Question   : " + rs.getString("question_text"));
                System.out.println("Answer     : " + rs.getString("correct_answer"));
                System.out.println("Marks      : " + rs.getInt("marks"));
                System.out.println("--------------------------------");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // UPDATE QUESTION
    // =====================================================

    public static void updateQuestion(Connection con, Scanner sc) {

        String sql = """
        UPDATE questions SET question_text = ?, correct_answer = ?, marks = ? WHERE question_id = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Question ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter New Question: ");
            String question = sc.nextLine();

            System.out.print("Enter New Correct Answer (A/B/C/D): ");
            String answer = sc.nextLine();

            System.out.print("Enter New Marks: ");
            int marks = Integer.parseInt(sc.nextLine());

            if (!Validation.isValidQuestion(question)
                    || !Validation.isValidAnswer(answer)
                    || marks <= 0) {

                System.out.println("Invalid question details.");
                return;
            }

            ps.setString(1, question);
            ps.setString(2, answer.toUpperCase());
            ps.setInt(3, marks);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Question updated successfully.");
            } else {
                System.out.println("Question not found.");
            }

        } catch (Exception e) {

            System.out.println("Invalid input.");
        }
    }

    // =====================================================
    // DELETE QUESTION
    // =====================================================

    public static void deleteQuestion(Connection con, Scanner sc) {

        String sql = """
        DELETE FROM questions WHERE question_id = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Question ID: ");
            int id = Integer.parseInt(sc.nextLine());

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Question deleted successfully.");

            } else {

                System.out.println("Question not found.");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // GENERATE RESULT
    // =====================================================

    public static void generateResult(Connection con, Scanner sc) {

        String checkStudent = """
        SELECT student_id FROM students WHERE student_id = ?
        """;

        String checkExam = """
        SELECT exam_id FROM exams WHERE exam_id = ?
        """;

        String sql = """
        INSERT INTO results (student_id, exam_id, marks_obtained, result_status) VALUES (?, ?, ?, ?)
        """;

        try {

            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(sc.nextLine());

            try (PreparedStatement ps = con.prepareStatement(checkStudent)) {

                ps.setInt(1, studentId);

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {

                    System.out.println("Student does not exist.");

                    return;
                }
            }

            System.out.print("Enter Exam ID: ");
            int examId = Integer.parseInt(sc.nextLine());

            try (PreparedStatement ps = con.prepareStatement(checkExam)) {

                ps.setInt(1, examId);

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {

                    System.out.println("Exam does not exist.");

                    return;
                }
            }

            System.out.print("Enter Marks Obtained: ");
            int marks = Integer.parseInt(sc.nextLine());

            if (marks < 0) {

                System.out.println("Marks cannot be negative.");

                return;
            }

            String status;

            if (marks >= 40) {
                status = "PASS";
            } else {
                status = "FAIL";
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, studentId);
                ps.setInt(2, examId);
                ps.setInt(3, marks);
                ps.setString(4, status);

                ps.executeUpdate();

                System.out.println("Result generated successfully.");
                System.out.println("Status: " + status);
            }

        } catch (Exception e) {

            System.out.println("Invalid result details.");
        }
    }

    // =====================================================
    // VIEW RESULTS - JOIN
    // =====================================================

    public static void viewResults(Connection con) {

        String sql = """
        SELECT r.result_id, s.name, e.exam_name, e.subject, r.marks_obtained, r.result_status FROM results r JOIN students s ON r.student_id = s.student_id JOIN exams e ON r.exam_id = e.exam_id
        """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== RESULTS ==========");

            while (rs.next()) {

                System.out.println("Result ID      : " + rs.getInt("result_id"));
                System.out.println("Student        : " + rs.getString("name"));
                System.out.println("Exam           : " + rs.getString("exam_name"));
                System.out.println("Subject        : " + rs.getString("subject"));
                System.out.println("Marks Obtained : " + rs.getInt("marks_obtained"));
                System.out.println("Status         : " + rs.getString("result_status"));
                System.out.println("--------------------------------");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // AGGREGATE FUNCTIONS
    // =====================================================

    public static void aggregateResults(Connection con) {

        String sql = """
        SELECT COUNT(*) AS total_results, SUM(marks_obtained) AS total_marks, AVG(marks_obtained) AS average_marks, MAX(marks_obtained) AS highest_marks, MIN(marks_obtained) AS lowest_marks FROM results
        """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                System.out.println("\n========== RESULT AGGREGATE REPORT ==========");

                System.out.println(
                        "Total Results  : "
                                + rs.getInt("total_results"));

                System.out.println(
                        "Total Marks    : "
                                + rs.getInt("total_marks"));

                System.out.println(
                        "Average Marks  : "
                                + rs.getDouble("average_marks"));

                System.out.println(
                        "Highest Marks  : "
                                + rs.getInt("highest_marks"));

                System.out.println(
                        "Lowest Marks   : "
                                + rs.getInt("lowest_marks"));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
