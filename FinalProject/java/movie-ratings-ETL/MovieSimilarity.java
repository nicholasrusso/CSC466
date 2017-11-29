/**
 * Created by charliegels on 3/5/17.
 */
// CSC 369 - Lab 8, Program 1
// Samantha Hsu (shsu03) and Charles Gels (cgels)


import com.alexholmes.json.mapreduce.MultiLineJsonInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class MovieSimilarity extends Configured implements Tool {

    public static class UserSimMapper
            extends Mapper<LongWritable, Text, Text, Text> {

        @Override

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            int numMovies = 9066;

            try {
                JSONObject json = new JSONObject(value.toString());
                JSONObject metadataObj;

                Iterator<String> iter = json.keys();
                Iterator<String> metaIter;

                String jsonKey; // stores current top-level json property key
                String rKey; // stores current 2-nd level json property kety (meta data object)

                String movieId = "";
                String userRatings = "";
                String pair;
                String movieTitle = "";
                String objId = "";

                while (iter.hasNext()) {
                    jsonKey = iter.next();

                    if (jsonKey.equals("meta")) {
                        metadataObj = new JSONObject(json.get(jsonKey).toString());
                        metaIter = metadataObj.keys();

                        while (metaIter.hasNext()) {
                            rKey = metaIter.next();
                            if (rKey.equals("title")) {
                                movieTitle = metadataObj.get(rKey).toString();
                            }
                        }
                    }

                    if (jsonKey.equals("ratings")) {
                        userRatings = json.get(jsonKey).toString();
                    }

                    if (jsonKey.equals("imbdMovieId"))
                    {
                        movieId = json.get(jsonKey).toString();
                    }

                    if (jsonKey.equals("objId"))
                    {
                        objId = json.get(jsonKey).toString();
                    }

                }

                if(movieTitle.equals("")) {
                    // for unknown titles... use the movie ID
                    movieTitle = movieId;
                }

                int i = Integer.valueOf(movieId);

                for (int j = 1; j < numMovies; j++)
                {
                    if ( i < j)
                    {
                        // index pair -- cannot use movie ids because not sequential!
                        pair = objId + ", " + String.valueOf(j);
                        context.write(new Text(pair), new Text(userRatings + ";" + movieId + ";" + movieTitle));
                    }
                    else if (i != j)
                    {
                        pair = String.valueOf(j) + ", " + objId;
                        context.write(new Text(pair), new Text(userRatings + ";" + movieId + ";" + movieTitle));
                    }
                }

//                context.write(new Text(rid), new Text(ratings + ";" + name));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static class UserSimReducer
            extends Reducer<Text, Text, Text, Text> {

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {

//            ArrayList<Text> cached = new ArrayList<>();
//            for (Text t : values)
//            {
//                cached.add(new Text(t));
//            }
//
//
//            ArrayList<String> ratings = new ArrayList<>();
//            for (Text v : cached)
//            {
//                ratings.add(v.toString());
//            }
//
//            String[] r_i = ratings.get(0).split(";");
//            String name_i = r_i[1];
//            r_i = r_i[0].substring(1, r_i[0].length()-1).split(",");
//
//            String[] r_j = ratings.get(1).split(";");
//            String name_j = r_j[1];
//            r_j = r_j[0].substring(1, r_j[0].length()-1).split(",");
//
//            double mu_i = 0;
//            double mu_j = 0;
//            for (int x = 0; x < r_i.length; x++)
//            {
//                mu_i += Double.valueOf(r_i[x]);
//                mu_j += Double.valueOf(r_j[x]);
//            }
//            mu_i /= r_i.length;
//            mu_j /= r_j.length;
//
//            double numerator = 0;
//            double denom_i = 0;
//            double denom_j = 0;
//            for (int x = 0; x < r_i.length; x++)
//            {
//                double dif_i = Double.valueOf(r_i[x]) - mu_i;
//                double dif_j = Double.valueOf(r_j[x]) - mu_j;
//
//                numerator += (dif_i) * (dif_j);
//                denom_i += (dif_i * dif_i);
//                denom_j += (dif_j * dif_j);
//            }
//
//            double sim = numerator / (Math.sqrt(denom_i) * Math.sqrt(denom_j));
//            context.write(new Text(name_i + ", " + name_j), new Text(String.valueOf(sim)));
            for (Text t: values) {
                context.write(new Text(key), new Text(t));

            }
//            context.write(key, new Text(String.valueOf(sim)));
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = super.getConf();
        Job job = Job.getInstance(conf, "user similarity job");

        job.setJarByClass(MovieSimilarity.class);
        job.setMapperClass(UserSimMapper.class);
        job.setReducerClass(UserSimReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(MultiLineJsonInputFormat.class);

        MultiLineJsonInputFormat.setInputJsonMember(job, "RID");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        int res = ToolRunner.run(conf, new MovieSimilarity(), args);
        System.exit(res);
    }
}

