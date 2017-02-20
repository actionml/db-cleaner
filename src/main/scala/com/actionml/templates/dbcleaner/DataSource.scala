/*
 * Copyright ActionML, LLC under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * ActionML licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.actionml.templates.dbcleaner

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.{EmptyActualResult, EmptyEvaluationInfo, PDataSource, Params}
import org.apache.predictionio.core.{EventWindow, SelfCleaningDataSource}
import org.apache.spark.SparkContext
import com.actionml.templates.dbcleaner._


case class DataSourceParams(
  appName: String,
  eventWindow: Option[EventWindow]) extends Params

/** Read specified events from the PEventStore and creates RDDs for each event. A list of pairs (eventName, eventRDD)
 *  are sent to the Preparator for further processing.
  *
  *  @param dsp parameters taken from engine.json
 */
class DataSource(val dsp: DataSourceParams)
  extends PDataSource[TrainingData, EmptyEvaluationInfo, Query, EmptyActualResult]
  with SelfCleaningDataSource {

  @transient override lazy implicit val logger: Logger = Logger[this.type]

  override def appName: String = dsp.appName
  override def eventWindow: Option[EventWindow] = dsp.eventWindow

  drawInfo("Init DataSource", Seq(
    ("══════════════════════════════", "════════════════════════════"),
    ("App name", appName),
    ("Event window", eventWindow)))

  /** Reads events from PEventStore and create and RDD for each */
  override def readTraining(sc: SparkContext): TrainingData = {

    // beware! the following call most likely will alter the event stream in the DB!
    cleanPersistedPEvents(sc)

    TrainingData()
  }
}

case class TrainingData() extends Serializable {}
