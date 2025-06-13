package org.felfeit.view;

import org.felfeit.model.Contact;
import org.felfeit.service.ContactService;

import java.util.List;
import java.util.Scanner;

public class ContactView {
    private final Scanner scanner;
    private final ContactService contactService;

    public ContactView(ContactService contactService) {
        this.scanner = new Scanner(System.in);
        this.contactService = contactService;
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== Contact Management System ===");
            System.out.println("1. Add Contact");
            System.out.println("2. View All Contacts");
            System.out.println("3. View Contact by ID");
            System.out.println("4. Update Contact");
            System.out.println("5. Delete Contact");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = readIntInput();

            switch (choice) {
                case 1 -> addContact();
                case 2 -> viewAllContacts();
                case 3 -> viewContactById();
                case 4 -> updateContact();
                case 5 -> deleteContact();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addContact() {
        System.out.println("\n=== Add New Contact ===");

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        Contact newContact = new Contact(name, phone, email);
        Contact savedContact = contactService.addContact(newContact);

        System.out.println("\nContact added successfully:");
        System.out.println(savedContact);
    }

    private void viewAllContacts() {
        System.out.println("\n=== All Contacts ===");
        List<Contact> contacts = contactService.getAllContacts();

        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
        } else {
            contacts.forEach(contact -> {
                System.out.println("ID: " + contact.getId());
                System.out.println("Name: " + contact.getName());
                System.out.println("Phone: " + contact.getPhone());
                System.out.println("Email: " + contact.getEmail());
            });
        }
    }

    private void viewContactById() {
        System.out.println("\n=== View Contact by ID ===");
        System.out.print("Enter contact ID: ");
        int id = readIntInput();

        try {
            Contact contact = contactService.getContactById(id);
            System.out.println("\nContact details:");
            System.out.println(contact);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateContact() {
        System.out.println("\n=== Update Contact ===");
        System.out.print("Enter contact ID to update: ");
        int id = readIntInput();

        try {
            Contact existingContact = contactService.getContactById(id);
            System.out.println("\nCurrent contact details:");
            System.out.println(existingContact);

            System.out.println("\nEnter new details:");
            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Phone: ");
            String phone = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            Contact updatedContact = new Contact(name, phone, email);
            Contact result = contactService.updateContact(id, updatedContact);

            System.out.println("\nContact updated successfully:");
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteContact() {
        System.out.println("\n=== Delete Contact ===");
        System.out.print("Enter contact ID to delete: ");
        int id = readIntInput();

        try {
            Contact contact = contactService.getContactById(id);
            System.out.println("\nContact to be deleted:");
            System.out.println(contact);

            System.out.print("\nAre you sure you want to delete this contact? (yes/no): ");
            String confirmation = scanner.nextLine();

            if ("yes".equalsIgnoreCase(confirmation)) {
                contactService.deleteContact(id);
                System.out.println("Contact deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private int readIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return input;
    }

    private long readLongInput() {
        while (!scanner.hasNextLong()) {
            System.out.println("Please enter a valid number!");
            scanner.next();
        }
        long input = scanner.nextLong();
        scanner.nextLine(); // consume newline
        return input;
    }
}