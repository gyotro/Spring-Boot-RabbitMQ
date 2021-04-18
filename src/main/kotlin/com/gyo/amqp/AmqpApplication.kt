package com.gyo.amqp

import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.jvm.javaClass


@SpringBootApplication
class AmqpApplication

/*@Autowired
lateinit var logger: Logger*/

//	companion object : Log() {}
// private val log = LoggerFactory.getLogger(this.javaClass)
	fun main(args: Array<String>) {

//		logger.info("Starting Spring Boot Application Logging")
//		Log.logger.info("Test Logging")
		runApplication<AmqpApplication>(*args)
	}

abstract class Log {
	val logger: Logger = LoggerFactory.getLogger(this.javaClass)
}