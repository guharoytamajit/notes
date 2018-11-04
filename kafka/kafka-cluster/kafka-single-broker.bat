start "zookeeper" zookeeper-server-start config\zookeeper.properties
start "zookeeper shell" zookeeper-shell localhost:2181
timeout 3
start "broker 1(9091)" kafka-server-start config\server1.properties
pause
kafka-topics.bat --delete --if-exists --zookeeper localhost:2181 --topic testA & ^
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic testA & ^
kafka-topics.bat --list --zookeeper localhost:2181 & ^
timeout 2 & ^
start "kafka producer" kafka-console-producer.bat  --broker-list localhost:9091 --topic testA & ^
start "kafka consumer old" kafka-console-consumer.bat --zookeeper localhost:2181 --topic testA --from-beginning & ^
start "kafka consumer new" kafka-console-consumer.bat --bootstrap-server localhost:9091 --topic testA --from-beginning & ^

