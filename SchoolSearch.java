import java.io.*;
import java.util.*;

class Student {
    final String lastName;
    final String firstName;
    final int grade;
    final int classroom;
    final int bus;
    final String teacherLastName;
    final String teacherFirstName;

    public Student(String lastName, String firstName, int grade, int classroom, int bus, String teacherLastName, String teacherFirstName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.grade = grade;
        this.classroom = classroom;
        this.bus = bus;
        this.teacherLastName = teacherLastName;
        this.teacherFirstName = teacherFirstName;
    }
}

public class SchoolSearch {
    private final List<Student> students = new ArrayList<>();

    public void loadStudents(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length == 7) {
                    try {
                        String lastName = parts[0];
                        String firstName = parts[1];
                        int grade = Integer.parseInt(parts[2]);
                        int classroom = Integer.parseInt(parts[3]);
                        int bus = Integer.parseInt(parts[4]);
                        String teacherLastName = parts[5];
                        String teacherFirstName = parts[6];

                        Student student = new Student(lastName, firstName, grade, classroom, bus, teacherLastName, teacherFirstName);
                        students.add(student);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid data format in file: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Find student(s) by last name");
        System.out.println("2. Find bus route for student(s) by last name");
        System.out.println("3. List students by teacher's last name");
        System.out.println("4. List students by bus route");
        System.out.println("5. List students by classroom");
        System.out.println("Q. Quit");
        System.out.print("Choose an option: ");
    }

    private void handleCommand(String command, Scanner scanner) {
        switch (command.toUpperCase()) {
            case "1" -> {
                System.out.print("Enter student last name: ");
                String lastName = scanner.nextLine().trim();
                printStudentsByLastName(lastName);
            }
            case "2" -> {
                System.out.print("Enter student last name: ");
                String lastName = scanner.nextLine().trim();
                printBusForStudentsByLastName(lastName);
            }
            case "3" -> {
                System.out.print("Enter teacher's last name: ");
                String teacherLastName = scanner.nextLine().trim();
                printStudentsByTeacherLastName(teacherLastName);
            }
            case "4" -> {
                System.out.print("Enter bus route number: ");
                String busNumber = scanner.nextLine().trim();
                printStudentsByBus(busNumber);
            }
            case "5" -> {
                System.out.print("Enter classroom number: ");
                String classroomNumber = scanner.nextLine().trim();
                printStudentsByClassroom(classroomNumber);
            }
            case "Q" -> {
                System.out.println("Exiting...");
                System.exit(0);
            }
            default -> System.out.println("Unknown option. Please choose again.");
        }
    }
    

    private void printStudentsByLastName(String lastName) {
        List<Student> studentsList = students.stream()
            .filter(student -> student.lastName.equals(lastName))
            .toList();
        if (studentsList.isEmpty()) {
            System.out.println("No students found with last name: " + lastName);
        } else {
            for (Student student : studentsList) {
                System.out.printf("%s %s, Grade: %d, Classroom: %d, Teacher: %s %s\n",
                        student.lastName, student.firstName, student.grade, student.classroom,
                        student.teacherFirstName, student.teacherLastName);
            }
        }
    }

    private void printBusForStudentsByLastName(String lastName) {
        List<Student> studentsList = students.stream()
            .filter(student -> student.lastName.equals(lastName))
            .toList();
        if (studentsList.isEmpty()) {
            System.out.println("No students found with last name: " + lastName);
        } else {
            for (Student student : studentsList) {
                System.out.printf("%s %s, Bus Route: %d\n", student.lastName, student.firstName, student.bus);
            }
        }
    }

    private void printStudentsByTeacherLastName(String teacherLastName) {
        List<Student> studentsList = students.stream()
            .filter(student -> student.teacherLastName.equals(teacherLastName))
            .toList();
        if (studentsList.isEmpty()) {
            System.out.println("No students found for teacher: " + teacherLastName);
        } else {
            for (Student student : studentsList) {
                System.out.printf("%s %s\n", student.lastName, student.firstName);
            }
        }
    }

    private void printStudentsByBus(String busNumber) {
        try {
            int bus = Integer.parseInt(busNumber);
            List<Student> studentsList = students.stream()
                .filter(student -> student.bus == bus)
                .toList();
            if (studentsList.isEmpty()) {
                System.out.println("No students found for bus number: " + bus);
            } else {
                for (Student student : studentsList) {
                    System.out.printf("%s %s, Grade: %d, Classroom: %d\n",
                            student.lastName, student.firstName, student.grade, student.classroom);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid bus number: " + busNumber);
        }
    }

    private void printStudentsByClassroom(String classroomNumber) {
        try {
            int classroom = Integer.parseInt(classroomNumber);
            List<Student> studentsList = students.stream()
                .filter(student -> student.classroom == classroom)
                .toList();
            if (studentsList.isEmpty()) {
                System.out.println("No students found in classroom: " + classroom);
            } else {
                for (Student student : studentsList) {
                    System.out.printf("%s %s\n", student.lastName, student.firstName);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid classroom number: " + classroomNumber);
        }
    }

    public static void main(String[] args) {
        SchoolSearch schoolSearch = new SchoolSearch();
        schoolSearch.loadStudents("students.txt");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                schoolSearch.printMenu();
                String command = scanner.nextLine().trim();
                long startTime = System.currentTimeMillis();
                schoolSearch.handleCommand(command, scanner);
                long endTime = System.currentTimeMillis();
                System.out.printf("Search time: %.3f ms\n", (endTime - startTime) / 1e6);
            }
        }
    }
}
