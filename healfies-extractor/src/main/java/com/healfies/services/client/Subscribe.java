package com.healfies.services.client;

import java.io.IOException;

import com.google.cloud.pubsub.spi.v1.AckReply;
import com.google.cloud.pubsub.spi.v1.AckReplyConsumer;
import com.google.cloud.pubsub.spi.v1.MessageReceiver;
import com.google.cloud.pubsub.spi.v1.Subscriber;
import com.google.cloud.pubsub.spi.v1.SubscriptionAdminClient;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Subscription;
import com.google.pubsub.v1.SubscriptionName;
import com.google.pubsub.v1.TopicName;

public class Subscribe {

    private SubscriptionName createOrRetrieveSubscription(TopicName topic, String subscriberID) {
        SubscriptionName subscription = SubscriptionName.create("api-project-299549388106", subscriberID);

        try {

            SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create();

            Subscription s = subscriptionAdminClient.getSubscription(subscription);
            if (s != null) {
                return s.getNameAsSubscriptionName();
            } else {

                subscriptionAdminClient.createSubscription(subscription, topic,
                    PushConfig.getDefaultInstance(), 0);

                return subscription;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void pull(TopicName topic, String subscriberID) {
        MessageReceiver receiver = new MessageReceiver() {

            public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
                System.out.println("got message: " + message.getData().toStringUtf8());
                consumer.accept(AckReply.ACK);
            }
        };
        Subscriber subscriber = null;
        try {
            subscriber =
                Subscriber.newBuilder(createOrRetrieveSubscription(topic, subscriberID), receiver).build();
            subscriber.addListener(new Subscriber.SubscriberListener() {
                @Override
                public void failed(Subscriber.State from, Throwable failure) {
                    // Handle failure. This is called when the Subscriber encountered a fatal error and is
                    // shutting down.
                    System.err.println(failure);
                }
            }, MoreExecutors.directExecutor());
            subscriber.startAsync().awaitRunning();

            // Pull messages for 60 seconds.
            Thread.sleep(60000);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (subscriber != null) {
                subscriber.stopAsync();
            }
        }
    }

}
