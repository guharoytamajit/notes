package tamajit.streaming;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import scala.Tuple2;

public class KafkaExample  {

    public static void main(String[] args) {
        //Window Specific property if Hadoop is not instaalled or HADOOP_HOME is not set
//        System.setProperty("hadoop.home.dir", "E:\\hadoop");
        //Logger rootLogger = LogManager.getRootLogger();
        //rootLogger.setLevel(Level.WARN);
        SparkConf conf = new SparkConf().setAppName("KafkaExample").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sc, Durations.seconds(10));
        streamingContext.checkpoint("c:\\hadoop\\checkpoint");
        Logger rootLogger = LogManager.getRootLogger();
        rootLogger.setLevel(Level.WARN);
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_strea");
        kafkaParams.put("auto.offset.reset", "latest");
        // kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("topic1");

        final JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(streamingContext,LocationStrategies.PreferConsistent(),
                ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));

        JavaPairDStream<String, String> pairRDD = stream.mapToPair(record-> new Tuple2<>(record.key(), record.value()));

        pairRDD.foreachRDD(pRDD-> { pRDD.foreach(tuple-> System.out.println(new Date()+" :: Kafka msg key ::"+tuple._1() +" the val is ::"+tuple._2()));});

        streamingContext.start();
        System.out.println("Running...");
        try {
            streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}