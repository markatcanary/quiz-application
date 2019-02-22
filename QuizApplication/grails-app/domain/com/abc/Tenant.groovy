package com.abc

class Tenant {
  static hasMany = [quiz: Quiz, contacts: Contact]

  static constraints = {
    name nullable: false
  }

  String name
}
