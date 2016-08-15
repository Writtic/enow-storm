package com.enow.storm;

/**
 * Created by writtic on 2016. 8. 15..
 */

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.kafka.trident.GlobalPartitionInformation;

public class TestTopologyStaticHosts {


    public static class PrinterBolt extends BaseBasicBolt {

        public void declareOutputFields(OutputFieldsDeclarer declarer) {
        }

        public void execute(Tuple tuple, BasicOutputCollector collector) {
            System.out.println(tuple.toString());
        }

    }

    public static void main(String[] args) throws Exception {

        GlobalPartitionInformation hostsAndPartitions = new GlobalPartitionInformation("storm-sentence");
        hostsAndPartitions.addPartition(0, new Broker("localhost", 9092));
        BrokerHosts brokerHosts = new StaticHosts(hostsAndPartitions);

        SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, "storm-sentence", "", "storm");
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("words", new KafkaSpout(kafkaConfig), 10);
        builder.setBolt("print", new PrinterBolt()).shuffleGrouping("words");
        LocalCluster cluster = new LocalCluster();
        Config config = new Config();
        cluster.submitTopology("kafka-test", config, builder.createTopology());

        Thread.sleep(600000);

    }
}

