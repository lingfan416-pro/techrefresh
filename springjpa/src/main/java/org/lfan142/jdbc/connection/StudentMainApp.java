package org.lfan142.jdbc.connection;

public class StudentMainApp {

    public static void main(String[] args) {
        StudentOperation.insertStusent("Alice", 22, "alice_2@gmail.com");
        StudentOperation.insertStusent("Bob", 23, "bob_2@gmail.com");

        System.out.println("All students: ");
        StudentOperation.getAllStudents();

        StudentOperation.updateStudent(1, "alice_new@gmail.com");

        StudentOperation.deleteStudent(2);

        System.out.println("\n After Update/Delete:");
        StudentOperation.getAllStudents();
    }
}
