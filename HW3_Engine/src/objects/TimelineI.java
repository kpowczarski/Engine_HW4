package objects;

public interface TimelineI {

    public long getCurrentTime ();

    public void pause ();

    public void unpause ();

    public void start ();

    public void doubleTime ();

    public void halfTime ();

    public void normalTime ();

    public void setTic ( int tic );

}
