/**
 * Created by mak on 2/21/19.
 */
package com.abc

class CanaryUtils {

  static String NULL_STR = "null"

  static def toEpoch(Date date) {
    def epoch = date.getTime() / 1000
    epoch.toLong()
  }

  static def getIncludeParamsString(params) {
    def include = []
    if (params.containsKey('include')) {
      include = params.include.split(",")
    }
    String includes = include.size() > 0 ? include[0] : null
    return includes
  }

  static def getStringProperty(def jsonObj, String property) {
    String value
    if (jsonObj.containsKey(property)) {
      value = jsonObj[property]
      if (!value || value == "" || value.isEmpty()) {
        value = null
      }
      return value
    }
    return value
  }

  static boolean getBooleanProp(def jsonObj, String property) {
    boolean isValue

    if (jsonObj.containsKey(property)) {
      isValue = jsonObj[property].toBoolean()
    }
    return isValue
  }

  static long getLongProperty(def jsonObj, String property) {
    long dob = 0
    if (jsonObj.containsKey(property)) {
      dob =  jsonObj[property].toString() == NULL_STR ? 0 : new Long(jsonObj[property]).longValue()
    }
    dob
  }

  static Integer getIntegerProperty(def jsonObj, String property) {
    Integer i = 0
    if (jsonObj.containsKey(property)) {
      i =  jsonObj[property].toString() == NULL_STR ? 0 : new Integer(jsonObj[property])
    }
    i
  }

  static def getListProperty(def jsonObj, String property) {
    def result = []
    if (jsonObj.containsKey(property) && jsonObj[property].toString() != NULL_STR ) {
      def objList = jsonObj[property]
      objList.each {
        result.push(it)
      }
    }
    result
  }

}
