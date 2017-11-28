$(cat set_hadoop_cp.sh)
echo $HADOOP_CLASSPATH
echo "writing data to hdfs"
./place_data.sh
echo "running programs"
./compileProg4.sh
./runUserSim.sh
