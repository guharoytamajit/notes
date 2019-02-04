package com.pluralsight.hazelcast.storage;

import com.pluralsight.hazelcast.shared.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@Service
public class EmailQueueStore implements com.hazelcast.core.QueueStore<Email> {

    private EmailDao emailDao;

    @Autowired
    public EmailQueueStore(EmailDao emailDao) {
        this.emailDao = emailDao;
    }

    @Override
    public void store(Long id, Email email) {
        System.out.println("Calling store with id " + id + " and email " + email);
        emailDao.save(new EmailQueueEntry(id, email));
    }

    @Override
    public void storeAll(Map<Long, Email> map) {

        List<EmailQueueEntry> emails = new ArrayList<>(map.size());

        for (Map.Entry<Long, Email> entry : map.entrySet() ) {
            EmailQueueEntry dbEntry = new EmailQueueEntry(entry.getKey(), entry.getValue());
            emails.add(dbEntry);
        }

        emailDao.save(emails);
    }

    @Override
    public void delete(Long key) {
        EmailQueueEntry emailQueueEntry = emailDao.findOne(key);
        System.out.println("Calling delete with index " + key + " and email " + emailQueueEntry.getEmail());
        emailDao.delete(emailQueueEntry);
    }

    @Override
    public void deleteAll(Collection<Long> collection) {
        emailDao.delete(emailDao.findAll(collection));
    }

    @Override
    public Email load(Long key) {
        System.out.println("Calling load with index " + key);
        return emailDao.findOne(key).getEmail();
    }

    @Override
    public Map<Long, Email> loadAll(Collection<Long> collection) {
        Iterable<EmailQueueEntry> emails = emailDao.findAll(collection);
        Map<Long, Email> map = new HashMap<>(collection.size());
        for (EmailQueueEntry entry : emails) {
            map.put(entry.getId(), entry.getEmail());
        }
        return map;
    }

    @Override
    public Set<Long> loadAllKeys() {
        Iterable<EmailQueueEntry> emails = emailDao.findAll();
        Set<Long> keys = new HashSet<>();
        for (EmailQueueEntry entry : emails) {
            keys.add(entry.getId());
        }
        return keys;
    }
}
