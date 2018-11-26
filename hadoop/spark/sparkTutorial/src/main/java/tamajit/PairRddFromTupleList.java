package tamajit;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class PairRddFromTupleList {

    public static void main(String[] args) throws Exception {

        SparkConf conf = new SparkConf().setAppName("create").setMaster("local[1]");

        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Tuple2<String, Integer>> tuple = Arrays.asList(new Tuple2<>("Jack", 23),
                                                            new Tuple2<>("Jack", 29),
                                                            new Tuple2<>("Mary", 29),
                                                            new Tuple2<>("Jack",8));

        JavaPairRDD<String, Integer> pairRDD = sc.parallelizePairs(tuple);

        pairRDD
        .saveAsTextFile("out/pair_rdd_from_tuple_list");
    }
}
