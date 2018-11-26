package tamajit.partition;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

/**
 * Created by ibm on 5/10/2018.
 */
public class Example2 {
    public static void main(String[] args) {
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkConf conf = new SparkConf().setAppName("reduce").setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> intRDD = sc.textFile("in/test",5);
        System.out.println(intRDD.getNumPartitions());

        JavaRDD<String> repartition = intRDD.repartition(3);
        System.out.println(repartition.getNumPartitions());
    }
}
