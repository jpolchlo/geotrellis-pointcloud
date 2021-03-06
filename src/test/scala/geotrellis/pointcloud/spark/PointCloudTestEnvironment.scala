/*
 * Copyright 2017 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geotrellis.pointcloud.spark

import geotrellis.spark.testkit._

import org.apache.hadoop.fs.Path
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import org.scalatest.Suite

import java.io.File

trait PointCloudTestEnvironment extends TestEnvironment { self: Suite =>
  val testResources = new File("src/test/resources")
  val lasPath = new Path(s"file://${testResources.getAbsolutePath}/las")
  val multipleLasPath = new Path(s"file://${testResources.getAbsolutePath}/las/files")

  def setS3Credentials: Unit = {
    try {
      val credentialsProviderChain = new DefaultAWSCredentialsProviderChain
      val conf = ssc.sparkContext.hadoopConfiguration

      conf.set("fs.s3.impl", classOf[org.apache.hadoop.fs.s3native.NativeS3FileSystem].getName)
      conf.set("fs.s3n.awsAccessKeyId", credentialsProviderChain.getCredentials.getAWSAccessKeyId)
      conf.set("fs.s3n.awsSecretAccessKey", credentialsProviderChain.getCredentials.getAWSSecretKey)
    } catch {
      case e => println(e.getMessage)
    }
  }
}
