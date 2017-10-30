import java.util.Scanner;

public class RunServerSide {
    public static void main(String[] args) throws Exception {
        String players[] = new String[3];
        boolean gameState = false;
        MulticastServer server = new MulticastServer("228.5.6.7", 49321);
        System.out.println(server.getIP());
        Scanner input = new Scanner(System.in);
        System.out.print("Enter height of grid (500-1000) >> ");
        String gridHeightString = input.nextLine();
        int gridHeight = Integer.parseInt(gridHeightString);
        while (gridHeight < 500 || gridHeight > 1000) {
            System.out.print("Make sure height is greater than 500 and less than 1000 >> ");
            gridHeightString = input.nextLine();
            gridHeight = Integer.parseInt(gridHeightString);
        }
        System.out.print("Enter width of grid (500-1000) >> ");
        String gridWidthString = input.nextLine();
        int gridWidth = Integer.parseInt(gridWidthString);
        while (gridWidth < 500 || gridWidth > 1000) {
            System.out.print("Make sure width is greater than 500 and less than 1000 >> ");
            gridWidthString = input.nextLine();
            gridWidth = Integer.parseInt(gridWidthString);
        }

        int playerNumber = 0;
        while (true) {
            String message = server.read();
            System.out.println(message);

            if (gameState) {
                for(String player : players) {
                    server.broadcast(player);
                }

            } if (message.startsWith("ADD USER")) {
                if (players[2] != null) {
                    server.broadcast("FAILED too many players");
                } else {
                    String[] gridSizeArray = message.split(" ");
                    String playerName = gridSizeArray[2];
                    TestLightCycle lightCycle = new TestLightCycle(gridHeight, gridWidth);
                    int playerX = lightCycle.getCyclePosition()[0];
                    int playerY = lightCycle.getCyclePosition()[1];
                    players[playerNumber] = playerName + "," + playerX + "," + playerY;
                    server.broadcast("OKAY");
                    playerNumber++;
                    if(playerNumber >= 3) {
                        gameState = true;
                    }
                }

            } else if (message.startsWith("USER")) {
                String[] userActionArray = message.split(" ");
                if(userActionArray[2].equals("TURN")) {
                    if(userActionArray[3].equals("left")) {
                        for(String player: players) {
                            String[] playerArray = player.split(",");
                            if(playerArray[0].equals(userActionArray[1])) {
                                //increment x coordinate of light cycle selected by -1
                            }
                        }
                    } else if(userActionArray[3].equals("right")) {
                        for(String player: players) {
                            String[] playerArray = player.split(",");
                            if(playerArray[0].equals(userActionArray[1])) {
                                //increment x coordinate of light cycle selected by +1
                            }
                        }
                    }
                }

            } else if (message.startsWith("REMOVE USER")) {

            } else if (message.startsWith("GRID SIZE")) {
                server.broadcast("GRID SIZE " + gridHeightString + " " + gridWidthString);
            } else if (message.startsWith("GAME STATE")) {

            } else if (message.startsWith("SAVE SCORE")) {

            } else if (message.startsWith("GAME LEADERBOARD")) {

            }

            //for (String player : players) {
            //    if(player != null) {
            //        server.broadcast(player);
            //    }
            //}
        }

    }
}