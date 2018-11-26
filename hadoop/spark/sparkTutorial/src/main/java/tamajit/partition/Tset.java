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
public class Tset {
    public static void main(String[] args) {
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkConf conf = new SparkConf().setAppName("reduce").setMaster("local[2]")
                .set("spark.default.parallelism", "4")
                .set("fs.local.block.size","");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<Integer> intRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(intRDD.getNumPartitions());


    }
}
