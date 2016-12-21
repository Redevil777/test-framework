package com.ihg.middleware.client

import groovy.util.logging.Log4j
import kafka.consumer.Consumer
import kafka.consumer.ConsumerConfig
import kafka.consumer.ConsumerIterator
import kafka.consumer.KafkaStream
import kafka.javaapi.consumer.ConsumerConnector
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

/**
 * Client class to collect messages from Kafka topic.
 * @author ilya.lapitan@ihg.com
 */
@Log4j
class KafkaClient {

    String zookeeperHosts
    String brokerHosts
    String kafkaTopic
    List<String> messages
    KafkaConsumer consumer
    String transactionId

    private KafkaClient( clientBuilder ){
        this.kafkaTopic = clientBuilder.kafkaTopic
        this.zookeeperHosts = clientBuilder.zookeeperHosts
        this.brokerHosts = clientBuilder.brokerHosts
    }

    void collectBy( transactionId ){
        messages  = new ArrayList<String>()
        this.transactionId = transactionId
        consumer = new KafkaConsumer( messages )
        consumer.start()
    }

    List<String> getCollected( ){
        consumer.shutdown()
        messages
    }

    void send( message ){
        log.debug("Send message to kafka: " + message)
        def props = new Properties()
        props.put("bootstrap.servers", brokerHosts)
        props.put("retries", 0)
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

        def producer = new KafkaProducer<>(props)
        producer.send( new ProducerRecord(kafkaTopic, message.toString()))
        producer.close()
    }

    public static class ClientBuilder {
        String kafkaTopic
        String zookeeperHosts
        String brokerHosts

        ClientBuilder(String kafkaTopic){
            this.kafkaTopic = kafkaTopic
        }

        ClientBuilder zookeeperHosts(zookeeperHosts){
            this.zookeeperHosts = zookeeperHosts
            this
        }

        ClientBuilder brokerHosts(brokerHosts){
            this.brokerHosts = brokerHosts
            this
        }

        KafkaClient build(){
            new KafkaClient(this)
        }
    }

    private class KafkaConsumer extends Thread {

        ConsumerConnector connector
        List<String> messages
        boolean process = true

        KafkaConsumer( messages ){
            this.messages = messages
            Properties props = new Properties()
            props.put( "zookeeper.connect" , zookeeperHosts )
            props.put( "group.id","testing-framework-group" )
            props.put( "auto.commit.enable" , "false" )
            connector = Consumer.createJavaConsumerConnector( new ConsumerConfig( props ) )
        }

        void shutdown( ){
            process = false
            connector.shutdown()
        }

        @Override
        void run() {
            Map<String, Integer> topicCountMap = new HashMap<>()
            topicCountMap.put( kafkaTopic , new Integer(1) )
            Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams( topicCountMap )
            KafkaStream<byte[], byte[]> stream =  consumerMap.get( kafkaTopic ).get( 0 );
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while( process && it.hasNext() ) {
                String message = new String( it.next().message() )
                if( message.contains( transactionId )){
                    messages.add( message );
                }
            }
        }
    }
}
