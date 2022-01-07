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

hdfs dfs -rmr hdfs://localhost:9000/user/lopata/output_lab_1

mvn package
hadoop fs -copyFromLocal /src/resourses/warandpeace1.txt
export HADOOP_CLASSPATH=target/hadoop-examples-1.0-SNAPSHOT.jar
hadoop WordCountApp warandpeace1.txt output_lab_1
hadoop fs -copyToLocal output_lab_1
