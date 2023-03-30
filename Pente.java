import java.util.Random;

class Pente implements PenteInterface{

    private String[][] board = new String[19][19];
    private int computerCaptures = 0; // how many pairs has the computer captured 
    private int playerCaptures = 0; // how many pairs has the player captured
    private String[] columnLabels = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s"};
    private Random random = new Random();
    private int[] freeSpace = new int[2]; 

// Methods
    public void startBoard(){
        // filling the board with the placeholder "-"
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                this.board[i][j] = "-";
            }
        }
    }

    public void printBoard(){
        // printing column labels
        System.out.println("   a b c d e f g h i j k l m n o p q r s");

        for(int i = 0; i < 19; i++){
            String row = "";

            // printing row labels
            int rowLabel = i+1;
            if(rowLabel < 10){
                row = row + rowLabel + "  ";
            } else {
                row = row + rowLabel + " ";
            }

            for(int j = 0; j < 19; j++){
                row = row + this.board[i][j] + " ";
            }

            System.out.println(row);
        }
    }

    public void printRules(){
        System.out.println("The goal of the game is to either align five or more tokens in a row in any vertical,\nhorizontal or diagonal direction or to make five captures.");
        System.out.println();
        System.out.println("A capture occurs when one player 'flanks' a pair of the other player's tokens:\nthus XOOX would result in X captureing a pair of O's pieces.");
        System.out.println();
        System.out.println("A player can place their token inbetween two flanking stones without being captured:\nthus if we had XO-X, O could place a token at - without penalty.");
        System.out.println();
        System.out.println("This version of Pente uses the Pro rule designed to mitigate the first player advantage.\nAccording to these rules the first player must place their token in the center of the board\nand the second player must place their token no more than three spots away.");
        System.out.println();
    }

    public boolean checkReady(String n){
        if(n.toLowerCase().equals("yes")){
            return true;
        } else {
            return false;
        }
    }

    public String checkWin(){ // this does not check for column wins, diagonal wins, or capture wins
        if(findRowOfN(5, "O")){
            return "computer";
        } else if(findRowOfN(5, "X")){
            return "player";
        } else { 
            return "continue";
        }
    }

    public void computerMove(int round){
        if(round == 1){
            this.board[9][9] = "O";
        } else if(round == 2){
            int[] rowOptions = {7,8,9,10,11};
            int[] columnOptions = {7,8,9,10,11};
            int rowPick = rowOptions[random.nextInt(rowOptions.length)];
            int columnPick = columnOptions[random.nextInt(columnOptions.length)];
            while(this.board[rowPick][columnPick].equals("X")){
                rowPick = rowOptions[random.nextInt(rowOptions.length)];
                columnPick = columnOptions[random.nextInt(columnOptions.length)];
            }
            this.board[rowPick][columnPick] = "O";
        } else if(computerCanWin()){
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(playerCanWin()){
            // block win
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(playerHasThree()){
            // block three
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        //} else if(playerCanCapture()){
            // block capture
        //} else if(computerCanCapture()){
            // make capture
        } else if(computerHasThree()){
            // make four
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        }
    }

    private boolean playerCanCapture(){ // non functional
        // check for two "O" in a row with an "X" beside it
        return false;
    }

    private boolean computerCanCapture(){ // non functional
        // check for two "X" in a row with an "O" beside it
        return false;
    }

    private boolean computerCanWin(){ // non functional
        // check for four "O" in a row with a free space on one side
        findRowOfN(4, "O"); 
        if(this.freeSpace[0] != 100){
            return true;
        }  else {
            return false;
        }
    }

    private boolean findRowOfN(int n, String piece){ // this is not working
        this.freeSpace[0] = 100;
        for(int i = 0; i < 19; i++){
            if(rowOfN(getRow(i), n, piece)){
                this.freeSpace[0] = i;
                return true;
            } 
        }

        return false;
    }

    private String[] getRow(int n){
        return this.board[n];
    }

    private boolean rowOfN(String[] row, int n, String piece){
        int numInRow = 0;
        for(int i = 1; i < 19; i++){
            if(n == 5 && numInRow == n){
                return true;
            } else if(n != 5 && numInRow == n && row[i] == "-" && row[i-(n+1)] == "-"){
                if(random.nextInt(2) == 0){
                    this.freeSpace[1] = i;
                } else {
                    this.freeSpace[1] = i-(n+1);
                }
                return true;
            } else if(n == 4 && numInRow == n && row[i] == "-"){
                this.freeSpace[1] = i;
                return true;
            } else if(n == 4 && numInRow == n && row[i-(n+1)] == "-"){
                this.freeSpace[1] = i-(n+1);
                return true;
            }  else if(row[i] == piece){
                numInRow++;
            } else if(row[i] != piece){
                numInRow = 0;
            }
        }
        return false;
    }

    private boolean playerCanWin(){ // non functional
        // check for four "X" in a row with a free space on one side
        return findRowOfN(4, "X"); 
    }

    private boolean playerHasThree(){ // non functional
        // check for three "X" in a row with free space on both sides
        return findRowOfN(3, "X"); 
    }

    private boolean computerHasThree(){ // non functional
        // check for three "O" in a row with free space on both sides
        return findRowOfN(3, "O"); 
    }

    public boolean playerMove(String columnLetter, int row){
        // determine the row
        int column = 0;
        for(int i = 0; i < 19; i++){
            if(columnLabels[i].equals(columnLetter)) column = i;
        }

        if(this.board[row-1][column].equals("X") || this.board[row-1][column].equals("O")){
            return false;
        } else {
            this.board[row-1][column] = "X";
            return true;
        }
    }
}
