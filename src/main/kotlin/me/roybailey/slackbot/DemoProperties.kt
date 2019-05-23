package me.roybailey.slackbot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "demo")
class DemoProperties {

    val slack = SlackProperties()

    class SlackProperties {
        var enabled = false
        lateinit var token: String
        lateinit var channel: String
        var filterlist: List<String> = emptyList()
    }
}
