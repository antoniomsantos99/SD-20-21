import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class User {
    private String username;
    private String password;
    private Coordinates xy;

    public User (String user, String pw, Coordinates xy) {
        this.username = user;
        this.password = pw;
        this.xy = xy;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.username);
        out.writeUTF(this.password);
        out.writeInt(this.xy.getX());
        out.writeInt(this.xy.getY());

        out.flush();
    }

    public static User deserialize(DataInputStream in) throws IOException {
        String username = in.readUTF();
        String password = in.readUTF();

        int x = in.readInt();
        int y = in.readInt();
        Coordinates xy = new Coordinates(x,y);


        return new User(username, password, xy);
    }
}
