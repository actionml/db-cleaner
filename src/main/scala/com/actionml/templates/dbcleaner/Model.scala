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

import org.apache.spark.SparkContext

/** Universal Recommender models to save in ES */
class Model()(implicit sc: SparkContext) {
  def save(dateNames: Seq[String], esIndex: String, esType: String): Boolean = {
    true
  }

}

/*object Model {
  @transient lazy val logger: Logger = Logger[this.type]

  def apply(): Model = {
    new Model(null, null, null, nullModel = true)(sc.get)
  }
}
*/
