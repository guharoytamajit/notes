package tamajit;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibm on 5/3/2018.
 */
public class Test {
    public static void main(String[] args) throws IOException {

       List<Integer> list=new ArrayList<>();
       list.add(2);
        SparkConf conf = new SparkConf().setAppName("create").setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(conf);
        //Broadcast<List<Integer>> postCodeMap = sc.broadcast());
        JavaRDD<String> rdd = sc.textFile("in/test2");

        String collect = rdd.map(x -> {
            System.out.println("value of list:"+list);
            return x;
        }).map(x -> {
            System.out.println("value of list:"+list);
            return x;
        }).toDebugString();


        System.out.println(collect);
        System.out.println(list);
        sc.close();
        System.out.println("done");
    }
}
