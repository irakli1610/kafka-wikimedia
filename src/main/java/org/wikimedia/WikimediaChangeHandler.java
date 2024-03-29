package org.wikimedia;


import com.launchdarkly.eventsource.MessageEvent;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class WikimediaChangeHandler implements BackgroundEventHandler {

    private final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class.getSimpleName());
    KafkaProducer<String,String> kafkaProducer;
    String topic;
    public WikimediaChangeHandler(KafkaProducer<String,String> kafkaProducer, String topic){
        this.kafkaProducer=kafkaProducer;
        this.topic=topic;
    }


    @Override
    public void onClosed(){
        kafkaProducer.close();
    }

    @Override
    public void onMessage(String s, MessageEvent messageEvent) {

        log.info(messageEvent.getData());
        //asynchronous
        kafkaProducer.send(new ProducerRecord<>(topic, messageEvent.getData()));

    }
    @Override
    public void onError(Throwable throwable) {
        log.error("Error in Stream Reading", throwable);
    }
    @Override
    public void onComment(String s) {

    }
    @Override
    public void onOpen() throws Exception {

    }
}
