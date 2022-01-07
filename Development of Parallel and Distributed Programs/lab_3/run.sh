#!/bin/bash

stop-yarn.sh
stop-dfs.sh

rm -rf ~/hdata/datanode
rm -rf ~/hdata/namenode

mkdir ~/hdata/datanode
mkdir ~/hdata/namenode

hdfs namenode -format

start-dfs.sh
start-yarn.sh

hdfs dfs -rmr hdfs://localhost:9000/user/lopata/output_lab_2

hadoop jar ~/hadoop-2.10.1/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.10.1.jar pi 2 5

rm -rf output_lab_2

mvn package
hadoop fs -copyFromLocal src/main/resourses/airports.csv
hadoop fs -copyFromLocal src/main/resourses/flights.csv
export HADOOP_CLASSPATH=target/hadoop-examples-1.0-SNAPSHOT.jar
hadoop FlightsStatsApp flights.csv airports.csv output_lab_2
hadoop fs -copyToLocal output_lab_2
