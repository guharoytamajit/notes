package com.pluralsight.hazelcast.client.helper;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Grant Little grant@grantlittle.me
 */
@Service
public class StorageNodeFactory {

    private Config config;
    List<HazelcastInstance> instances = Collections.synchronizedList(new ArrayList<>());
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    public StorageNodeFactory(@Qualifier("StorageNodeConfig") Config config) {
        this.config = config;
    }

    public void ensureClusterSize(int size) throws InterruptedException {
        if (instances.size() < size) {
            int diff = size - instances.size();
            CountDownLatch latch = new CountDownLatch(diff);
            for (int x=0; x<diff; x++) {
                executorService.submit(new CreateHazelcastInstance(latch, config));
            }
            latch.await(10, TimeUnit.SECONDS);
        } else if (instances.size() > size) {
            for (int x=instances.size()-1; x>=size; x--) {
                HazelcastInstance instance = instances.remove(x);
                instance.shutdown();
            }
        }
    }

    public final class CreateHazelcastInstance implements Callable<HazelcastInstance> {

        private Config config;
        private CountDownLatch latch;

        public CreateHazelcastInstance(CountDownLatch latch, Config config) {
            this.config = config;
            this.latch = latch;
        }

        @Override
        public HazelcastInstance call() throws Exception {
            HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
            instances.add(instance);
            if (latch != null) {
                latch.countDown();
            }
            return instance;
        }
    }

}
