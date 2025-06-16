package org.felfeit;

import org.felfeit.model.Contact;
import org.felfeit.repository.ContactRepository;
import org.felfeit.repository.IContactRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class RepositoryTest {

    private IContactRepository contactRepository;

    @BeforeEach
    void setUp() {
        contactRepository = new ContactRepository();
    }

    @Test
    void testSaveAndFindById() {
        Contact contact = new Contact("arya", "0912837465", "arya@test.com");
        Contact saved = contactRepository.save(contact);
        Optional<Contact> found = contactRepository.findById(saved.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("arya", found.get().getName());
    }

    @Test
    void testFindAll() {
        contactRepository.save(new Contact("one", "1", "one@test.com"));
        contactRepository.save(new Contact("two", "2", "two@test.com"));

        List<Contact> contacts = contactRepository.findAll();
        Assertions.assertTrue(contacts.size() >= 2);
    }

    @Test
    void testUpdate() {
        Contact contact = contactRepository.save(new Contact("Old", "111", "old@test.com"));
        contact.setName("New");
        Contact updated = contactRepository.update(contact);

        Assertions.assertEquals("New", updated.getName());
    }

    @Test
    void testDelete() {
        Contact contact = contactRepository.save(new Contact("Delete", "999", "Delete@test.com"));
        int id = contact.getId();
        contactRepository.delete(id);

        Optional<Contact> found = contactRepository.findById(id);
        Assertions.assertFalse(found.isPresent());
    }
}
