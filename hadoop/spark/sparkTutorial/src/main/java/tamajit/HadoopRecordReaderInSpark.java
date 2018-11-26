package tamajit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import scala.Tuple2;
import tamajit.recordreader.FilePathInputFormat;

import java.io.IOException;

import static org.apache.spark.sql.functions.col;

/**
 * Created by tamajit on 4/28/2018.
 */
public class HadoopRecordReaderInSpark {
    public static void main(String[] args) throws IOException {
        final String INPUT = "input/";
        final String OUTPUT = "output";
        SparkConf conf = new SparkConf().setAppName("wordCounts").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);


        JavaPairRDD<NullWritable, Text> rdd = sc.newAPIHadoopFile(INPUT, FilePathInputFormat.class, NullWritable.class, Text.class, new Configuration());
        rdd.map(x -> {
            ExtractUtil.process(INPUT + x._2.toString(), "zip", OUTPUT, false);
            return null;
        }).collect();

        System.in.read();

    }
}
