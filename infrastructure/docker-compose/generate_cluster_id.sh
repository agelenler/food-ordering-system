#!/bin/bash

cluster_id_file_path="/tmp/cluster-id-dir/clusterId"
#[-f FILE ]	True if FILE exists and is a regular file.
if [ ! -f "$cluster_id_file_path" ]; then
  /bin/kafka-storage random-uuid > /tmp/cluster-id-dir/clusterId
  echo "Cluster id is created for Kraft!"
fi
