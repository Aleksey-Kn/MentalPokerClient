import DTO.Card;
import DTO.DecodingMessage;
import baseAlghoritms.SpecialMath;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

public class PokerClient extends Listener {
    private static final Client client = new Client();
    private static Thread mainThread;

    private int c, d, p, printCount = 0;
    private final Random random;
    private final ArrayDeque<Integer> encodingCards = new ArrayDeque<>();

    public static void main(String[] args) throws Exception {
        client.getKryo().register(Integer.class);
        client.getKryo().register(ArrayList.class);
        client.getKryo().register(Long.class);
        client.getKryo().register(DecodingMessage.class);
        client.getKryo().register(Boolean.class);
        client.getKryo().register(Integer[].class);
        mainThread = new Thread(client);
        mainThread.start();
        client.start();
        String ip = "localhost";
        int tcpPort = 27960;
        client.connect(5000, ip, tcpPort);
        client.addListener(new PokerClient());
        client.sendTCP(true);
    }

    private PokerClient() {
        random = new Random();
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Long) {
            p = (int) (long)object;
            int[] t;
            do {
                c = Math.abs(random.nextInt()) % (p - 3) + 2;
                t = SpecialMath.nod(p - 1, c);
                d = (t[2] > 0 ? t[2] : t[2] % (p - 1) + (p - 1));
            } while (t[0] != 1);
        } else if (object instanceof ArrayList) {
            ArrayList<Integer> cards = (ArrayList<Integer>) object;
            cards.replaceAll(a -> SpecialMath.powOnModule(a, c, p));
            Supplier<Integer> randomIndex = () -> Math.abs(random.nextInt()) % cards.size();
            for (int i = 0, temp, x = randomIndex.get(), y = randomIndex.get(); i < cards.size();
                 i++, x = randomIndex.get(), y = randomIndex.get()) {
                temp = cards.get(x);
                cards.set(x, cards.get(y));
                cards.set(y, temp);
            }
            connection.sendTCP(cards);
        } else if (object instanceof Integer) {
            encodingCards.add((int) object);
            if (encodingCards.size() > 1) {
                connection.sendTCP(new DecodingMessage(connection.getID(), encodingCards.peekFirst()));
                connection.sendTCP(new DecodingMessage(connection.getID(), encodingCards.peekLast()));
            }
        } else if (object instanceof DecodingMessage) {
            DecodingMessage decodingMessage = (DecodingMessage) object;
            if (decodingMessage.getOwnerID() == connection.getID()) {
                System.out.println(new Card(SpecialMath.powOnModule(decodingMessage.getMessage(), d, p)));
            } else {
                decodingMessage.setMessage(SpecialMath.powOnModule(decodingMessage.getMessage(), d, p));
                connection.sendTCP(decodingMessage);
            }
        } else if(object instanceof Integer[]) {
            connection.sendTCP(Arrays.stream((Integer[]) object)
                    .map(val -> SpecialMath.powOnModule(val, d, p))
                    .toArray(Integer[]::new));
        }
    }
}
