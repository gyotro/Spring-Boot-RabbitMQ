package com.gyo.amqp

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class CommandLine: CommandLineRunner {

    @Autowired
    lateinit var connectionFactory: ConnectionFactory

    companion object {
        const val defaultQueue = "defaultQueue"
        const val defaultExchange = "defaultTopic"
        const val defaultRouting = "defaultRouting"
    }

    private val log = LoggerFactory.getLogger(this.javaClass)
    override fun run(vararg args: String?) {

            log.info("Starting Spring Boot Application Component....")
        }


   // @Bean  // Inserendo Bean lanciamo la funzione non appena si instanzia la classe component
    fun log() = log.info("Starting Spring Boot Application Bean....")

   // @Bean
    fun queue() = Queue(defaultQueue)

   // @Bean
    fun topicInitializer(): TopicExchange = TopicExchange(defaultExchange)

  //  @Bean
    fun binding(queue: Queue, topicExchange: TopicExchange) = BindingBuilder.bind(queue).to(topicExchange).with(defaultRouting)

 //   @Bean
    fun converter(): MessageConverter = Jackson2JsonMessageConverter() // ci serve per convertire da POJO a JSON

//    @Bean
    fun template(): RabbitTemplate
    {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = converter()
        return rabbitTemplate
    }

/*    @Bean
    fun rabbitAdmin(): RabbitAdmin = RabbitAdmin( rabbitTemplate.connectionFactory )*/

}