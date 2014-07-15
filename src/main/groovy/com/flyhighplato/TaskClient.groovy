package com.flyhighplato

import com.netflix.astyanax.AstyanaxContext
import com.netflix.astyanax.Keyspace
import com.netflix.astyanax.connectionpool.NodeDiscoveryType
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl
import com.netflix.astyanax.model.ColumnFamily
import com.netflix.astyanax.serializers.StringSerializer
import com.netflix.astyanax.thrift.ThriftFamilyFactory


class TaskClient {

    String clusterName = "CrawlCluster"
    String keyspaceName = "CrawlKeyspace"
    String columnFamilyName = "CrawlColumnFamily"
    String connectionPoolName = "CrawlerConnectionPool"

    String seeds = ["192.168.80.137:9160"].join(',')

    Keyspace keyspace

    ColumnFamily<String, String> columnFamily


    public TaskClient() {

    }

    Keyspace getKeyspace() {
        if(!keyspace) {
            def context = new AstyanaxContext.Builder()
                    .forCluster(clusterName)
                    .forKeyspace(keyspaceName)
                    .withAstyanaxConfiguration(
                        new AstyanaxConfigurationImpl()
                            .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
                    )
                    .withConnectionPoolConfiguration(
                        new ConnectionPoolConfigurationImpl(connectionPoolName)
                            .setPort(9160)
                            .setMaxConnsPerHost(1)
                            .setSeeds(seeds)
                    )
                    .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                    .buildKeyspace(ThriftFamilyFactory.getInstance())


            context.start()

            keyspace = context.getClient()

            keyspace.createKeyspaceIfNotExists(
                    [
                            strategy_options: [
                                    replication_factor: "1"
                            ],
                            strategy_class: "SimpleStrategy"
                    ]
            )

        }

        keyspace
    }

    ColumnFamily getColumnFamily() {

        if(!columnFamily) {
            columnFamily = new ColumnFamily<String, String>(
                        columnFamilyName,
                        StringSerializer.get(),
                        StringSerializer.get())

            try {
                getKeyspace().getColumnFamilyProperties(columnFamilyName)
            } catch(e) {
                getKeyspace().createColumnFamily(columnFamily.properties)
            }
        }

        columnFamily
    }
}
