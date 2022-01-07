package onl.tesseract.hermes;

public interface DiscordSubCommand extends DiscordCommand {

    Class<? extends DiscordRootCommand> getParentCommand();
}
