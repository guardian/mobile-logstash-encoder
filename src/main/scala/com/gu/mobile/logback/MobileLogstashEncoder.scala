package com.gu.mobile.logback

import com.amazonaws.util.EC2MetadataUtils
import com.gu.{AppIdentity, AwsIdentity}
import net.logstash.logback.encoder.LogstashEncoder
import play.api.libs.json.{JsObject, JsString, Json}

import scala.util.Try

final class MobileLogstashEncoder extends LogstashEncoder {
  private var maybeDefaultAppName: Option[String] = None

  def setDefaultAppName(defaultAppName: String): Unit = maybeDefaultAppName = Some(defaultAppName)

  private def loadAppStackStageJsObject: JsObject = {
    val defaultAppName: String = maybeDefaultAppName.getOrElse(throw new IllegalArgumentException(
      s"""Logback xml must include a defaultAppName like
         |<encoder class="${classOf[MobileLogstashEncoder].getCanonicalName}">
         |  <defaultAppName>APP_NAME_HERE</defaultAppName>
         |</encoder>
      """.stripMargin))
    JsObject(((AppIdentity.whoAmI(defaultAppName = defaultAppName) match {
      case awsIdentity: AwsIdentity => Map(
        "app" -> awsIdentity.app,
        "stack" -> awsIdentity.stack,
        "stage" -> awsIdentity.stage
      )
      case _ => Map("app" -> defaultAppName)
    }) ++ Option(EC2MetadataUtils.getInstanceId).map("ec2_instance" -> _)).mapValues(JsString))
  }

  override def start(): Unit = {
    this.setCustomFields(Json.stringify(getCustomFieldsAsPlayJsObject.deepMerge(loadAppStackStageJsObject)))
    super.start()
  }

  private def getCustomFieldsAsPlayJsObject = Try(Json.parse(getCustomFields))
    .map(_.validate[JsObject].asOpt)
    .toOption
    .flatten
    .getOrElse(JsObject(Seq()))

}
