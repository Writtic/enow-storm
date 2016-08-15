writtic/enow-storm
=========================

Test project for enow-storm based on information provided in:

- [https://github.com/nathanmarz/storm-contrib/blob/master/storm-kafka/src/jvm/storm/kafka/TestTopology.java] (https://github.com/nathanmarz/storm-contrib/blob/master/storm-kafka/src/jvm/storm/kafka/TestTopology.java)
- [https://github.com/nathanmarz/storm/wiki/Trident-tutorial] (https://github.com/nathanmarz/storm/wiki/Trident-tutorial)
- [https://github.com/nathanmarz/storm/wiki/Trident-state] (https://github.com/nathanmarz/storm/wiki/Trident-state)
- [https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example] (https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example)
-[https://github.com/wurstmeister/storm-kafka-0.8-plus-test](https://github.com/wurstmeister/storm-kafka-0.8-plus-test)

Also contains an attempt at a sample implementation of trident state based on [Hazelcast](http://www.hazelcast.com/)


## Environment setup with [Docker](https://www.docker.io/)

If you are using a Mac follow the instructions [here](https://docs.docker.com/installation/mac/) to setup a docker environment.

- Install [docker-compose](http://docs.docker.com/compose/install/)

- Install [storm](https://storm.incubator.apache.org/downloads.html) (so you can upload your topology to the test cluster)

- Start the test environment
    - ```docker-compose up```
- Start a kafka shell
    - ```start-kafka-shell.sh <Docker Ip> <Zookeeper>```
- From within the shell, create a topic
    - ```$KAFKA_HOME/bin/kafka-topics.sh --create --topic storm-sentence --partitions 2 --zookeeper $ZK --replication-factor 1```
-

- For more details and troubleshooting see [https://github.com/enow/kafka-docker](https://github.com/enow/kafka-docker) </br>
and </br> [https://github.com/enow/storm-docker](https://github.com/enow/storm-docker)


## Build for running locally:

- ```mvn clean package```

## Build for running on a Storm cluster:

- ```mvn clean package -P cluster```

## Run a docker container
Then run it. You can then use docker port to find out what host port the container’s port 22 is mapped to:

$ docker run -d -P --name test_sshd eg_sshd
$ docker port test_sshd 22
0.0.0.0:49154
And now you can ssh as root on the container’s IP address (you can find it with docker inspect) or on port 49154 of the Docker daemon’s host IP address (ip address or ifconfig can tell you that) or localhost if on the Docker daemon host:

$ ssh root@192.168.1.2 -p 49154
# The password is ``screencast``.
$$

## Running the test topologies locally

- ```java -cp target/storm-kafka-0.8-plus-test-0.2.0-SNAPSHOT-jar-with-dependencies.jar storm.kafka.trident.SentenceAggregationTopology <kafkaZookeeper>```
- ```java -cp target/storm-kafka-0.8-plus-test-0.2.0-SNAPSHOT-jar-with-dependencies.jar storm.kafka.KafkaSpoutTestTopology <kafkaZookeeper>```
- ```java -cp target/storm-kafka-0.8-plus-test-0.2.0-SNAPSHOT-jar-with-dependencies.jar storm.kafka.TestTopologyStaticHosts```

## Running the test topologies on a storm cluster


- ```storm jar target/storm-kafka-0.8-plus-test-0.2.0-SNAPSHOT-jar-with-dependencies.jar storm.kafka.trident.SentenceAggregationTopology <kafkaZookeeper> sentences <dockerIp>```
- ```storm jar target/storm-kafka-0.8-plus-test-0.2.0-SNAPSHOT-jar-with-dependencies.jar storm.kafka.KafkaSpoutTestTopology <kafkaZookeeper> sentences <dockerIp>```

The Storm UI will be available under: ```http://<dockerIp>:8080/```

The Logviewer will be available under: ```http://<dockerIp>:8000/``` e.g. ```http://<dockerIp>:8000/log?file=supervisor.log```

## Producing data

To feed the topologies with data, start the StormProducer (built in local mode)

- ```java -cp target/storm-kafka-0.8-plus-test-0.2.0-SNAPSHOT-jar-with-dependencies.jar storm.kafka.tools.StormProducer <dockerIp>:<kafkaPort>```

Alternatively use the kafka console producer from within the kafka shell (see above)

- ```$KAFKA_HOME/bin/kafka-console-producer.sh --topic=storm-sentence --broker-list=<dockerIp>:<kafkaPort>```

## Consuming data

To run a DRPC query, start the DrpcClient (built in local mode)

- ```java -cp target/storm-kafka-0.8-plus-test-0.2.0-SNAPSHOT-jar-with-dependencies.jar storm.kafka.tools.DrpcClient <dockerIp> 3772```
