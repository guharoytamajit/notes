package com.pluralsight.hazelcast.storage.listeners;

import com.hazelcast.partition.PartitionLostEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@Service
public class AlertingService implements com.hazelcast.partition.PartitionLostListener {

    private static final Log LOG = LogFactory.getLog(AlertingService.class);

    @Override
    public void partitionLost(PartitionLostEvent event) {
        LOG.error(event);
    }
}
