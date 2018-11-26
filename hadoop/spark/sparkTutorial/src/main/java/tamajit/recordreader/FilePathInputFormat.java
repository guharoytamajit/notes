package tamajit.recordreader;


import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class FilePathInputFormat extends FileInputFormat<NullWritable,Text> {

    @Override
    public RecordReader<NullWritable, Text> createRecordReader(InputSplit arg0, TaskAttemptContext arg1)
            throws IOException, InterruptedException {
        return new FilePathRecordReader();
    }



}