package onl.tesseract.hermes.suggestion;

import com.julienvey.trello.domain.TList;
import onl.tesseract.hermes.HermesApplication;

public enum TrelloList {
    SUGGESTIONS("Suggestions"),
    ;
    private final String name;

    TrelloList(final String name)
    {
        this.name = name;
    }

    public TList getList()
    {
        return HermesApplication.trelloBoard.fetchLists()
                                            .stream()
                                            .filter(list -> list.getName().equals(getName()))
                                            .findAny()
                                            .orElseThrow();
    }

    public String getName()
    {
        return name;
    }
}
