package com.pluralsight.hazelcast.storage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@Repository
public interface EmailDao extends CrudRepository<EmailQueueEntry, Long> {


}
