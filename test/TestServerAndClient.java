/**
 * Created by jc428209 on 15/09/17.
 */
public class TestServerAndClient {
    public static void main(String[] args) throws Exception{

        //Create server and client objects
        MulticastServer server = new MulticastServer("228.5.6.7", 49321);
        Client client = new Client(49321);

        //Send a message to server from client
        client.send(server.getIP(), server.getPort(), "OldClientIgnore to server test");
        String message = server.read();
        System.out.println(message);

        //Broadcast a message from server and listen on client
        server.broadcast("Server to client test");
        String clientMessage = client.listen(server.getMulticast());
        System.out.println(clientMessage);

        //Close server
        server.close();
    }
}
