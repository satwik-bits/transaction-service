package com.transaction.management.kafka.producer;

import com.transaction.management.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@EnableKafka
@RequiredArgsConstructor
public class NotificationServiceProducer {

    private final KafkaTemplate<String, NotificationResponse> kafkaNotificationProducerTemplate;

    @Async
    public void send(String topic, String key, NotificationResponse message){
        try{
         kafkaNotificationProducerTemplate.send(topic, key, message).whenComplete(
                 ((result, exception)->{
                     if(exception==null){
                         System.out.println("Successfully sent message");
                     }
                     else{
                         System.out.println("Unable to send event message: "+message);
                     }
                 })
         );
        }
        catch(Exception e){
            System.out.println("Exception occured while sending message: "+e);
        }
    }

}
