import java.io.*;
import java.util.*;

public class expTracker {
    private static final String FILE_PATH = "expenses.txt";
    private static Map<String, List<Expense>> expenses = new HashMap<>();

    public static void main(String[] args) {
        loadExpenses();
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("Welcome to Personal Expense Tracker!");
        System.out.println("Available commands: add, view, delete, summary, exit");

        while (true) {
            System.out.print("Enter a command: ");
            command = scanner.nextLine().toLowerCase();

            switch (command) {
                case "add":
                    addExpense(scanner);
                    break;
                case "view":
                    viewExpenses();
                    break;
                case "delete":
                    deleteExpense(scanner);
                    break;
                case "summary":
                    showSummary();
                    break;
                case "exit":
                    saveExpenses();
                    System.out.println("Exiting the expense tracker. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid command. Try again.");
            }
        }
    }

    // Load expenses from file
    private static void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String category = parts[0];
                String description = parts[1];
                double amount = Double.parseDouble(parts[2]);
                Expense expense = new Expense(description, amount);

                if (!expenses.containsKey(category)) {
                    expenses.put(category, new ArrayList<>());
                }
                expenses.get(category).add(expense);
            }
        } catch (IOException e) {
            System.out.println("No previous expense data found.");
        }
    }

    // Save expenses to file
    private static void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String category : expenses.keySet()) {
                for (Expense expense : expenses.get(category)) {
                    writer.write(category + "," + expense.getDescription() + "," + expense.getAmount());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses to file.");
        }
    }

    // Add a new expense
    private static void addExpense(Scanner scanner) {
        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        Expense expense = new Expense(description, amount);

        if (!expenses.containsKey(category)) {
            expenses.put(category, new ArrayList<>());
        }
        expenses.get(category).add(expense);
        System.out.println("Expense added.");
    }

    // View all expenses
    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        for (String category : expenses.keySet()) {
            System.out.println("Category: " + category);
            for (Expense expense : expenses.get(category)) {
                System.out.println(" - " + expense.getDescription() + ": $" + expense.getAmount());
            }
        }
    }

    // Delete an expense
    private static void deleteExpense(Scanner scanner) {
        System.out.print("Enter category of expense to delete: ");
        String category = scanner.nextLine();

        if (!expenses.containsKey(category)) {
            System.out.println("Category not found.");
            return;
        }

        List<Expense> categoryExpenses = expenses.get(category);
        for (int i = 0; i < categoryExpenses.size(); i++) {
            System.out.println(i + 1 + ". " + categoryExpenses.get(i).getDescription() + ": $" + categoryExpenses.get(i).getAmount());
        }

        System.out.print("Enter the number of the expense to delete: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < categoryExpenses.size()) {
            categoryExpenses.remove(index);
            if (categoryExpenses.isEmpty()) {
                expenses.remove(category);
            }
            System.out.println("Expense deleted.");
        } else {
            System.out.println("Invalid selection.");
        }
    }

    // Show summary of expenses by category
    private static void showSummary() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        double total = 0;

        for (String category : expenses.keySet()) {
            double categoryTotal = expenses.get(category).stream().mapToDouble(Expense::getAmount).sum();
            total += categoryTotal;
            System.out.println("Category: " + category + " - Total: $" + categoryTotal);
        }

        System.out.println("Total Spending: $" + total);
    }
}

// Helper class to represent an expense
class Expense {
    private String description;
    private double amount;

    public Expense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
}
