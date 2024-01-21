/*
- Project 2
- Lance Campos, Tyler Beach, Justin Parrondo
*/

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Project2 {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Scanner in = new Scanner(System.in);
        // Array of people and counter to keep track of index
        int peopleCounter = 0;
        Person[] people = new Person[100];

        // This HashMap from each "read" option in the program to its corresponding
        // Class object will allow us to handle all "reads" with the same code
        HashMap<Character, Class<?>> reads = new HashMap<>();
        reads.put('3', Student.class);
        reads.put('4', Faculty.class);
        reads.put('6', Staff.class);

        // This HashMap from each "write" option in the program to its corresponding
        // Class object will allow us to handle all "writes" with the same code
        HashMap<Character, Class<?>> writes = new HashMap<>();
        writes.put('1', Faculty.class);
        writes.put('2', Student.class);
        writes.put('5', Staff.class);

        System.out.println("Welcome to my Personal Management Program\n");
        System.out.println("Choose one of the options:");

        boolean programContinue = true;
        while (programContinue) {
            System.out.println("1-  Enter the information a faculty \n" +
                    "2-  Enter the information of a student \n" +
                    "3-  Print tuition invoice for a student \n" +
                    "4-  Print faculty information \n" +
                    "5-  Enter the information of a staff member \n" +
                    "6-  Print the information of a staff member \n" +
                    "7-  Exit Program\n");

            System.out.print("Enter your selection: ");
            char selection = in.nextLine().charAt(0);

            if (reads.containsKey(selection)) {
                // Get the corresponding Class object (Student.class, Faculty.class, or Staff.class)
                Class<?> selectedClass = reads.get(selection);

                // Get its name as a String ("Student", "Faculty", or "Staff")
                String selectedClassName = selectedClass.getName();

                System.out.printf("Enter the %s's ID: ", selectedClass.getName());
                String idToLookup = in.nextLine();
                System.out.println();

                // Check if the given ID is present in the people array, and if it is,
                // if it is also an instance of the selectedClass
                if (!(searchId(idToLookup, peopleCounter, people))|| !(selectedClass.isInstance(people[returnIndex(idToLookup, peopleCounter, people)]))) {
                    // Either no one by that ID was given, or they are not of the right class
                    System.out.printf("No %s matched!\n", selectedClassName);
                } else {
                    people[returnIndex(idToLookup, peopleCounter, people)].print();
                }
            } else if (writes.containsKey(selection)) {
                // Get the corresponding Class object (Student.class, Faculty.class, or Staff.class)
                Class<?> selectedClass = writes.get(selection);

                // Get its name as a String ("Student", "Faculty", or "Staff")
                String selectedClassName = selectedClass.getName();

                // The following line looks complex, but it is simply a way to create a new instance
                // of the selectedClass. For example, if the selectedClass was Faculty.class, the
                // following line would be equivalent to:
                // Person newPerson = new Faculty(in);
                Person newPerson = (Person) selectedClass.getConstructor(Scanner.class).newInstance(in);

                // Add them to the array of people by their ID
                people[peopleCounter] = newPerson;
                peopleCounter++;

                System.out.printf("\n%s added!\n", selectedClassName);
            } else if (selection == '7') {
                System.out.println("\nGoodbye!");
                programContinue = false;
            } else {
                System.out.println("\nInvalid entry- please try again");
            }
            System.out.println();
        }
    }

    // checks if id is in the array of people
    static Boolean searchId(String lookUpId, int peopleCounter, Person[] people) {

        for(int i = 0; i < peopleCounter; i++){
            if(lookUpId.equals(people[i].getId())){return true;}
        }
        return false;
    }
    
    // returns index of object in people array 
    static int returnIndex(String lookUpId, int peopleCounter, Person[] people) {

        for(int i = 0; i < peopleCounter; i++){
            if(lookUpId.equals(people[i].getId())){return i;}
        }
        return 1;
    }


}

abstract class Person {
    // Constants to be used by children when defining print();
    static final int outputWidth = 75;
    static final String borderLine = repeatString("-", outputWidth);

    private String fullName;
    private String id;

    // Usually we do not like to pass a Scanner into the constructor
    // and rely on the constructor to then use it to build the object,
    // but in this case it leads to the cleanest code because each
    // of the concrete classes begins by prompting the user for these two
    // fullName and id first.
    public Person(Scanner in) {
        String objectName = this.getClass().getName();
        System.out.printf("Enter the %s info:\n", objectName.toLowerCase());

        System.out.printf("\tName of %s: ", objectName);
        fullName = in.nextLine();

        System.out.print("\tID: ");
        id = in.nextLine();
    }

    public Person() {
        fullName = "";
        id = "";
    }

    // Simple helper method that can also be used by children
    static String repeatString(String s, int n) {
        return String.valueOf(s).repeat(n);
    }

    // Simple getters
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // nameAndIdString is simply the line containing both
    // the name and ID that appears in both Student invoices
    // and Employee reports
    String getNameAndIdString() {
        return String.format("%s%32s", fullName, id);
    }

    public abstract void print();
}

class Student extends Person {
    // Constants
    private static final DecimalFormat moneyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance();
    private static final double pricePerCreditHour = 236.45;
    private static final int administrativeFee = 52;
    private static final double minimumGpaForDiscount = 3.85;
    private static final double discountPercentage = 0.25;

    private double gpa;
    private int creditHours;

    public Student(Scanner in) {
        // Prompt name and ID
        super(in);

        // Prompt for GPA and Credit Hours
        System.out.print("\tGpa: ");
        gpa = in.nextDouble();
        System.out.print("\tCredit hours: ");
        creditHours = in.nextInt();
        in.nextLine();
    }

    public Student() {
        super();
        gpa = 3.0;
        creditHours = 0;
    }

    public double getGpa() {
        return gpa;
    }

    public int creditHours() {
        return creditHours;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    private double getTotalPriceWithoutDiscount() {
        return creditHours * pricePerCreditHour + administrativeFee;
    }

    private double getDiscount() {
        return gpa >= minimumGpaForDiscount ? getTotalPriceWithoutDiscount() * (1 - discountPercentage) : 0;
    }

    @Override
    public void print() {
        System.out.printf("Here is the tuition invoice for %s\n\n", getFullName());
        System.out.println(borderLine);

        System.out.println(getNameAndIdString());
        System.out.printf("Credit Hours: %d (%s / credit hour)\n", creditHours, moneyFormat.format(pricePerCreditHour));
        System.out.printf("Fees: $%d\n\n", administrativeFee);

        double discount = getDiscount();
        double totalPriceWithDiscount = getTotalPriceWithoutDiscount() - discount;

        String totalPaymentString = String.format("Total payment (after discount): %s", moneyFormat.format(totalPriceWithDiscount));
        String discountString = String.format("(%s discount applied)", moneyFormat.format(discount));
        String padding = repeatString(" ", outputWidth - (totalPaymentString.length() + discountString.length()));

        System.out.println(totalPaymentString + padding + discountString);
        System.out.println(borderLine);
    }
}

abstract class Employee extends Person {
    // Constant ArrayList containing the valid values for department
    private static final ArrayList<String> validDepartments = new ArrayList<>(Arrays.asList("Mathematics", "Engineering", "Sciences"));

    // Note, even though the field for Faculty was dubbed "rank", we will still
    // call it status in Employee because it appears in the same place when
    // generating the report in print() for both Faculty and Staff.
    private String status;
    private String department;

    public Employee(Scanner in) {
        // Prompt name and ID
        super(in);

        // The rest is to be handled by the concrete classes
        // that extend Employee. This is done because the order
        // for getting status and department varies for each.
    }

    public Employee() {
        super();
        this.status = "";
        this.department = "";
    }

    private boolean isDepartmentValid(String department) {
        return validDepartments.contains(department);
    }

    public String getStatus() {
        return status;
    }

    public String getDepartment() {
        return department;
    }

    // Simple setter
    public void setStatus(String status) {
        this.status = status;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Prompts user for department field, repeating
    // if an invalid department is given
    void setDepartmentFromUser(Scanner in) {
        String rawDepartment;
        String department;
        boolean isDepartmentValid;

        do {
            System.out.print("\tDepartment: ");
            rawDepartment = in.nextLine();
            department = rawDepartment.substring(0, 1).toUpperCase() + rawDepartment.substring(1).toLowerCase();
            isDepartmentValid = isDepartmentValid(department);
            if (!isDepartmentValid)
                System.out.printf("\t\t\"%s\" is invalid\n", rawDepartment);
        } while (!isDepartmentValid);

        this.department = department;
    }

    // print() that works for all Employees
    public void print() {
        System.out.println(borderLine);
        System.out.println(getNameAndIdString());
        System.out.printf("%s Department, %s\n", department, status);
        System.out.println(borderLine);
    }
}

class Faculty extends Employee {
    // Constant ArrayList for the valid ranks
    private static final ArrayList<String> validRanks = new ArrayList<>(Arrays.asList("Professor", "Adjunct"));

    private boolean isRankValid(String rank) {
        return validRanks.contains(rank);
    }

    public Faculty(Scanner in) {
        // Prompt name and ID
        super(in);

        // Prompt for rank (repeating if necessary),
        // and then set is as the Employee status
        String rawRank;
        String rank;
        boolean isRankValid;
        do {
            System.out.print("\tRank: ");
            rawRank = in.nextLine();
            rank = rawRank.substring(0, 1).toUpperCase() + rawRank.substring(1).toLowerCase();
            isRankValid = isRankValid(rank);
            if (!isRankValid)
                System.out.printf("\t\t\"%s\" is invalid\n", rawRank);
        } while (!isRankValid);
        setStatus(rank);

        // Prompt for the department and set it
        setDepartmentFromUser(in);
    }

    public Faculty() {
        super();
    }
}

class Staff extends Employee {
    public Staff(Scanner in) {
        // Prompt name and ID
        super(in);

        // This time, prompt for the department first (for some silly reason
        // because it was in the Sample Runs)
        setDepartmentFromUser(in);

        // Prompt for status, and then set it for the Employee.

        // If you are seeing this and thinking it is repetitive
        // of the code in Faculty's constructor, just know the only
        // reason we couldn't make a good function for both is the silly
        // catch that in this case, we have to ask for a single letter
        // than then represents the entire status.
        char statusChar;
        boolean statusIsValid;
        do {
            System.out.print("\tStatus (Enter P for Part Time, or Enter F for Full Time): ");
            statusChar = in.nextLine().toLowerCase().charAt(0);
            statusIsValid = statusChar == 'p' || statusChar == 'f';
            if (!statusIsValid)
                System.out.println("\t\tInvalid!");
        } while (!statusIsValid);
        setStatus(statusChar == 'p' ? "Part Time" : "Full Time");
    }
    
    public Staff() {
        super();
    }
}
