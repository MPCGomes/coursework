package college.contactlist;

import java.util.ArrayList;

public class ContactList {

    private ArrayList<Contact> contacts = new ArrayList<>();

    public void save(Contact contact) {
        contacts.add(contact);
    }

    public void createContact() {
        System.out.println("""
                What kind of contact would you like to add?
                1. Person.
                2. Company.
                """);

        int choice = InputScanner.scanner.nextInt();
        InputScanner.scanner.nextLine(); // consume leftover line break

        if (choice == 1) {
            Person person = new Person();

            System.out.println("Type the contact's name.");
            person.setName(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's address.");
            person.setAddress(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's email.");
            person.setEmail(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's CPF.");
            person.setCpf(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's birthday.");
            person.setBirthday(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's marital status.");
            person.setMaritalStatus(InputScanner.scanner.nextLine());

            save(person);

        } else if (choice == 2) {
            Company company = new Company();

            System.out.println("Type the contact's name.");
            company.setName(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's address.");
            company.setAddress(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's email.");
            company.setEmail(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's CNPJ.");
            company.setCnpj(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's state registration.");
            company.setStateRegistration(InputScanner.scanner.nextLine());
            System.out.println("Type the contact's business name.");
            company.setBusinessName(InputScanner.scanner.nextLine());

            save(company);

        } else {
            System.out.println("Invalid option, please try again.");
            createContact();
        }
    }

    public void delete(String documentNumber) {
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);

            if (contact instanceof Person) {
                if (((Person) contact).getCpf().equals(documentNumber)) {
                    contacts.remove(i);
                    i--;
                }
            }
            if (contact instanceof Company) {
                if (((Company) contact).getCnpj().equals(documentNumber)) {
                    contacts.remove(i);
                    i--;
                }
            } else {
                continue;
            }
        }
    }

    public void findContactByDocumentNumber(String documentNumber) {
        for (Contact contact : contacts) {
            if (contact instanceof Person) {
                if (((Person) contact).getCpf().equals(documentNumber)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("--- // ---").append("\n")
                            .append(contact.getName()).append("\n")
                            .append(contact.getAddress()).append("\n")
                            .append(contact.getEmail()).append("\n")
                            .append(((Person) contact).getCpf()).append("\n")
                            .append(((Person) contact).getBirthday()).append("\n")
                            .append(((Person) contact).getMaritalStatus()).append("\n");
                    System.out.println(stringBuilder);
                    break;
                }
            } else if (contact instanceof Company) {
                if (((Company) contact).getCnpj().equals(documentNumber)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("--- // ---").append("\n")
                            .append(contact.getName()).append("\n")
                            .append(contact.getAddress()).append("\n")
                            .append(contact.getEmail()).append("\n")
                            .append(((Company) contact).getCnpj()).append("\n")
                            .append(((Company) contact).getStateRegistration()).append("\n")
                            .append(((Company) contact).getBusinessName()).append("\n");
                    System.out.println(stringBuilder);
                    break;
                }
            }
        }
    }

    public void findAllContacts() {
        for (Contact contact : contacts) {
            if (contact instanceof Person) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("--- // ---").append("\n")
                        .append(contact.getName()).append("\n")
                        .append(contact.getAddress()).append("\n")
                        .append(contact.getEmail()).append("\n")
                        .append(((Person) contact).getCpf()).append("\n")
                        .append(((Person) contact).getBirthday()).append("\n")
                        .append(((Person) contact).getMaritalStatus()).append("\n");
                System.out.println(stringBuilder);
            }
            if (contact instanceof Company) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("--- // ---").append("\n")
                        .append(contact.getName()).append("\n")
                        .append(contact.getAddress()).append("\n")
                        .append(contact.getEmail()).append("\n")
                        .append(((Company) contact).getCnpj()).append("\n")
                        .append(((Company) contact).getStateRegistration()).append("\n")
                        .append(((Company) contact).getBusinessName()).append("\n");
                System.out.println(stringBuilder);
            }
        }
    }

    public void sortContactsByInstanceOfAndDocumentNumber() {
        contacts.sort((contactA, contactB) -> {
            if (contactA instanceof Person && contactB instanceof Company) {
                return -1;
            } else if (contactA instanceof Company && contactB instanceof Person) {
                return 1;
            } else {
                String documentNumberA = contactA instanceof Person ? ((Person) contactA).getCpf() : ((Company) contactA).getCnpj();
                String documentNumberB = contactB instanceof Person ? ((Person) contactB).getCpf() : ((Company) contactB).getCnpj();
                return documentNumberA.compareTo(documentNumberB);
            }
        });
    }

    public void showMenu() {
        int option;

        do {
            System.out.println("""
                        Choose one of the following options:
                        1. Add a contact.
                        2. List all contacts.
                        3. Find contact by document number.
                        4. Delete a contact.
                        0. End the program.
                        """);

            option = InputScanner.scanner.nextInt();
            InputScanner.scanner.nextLine(); // consume leftover line break

            switch (option) {
                case 1:
                    createContact();
                    break;
                case 2:
                    sortContactsByInstanceOfAndDocumentNumber();
                    findAllContacts();
                    break;
                case 3, 4: {
                    System.out.println("Type the contact's CPF or CNPJ.");
                    String documentNumber = InputScanner.scanner.nextLine();

                    if (option == 3) {
                        findContactByDocumentNumber(documentNumber);
                    } else {
                        delete(documentNumber);
                    }
                    break;
                }
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please, try again.");
                    break;
            }
        } while (option != 0);
    }
}
