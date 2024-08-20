#!/bin/sh

## Docker workaround: Remove check for KAFKA_ZOOKEEPER_CONNECT parameter
#sed -i 's/dub ensure KAFKA_ZOOKEEPER_CONNECT.*/:/g' /etc/confluent/docker/configure
#
## Docker workaround: Remove check for KAFKA_ADVERTISED_LISTENERS parameter
#sed -i 's/dub ensure KAFKA_ADVERTISED_LISTENERS.*/:/g' /etc/confluent/docker/configure
#
## Docker workaround: Ignore cub zk-ready
#sed -i 's/cub zk-ready/echo ignore zk-ready/' /etc/confluent/docker/ensure

cluster_id_file_path="/tmp/cluster-id-dir/clusterId"
interval=3  # wait interval in seconds

#[-e FILE]	True if FILE exists.
#[-s FILE]	True if FILE exists and has a size greater than zero.

while [ ! -e "$cluster_id_file_path" ] || [ ! -s "$cluster_id_file_path" ]; do
  echo "Waiting for cluster id to be created!"
  sleep $interval
done

echo "Cluster id: $(cat $cluster_id_file_path)"
# KRaft required step: Format the storage directory with a new cluster ID
echo -e "\nkafka-storage format --ignore-formatted -t $(cat $cluster_id_file_path) -c /etc/kafka/kafka.properties" >> /etc/confluent/docker/ensure