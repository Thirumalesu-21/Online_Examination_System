package onlineExaminationsystem;


import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/Online_Management_System";
        String user = "root";
        String password = "System";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con =
                    DriverManager.getConnection(url, user, password);

            System.out.println("Database connected successfully.");

            Tablescreation.createTables(con);

            Scanner sc = new Scanner(System.in);

            while (true) {

                System.out.println("\n==========================================");
                System.out.println("       ONLINE EXAMINATION SYSTEM");
                System.out.println("==========================================");
                System.out.println("1. Student Management");
                System.out.println("2. Student Login");
                System.out.println("3. Exam Management");
                System.out.println("4. Question Management");
                System.out.println("5. Generate Result");
                System.out.println("6. View Results");
                System.out.println("7. Result Aggregate Report");
                System.out.println("8. Exit");

                System.out.print("Enter your choice: ");

                int choice;

                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid number.");
                    continue;
                }

                switch (choice) {

                    case 1:
                        studentMenu(con, sc);
                        break;

                    case 2:
                        Tablescreation.studentLogin(con, sc);
                        break;

                    case 3:
                        examMenu(con, sc);
                        break;

                    case 4:
                        questionMenu(con, sc);
                        break;

                    case 5:
                        Tablescreation.generateResult(con, sc);
                        break;

                    case 6:
                        Tablescreation.viewResults(con);
                        break;

                    case 7:
                        Tablescreation.aggregateResults(con);
                        break;

                    case 8:
                        System.out.println("Thank you.");
                        sc.close();
                        con.close();
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // STUDENT MENU
    // =====================================================

    public static void studentMenu(Connection con, Scanner sc) {

        while (true) {

            System.out.println("\n========== STUDENT MANAGEMENT ==========");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            switch (choice) {

                case 1:
                    Tablescreation.addStudent(con, sc);
                    break;

                case 2:
                    Tablescreation.viewStudents(con);
                    break;

                case 3:
                    Tablescreation.searchStudent(con, sc);
                    break;

                case 4:
                    Tablescreation.updateStudent(con, sc);
                    break;

                case 5:
                    Tablescreation.deleteStudent(con, sc);
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // EXAM MENU
    // =====================================================

    public static void examMenu(Connection con, Scanner sc) {

        while (true) {

            System.out.println("\n========== EXAM MANAGEMENT ==========");
            System.out.println("1. Add Exam");
            System.out.println("2. View Exams");
            System.out.println("3. Update Exam");
            System.out.println("4. Delete Exam");
            System.out.println("5. Back");

            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            switch (choice) {

                case 1:
                    Tablescreation.addExam(con, sc);
                    break;

                case 2:
                    Tablescreation.viewExams(con);
                    break;

                case 3:
                    Tablescreation.updateExam(con, sc);
                    break;

                case 4:
                    Tablescreation.deleteExam(con, sc);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // QUESTION MENU
    // =====================================================

    public static void questionMenu(Connection con, Scanner sc) {

        while (true) {

            System.out.println("\n========== QUESTION MANAGEMENT ==========");
            System.out.println("1. Add Question");
            System.out.println("2. View Questions");
            System.out.println("3. Update Question");
            System.out.println("4. Delete Question");
            System.out.println("5. Back");

            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            switch (choice) {

                case 1:
                    Tablescreation.addQuestion(con, sc);
                    break;

                case 2:
                    Tablescreation.viewQuestions(con);
                    break;

                case 3:
                    Tablescreation.updateQuestion(con, sc);
                    break;

                case 4:
                    Tablescreation.deleteQuestion(con, sc);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
