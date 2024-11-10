package game;

public final class ProgramConstants {
    public static final boolean DRAW_NET_BY_DEFAULT = false;
    public static final int SIZE_OF_SQUARE = 20;
    public static final int ROUNDING_OF_SQUARE = 7;
    public static final byte WIDTH_OF_EYE = 6;
    public static final byte HEIGHT_OF_EYE = 6;
    public static final byte POINTS_FOR_BONUS = 20;
    public static byte SECONDS_FOR_BONUS = 6;
    public static final String HELP_TEXT = """
            Управляйте жёлтой змейкой стрелками на клавиатуре.
            Кормите змейку красными яблоками,
            но остерегайтесь синих и фиолетовых, ведь они наоборот отнимают очки!
            Будьте внимательны: иногда на поле появляется бонус, который даёт
            """ + POINTS_FOR_BONUS + """
             очков сразу!
            
            Старайтесь, чтобы змейка не врезалась в край поля,
            в саму себя или в бордового червяка.
            Червяк не ест яблоки, но мешает змейке. Он может проходить сквозь
            змейку, но если змейка врежется в червяка своей головой, то это означает проигрыш.
            
            Приятной вам игры!
            """;
    
    private ProgramConstants() {
        throw new AssertionError("Не должно быть экземпляров класса ProgramConstants!");
    }
}
