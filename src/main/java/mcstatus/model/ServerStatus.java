package mcstatus.model;

public class ServerStatus {
    public String host;
    public int port;
    public String version;
    public int protocolVersion;
    public PlayersInfo players = new PlayersInfo();
    public String description;
    public byte[] icon;
    public Motd motd;
}
