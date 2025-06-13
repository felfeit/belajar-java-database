package org.felfeit;

import org.felfeit.repository.ContactRepository;
import org.felfeit.repository.IContactRepository;
import org.felfeit.service.ContactService;
import org.felfeit.view.ContactView;

public class App {
    public static void main(String[] args) {
        IContactRepository contactRepository = new ContactRepository();
        ContactService contactService = new ContactService(contactRepository);
        ContactView contactView = new ContactView(contactService);

        contactView.displayMenu();
    }
}
