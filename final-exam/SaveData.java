import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SaveData extends Thread {
    private static final String FILE_PATH = "data.txt";
    private static final String EXPORT_DIR = "exports/";
    private static final Object FILE_LOCK = new Object();

    private Person person;
    private List<Person> personsInMemory;

    public SaveData(Person person) {
        this.person = person;
        this.personsInMemory = new ArrayList<>();
    }

    @Override
    public void run() {
        synchronized (FILE_LOCK) {
            FileWriter fileWriter = null;
            BufferedWriter bufferedWriter = null;
            PrintWriter printWriter = null;

            try {
                File file = new File(FILE_PATH);
                if (!file.exists()) {
                    file.createNewFile();
                    System.out.println("File created: " + FILE_PATH);
                }

                loadDataToArrayList();

                personsInMemory.add(person);

                fileWriter = new FileWriter(FILE_PATH, true);
                bufferedWriter = new BufferedWriter(fileWriter);
                printWriter = new PrintWriter(bufferedWriter);

                printWriter.println(person.toString());
                System.out.println("Person saved: " + person.getName());

                calculateStatistics();

            } catch (IOException e) {
                System.err.println("Error saving data: " + e.getMessage());
            } finally {
                try {
                    if (printWriter != null) printWriter.close();
                    if (bufferedWriter != null) bufferedWriter.close();
                    if (fileWriter != null) fileWriter.close();
                } catch (IOException e) {
                    System.err.println("Error closing file: " + e.getMessage());
                }
            }
        }
    }

    private void loadDataToArrayList() {
        BufferedReader reader = null;

        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return;
            }

            reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    personsInMemory.add(Person.fromString(line));
                }
            }

            System.out.println("Loaded " + personsInMemory.size() + " persons into memory");

        } catch (IOException e) {
            System.err.println("Error loading data to ArrayList: " + e.getMessage());
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                System.err.println("Error closing file: " + e.getMessage());
            }
        }
    }

    private void calculateStatistics() {
        int totalPeople = personsInMemory.size();

        if (totalPeople == 0) {
            System.out.println("No people registered");
            return;
        }

        double sumAge = 0;
        double sumHeight = 0;
        int minAge = Integer.MAX_VALUE;
        int maxAge = Integer.MIN_VALUE;
        double minHeight = Double.MAX_VALUE;
        double maxHeight = Double.MIN_VALUE;
        String youngestPerson = "";
        String oldestPerson = "";
        String tallestPerson = "";
        String shortestPerson = "";

        for (Person personItem : personsInMemory) {
            try {
                int age = Integer.parseInt(personItem.getAge());
                double height = Double.parseDouble(personItem.getHeight().replace(",", "."));

                sumAge += age;
                sumHeight += height;

                if (age < minAge) {
                    minAge = age;
                    youngestPerson = personItem.getName();
                }
                if (age > maxAge) {
                    maxAge = age;
                    oldestPerson = personItem.getName();
                }
                if (height < minHeight) {
                    minHeight = height;
                    shortestPerson = personItem.getName();
                }
                if (height > maxHeight) {
                    maxHeight = height;
                    tallestPerson = personItem.getName();
                }

            } catch (NumberFormatException e) {
                System.err.println("Error processing data for: " + personItem.getName());
            }
        }

        double averageAge = sumAge / totalPeople;
        double averageHeight = sumHeight / totalPeople;

        System.out.println("\n========== STATISTICS ==========");
        System.out.println("Total people registered: " + totalPeople);
        System.out.println();
        System.out.println("AGE STATISTICS:");
        System.out.printf("  Average age: %.2f years%n", averageAge);
        System.out.println("  Minimum age: " + minAge + " years (" + youngestPerson + ")");
        System.out.println("  Maximum age: " + maxAge + " years (" + oldestPerson + ")");
        System.out.println();
        System.out.println("HEIGHT STATISTICS:");
        System.out.printf("  Average height: %.2f m%n", averageHeight);
        System.out.printf("  Minimum height: %.2f m (%s)%n", minHeight, shortestPerson);
        System.out.printf("  Maximum height: %.2f m (%s)%n", maxHeight, tallestPerson);
        System.out.println("================================\n");
    }

    public void sortData(String sortBy) {
        synchronized (FILE_LOCK) {
            BufferedReader reader = null;
            PrintWriter writer = null;
            List<Person> persons = new ArrayList<>();

            try {
                File file = new File(FILE_PATH);
                if (!file.exists()) {
                    System.out.println("File not found");
                    return;
                }

                reader = new BufferedReader(new FileReader(FILE_PATH));
                String line;

                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        persons.add(Person.fromString(line));
                    }
                }
                reader.close();

                Comparator<Person> comparator = getComparator(sortBy);
                if (comparator != null) {
                    Collections.sort(persons, comparator);

                    writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH)));
                    for (Person personItem : persons) {
                        writer.println(personItem.toString());
                    }

                    System.out.println("Data sorted by: " + sortBy);
                } else {
                    System.out.println("Invalid sort field: " + sortBy);
                }

            } catch (IOException e) {
                System.err.println("Error sorting data: " + e.getMessage());
            } finally {
                try {
                    if (reader != null) reader.close();
                    if (writer != null) writer.close();
                } catch (IOException e) {
                    System.err.println("Error closing file: " + e.getMessage());
                }
            }
        }
    }

    private Comparator<Person> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "name":
                return Comparator.comparing(Person::getName);
            case "age":
                return Comparator.comparing((Person personItem) -> {
                    try {
                        return Integer.parseInt(personItem.getAge());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                });
            case "height":
                return Comparator.comparing((Person personItem) -> {
                    try {
                        return Double.parseDouble(personItem.getHeight().replace(",", "."));
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                });
            case "cpf":
                return Comparator.comparing(Person::getCpf);
            case "address":
                return Comparator.comparing(Person::getAddress);
            case "birthdate":
                return Comparator.comparing(Person::getBirthDate);
            default:
                return null;
        }
    }

    public void exportData() {
        synchronized (FILE_LOCK) {
            try {
                File exportDirectory = new File(EXPORT_DIR);
                if (!exportDirectory.exists()) {
                    exportDirectory.mkdir();
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String timestamp = sdf.format(new Date());
                String exportFileName = EXPORT_DIR + "data_export_" + timestamp + ".txt";

                if (personsInMemory.isEmpty()) {
                    System.out.println("No data to export");
                    return;
                }

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(new BufferedWriter(new FileWriter(exportFileName)));
                    for (Person personItem : personsInMemory) {
                        writer.println(personItem.toString());
                    }
                    System.out.println("Data exported to: " + exportFileName);
                } finally {
                    if (writer != null) writer.close();
                }

            } catch (IOException e) {
                System.err.println("Error exporting data: " + e.getMessage());
            }
        }
    }

    public List<Person> getPersonsInMemory() {
        return personsInMemory;
    }
}