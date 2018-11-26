package tamajit.streaming;


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

public class FileStreamingWindow {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("KafkaExample").setMaster("local[*]");
        String inputDirectory="C:\\tamajit\\sparkTutorial\\files";

        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sc, Durations.seconds(10));
        Logger rootLogger = LogManager.getRootLogger();
        rootLogger.setLevel(Level.WARN);
        JavaDStream<String> streamfile = streamingContext.textFileStream(inputDirectory);
       JavaDStream<String> words = streamfile.flatMap( str -> Arrays.asList(str.split(" ")).iterator() );
        JavaPairDStream<String, Integer> pairs = words.mapToPair(str -> new Tuple2<>(str, 1));
        JavaPairDStream<String, Integer> wordCounts =pairs.reduceByKey((count1, count2) ->count1+count2 );
      //  wordCounts.print();
       wordCounts.foreachRDD(rdd-> rdd.foreach(x -> System.out.println(x._1()+"="+x._2())));


        streamingContext.start();
        System.out.println("Running...");

        try {
            streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}