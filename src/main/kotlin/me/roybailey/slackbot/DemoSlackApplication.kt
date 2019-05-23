package me.roybailey.slackbot

import com.google.inject.Guice
import com.hubspot.slack.client.SlackClient
import com.hubspot.slack.client.SlackClientModule
import com.hubspot.slack.client.SlackClientRuntimeConfig
import com.hubspot.slack.client.SlackWebClient
import com.hubspot.slack.client.methods.params.chat.ChatPostMessageParams
import com.hubspot.slack.client.methods.params.users.UserEmailParams
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct


fun main(args: Array<String>) {
    runApplication<DemoSlackApplication>(*args)
}


@SpringBootApplication // (scanBasePackages = ["me.roybailey.slackbot"])
class DemoSlackApplication {

    @Autowired
    lateinit var slackService:SlackService

    @PostConstruct
    fun initialize() {
        slackService.post("Hello Slack Client :grin:")
    }
}



interface SlackService {
    fun post(messageToPost:String): ChatPostMessageResponse
}


class DefaultSlackService(private val slackClient: SlackClient, private val channel: String): SlackService {

    companion object {
        private val logger = LoggerFactory.getLogger(DefaultSlackService::class.java)
    }

    override fun post(messageToPost:String): ChatPostMessageResponse {
        val post =  ChatPostMessageParams.builder().setText(messageToPost).setChannelId(channel).build()
        logger.debug("Posting to channel: $channel with message: $messageToPost")
        return slackClient.postMessage(post).get().unwrapOrElseThrow()
    }

}
