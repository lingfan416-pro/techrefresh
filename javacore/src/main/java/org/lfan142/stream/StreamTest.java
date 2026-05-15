package org.lfan142.stream;

import java.util.List;
import java.util.Optional;

public class StreamTest {


    public static void main(String[] args) {
        reduceExec();

    }

    public static void mapTransform(){
        List<String> names = List.of("alice", "bob", "cathy");
        Optional<Employee> result = names.stream().filter(name -> name.equals("bob"))
                .map(name -> new Employee(name, 10, 10000)).findFirst();
        result.ifPresent(employ -> System.out.println(employ.getName()));
    }


    public static void employStream(){
        List<Employee> employees = List.of(
                new Employee("Alice", 25, 5000),
                new Employee("Bob", 35, 8000),
                new Employee("Cathy", 40, 9000),
                new Employee("David", 28, 6000)
        );
        List<String> olderThen30Employee =
                employees.stream().filter(employee -> employee.getAge() > 30)
                        .map(Employee::getName)
                        .toList();
        olderThen30Employee.forEach(System.out::println);
    }


    public static void reduceExec(){
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        int result = numbers.stream().reduce(1, Integer::sum);
        System.out.println(result);
    }
}


class Employee {
    private String name;
    private int age;
    private double salary;

    public Employee(String name, int age, double salary){
        this.age = age;
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }
}