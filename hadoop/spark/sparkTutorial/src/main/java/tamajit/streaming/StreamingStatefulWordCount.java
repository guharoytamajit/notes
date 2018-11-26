package tamajit.streaming;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.*;

/**
 * Created by ibm on 5/8/2018.
 */
public class StreamingStatefulWordCount {

    public static void main(String[] args) throws Exception {
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkConf sparkConf = new SparkConf().setAppName("WordCountSocketEx").setMaster("local[*]");
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

        JavaDStream<String> stringJavaDStream = streamingContext.textFileStream("C:\\tamajit\\sparkTutorial\\files");
        streamingContext.checkpoint("C:\\Users\\sgulati\\spark-checkpoint");

        JavaDStream<String> words = stringJavaDStream.flatMap(str -> Arrays.asList(str.split(" ")).iterator());
        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(str -> new Tuple2<>(str, 1)).reduceByKey((count1, count2) -> count1 + count2);

        Function3<String, Optional<Integer>, State<Map<String, Integer>>, Tuple2<String, Integer>> mappingFunc = (
                word, optionalCount, state) -> {
            Map<String, Integer> details = state.exists() ? state.get() : new HashMap<>();
            if(details.get(word)==null){
                details.put(word,optionalCount.get());
            }else
            {
                details.put(word, details.get(word)+optionalCount.get());
            }
            if(word.equals("#####"))state.remove();
            else
            state.update(details);
            return new Tuple2<String, Integer>(word, details.get(word));
        };

        JavaMapWithStateDStream<String, Integer, Map<String, Integer>, Tuple2<String, Integer>> ds = wordCounts.mapWithState(StateSpec.function(mappingFunc).timeout(Durations.minutes(5)));


        wordCounts.foreachRDD(rdd -> {
            List<Tuple2<String, Integer>> list = rdd.collect();
            if (!list.isEmpty()) System.out.println(list);
        });
        /*ds.foreachRDD(rdd->{
            rdd.collect().forEach((x)-> System.out.println(x));
        });*/

//        ds.foreachRDD(rdd->{
//            List<Tuple2<String, Integer>> list = rdd.collect();
//            if(!list.isEmpty()) System.out.println(list);
//        });
        streamingContext.start();
        System.out.println("Running...");
        streamingContext.awaitTermination();
    }
}
