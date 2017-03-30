package com.healfies.services.client;

import java.io.IOException;

import com.google.api.gax.core.ApiFuture;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.grpc.InstantiatingChannelProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.spi.v1.Publisher;
import com.google.cloud.pubsub.spi.v1.TopicAdminClient;
import com.google.cloud.pubsub.spi.v1.TopicAdminSettings;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.Topic;
import com.google.pubsub.v1.TopicName;

public class Publish {

    public TopicName createOrRetrieveTopic() {

        try {

            ServiceAccountCredentials credentials =
                ServiceAccountCredentials.fromStream(this.getClass().getResourceAsStream("/api-key.json"));

            InstantiatingChannelProvider channelProvider = TopicAdminSettings.defaultChannelProviderBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            TopicAdminSettings topicAdminSettings =
                TopicAdminSettings.defaultBuilder().setChannelProvider(channelProvider).build();

            TopicName topic = TopicName.create("api-project-299549388106", "test-topic");

            TopicAdminClient topicAdminClient = TopicAdminClient.create(topicAdminSettings);
            Topic t = topicAdminClient.getTopic(topic);
            if (t != null) {
                return t.getNameAsTopicName();
            } else {
                topicAdminClient.createTopic(topic);
                return topic;
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

    public void publish(TopicName topic, String msg) {
        Publisher publisher = null;
        try {
            publisher = Publisher.newBuilder(topic).build();
            ByteString data = ByteString.copyFromUtf8(msg);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (publisher != null) {
                try {
                    publisher.shutdown();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
