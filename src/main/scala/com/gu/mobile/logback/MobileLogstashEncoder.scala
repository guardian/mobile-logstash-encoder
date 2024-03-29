package com.gu.mobile.logback

import java.io.StringWriter

import com.fasterxml.jackson.core.{JsonFactory, TreeNode}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.gu.{AppIdentity, AwsIdentity, DevIdentity}
import net.logstash.logback.encoder.LogstashEncoder
import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils

import scala.util.{Failure, Success, Try}
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider

trait JsonConcatenationLogic {

  private val mapper = new ObjectMapper()
  private val jsonFactory = new JsonFactory(mapper)

  def concatenateEnvToCustomFields(customFields: String, extraFields: Map[String, String]): String = {
    def parsedTreeNode: Try[TreeNode] = {
      if (customFields != null && customFields.trim != "") {
        Try(jsonFactory.createParser(customFields).readValueAsTree[TreeNode]())
      } else {
        Success(mapper.createObjectNode())
      }
    }

    def appendToObjectNode(treeNode: TreeNode): Try[ObjectNode] = treeNode match {
      case objectNode: ObjectNode => {
        extraFields.foreach { case (key, value) => objectNode.put(key, value) }
        Success(objectNode)
      }
      case _ => Failure(new RuntimeException(s"Parsed Json was not an object: ${customFields}"))
    }

    def serialiseToString(objectNode: ObjectNode): Try[String] = Try {
      val sw = new StringWriter()
      val generator = jsonFactory.createGenerator(sw)
      generator.writeTree(objectNode)
      sw.toString
    }

    val concatenatedFields = for {
      treeNode <- parsedTreeNode
      objectNode <- appendToObjectNode(treeNode)
      output <- serialiseToString(objectNode)
    } yield output

    concatenatedFields match {
      case Success(result) => result
      case Failure(exception) => throw exception
    }
  }
}

final class MobileLogstashEncoder extends LogstashEncoder with JsonConcatenationLogic {
  private var maybeDefaultAppName: Option[String] = None

  def setDefaultAppName(defaultAppName: String): Unit = maybeDefaultAppName = Some(defaultAppName)

  private lazy val loadAppStackStage: Map[String, String] = {
    val defaultAppName: String = maybeDefaultAppName.getOrElse(throw new IllegalArgumentException(
      s"""Logback xml must include a defaultAppName like
         |<encoder class="${classOf[MobileLogstashEncoder].getCanonicalName}">
         |  <defaultAppName>APP_NAME_HERE</defaultAppName>
         |</encoder>
      """.stripMargin))

    val identity = Option(System.getenv("MOBILE_LOCAL_DEV")) match {
      case Some(_) => DevIdentity(defaultAppName)
      case None => 
        AppIdentity
          .whoAmI(defaultAppName, DefaultCredentialsProvider.builder().build())
          .getOrElse(DevIdentity(defaultAppName))
      
    }    

    (identity match {
      case awsIdentity: AwsIdentity => Map(
        "app" -> awsIdentity.app,
        "stack" -> awsIdentity.stack,
        "stage" -> awsIdentity.stage
      ) ++ Try(EC2MetadataUtils.getInstanceId).toOption.map("ec2_instance" -> _)
      case _ => Map("app" -> defaultAppName)
    })
  }

  override def start(): Unit = {
    this.setCustomFields(concatenateEnvToCustomFields(this.getCustomFields, loadAppStackStage))
    super.start()
  }
}
