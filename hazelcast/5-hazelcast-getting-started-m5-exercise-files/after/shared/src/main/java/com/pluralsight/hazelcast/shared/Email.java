package com.pluralsight.hazelcast.shared;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@Entity
public class Email implements Serializable {

    @Id
    private String uuid;
    private String toAddress;
    private String subject;
    private String body;

    public Email() {
    }

    public Email(String uuid) {
        this.uuid = uuid;
    }

    public Email(String uuid, String toAddress, String subject, String body) {
        this.uuid = uuid;
        this.toAddress = toAddress;
        this.subject = subject;
        this.body = body;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email email = (Email) o;

        if (uuid != null ? !uuid.equals(email.uuid) : email.uuid != null) return false;
        if (toAddress != null ? !toAddress.equals(email.toAddress) : email.toAddress != null) return false;
        if (subject != null ? !subject.equals(email.subject) : email.subject != null) return false;
        return !(body != null ? !body.equals(email.body) : email.body != null);

    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (toAddress != null ? toAddress.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Email{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append(", toAddress='").append(toAddress).append('\'');
        sb.append(", subject='").append(subject).append('\'');
        sb.append(", body='").append(body).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
