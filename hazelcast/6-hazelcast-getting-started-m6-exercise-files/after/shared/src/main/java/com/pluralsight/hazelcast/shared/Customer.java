package com.pluralsight.hazelcast.shared;

import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.pluralsight.hazelcast.shared.serialization.PluralsightSerializationFactory;
import com.pluralsight.hazelcast.shared.serialization.SerializationUtils;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;

/**
 * Grant Little grant@grantlittle.me
 */
@Entity
public class Customer implements com.hazelcast.nio.serialization.VersionedPortable {


    private Long id;


    private String title;


    private String name;
    private Date dob;
    private String email;

    public static final int CLASS_ID=1;

    public static final int VERSION_ID=2;


    public static final String TITLE_FIELD = "title";


    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String DOB_FIELD = "dob";
    public static final String EMAIL_FIELD = "email";
















    @Transient
    @Override
    public int getFactoryId() {
        return PluralsightSerializationFactory.FACTORY_ID;
    }

    @Transient
    @Override
    public int getClassId() {
        return CLASS_ID;
    }

    @Transient
    @Override
    public int getClassVersion() {
        return VERSION_ID;
    }










    @Override
    public void writePortable(PortableWriter writer) throws IOException {
        writer.writeLong(ID_FIELD, id);
        writer.writeUTF(NAME_FIELD, name);
        writer.writeUTF(EMAIL_FIELD, email);
        SerializationUtils.writeDate(writer, dob, DOB_FIELD);
        writer.writeUTF(TITLE_FIELD, title);
    }

    @Override
    public void readPortable(PortableReader reader) throws IOException {
        if (reader.hasField(ID_FIELD)) {
            id = reader.readLong(ID_FIELD);
        }
        if (reader.hasField(NAME_FIELD)) {
            name = reader.readUTF(NAME_FIELD);
        }
        if (reader.hasField(EMAIL_FIELD)) {
            email = reader.readUTF(EMAIL_FIELD);
        }
        dob = SerializationUtils.readDate(reader, DOB_FIELD);
        if (reader.hasField(TITLE_FIELD)) {
            title = reader.readUTF(TITLE_FIELD);
        }
    }









    public Customer() {
    }

    public Customer(Long id) {
        this.id = id;
    }

    public Customer(Long id, String name, Date dob, String email) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
    }

    public Customer(Long id, String title, String name, Date dob, String email) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.dob = dob;
        this.email = email;
    }












    @Id
    public Long getId() {
        return id;
    }



    public String getName() {
        return name;
    }

    @Temporal(TemporalType.DATE)
    public Date getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
        if (name != null ? !name.equals(customer.name) : customer.name != null) return false;
        if (dob != null ? !dob.equals(customer.dob) : customer.dob != null) return false;
        return !(email != null ? !email.equals(customer.email) : customer.email != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }


















    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Customer{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", dob=").append(dob);
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }











}
