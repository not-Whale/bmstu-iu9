#1/bin/bash

stop-yarn.sh
stop-dfs.sh

rm -rf ~/hdata/datanode
rm -rf ~/hdata/namenode

mkdir ~/hdata/datanode
mkdir ~/hdata/namenode

hdfs namenome -format

start-dfs.sh
start-yarn.sh

hadoop jar ~/hadoop-2.10.1/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.10.1.jar pi 2 5

hdfs dfs -rmr hdfs://localhost:9000/user/lopata/output_lab_1

mvn package
hadoop fs -copyFromLocal src/resourses/warandpeace1.txt
export HADOOP_CLASSPATH=target/hadoop-examples-1.0-SNAPSHOT.jar
hadoop WordCountApp warandpeace1.txt output_lab_1
hadoop fs -copyToLocal output_lab_1
