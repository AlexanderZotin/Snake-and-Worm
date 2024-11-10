package game.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ModelManager {
    private static @Getter ModelManager instance = new ModelManager();

    @Getter
    @Setter
    private volatile ModelRepository models;
}
