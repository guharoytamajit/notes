package com.pluralsight.hazelcast.shared.serialization;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableFactory;
import com.pluralsight.hazelcast.shared.Customer;
import org.springframework.stereotype.Service;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@Service
public class PluralsightSerializationFactory implements PortableFactory {

    public static final int FACTORY_ID = 1;

    @Override
    public Portable create(int classId) {

        switch (classId) {
            case Customer.CLASS_ID:
                return new Customer();
        }
        return null;
    }
}
