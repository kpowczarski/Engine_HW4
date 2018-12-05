package objects;

public enum Events {

    RECORD ( 0 ),

    COLLISION ( 1 ),

    DEATH ( 2 ),

    MOVEMENT ( 3 ),

    SPAWN ( 4 ),

    STOPRECORD ( 5 ),
    
    GAMEOVER( 6 ),

    SHOOT ( 7 ),

    ALIENSMOVEDOWN ( 8 )

    ;

    private int code;

    private Events ( int code ) {
        this.code = code;
    }

    public int getCode () {
        return code;
    }

}
