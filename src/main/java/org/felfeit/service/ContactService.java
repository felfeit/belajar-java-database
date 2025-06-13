package org.felfeit.service;

import org.felfeit.model.Contact;
import org.felfeit.repository.ContactRepository;
import org.felfeit.repository.IContactRepository;

import java.util.List;

public class ContactService {
    private final IContactRepository repository;

    public ContactService(IContactRepository repository) {
        this.repository = repository;
    }

    public Contact addContact(Contact contact) {
        return repository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return repository.findAll();
    }

    public Contact getContactById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with id: " + id));
    }

    public Contact updateContact(int id, Contact contactDetails) {
        Contact existingContact = getContactById(id);
        existingContact.setName(contactDetails.getName());
        existingContact.setPhone(contactDetails.getPhone());
        existingContact.setEmail(contactDetails.getEmail());
        return repository.update(existingContact);
    }

    public void deleteContact(int id) {
        repository.delete(id);
    }
}
