import java.util.*;

// Custom Exception
class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}

public class EmployeeManager {

    public static void main(String[] args) {
        // 1. Store employees using ArrayList
        List<String> employees = new ArrayList<>();
        employees.add("Chakri");
        employees.add("Nitheesh");
        employees.add("Rohith");

        // 2. Count word frequency using HashMap
        String text = "java is fun and java is powerful";
        Map<String, Integer> wordCounts = new HashMap<>();
        
        for (String word : text.split(" ")) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }
        System.out.println("Word Frequencies: " + wordCounts);

        // 3. Unique tags using HashSet
        Set<String> roles = new HashSet<>(Arrays.asList("Dev", "Ops", "Dev", "QA"));
        System.out.println("Unique Roles: " + roles);

        // 4. Exception Handling: Try-catch & Custom Exception
        try {
            checkEmployee(employees, "Sagar");
        } catch (EmployeeNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Operation complete.");
        }
    }

    public static void checkEmployee(List<String> list, String name) throws EmployeeNotFoundException {
        if (!list.contains(name)) {
            throw new EmployeeNotFoundException(name + " is not in the system.");
        }
        System.out.println("Found: " + name);
    }
}

