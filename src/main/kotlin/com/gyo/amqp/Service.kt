package com.gyo.amqp


import org.slf4j.LoggerFactory
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service


@Service
class Service(): RabbitQueueService {

    @Autowired
    lateinit var connectionFactory: ConnectionFactory

    companion object {
        const val defaultQueue = "defaultQueue"
        const val defaultExchange = "defaultTopic"
        const val defaultRouting = "defaultRouting"
    }

    private val log = LoggerFactory.getLogger(this.javaClass)

/*        @Autowired
        lateinit var rabbitTemplate: RabbitTemplate*/

        @Autowired
        lateinit var rabbitAdmin: RabbitAdmin

/*        @Autowired
        lateinit var connectionFactory: ConnectionFactory*/

        @Autowired
        lateinit var rabbitListenerEndpointRegistry: RabbitListenerEndpointRegistry

        override fun addNewQueue(queueName: String?, exchangeName: String?, routingKey: String?) {
            val queue = Queue(queueName, true, false, false)
/*            val binding = Binding(
                    queueName,
                    Binding.DestinationType.QUEUE,
                    exchangeName,
                    routingKey,
                    null
            )*/
 //           val binding = BindingBuilder.bind(queue).to(TopicExchange(exchangeName)).with(routingKey)
            this.binding(queue, topicInitializer(exchangeName), routingKey)
            rabbitAdmin.declareQueue(queue)
            rabbitAdmin.declareExchange(topicInitializer(exchangeName));
            rabbitAdmin.declareBinding(this.binding(queue, topicInitializer(exchangeName), routingKey))

 //           addQueueToListener(exchangeName, queueName)
        }

        override fun addQueueToListener(listenerId: String?, queueName: String?) {
            log.info("adding queue : $queueName to listener with id : $listenerId")
            if (!checkQueueExistOnListener(listenerId, queueName)!!) {
                getMessageListenerContainerById(listenerId).addQueueNames(queueName)
                log.info("queue ")
            } else {
                log.info("given queue name : $queueName not exist on given listener id : $listenerId")
            }
        }

        override fun removeQueueFromListener(listenerId: String?, queueName: String?) {
            log.info("removing queue : $queueName from listener : $listenerId")
            if (checkQueueExistOnListener(listenerId, queueName)!!) {
                getMessageListenerContainerById(listenerId).removeQueueNames(queueName)
                log.info("deleting queue from rabbit management")
                rabbitAdmin!!.deleteQueue(queueName!!)
            } else {
                log.info("given queue name : $queueName not exist on given listener id : $listenerId")
            }
        }

        override fun checkQueueExistOnListener(listenerId: String?, queueName: String?): Boolean? {
            return try {
                log.info("checking queueName : $queueName exist on listener id : $listenerId")
                log.info("getting queueNames")
                val queueNames = getMessageListenerContainerById(listenerId).queueNames
                log.info("queueNames : $queueNames")
                if (queueNames != null) {
                    log.info("checking $queueName exist on active queues")
                    for (name in queueNames) {
                        log.info("name : $name with checking name : $queueName")
                        if (name == queueName) {
                            log.info("queue name exist on listener, returning true")
                            return java.lang.Boolean.TRUE
                        }
                    }
                    java.lang.Boolean.FALSE
                } else {
                    log.info("there is no queue exist on listener")
                    java.lang.Boolean.FALSE
                }
            } catch (e: Exception) {
                java.lang.Boolean.FALSE
            }
        }

        private fun getMessageListenerContainerById(listenerId: String?): AbstractMessageListenerContainer {
            log.info("getting message listener container by id : $listenerId")
            return rabbitListenerEndpointRegistry?.getListenerContainer(listenerId!!) as AbstractMessageListenerContainer
        }

    @Bean
    fun rabbitAdmin() = RabbitAdmin(connectionFactory)

    @Bean
    fun template(): RabbitTemplate
    {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
        return rabbitTemplate
    }
    fun binding(queue: Queue?, topicExchange: TopicExchange?, routingKey: String?) = BindingBuilder.bind(queue).to(topicExchange).with(routingKey)

    fun topicInitializer(exchange: String?): TopicExchange = TopicExchange(exchange)
}

