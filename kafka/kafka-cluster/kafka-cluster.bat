start "zookeeper" zookeeper-server-start config\zookeeper.properties
start "zookeeper shell" zookeeper-shell localhost:2181
timeout 3
start "broker 1(9091)" kafka-server-start config\server1.properties
start "broker 2(9092)" kafka-server-start config\server2.properties
start "broker 3(9093)" kafka-server-start config\server3.properties
pause
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic testB & ^
timeout 2 & ^
start "kafka producer" kafka-console-producer.bat  --broker-list localhost:9091,localhost:9092 --topic testB & ^
start "kafka consumer old" kafka-console-consumer.bat --zookeeper localhost:2181 --topic testB --from-beginning & ^
start "kafka consumer new" kafka-console-consumer.bat --bootstrap-server localhost:9091,localhost:9092 --topic testB --from-beginning & ^

