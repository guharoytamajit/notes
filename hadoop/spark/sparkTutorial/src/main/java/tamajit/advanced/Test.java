package tamajit.advanced;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by ibm on 5/7/2018.
 */
public class Test {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("reduce").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> rdd = sc.textFile("in/test");

        System.out.println(rdd.getNumPartitions());
        sc.close();


    }
}

