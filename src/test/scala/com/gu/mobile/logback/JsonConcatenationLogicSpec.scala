package com.gu.mobile.logback

import org.specs2.matcher.Matchers
import org.specs2.mutable.Specification

class JsonConcatenationLogicSpec extends Specification with Matchers {
  "The concatenation logic" should {
    val logic = new JsonConcatenationLogic {}
    "Gracefully handle and empty customFields" in {
      logic.concatenateEnvToCustomFields("", Map()) shouldEqual "{}"
    }
    "Concatenate fields to an empty custom fields" in {
      val customFields = ""
      val extraFields = Map("Extra" -> "Field")
      val expected = "{\"Extra\":\"Field\"}"
      logic.concatenateEnvToCustomFields(customFields, extraFields) shouldEqual expected
    }
    "Concatenate fields" in {
      val customFields = "{\"Custom1\":\"Field1\",\"Custom2\":\"Field2\"}"
      val extraFields = Map("Extra3" -> "Field3", "Extra4" -> "Field4")
      val expected = "{\"Custom1\":\"Field1\",\"Custom2\":\"Field2\",\"Extra3\":\"Field3\",\"Extra4\":\"Field4\"}"
      logic.concatenateEnvToCustomFields(customFields, extraFields) shouldEqual expected
    }
  }
}
