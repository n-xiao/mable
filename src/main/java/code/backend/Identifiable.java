package code.backend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CountdownFolder.class, name = "CountdownFolder")
    , @JsonSubTypes.Type(value = Countdown.class, name = "Countdown")
})
public abstract class Identifiable {
    private final UUID ID;
    @JsonIgnore private boolean markedForDeletion;

    protected Identifiable(final String ID_STRING) {
        this(UUID.fromString(ID_STRING));
    }

    protected Identifiable(final UUID ID) {
        this.ID = ID;
        this.markedForDeletion = false;
    }

    @JsonProperty("ID")
    public UUID getID() {
        return ID;
    }

    public void setMarkForDeletion(final boolean MARK) {
        this.markedForDeletion = MARK;
    }

    public boolean getMarkedForDeletion() {
        return this.markedForDeletion;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Identifiable))
            return false;
        else {
            Identifiable other = (Identifiable) obj;
            return other.ID.equals(this.ID);
        }
    }
}
