package tamajit;

import com.sparkTutorial.rdd.commons.Utils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Created by ibm on 4/28/2018.
 */
public class DatasetWordCount
{
    public static void main(String[] args) {
        /*SparkConf conf = new SparkConf().setAppName("wc").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile("in/word_count.text");
        JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String, Integer> pair = words.mapToPair(word -> new Tuple2<>(word, 1));
        pair.reduceByKey((x,y)->x+y).saveAsTextFile("out/test.text");*/


        SparkSession spark = SparkSession
                .builder()
                .master("local")
                .appName("WordCount")
                .getOrCreate();


        Dataset<String> df = spark.read().text(args[0]).as(Encoders.STRING());
        Dataset<String> words = df.flatMap((String s) -> {
            return  Arrays.asList(s.toLowerCase().split(" ")).iterator();
        }, Encoders.STRING())
                .filter((String s) -> !s.isEmpty())
                .coalesce(1); //one partition (parallelism level)
        words.printSchema();   // { value: string (nullable = true) }
        Dataset<Row> t = words.groupBy("value") //<k, iter(V)>
                .count()
                .toDF("word","count");
        t = t.sort(functions.desc("count"));
        t.toJavaRDD().saveAsTextFile("out/wc");
    }
}
