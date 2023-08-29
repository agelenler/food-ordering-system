package com.food.ordering.system.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;

public interface KafkaSingleItemConsumer<T extends SpecificRecordBase> {
    void receive(T message, String key, Integer partition, Long offset);
}
