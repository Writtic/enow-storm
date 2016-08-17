writtic/enow-storm
=========================

Test project for enow-storm based on information provided in and referenced by:

- [https://github.com/nathanmarz/storm-contrib/blob/master/storm-kafka/src/jvm/storm/kafka/TestTopology.java](https://github.com/nathanmarz/storm-contrib/blob/master/storm-kafka/src/jvm/storm/kafka/TestTopology.java)
- [https://github.com/nathanmarz/storm/wiki/Trident-tutorial](https://github.com/nathanmarz/storm/wiki/Trident-tutorial)
- [https://github.com/nathanmarz/storm/wiki/Trident-state](https://github.com/nathanmarz/storm/wiki/Trident-state)
- [https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example](https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example)
- [https://github.com/wurstmeister/storm-kafka-0.8-plus-test](https://github.com/wurstmeister/storm-kafka-0.8-plus-test)
- [https://github.com/fhussonnois/docker-storm](https://github.com/fhussonnois/docker-storm)

Also contains an attempt at a sample implementation of trident state based on [Hazelcast](http://www.hazelcast.com/)


Environment setup with [Docker](https://www.docker.io/)
------------------------------
If you are using a Mac follow the instructions [here](https://docs.docker.com/installation/mac/) to setup a docker environment.

- Install [docker-compose](http://docs.docker.com/compose/install/)

- Install [storm](https://storm.incubator.apache.org/downloads.html) (so you can upload your topology to the test cluster)

- Pull the Docker image : ```docker pull enow/storm-dev```
- Start docker compose :
  - ```docker-compose -p storm up```</br>(pass the -d flag to run container in background)
  - If you need more information or check again, </br>Try this ```docker-compose -p storm ps```
- Start a kafka shell
  - ```start-kafka-shell.sh <Docker Ip> <Zookeeper>```
- From within the shell, create a topic
  - ```$KAFKA_HOME/bin/kafka-topics.sh --create --topic test --partitions 2 --zookeeper $ZK --replication-factor 1```

- For more details and troubleshooting see</br>   [https://github.com/Writtic/kafka-docker](https://github.com/Writtic/kafka-docker) </br>
and </br> [https://github.com/Writtic/docker-storm](https://github.com/Writtic/docker-storm)


Build for running locally:
-------------------------
- ```mvn clean package```

Build for running on a Storm cluster:
-------------------------------------
- ```mvn clean package -P cluster```

Running the test topologies locally
-----------------------------------
- ```java -cp target/enow-storm-1.0.jar com.enow.storm.trident.SentenceAggregationTopology <kafkaZookeeper>```

- ```java -cp target/enow-storm-1.0.jar com.enow.storm.KafkaSpoutTestTopology <kafkaZookeeper>```

- ```java -cp target/enow-storm-1.0.jar com.enow.storm.TestTopologyStaticHosts```

Running the test topologies on a storm cluster
----------------------------------------------
- ```storm jar target/enow-storm-1.0.jar com.enow.storm.trident.SentenceAggregationTopology <kafkaZookeeper> <topologyName> <dockerIp>```

- ```storm jar target/enow-storm-1.0.jar com.enow.storm.KafkaSpoutTestTopology <kafkaZookeeper> <topologyName> <dockerIp>```

You just edit ```<HOST_TOPOLOGY_TARGET_DIR>```, ```<TOPOLOGY_JAR>```, ```<TOPOLOGY_ARGS>``` sectors.

The Storm UI will be available under: ```http://<dockerIp>:8080/```

The Logviewer will be available under: ```http://<dockerIp>:8000/``` e.g. ```http://<dockerIp>:8000/log?file=supervisor.log```

Producing data
--------------
To feed the topologies with data, start the StormProducer (built in local mode)

- ```java -cp target/enow-storm-1.0.jar com.enow.storm.tools.StormProducer <dockerIp>:<kafkaPort>```

Alternatively use the kafka console producer from within the kafka shell (see above)

- ```$KAFKA_HOME/bin/kafka-console-producer.sh --topic=storm-sentence --broker-list=<dockerIp>:<kafkaPort>```

You can replace ```<dockerIp>:<kafkaPort>``` with ``` `broker-list.sh` ```, if you use lots of brokers

Consuming data
--------------
To run a DRPC query, start the DrpcClient (built in local mode)
or you make other bolt for cosuming data from kafka.

- ```java -cp target/enow-storm-1.0.jarfrom com.enow.storm.tools.DrpcClient <dockerIp> 3772```

Also alternatively use the kafka console consume from within the kafka shell (see above)

- ```$KAFKA_HOME/bin/kafka-console-consumer.sh --topic test --zookeeper $ZK --from-beginning```</br>(with ```--from-beginning``` you can see whole produced messages)

Troubleshooting
---------------
If for some reasons you need to debug a container you can use docker exec command:

Example : ```docker exec -it storm_nimbus_1 /bin/bash```
