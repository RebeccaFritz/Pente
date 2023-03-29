import java.util.Random;

class Pente implements PenteInterface{

    private String[][] board = new String[19][19];
    private int computerCaptures = 0; // how many pairs has the computer captured 
    private int playerCaptures = 0; // how many pairs has the player captured
    private String[] columnLabels = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s"};
    private Random random = new Random();
    private int[][] piecePositions;
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
            // black three
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
        findRow(4, "O"); 
        if(this.piecePositions[0][0] != 100){
            return true;
        }  else {
            return false;
        }
    }

    private void findRow(int n, String piece){ // this is not currently checking if there is a space free beside the four
        // the piecePositions array looks as follows for n = 4
        // [][][][] column position
        // [][][][] row position
        this.piecePositions = new int[2][n];

        String opposingPiece;
        if(piece == "X"){
            opposingPiece = "O";
        } else {
            opposingPiece = "X";
        }

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 16; j++){
                if(j > 0){
                    if(this.board[i][j] == piece){ 
                        this.piecePositions[0][0] = j;
                        this.piecePositions[1][0] = i;
                    // if we have two in a row and are looking for two in a row, check if there is a free space to the left
                    } else if(this.board[i][j+1] == piece && n == 2 && this.board[i][j-1] != opposingPiece){
                        this.piecePositions[0][1] = j+1;
                        this.piecePositions[1][1] = i;
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j-1;
                        break;
                    // if we have two in a row and are looking for two in a row, check if there is a free space to the right
                    } else if(this.board[i][j+1] == piece && n == 2 && this.board[i][j+2] != opposingPiece){
                        this.piecePositions[0][1] = j+1;
                        this.piecePositions[1][1] = i;
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j+2;
                        break;
                    } else if(this.board[i][j+1] == piece){
                        this.piecePositions[0][1] = j+1;
                        this.piecePositions[1][1] = i;
                    // if we have three in a row and are looking for three in a row, check if there is a free space to the left
                    } else if(this.board[i][j+2] == piece && n == 3 && this.board[i][j-1] != opposingPiece){ //  !this.board[i][j+3].equals(opposingPiece)
                        this.piecePositions[0][2] = j+2;
                        this.piecePositions[1][2] = i;
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j-1;
                        break;
                    // if we have three in a row and are looking for three in a row, check if there is a free space to the right
                    } else if(this.board[i][j+2] == piece && n == 3 && this.board[i][j+3] != opposingPiece){
                        this.piecePositions[0][2] = j+2;
                        this.piecePositions[1][2] = i;
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j+3;
                        break;
                    } else if(this.board[i][j+2] == piece){
                        this.piecePositions[0][2] = j+2;
                        this.piecePositions[1][2] = i;
                    // if we have four in a row and are looking for four in a row, check if there is a free space to the left
                    } else if(this.board[i][j+3] == piece && n == 4 && this.board[i][j-1] != opposingPiece){ // !this.board[i][j+4].equals(opposingPiece)
                        this.piecePositions[0][3] = j+3;
                        this.piecePositions[1][3] = i;
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j-1;
                        break;
                    // if we have four in a row and are looking for four in a row, check if there is a free space to the right
                    } else if(this.board[i][j+3] == piece && n == 4 && this.board[i][j+4] != opposingPiece){
                        this.piecePositions[0][3] = j+3;
                        this.piecePositions[1][3] = i;
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j+4;
                        break;
                    } else {
                        // impossible postion value to indicate there are no rows of length n
                        this.piecePositions[0][0] = 100;
                    }
                } else {
                    if(this.board[i][j] == piece){ 
                        this.piecePositions[0][0] = j;
                        this.piecePositions[1][0] = i;
                    // if we have two in a row and are looking for two in a row, check if there is a free space to the right
                    } else if(this.board[i][j+1] == piece && n == 2 && this.board[i][j+2] != opposingPiece){
                        this.piecePositions[0][1] = j+1;
                        this.piecePositions[1][1] = i;
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j+2;
                        break;
                    } else if(this.board[i][j+1] == piece){
                        this.piecePositions[0][1] = j+1;
                        this.piecePositions[1][1] = i;
                    // if we have 3 in a row and are looking for 3 in a row, check if there is a free space to the right
                    } else if(this.board[i][j+2] == piece && n == 3 && this.board[i][j+3] != opposingPiece){
                        this.piecePositions[0][2] = j+2;
                        this.piecePositions[1][2] = i;
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j+3;
                        break;
                    } else if(this.board[i][j+2] == piece){
                        this.piecePositions[0][2] = j+2;
                        this.piecePositions[1][2] = i;
                    // if we have 4 in a row and are looking for 4 in a row, check if there is a space to the right
                    } else if(this.board[i][j+2] == piece && n == 4 && this.board[i][j+4] != opposingPiece){
                        this.piecePositions[0][3] = j+2;
                        this.piecePositions[1][3] = i;
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j+4;
                        break;
                    } else {
                        // impossible postion value to indicate there are no rows of length n
                        this.piecePositions[0][0] = 100; 
                    }
                }
            }
        }
    }

    private boolean playerCanWin(){ // non functional
        // check for four "X" in a row with a free space on one side
        return false;
    }

    private boolean playerHasThree(){ // non functional
        // check for three "X" in a row with free space on both sides
        findRow(3, "X"); 
        if(this.piecePositions[0][0] != 100){
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
