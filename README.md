Food ordering system application in Udemy course: Microservices: Clean Architecture, DDD, SAGA, Outbox & Kafka

Implement SAGA, Outbox and CQRS patterns using the 4 Spring boot Java microservices that you will develop using Clean and Hexagonal architecture principles and with DDD.

https://www.udemy.com/course/microservices-clean-architecture-ddd-saga-outbox-kafka-kubernetes/?referralCode=D9CF425EC696F08E501F

To run the project please follow the steps mentioned below:

1.) Clone the project in your local system

2.) Set $JAVA_HOME environment variable to point to JDK 17

3.) Make sure having maven installed and using JDK 17 by checking "mvn -v" command

4.) Enable annotation processing and install lombok plugin in your IDE in case of compile time errors regarding lombok annotations

5.) Run "mvn clean install" to make sure the project compiles and build successfully

6.) Install and run standalone postgres database on your local system that runs on 5432 port by default. 

7.) Run docker compose up commands for zookeeper, init_kafka and kafka_cluster yaml files unders infrastructure/docker-compose folder
    For latest code in master run only -> docker compose -f common.yml -f kafka_cluster.yml up -d

8.) Run the customer-service, order-service, payment-service and restaurant-service in your IDE using the spring boot main class for each module



