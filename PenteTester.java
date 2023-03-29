import java.util.Scanner;

class PenteTester {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        Pente game = new Pente();

        int round = 1; // which game round are we on

        game.printRules();
        game.startBoard();
        game.printBoard();

        int row;
        String column;

        System.out.println("When you are ready to play type 'yes'.");
        if(game.checkReady(scan.nextLine())){
            while(game.checkWin() == "continue"){
                game.computerMove(round);
                game.printBoard();
                System.out.println();

                System.out.println("The computer just went. Now it is your turn.");
                System.out.println("Enter a letter corresponding to the column in which you want to place your piece.");
                column = scan.next();
                System.out.println("Enter a number corresponding to the row in which you want to place your piece.");
                row = scan.nextInt();
                while(!game.playerMove(column, row)){
                    System.out.println("There is already a piece there. Try again");
                    System.out.println("Enter a letter:");
                    column = scan.next();
                    System.out.println("Enter a number:");
                    row = scan.nextInt();
                }
                game.printBoard();

                round++;
            }
        }
    }
}