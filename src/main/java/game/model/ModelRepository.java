package game.model;

import java.awt.Dimension;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@NonNull
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ModelRepository {
    private volatile GameProcess gameProcess;
    private volatile Snake snake;
    private volatile Worm worm;
    private volatile Apple[] apples;
    private volatile Bonus bonus;
    private final Dimension fieldSize;
}
