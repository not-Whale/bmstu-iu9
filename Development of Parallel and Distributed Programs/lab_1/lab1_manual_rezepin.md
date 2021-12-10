# Лабораторная работа 1.
## 1. Настраиваем подключение к компьютеру по ssh без пароля
Проверяем наличие ssh на компьютере :
``` 
$ sudo apt-get install ssh 
$ sudo apt-get install rsync 
$ sudo apt-get install openssh-server 
```

Настраиваем логин ssh без пароля :
```
$ ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa 
$ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
$ sudo /etc/init.d/ssh restart
$ ssh-add
```

Проверяем подключение по ssh без пароля :
```
$ ssh localhost
```

В случае если при подключении требуется пароль — надо выставить права на папки ssh :
```
$ chmod go-w $HOME $HOME/.ssh
$ chmod 600 $HOME/.ssh/authorized_keys
$ chown `whoami` $HOME/.ssh/authorized_keys 
```

## 2. Установка java и maven
Сохраняем локально файлы (можно взять на диске) jdk-8u261-linux-x64.tar.gz и apache-maven-3.6.3-bin.tar.gz и распаковываем их в корень home папки пользователя, прописываем в ~/.bashrc :
```
export JAVA_HOME=~/jdk1.8.0_261
export MAVEN_HOME=~/apache-maven-3.6.3
export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
```

Проверяем установку Java — открываем консоль и набираем :
```
$ java -version
```

Вывод должен иметь примерно следующий вид :
```
java version "1.8.0_261"
Java(TM) SE Runtime Environment (build 1.8.0_261-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.261-b12, mixed mode)
```

Проверяем установку Maven — открываем консоль и набираем :
```
$ mvn -v
```

Вывод должен иметь примерно следующий вид :
``` 
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: /home/lopata/apache-maven-3.6.3
Java version: 1.8.0_261, vendor: Oracle Corporation, runtime: /home/lopata/jdk1.8.0_261/jre
Default locale: ru_RU, platform encoding: UTF-8
OS name: "linux", version: "5.11.0-41-generic", arch: "amd64", family: "unix"
```

## 3. Установка hadoop
Копируем hadoop-2.10.1.tar.gz и распаковываем в корень папки home пользователя, прописываем в ~/.bashrc :
``` 
export HADOOP_INSTALL=~/hadoop-2.10.1
export HADOOP_PREFIX=$HADOOP_INSTALL
export PATH=$PATH:$HADOOP_INSTALL/bin
export PATH=$PATH:$HADOOP_INSTALL/sbin
export HADOOP_MAPRED_HOME=$HADOOP_INSTALL
export HADOOP_COMMON_HOME=$HADOOP_INSTALL
export HADOOP_HDFS_HOME=$HADOOP_INSTALL
export YARN_HOME=$HADOOP_INSTALL
export HADOOP_CONF_DIR=$HADOOP_INSTALL/etc/hadoop
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_INSTALL/lib/native
export HADOOP_OPTS="-Djava.library.path=$HADOOP_INSTALL/lib"
export PATH=$HADOOP_INSTALL/bin:$HADOOP_INSTALL/sbin:$PATH
```

Прописываем в первую строку файла ~/hadoop-2.10.1/etc/hadoop/hadoop-env.sh 
```
export JAVA_HOME=~/jdk1.8.0_261
```

## 4.Настройка hadoop для режима pseudo distributed
Вносим следующие настройки в конфигурационные файлы hadoop (если в конце файла есть приписка `.template`, то просто удаляем её, чтобы название файла было таким же, как в заголовке методических указаний) :

## 4.1 etc/hadoop/core-site.xml
```
<configuration> 
	<property> 
		<name>fs.default.name</name> 
		<value>hdfs://localhost:9000</value> 
	</property> 
</configuration>
```


## 4.2 etc/hadoop/yarn-site.xml
```
<configuration> 
     	<property> 
		<name>yarn.nodemanager.aux-services</name> 
		<value>mapreduce_shuffle</value> 
     	</property> 
     	<property> 
		<name>yarn.nodemanager.aux-services.mapreduce.shuffle.class</name> 
		<value>org.apache.hadoop.mapred.ShuffleHandler</value> 
     	</property> 
     	<property>
		<name>yarn.nodemanager.disk-health-checker.enable</name>
		<value>false</value>
     	</property> 
	<property>
		<name>yarn.nodemanager.pmem-check-enabled</name>
		<value>false</value>
	</property>
	<property>
		<name>yarn.nodemanager.vmem-check-enabled</name>
		<value>false</value>
	</property> 
</configuration>
```

## 4.3 etc/hadoop/mapred-site.xml
```
<configuration> 
	<property> 
		<name>mapreduce.framework.name</name> 
		<value>yarn</value> 
	</property> 
</configuration>
```

## 4.4 etc/hadoop/hdfs-site.xml
```
<configuration> 
	<property> 
		<name>dfs.replication</name> 
		<value>1</value> 
	</property> 
	<property> 
		<name>dfs.namenode.name.dir</name> 
		<value>file:/home/<user>/hdata/namenode</value> 
	</property> 
	<property> 
		<name>dfs.datanode.data.dir</name> 
		<value>file:/home/<user>/hdata/datanode</value> 
	</property> 
</configuration>
```
Тут <user> заменяем на имя пользователя операционной системы.
Далее создаем в локальной папке пользователя папку hdata и в ней две папки — namenode и datanode.

## 5.1 Запуск hadoop
Форматируем файловую систему hadoop :
```
$ hdfs namenode -format
```

Запускаем hadoop :
```
$ start-dfs.sh
$ start-yarn.sh
```

Проверяем работает ли hadoop запуском примера :
```
hadoop jar ~/hadoop-2.10.1/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.10.1.jar pi 2 5
```
Запуск примера является обязательным "ритуалом" перед сборкой и запуском своего проекта (как показала практика, у многих по-другому в принципе ничего не работает).

## 5.2 Восстановление hadoop в случае проблем
Останавливаем hadoop командами stop-yarn.sh и stop-dfs.sh, удаляем все файлы из папок `~/hdata/namenode` и `~/hdata/datanode`.

Форматируем namenode :
```
$ hdfs namenode -format
```

Если вы уже работали с файловой системой и загружали файлы, то очищаем соответствующую папку в хранилище, либо которая появилась там в результате работы программы (например, для первого случая - warandpeace1.txt, для второго случая - output) :
```
hdfs dfs -rmr hdfs://localhost:9000/user/lopata/<path>
```
Тут следует заменить <path> на путь к папке, которую необходимо удалить из хранилища.

Далее заново запускаем hadoop :
```
$ start-dfs.sh
$ start-yarn.sh
```

# 6. Каркас простейшего проекта maven для работы с hadoop
## 6.1 Создаем maven проект следующего вида
/pom.xml – файл проекта (копируем из приложений)

/src/main/java – исходный код java

/src/main/resources – файлы ресурсов

Тут важно положить файлы именно в папку Java без создания отдельного пакета и поддиректории (у меня была подобного рода ошибка, в случае желания собрать пакет нужно будет менять пути при вызове класса).

# 7. Задача составления частотного словаря
## 7.1 Класс приложения hadoop :
```
public class WordCountApp { 
    public static void main(String[] args) throws Exception { 
        if (args.length != 2) { 
            System.err.println("Usage: WordCountApp <input path> <output path>"); 
            System.exit(-1); 
        } 
        Job job = Job.getInstance(); 
        job.setJarByClass(WordCountApp.class); 
        job.setJobName("Word count"); 
        FileInputFormat.addInputPath(job, new Path(args[0])); 
        FileOutputFormat.setOutputPath(job, new Path(args[1])); 
        job.setMapperClass(WordMapper.class); 
        job.setReducerClass(WordReducer.class); 
        job.setOutputKeyClass(Text.class); 
        job.setOutputValueClass(IntWritable.class); 
        job.setNumReduceTasks(2); 
        System.exit(job.waitForCompletion(true) ? 0 : 1); 
    } 
} 
```

## 7.2 Класс mapper :
```
public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> { 
    @Override 
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException { 
	// <получаем строку из value, удаляем все спецсимволы, переводим в lowercase, 
        // разбиваем на слова и каждое слово пишем в контекст с счетчиком 1 в контекст пишется пара — Text и IntWritable >
    } 
}
```

## 7.3 Класс reducer :
```
public class WordReducer extends Reducer<Text, IntWritable, Text, LongWritable> { 
    @Override 
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException { 
       // <считаем количество записей в итераторе и генерируем запись в контекст 
       // В контекст пишется пара — Text и LongWritable>
    } 
}
```

# 8. Запуск приложения
Считается, что мы уже запустили hadoop (см. пункт 5.1), либо восстановили его после ошибки (см. пункт 5.2).

Собираем проект :
```
$ mvn package
```

Копируем в HDFS тестовый файл :
```
$ hadoop fs -copyFromLocal warandpeace1.txt
```

Запускаем map reduce :
```
$ export HADOOP_CLASSPATH=target/имя проекта.jar
$ hadoop ru.bmstu.hadoop.example.book.WordCountApp warandpeace1.txt output
```

Копируем результаты в локальную папку :
```
$  hadoop fs -copyToLocal output 
```

Все готово!

# 9. Завершение работы
После выполнения работы необходимо очистить хранилище от загруженных файлов, чтобы в следующий раз не вспоминать, с чем работали ранее, а также выключить hadoop (см. пункт 5.2). Также стоит отметить, что если мы хотим перезапустить одну и ту же лабораторную, то необходимо удалять папку с выводом, чтобы при выгрузке из хранилища не возникало попытки создать уже существующую папку (см. пункт 5.2).
