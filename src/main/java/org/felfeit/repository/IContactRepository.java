package org.felfeit.repository;

import org.felfeit.model.Contact;

import java.util.List;
import java.util.Optional;

public interface IContactRepository {
    Contact save(Contact contact);
    Optional<Contact> findById(int id);
    List<Contact> findAll();
    Contact update(Contact contact);
    void delete(int id);
}
