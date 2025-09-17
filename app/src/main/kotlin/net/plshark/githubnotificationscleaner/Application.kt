package net.plshark.githubnotificationscleaner

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/** The main application class. */
@SpringBootApplication
class Application

/** The application entry point. */
fun main(args: Array<String>) {
    runApplication<Application>(args = args) {
        webApplicationType = WebApplicationType.NONE
    }
}
