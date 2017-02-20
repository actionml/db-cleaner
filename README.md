# PIO db-trim Template

This template can be used with any dataset stored in the PredictionIO EventServer. It maintains a timeWindow of events and preserves the current state of any properties of objects. It does this by dropping old named events, removing duplicates, and aggregating an $set/$unset events.

**WARNING**: This modifies the database. Backup before trying the first time, and read the docs carefully.

# Config

```
{
  "id": "default",
  "description": "Default settings",
  "engineFactory": "com.actionML.DBClean",
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
    - **duration**: how far in the past to keep named events, drop the rest
    - **removeDuplicates**: de-duplicate named events
    - **compressProperties**: aggregate $set/$unset events so as to preserve the state of properties they define. This will result in no property data being lost.

Named events are any events not named by a reserve name like $set/$unset/$delete. Named events carry data that will accumulate in the EventServer forever if left untrimmed.

# History

**V 0.1**

This is the first standalone template implementing the `SelfCleaningDatasourse` so that it can be driven by Spark tasks that are scaled to writing to the DB whereas putting this in each template may overload the DB due to the scale of Spark needed for training. In other words this Template can be scaled independently of training, to match the HBase or DB used.

**V 0.0**

The initial code for most of this is in PredictionIO 0.11.0. It is documents as the `SelfCleaningDatasoure`. It is made to execute before at the beginning of the training task for any Template and may be included by setting params in Engine.json. However due to scaling needs for training the required modifications to the EventServer data may put too much load on the DB.
 
#License
This Software is licensed under the Apache Software Foundation version 2 licence found here: http://www.apache.org/licenses/LICENSE-2.0
