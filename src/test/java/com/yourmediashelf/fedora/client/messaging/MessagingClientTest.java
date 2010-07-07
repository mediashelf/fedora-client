/**
 * Copyright (C) 2010 MediaShelf <http://www.yourmediashelf.com/>
 *
 * This file is part of fedora-client.
 *
 * fedora-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fedora-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fedora-client.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.yourmediashelf.fedora.client.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

// TODO consider using
// https://cwiki.apache.org/confluence/display/ACTIVEMQ/Maven2+ActiveMQ+Broker+Plugin
// for actually spinning up ActiveMQ for tests
/**
 * Tests the messaging client
 *
 * @author Bill Branan
 */
public class MessagingClientTest implements MessagingListener {

    private static final String TOPIC_NAME = "messageTopic";
    private static final String TOPIC = "org.fcrepo.test.topic";
    private static final String QUEUE_NAME = "messageQueue";
    private static final String QUEUE = "org.fcrepo.test.queue";

    private int messageCount = 0;
    private final int messageTimeout = 5000; // Maximum number of milliseconds to wait for a message
    private Message currentMessage = null;
    private String currentClientId = null;
    private Properties properties = null;

    private final String messageText =
            "This is a message sent as part of a junit test";
    private final String propertyName = "testProperty";
    private final String propertyValue = "testProperty value";

    @Before
    public void setUp() throws Exception {
        properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                               "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty(Context.PROVIDER_URL,
                               "vm://localhost?broker.useShutdownHook=false&broker.persistent=true&broker.useJmx=false");
        properties.setProperty(JMSManager.CONNECTION_FACTORY_NAME,
                               "ConnectionFactory");
        properties.setProperty("topic." + TOPIC_NAME, TOPIC);
        properties.setProperty("queue." + QUEUE_NAME, QUEUE);
    }

    @Test
    public void testMessagingClientTopic() throws Exception {

        String clientId = "0";
        MessagingClient messagingClient =
                new MessagingClient(clientId, this, properties, false);

        messagingClient.start();
        sendMessage(TOPIC_NAME);
        checkMessage(clientId, TOPIC);
        messagingClient.stop(true);
    }

    @Test
    public void testMessagingClientDurableTopic() throws Exception {

        String clientId = "1";
        MessagingClient messagingClient =
                new MessagingClient(clientId, this, properties, true);

        // Establish that the client can start and receive messages
        messagingClient.start();
        sendMessage(TOPIC_NAME);
        checkMessage(clientId, TOPIC);
        messagingClient.stop(false);

        // Check to see if messages are received in a durable fashion
        sendMessage(TOPIC_NAME);
        messagingClient.start();
        checkMessage(clientId, TOPIC);
        messagingClient.stop(true);

        // Make sure durable subscriptions were closed
        sendMessage(TOPIC_NAME);
        messagingClient.start();
        checkNoMessages();
        messagingClient.stop(true);
    }

    @Test
    public void testMessagingClientMultipleTopics() throws Exception {

        String clientId = "2";
        String topicName = "additionalTopic";
        String topic = "org.fcrepo.test.additional";
        properties.setProperty("topic." + topicName, topic);
        MessagingClient messagingClient =
                new MessagingClient(clientId, this, properties, true);

        messagingClient.start();
        sendMessage(TOPIC_NAME);
        checkMessage(clientId, TOPIC);
        sendMessage(topicName);
        checkMessage(clientId, topic);
        messagingClient.stop(true);
    }

    @Test
    public void testMessagingClientQueue() throws Exception {

        String clientId = "3";
        MessagingClient messagingClient =
                new MessagingClient(clientId, this, properties, false);

        messagingClient.start();
        sendMessage(QUEUE_NAME);
        checkMessage(clientId, QUEUE);
        messagingClient.stop(true);
    }

    @Test
    public void testInvalidProperties() throws Exception {
        // Null properties
        try {
            new MessagingClient("4", this, null, false);
            fail("Creating a Messagingient with null properties " +
                 "should throw an exception");
        } catch(MessagingException me) {
            assertTrue(me.getMessage().contains("Connection properties may not be null"));
        }

        // Missing all properties
        properties = new Properties();

        try {
            new MessagingClient("5", this, properties, false);
            fail("Creating a Messaging Client with no properties " +
                 "should throw an exception");
        } catch(MessagingException me) {
            assertTrue(me.getMessage().contains("Propery values"));
            assertTrue(me.getMessage().contains("must be provided"));
        }

        // Missing connection factory property
        properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                               "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty(Context.PROVIDER_URL,
                               "vm://localhost?broker.useShutdownHook=false&broker.persistent=false");

        try {
            new MessagingClient("6", this, properties, false);
            fail("Creating a Messaging Client with no connection factory " +
                 "property should throw an exception");
        } catch(MessagingException me) {
            assertTrue(me.getMessage().contains("Propery values"));
            assertTrue(me.getMessage().contains("must be provided"));
        }

        // Missing provider url property
        properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                               "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty(JMSManager.CONNECTION_FACTORY_NAME,
                               "ConnectionFactory");

        try {
            new MessagingClient("7", this, properties, false);
            fail("Creating a Messaging Client with no provider url " +
                 "property should throw an exception");
        } catch(MessagingException me) {
            assertTrue(me.getMessage().contains("Propery values"));
            assertTrue(me.getMessage().contains("must be provided"));
        }

        // Missing initial context factory property
        properties = new Properties();
        properties.setProperty(Context.PROVIDER_URL,
                               "vm://localhost?broker.useShutdownHook=false&broker.persistent=false");
        properties.setProperty(JMSManager.CONNECTION_FACTORY_NAME,
                               "ConnectionFactory");

        try {
            new MessagingClient("8", this, properties, false);
            fail("Creating a Messaging Client with no initial context factory " +
                 "property should throw an exception");
        } catch(MessagingException me) {
            assertTrue(me.getMessage().contains("Propery values"));
            assertTrue(me.getMessage().contains("must be provided"));
        }
    }

    @Test
    public void testMessageSelectors() throws Exception {

        String clientId = "9";
        // Selector to include test message
        String messageSelector = propertyName + " LIKE 'test%'";
        MessagingClient messagingClient =
                new MessagingClient(clientId, this, properties,
                                       messageSelector, false);
        messagingClient.start();
        sendMessage(TOPIC_NAME);
        checkMessage(clientId, TOPIC);
        messagingClient.stop(true);
    }

    @Test
    public void testAsynchronousStart() throws Exception {
        String clientId = "11";
        MessagingClient messagingClient =
                new MessagingClient(clientId, this, properties, false);

        messagingClient.start(false);
        long startTime = System.currentTimeMillis();
        long maxWaitTime = 60000;
        while(!messagingClient.isConnected()) {
            // Don't wait forever
            if(System.currentTimeMillis() - startTime > maxWaitTime) {
                fail("Messaging client did not connect in " +
                     maxWaitTime/1000 + " seconds.");
            }
            Thread.sleep(100);
        }
        sendMessage(TOPIC_NAME);
        checkMessage(clientId, TOPIC);
        messagingClient.stop(true);
    }

    private void sendMessage(String jndiName) throws Exception {
        JMSManager jmsManager = new JMSManager(properties);
        TextMessage message = jmsManager.createTextMessage(jndiName, messageText);
        message.setStringProperty(propertyName, propertyValue);
        jmsManager.send(jndiName, message);
        jmsManager.stop(jndiName);
        jmsManager.close();
    }

    /**
     * Waits for a message and checks to see if it is valid.
     */
    private void checkMessage(String clientId, String destination) throws JMSException {
        long startTime = System.currentTimeMillis();

        while (true) { // Wait for the message
            if (messageCount > 0) {
                assertNotNull(currentMessage);
                if(currentMessage instanceof TextMessage) {
                    assertEquals(messageText, ((TextMessage)currentMessage).getText());
                } else {
                    fail("Text Message expected.");
                }

                assertEquals(clientId, currentClientId);

                Destination messageDestination = currentMessage.getJMSDestination();
                if (messageDestination instanceof Topic) {
                    String topic = ((Topic) messageDestination).getTopicName();
                    assertEquals(topic, destination);
                } else if (messageDestination instanceof Queue) {
                    String queue = ((Queue) messageDestination).getQueueName();
                    assertEquals(queue, destination);
                }

                String propertyTest =
                    currentMessage.getStringProperty(propertyName);
                assertEquals(propertyValue, propertyTest);
                break;
            } else { // Check for timeout
                long currentTime = System.currentTimeMillis();
                if (currentTime > (startTime + messageTimeout)) {
                    fail("Timeout reached waiting for message.");
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        messageCount = 0;
        currentMessage = null;
        currentClientId = null;
    }

    /**
     * Waits for a message to make sure none come through.
     */
    private void checkNoMessages() {
        long startTime = System.currentTimeMillis();

        while (true) { // Wait for the message
            if (messageCount > 0) {
                fail("No messagess should be received during this test.");
                break;
            } else { // Check for timeout
                long currentTime = System.currentTimeMillis();
                if (currentTime > (startTime + messageTimeout)) {
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        messageCount = 0;
        currentMessage = null;
        currentClientId = null;
    }

    public void onMessage(String clientId, Message message) {
        messageCount++;
        currentMessage = message;
        currentClientId = clientId;
    }
}
