package com.pluralsight.hazelcast.storage;

import com.pluralsight.hazelcast.shared.Email;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@Entity
public class EmailQueueEntry {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Email email;

    public EmailQueueEntry() {
    }

    public EmailQueueEntry(Long id, Email email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailQueueEntry that = (EmailQueueEntry) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(email != null ? !email.equals(that.email) : that.email != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EmailQueueEntry{");
        sb.append("id=").append(id);
        sb.append(", email=").append(email);
        sb.append('}');
        return sb.toString();
    }
}