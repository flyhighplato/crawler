package com.flyhighplato

import com.netflix.astyanax.MutationBatch
import com.netflix.astyanax.connectionpool.OperationResult
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException
import com.netflix.astyanax.model.ColumnList

class CrawlerMain {

    public static void main(String[] args) {
        TaskClient taskClient = new TaskClient()
        MutationBatch m = taskClient.keyspace.prepareMutationBatch()

        m.withRow(taskClient.columnFamily, "task1")
                .putColumn("taskid", "1", null)
                .putColumn("taskname", "gary", null)

        try {
            m.execute()
        } catch (ConnectionException e) { }

        OperationResult<ColumnList<String>> result =
            taskClient.keyspace.prepareQuery(taskClient.columnFamily)
                    .getKey("task1")
                    .execute()

        result.result.each{
            println "${it.name}:${it.stringValue}"
        }
    }


}
