import DTO.Card;
import DTO.DecodingMessage;
import baseAlghoritms.SpecialMath;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class PokerClient extends Listener {
    private int c, d, p;
    private final Random random;
    private final ArrayDeque<Integer> encodingCards = new ArrayDeque<>();
    private final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        System.out.println("Подключаемся к серверу");
        Client client = new Client();
        client.getKryo().register(Integer.class);
        client.getKryo().register(String.class);
        client.getKryo().register(Long.class);
        client.getKryo().register(DecodingMessage.class);
        new Thread(client).start();
        client.start();
        String ip = "localhost";
        int tcpPort = 27960;
        int udpPort = 27960;
        client.connect(5000, ip, tcpPort, udpPort);
        client.addListener(new PokerClient());
        Log.DEBUG();
    }

    private PokerClient(){
        random = new Random();
    }

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof Long){
            p = (int) (long) object;
            int[] t;
            do {
                c = Math.abs(random.nextInt()) % (p - 3) + 2;
                t = SpecialMath.nod(p - 1, c);
                d = (t[1] > 0? t[1]: t[1] % (p - 1) + (p - 1));
            } while (t[0] != 1);
        } else if (object instanceof String){
            ArrayList<Integer> cards = (ArrayList<Integer>) gson.fromJson((String)object, ArrayList.class);
            System.out.println("In list category!");
            for(int i = 0; i < cards.size(); i++){
                cards.set(i, SpecialMath.powOnModule(cards.get(i), c, p));
            }
            Supplier<Integer> randomIndex = () -> Math.abs(random.nextInt()) % cards.size();
            for(int i = 0, temp, x = randomIndex.get(), y = randomIndex.get(); i < cards.size();
                i++, x = randomIndex.get(), y = randomIndex.get()){
                temp = cards.get(x);
                cards.set(x, cards.get(y));
                cards.set(y, temp);
            }
            connection.sendTCP(gson.toJson(cards));
        } else if(object instanceof Integer){
            encodingCards.add((int)object);
            if(encodingCards.size() > 1) {
                connection.sendTCP(new DecodingMessage(connection.getID(), encodingCards.peekFirst()));
                connection.sendTCP(new DecodingMessage(connection.getID(), encodingCards.peekLast()));
            }
        } else if(object instanceof DecodingMessage){
            DecodingMessage decodingMessage = (DecodingMessage) object;
            if(decodingMessage.getOwnerID() == connection.getID())
                System.out.println(new Card(SpecialMath.powOnModule(decodingMessage.getMessage(), d, p)));
            else{
                decodingMessage.setMessage(SpecialMath.powOnModule(decodingMessage.getMessage(), d, p));
                connection.sendTCP(decodingMessage);
            }
        }
    }
}
