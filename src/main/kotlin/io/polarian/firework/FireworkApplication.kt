package io.polarian.firework

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FireworkApplication

fun main(args: Array<String>) {
    runApplication<FireworkApplication>(*args)
}
