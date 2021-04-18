package com.gyo.amqp

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/fattura")
class Controller(var template: RabbitTemplate) {

    private val log = LoggerFactory.getLogger(this.javaClass)

//    @Autowired
//    lateinit var template: RabbitTemplate

    @Autowired
    lateinit var rabbitQueueService: RabbitQueueService

    // Metodo con i parametri di default
    @PostMapping
    fun postFattura(@RequestBody fattura: Fattura): InvoiceConfirmation
    {

        template.run {
            convertAndSend(CommandLine.defaultExchange, CommandLine.defaultRouting, fattura)
            log.info("Sendig to the queue....$fattura")
            return InvoiceConfirmation(fattura.numFatt, Status = "Inviata")
        }
    }
    @PostMapping("advanced")
    fun postFatturaQueue(@RequestBody fattura: Fattura, @RequestParam exchange: String?, @RequestParam routing: String?, @RequestParam queue: String?): InvoiceConfirmation
    {

        return template.run {
            val exc = exchange ?: CommandLine.defaultExchange
            val rout = routing ?: CommandLine.defaultRouting
            val qu = queue ?: CommandLine.defaultQueue
//            if (exchange != null && queue != null && routing != null)
            rabbitQueueService.addNewQueue(qu,exc, rout)
            convertAndSend(exc, rout, fattura)
            log.info("Sendig to the queue....$fattura")
            InvoiceConfirmation(fattura.numFatt, Status = "Inviata")
        }
    }
}