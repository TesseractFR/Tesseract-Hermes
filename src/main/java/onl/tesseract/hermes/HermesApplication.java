package onl.tesseract.hermes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
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
    public JDA initJDA(@Value("${jda.token}") final String botToken) throws LoginException, InterruptedException
    {
        JDA jda = JDABuilder.createDefault(botToken)
                                    .build();
        jda.awaitReady();
        return jda;
    }

    @Bean
    public Guild guild(final JDA jda, @Value("${discord.guild.id}") final long guildId) {
        return jda.getGuildById(guildId);
    }

    @Bean
    public CommandLineRunner registerCommands(CommandManager commandManager) {
        return args -> commandManager.registerCommands();
    }
}
