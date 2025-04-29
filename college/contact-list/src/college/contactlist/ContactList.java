package college.contactlist;

import java.util.ArrayList;
import java.util.Scanner;

public class ContactList {

    private ArrayList<Contact> contacts = new ArrayList<>();

    private Scanner scanner = new Scanner(System.in);

    public String readInput(String message) {
        System.out.println(message);

        return scanner.next();
    }

    public void showMenu() {

    }

    public void save(Contact contact) {
        contacts.add(contact);
    }

    public void createContact() {

    }

    public void deleteContact(String documentNumber) {


    }

    public void getContactByDocumentNumber(String documentNumber) {

    }

    public void getAllContacts() {
        for (Contact contact : contacts) {
            System.out.println(contact);
        }
    }

    public void sortContactsByDocumentNumberAndInstanceOf() {

    }
}
