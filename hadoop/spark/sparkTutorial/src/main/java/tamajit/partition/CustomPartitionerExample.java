package tamajit.partition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class CustomPartitionerExample {
    public static void main(String[] args) {
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Partitioning");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        JavaPairRDD<String, String> pairRdd = jsc.parallelizePairs(
                Arrays.asList(new Tuple2<String, String>("India", "Asia"),new Tuple2<String, String>("Italy", "Europe"),
                        new Tuple2<String, String>("Japan", "Asia"),new Tuple2<String, String>("France", "Europe"))
                ,3);

        System.out.println("Initial No. of partitions:"+pairRdd.getNumPartitions());
        JavaPairRDD<String, String> customPartitioned = pairRdd.partitionBy(new CustomPartitioner());

        System.out.println("No. of partitions after shuffle:"+customPartitioned.getNumPartitions());


        JavaRDD<String> mapPartitionsWithIndex = customPartitioned.mapPartitionsWithIndex((index, tupleIterator) -> {

            List<String> list=new ArrayList<>();

            while(tupleIterator.hasNext()){
                list.add("Partition number:"+index+",key:"+tupleIterator.next()._1());
            }

            return list.iterator();
        }, true);

        System.out.println(mapPartitionsWithIndex.collect());
    }
}