#!/bin/bash

echo "Shutdown zookeeper"

docker-compose -f common.yml -f zookeeper.yml down

sleep 5

echo "Shutdown kafka cluster"

docker-compose -f common.yml -f kafka_cluster.yml down

sleep 5

echo "Shutdown init kafka"

docker-compose -f common.yml -f init_kafka.yml down

sleep 5

echo "Deleting Kafka and Zookeeper volumes"

yes | rm -r ./volumes/kafka/*

yes | rm -r ./volumes/zookeeper/*

echo "Shutdown services"