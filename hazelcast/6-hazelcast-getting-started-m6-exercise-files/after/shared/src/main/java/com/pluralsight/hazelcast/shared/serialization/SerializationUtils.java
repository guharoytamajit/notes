package com.pluralsight.hazelcast.shared.serialization;

import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
public class SerializationUtils {

    public static void writeDate(PortableWriter portableWriter,
                                 Date date,
                                 String id) throws IOException {
        if (date != null) {
            Long value = date.getTime();
            portableWriter.writeLong(id, value);
        } else {
            portableWriter.writeLong(id, -1);
        }
    }

    public static Date readDate(PortableReader portableReader,
                                String id) throws IOException {
        if (portableReader.hasField(id)) {
            long value = portableReader.readLong(id);
            return value == -1L ? null : new Date(value);
        }
        return null;
    }
}
