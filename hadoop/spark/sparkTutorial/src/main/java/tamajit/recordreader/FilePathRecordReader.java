package tamajit.recordreader;
import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class FilePathRecordReader extends RecordReader<NullWritable, Text> {

    boolean done = false;
    Text value;
    private InputSplit inputSplit;

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public void initialize(InputSplit split, TaskAttemptContext arg1) throws IOException, InterruptedException {
        inputSplit = split;

    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if(done==false) {
            String filepath = ((FileSplit)inputSplit).getPath().getName();
            value=new Text(filepath);
            done=true;
            return true;
        }else {
            value =null;
        }

        return false;
    }

}