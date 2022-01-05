package onl.tesseract.hermes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class HermesApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(HermesApplication.class, args);
    }

    @Bean
    public JDA initJDA(@Value("${jda.token}") final String botToken) throws LoginException
    {
        return JDABuilder.createDefault(botToken)
                     .build();
    }
}
