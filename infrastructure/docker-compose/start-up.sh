#!/bin/bash

echo "Starting Zookeeper"

# start zookeeper
docker-compose -f common.yml -f zookeeper.yml up -d

# check zookeeper health
zookeeperCheckResult=$(echo ruok | nc localhost 2181)

while [[ ! $zookeeperCheckResult == "imok" ]]; do
  >&2 echo "Zookeeper is not running yet!"
  sleep 2
  zookeeperCheckResult=$(echo ruok | nc localhost 2181)
done

echo "Starting Kafka cluster"

# start kafka
docker-compose -f common.yml -f kafka_cluster.yml up -d

# check kafka health
kafkaCheckResult=$(kcat -L -b localhost:19092 | grep '3 brokers:')

while [[ ! $kafkaCheckResult == " 3 brokers:" ]]; do
  >&2 echo "Kafka cluster is not running yet!"
  sleep 2
  kafkaCheckResult=$(kcat -L -b localhost:19092 | grep '3 brokers:')
done

echo "Creating Kafka topics"

# start kafka init
docker-compose -f common.yml -f init_kafka.yml up -d

# check topics in kafka
kafkaTopicCheckResult=$(kcat -L -b localhost:19092 | grep 'debezium.restaurant.order_outbox')

while [[ $kafkaTopicCheckResult == "" ]]; do
  >&2 echo "Kafka topics are not created yet!"
  sleep 2
  kafkaTopicCheckResult=$(kcat -L -b localhost:19092 | grep 'debezium.restaurant.order_outbox')
done

# check debezium
servicesCheckResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://localhost:8083)

echo "Result status code:" "$curlResult"

while [[ ! $servicesCheckResult == "200" ]]; do
  >&2 echo "Debezium is not running yet!"
  sleep 2
  servicesCheckResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://localhost:8083)
done

echo "Creating debezium connectors"

curl --location --request POST 'localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "order-payment-connector",
  "config": {
      "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
      "tasks.max": "1",
      "database.hostname": "host.docker.internal",
      "database.port": "5432",
      "database.user": "postgres",
      "database.password": "admin",
      "database.dbname" : "postgres",
      "database.server.name": "PostgreSQL-15",
      "table.include.list": "order.payment_outbox",
      "topic.prefix": "debezium",
      "tombstones.on.delete" : "false",
      "slot.name" : "order_payment_outbox_slot",
      "plugin.name": "pgoutput",
      "auto.create.topics.enable": false,
      "auto.register.schemas": false
      }
 }'

curl --location --request POST 'localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "order-restaurant-connector",
  "config": {
      "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
      "tasks.max": "1",
      "database.hostname": "host.docker.internal",
      "database.port": "5432",
      "database.user": "postgres",
      "database.password": "admin",
      "database.dbname" : "postgres",
      "database.server.name": "PostgreSQL-15",
      "table.include.list": "order.restaurant_approval_outbox",
      "topic.prefix": "debezium",
      "tombstones.on.delete" : "false",
      "slot.name" : "order_restaurant_approval_outbox_slot",
      "plugin.name": "pgoutput",
      "auto.create.topics.enable": false,
      "auto.register.schemas": false
      }
 }'

curl --location --request POST 'localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "payment-order-connector",
  "config": {
      "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
      "tasks.max": "1",
      "database.hostname": "host.docker.internal",
      "database.port": "5432",
      "database.user": "postgres",
      "database.password": "admin",
      "database.dbname" : "postgres",
      "database.server.name": "PostgreSQL-15",
      "table.include.list": "payment.order_outbox",
      "topic.prefix": "debezium",
      "tombstones.on.delete" : "false",
      "slot.name" : "payment_order_outbox_slot",
      "plugin.name": "pgoutput",
      "auto.create.topics.enable": false,
      "auto.register.schemas": false
      }
 }'

curl --location --request POST 'localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "restaurant-order-connector",
  "config": {
      "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
      "tasks.max": "1",
      "database.hostname": "host.docker.internal",
      "database.port": "5432",
      "database.user": "postgres",
      "database.password": "admin",
      "database.dbname" : "postgres",
      "database.server.name": "PostgreSQL-15",
      "table.include.list": "restaurant.order_outbox",
      "topic.prefix": "debezium",
      "tombstones.on.delete" : "false",
      "slot.name" : "restaurant_order_outbox_slot",
      "plugin.name": "pgoutput",
      "auto.create.topics.enable": false,
      "auto.register.schemas": false
      }
 }'

echo "Start-up completed"