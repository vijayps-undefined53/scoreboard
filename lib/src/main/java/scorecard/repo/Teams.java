package scorecard.repo;

import java.util.Objects;
import java.util.Set;

public class Teams {
    private final Integer id;
    private final String name;
    private Set<String> players;

    public Teams(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teams)) return false;
        Teams teams = (Teams) o;
        return getId().equals(teams.getId());
    }

    @Override
    public String toString() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
