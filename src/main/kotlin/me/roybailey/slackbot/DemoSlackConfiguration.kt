package me.roybailey.slackbot

import com.google.inject.Guice
import com.hubspot.slack.client.SlackClient
import com.hubspot.slack.client.SlackClientModule
import com.hubspot.slack.client.SlackClientRuntimeConfig
import com.hubspot.slack.client.SlackWebClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


class DemoSlackConfiguration {

    @ConditionalOnProperty(prefix = "demo.slack", name = ["enabled"], havingValue = "true", matchIfMissing = false)
    @Configuration
    class SlackConfiguration(private val properties: DemoProperties) {

        private fun slackClientModule() = SlackClientModule()

        private fun slackWebClientFactory(): SlackWebClient.Factory = Guice.createInjector(slackClientModule()).getInstance(SlackWebClient.Factory::class.java)

        private fun slackClient(): SlackClient = slackWebClientFactory().build(
                SlackClientRuntimeConfig.builder()
                        .setTokenSupplier { properties.slack.token }
                        .build())

        @Bean
        fun slackService() = DefaultSlackService(slackClient(), properties.slack.channel)

    }
}

