# `db-cleaner` Template

This template can be used with any dataset stored in the PredictionIO EventServer. It maintains a timeWindow of events and preserves the current state of any properties of objects. It does this by dropping old named events. It can also remove duplicates and aggregate `$set/$unset` and `$delete` events, thereby compacting the dataset.

This is possible by simple mods to your template but since it requires a long time to complete, it provided here as a separate template so it can be run on its own schedule independent of your main template train schedule.

**WARNING**: This modifies the database. Backup before trying the first time, and read the docs carefully.

# Config

```
{
  "id": "default",
  "description": "Default settings",
  "engineFactory": "com.actionml.DBClean",
  "datasource": {
    "params" : {
      "appName": "test-app",
      "eventWindow": {
        "duration": "7 days",
        "removeDuplicates": true,
        "compressProperties": true
      }
    }
  }
}
```

 - **id**: String id of engine.
 - **description**: Engine description
 - **engineFactory**: leave as it above
 - **appId**: id from `pio app list` and **remember** that data may be dropped from the dataset irretrievably.
 - **eventWindow**: typical use of this Template is to maintain a window of events based on their timestamps&mdash;to trim off older events except for property setting events.
    - **duration**: how far back in time to keep named events, drop/trim older events. Reserved events like $set/%unset are unaffected by this duration.
    - **removeDuplicates**: de-duplicate named events retaining the most recent timestamp of all dups. Reserved events are unaffected by this.
    - **compressProperties**: aggregate $set/$unset events so as to preserve the state of properties they define. This will result in no property data being lost for all time. Individual named properties are aggreagated over all time where the most recent $set of the property wins and all other interstitial values the property might have taken on are removed from the record since they do not affect how the values look in aggregation. In other words the most recent $set of an individual property wins.

Named events are any events not called by a reserve name like $set/$unset/$delete. Named events carry data that will accumulate in the EventServer forever if left uncompressed.

# Integration Test

The integration test is meant to test the `SelfCleaningDataSource` feature of PredictionIO. With the EventServer running and HDFS running on localhost in Pseudo-cluster mode, run `./examples/integration-test`. This will create 2 JSON files in `data/` for pre and post cleaning data. For now you'll have to compare the 2 by eye. To experiment change `data/sample-data.txt` which is used to create the original pre-cleaning data, then run the test.

# History

**V 0.1**

This is the first standalone template implementing the `SelfCleaningDatasource` so that it can be driven by Spark tasks that are scaled to writing to the DB whereas putting this in each template may overload the DB due to the scale of Spark needed for training. In other words this Template can be scaled independently of training, to match the HBase or DB used.

**V 0.0**

The initial code for most of this is in PredictionIO 0.11.0. It is documents as the `SelfCleaningDatasource`. It is made to execute at the beginning of the training task for any Template and may be included by setting params in Engine.json. However due to scaling needs for training, the required modifications to the EventServer data may put too much load on the DB.
 
#License
This Software is licensed under the Apache Software Foundation version 2 licence found here: http://www.apache.org/licenses/LICENSE-2.0
