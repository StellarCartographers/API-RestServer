package space.tscg;


public class Launcher
{
    public static void main(String[] args) {
        var api = new TSCGServer();
        api.start();
    }
}
