hadoop fs -rm -r test/lab8/userSim
hadoop jar MovieSim.jar MovieSimilarity -libjars org.json-20120521.jar,json-mapreduce-1.0.jar MovieSimilarity-input.json test/lab8/userSim
