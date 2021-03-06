import java.util.Scanner;

public class RunServerSide {
    public static void main(String[] args) throws Exception {
        final int MAXPLAYERS = 3;
        String gameStates[] = {"IDLE", "WAITING", "PLAYING", "OVER"};
        NewLightCycle lightCycles[] = new NewLightCycle[MAXPLAYERS];
        String players[] = new String[MAXPLAYERS];
        String gameState = gameStates[1];
        String playerString = "";

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
        int messageNumber = 0;
        while (true) {
            String message = server.read();
            System.out.println(message);

             if (message.startsWith("ADD USER")) {
                if (players[2] != null) {
                    server.broadcast("FAILED too many players");
                } else {
                    String[] gridSizeArray = message.split(" ");
                    String playerName = gridSizeArray[2];
                    NewLightCycle lightCycle = new NewLightCycle(gridHeight, gridWidth);
                    lightCycles[playerNumber] = lightCycle;

                    int playerX = lightCycle.getCyclePosition()[0];
                    int playerY = lightCycle.getCyclePosition()[1];
                    players[playerNumber] = playerName + "," + playerX + "," + playerY;

                    if(playerString.equals("")) {
                        playerString = players[playerNumber];
                    } else {
                        playerString = playerString + "-" + players[playerNumber];
                    }


                    server.broadcast("OKAY " + playerName);
                    playerNumber++;
                    if(playerNumber >= MAXPLAYERS) {
                        server.broadcast("PLAY " + playerString);
                        gameState = gameStates[2]; //set gameState to PLAYING
                        server.broadcast(playerString.trim());
                    }
                }

            } else if (message.startsWith("USER")) {
                String[] userActionArray = message.trim().split(" ");
                if(userActionArray[2].equals("TURN")) {
                    if(userActionArray[3].equals("left")) {
                        for(int i = 0; i < players.length; i++) {
                            String[] playerArray = players[i].split(",");
                            if(playerArray[0].equals(userActionArray[1])) {
                                //increment x coordinate of light cycle selected by -1
                                int newX = Integer.parseInt(playerArray[1]) - 1;
                                int newY = Integer.parseInt(playerArray[2]);
                                playerArray[1] = Integer.toString(newX);
                                playerArray[2] = Integer.toString(newY);
                                players[i] = playerArray[0] + "," + playerArray[1] + "," + playerArray[2];
                                System.out.println(players[i]);
                            }

                        }

                    } else if(userActionArray[3].equals("right")) {
                        for(int i = 0; i < players.length; i++) {
                            String[] playerArray = players[i].split(",");
                            if(playerArray[0].equals(userActionArray[1])) {
                                //increment x coordinate of light cycle selected by -1
                                int newX = Integer.parseInt(playerArray[1]) + 1;
                                int newY = Integer.parseInt(playerArray[2]);
                                playerArray[1] = Integer.toString(newX);
                                playerArray[2] = Integer.toString(newY);
                                players[i] = playerArray[0] + "," + playerArray[1] + "," + playerArray[2];
                                System.out.println(players[i]);
                            }

                        }

                    } else if(userActionArray[3].equals("up")) {
                        for(int i = 0; i < players.length; i++) {
                            String[] playerArray = players[i].split(",");
                            if(playerArray[0].equals(userActionArray[1])) {
                                //increment x coordinate of light cycle selected by -1
                                int newX = Integer.parseInt(playerArray[1]);
                                int newY = Integer.parseInt(playerArray[2]) - 1;
                                playerArray[1] = Integer.toString(newX);
                                playerArray[2] = Integer.toString(newY);
                                players[i] = playerArray[0] + "," + playerArray[1] + "," + playerArray[2];
                                System.out.println(players[i]);
                            }

                        }

                    } else if(userActionArray[3].equals("down")) {
                        for(int i = 0; i < players.length; i++) {
                            String[] playerArray = players[i].split(",");
                            if(playerArray[0].equals(userActionArray[1])) {
                                //increment x coordinate of light cycle selected by -1
                                int newX = Integer.parseInt(playerArray[1]);
                                int newY = Integer.parseInt(playerArray[2]) + 1;
                                playerArray[1] = Integer.toString(newX);
                                playerArray[2] = Integer.toString(newY);
                                players[i] = playerArray[0] + "," + playerArray[1] + "," + playerArray[2];
                                System.out.println(players[i]);
                            }

                        }
                    }

                    playerString = "";
                    for (String player : players) {
                        if(player != null) {
                            if (playerString.equals("")) {
                                playerString = player;
                            } else {
                                playerString = playerString + "-" + player;
                            }
                        }

                    }
                    messageNumber++;
                    if(playerNumber == MAXPLAYERS) {
                        System.out.println(playerString);
                        server.broadcast(playerString);
                        messageNumber = 0;
                    }
                }

            } else if (message.startsWith("REMOVE USER")) {
                String[] removeUserArray = message.trim().split(" ");
                for(int i = 0; i < players.length; i++) {
                     String[] playerArray = players[i].split(",");
                     if(playerArray[0].equals(removeUserArray[2])) {
                         players[i] = null;
                     }
                 }
                 playerString = "";
                 for (String player : players) {
                     if(player != null) {
                         if (playerString.equals("")) {
                             playerString = player;
                         } else {
                             playerString = playerString + "-" + player;
                         }
                     }
                 }

            } else if (message.startsWith("GRID SIZE")) {
                server.broadcast("GRID SIZE " + gridHeightString + " " + gridWidthString);

            } else if (message.startsWith("GAME STATE")) {
                if(gameState.equals(gameStates[2])) { //if playing
                    server.broadcast("PLAYING");
                } else if(gameState.equals(gameStates[1])) {
                    server.broadcast("WAITING FOR USERS");
                } else if(gameState.equals(gameStates[0])) { //if idle
                    server.broadcast("IDLE");
                } else if(gameState.equals(gameStates[3])) { //if gameover
                    for(String player : players) {
                        if(player != null) {
                            String[] winnerArray = player.split(",");
                            String winnerName = winnerArray[0];
                            server.broadcast("GAME OVER " + winnerName);
                        }
                    }

                }

            } else if (message.startsWith("SAVE SCORE")) {

            } else if (message.startsWith("GAME LEADERBOARD")) {

            }
        }

    }
}