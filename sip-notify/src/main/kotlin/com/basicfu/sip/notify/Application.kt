
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SipApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipApplication::class.java, *args)
}