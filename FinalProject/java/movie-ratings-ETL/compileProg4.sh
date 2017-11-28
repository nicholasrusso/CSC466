javac -cp org.json-20120521.jar:json-mapreduce-1.0.jar:hadoop-core-1.2.1.jar MovieSimilarity.java

rm UserSim.jar

jar cvf UserSim.jar *.class

rm *.class
