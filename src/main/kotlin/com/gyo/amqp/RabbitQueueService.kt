package com.gyo.amqp

interface RabbitQueueService {

    fun addNewQueue(queueName: String?, exchangeName: String?, routingKey: String?)
    fun addQueueToListener(listenerId: String?, queueName: String?)
    fun removeQueueFromListener(listenerId: String?, queueName: String?)
    fun checkQueueExistOnListener(listenerId: String?, queueName: String?): Boolean?

}