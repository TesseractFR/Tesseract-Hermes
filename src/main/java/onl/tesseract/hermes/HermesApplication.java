package onl.tesseract.hermes;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.impl.TrelloImpl;
import com.julienvey.trello.impl.http.ApacheHttpClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.security.auth.login.LoginException;
import java.util.Objects;

@SpringBootApplication
public class HermesApplication {

    private static final Logger logger = LoggerFactory.getLogger(HermesApplication.class);

    public static Board trelloBoard;

    public static void main(String[] args)
    {
        SpringApplication.run(HermesApplication.class, args);
    }

    @Bean
    public Trello trello(@Value("${trello.key}") final String key,
                         @Value("${trello.access-token}") final String accessToken) {
        return new TrelloImpl(key, accessToken, new ApacheHttpClient());
    }

    @Bean
    public Board trelloBoard(@Value("${trello.boardId}") final String boardId, Trello trello)
    {
        trelloBoard = trello.getBoard(boardId);
        return trelloBoard;
    }

    @Bean
    public TextChannel suggestionChannel(@Value("${discord.channel.id}") final String suggestionChannelId, final Guild guild) {
        return Objects.requireNonNull(guild.getTextChannelById(suggestionChannelId));
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
    public CommandLineRunner registerCommands(CommandManager commandManager, JDA jda) {
        return args -> {
            jda.addEventListener(commandManager);
            commandManager.registerCommands();
        };
    }
}
