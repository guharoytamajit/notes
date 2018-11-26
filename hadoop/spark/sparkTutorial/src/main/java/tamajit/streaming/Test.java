package tamajit.streaming;

import io.netty.handler.codec.string.StringDecoder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Function2;
import scala.Tuple2;

import java.util.*;
import java.util.function.Function;

/**
 * Created by ibm on 5/10/2018.
 */
public class Test {
    public static void main(String[] args) throws  Exception {
        // Configure Spark to connect to Kafka running on local machine
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG,"group1");
        kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        kafkaParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);



        //Configure Spark to listen messages in topic test
        Collection<String> topics = Arrays.asList("topic1");

        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("SparkKafka10WordCount");

        //Read messages in batch of 30 seconds
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(30));

        // Start reading messages from Kafka and get DStream
        final JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(jssc, LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String,String>Subscribe(topics,kafkaParams));

        // Read value of each message from Kafka and return it
        JavaDStream<String> lines = stream.map((ConsumerRecord<String, String> kafkaRecord) ->{
                return kafkaRecord.value();
            }
        );

        // Break every message into words and return list of words
        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" ")).iterator();
            }
        });

        // Take every word and return Tuple with (word,1)
        JavaPairDStream<String,Integer> wordMap = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<>(word,1);
            }
        });

        // Count occurance of each word
        JavaPairDStream<String,Integer> wordCount = wordMap.reduceByKey((Integer first, Integer second)-> {
                return first+second;
        });

        //Print the word count
        wordCount.print();

        jssc.start();
        jssc.awaitTermination();
    }
}
