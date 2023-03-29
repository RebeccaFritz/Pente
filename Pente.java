import java.util.Random;

class Pente implements PenteInterface{

    private String[][] board = new String[19][19];
    private int computerCaptures = 0; // how many pairs has the computer captured 
    private int playerCaptures = 0; // how many pairs has the player captured
    private String[] columnLabels = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s"};
    private Random random = new Random();
    private int[] freeSpace = new int[2]; // holds the row and column number of the free space

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

    public String checkWin(){
        return "continue";
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
        //} else if(computerCanWin()){
        //    this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        //} else if(playerCanWin()){
            // block win
        } else if(playerHasThree()){
            // block three
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        //} else if(playerCanCapture()){
            // block capture
        //} else if(computerCanCapture()){
            // make capture
        //} else if(computerHasThree()){
            // make four
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

    private void findRowOfN(int n, String piece){ // this is not working
        this.freeSpace[0] = 100;
        for(int i = 0; i < 19; i++){
            if(setOfN(getRow(i), n, piece)){
                this.freeSpace[0] = i;
                break;
            } 
        }
    }

    private String[] getRow(int n){
        return this.board[n];
    }

    private boolean setOfN(String[] row, int n, String piece){
        int numInRow = 0;
        for(int i = 0; i < 19; i++){
            if(numInRow == n && row[i] == "-"){
                freeSpace[1] = i;
                return true;
            } else if(row[i] == piece){
                numInRow++;
            } else if(row[i] != piece){
                numInRow = 0;
            }
        }
        return false;
    }

    private boolean playerCanWin(){ // non functional
        // check for four "X" in a row with a free space on one side
        return false;
    }

    private boolean playerHasThree(){ // non functional
        // check for three "X" in a row with free space on both sides
        findRowOfN(3, "X"); 
        if(this.freeSpace[0] != 100){
            return true;
        }  else {
            return false;
        }
    }

    private boolean computerHasThree(){ // non functional
        // check for three "O" in a row with free space on both sides
        return false;
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
