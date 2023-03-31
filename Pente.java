import java.util.Random;

class Pente implements PenteInterface{

    private String[][] board = new String[19][19];
    private int computerCaptures = 0; // how many pairs has the computer captured 
    private int playerCaptures = 0; // how many pairs has the player captured
    private String[] columnLabels = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s"};
    private Random random = new Random();
    private int[] freeSpace = new int[2]; // {row, column}
    private int[] captivePieceOne = new int[2]; // {row, column}
    private int[] captivePieceTwo = new int[2]; // {row, column}

// Public Methods
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

    public String checkWin(){ // this does not check for diagonal wins
        if(findRowOfN(5, "O") || findColumnOfN(5, "O") || computerCaptures == 5){
            return "computer";
        } else if(findRowOfN(5, "X") || findColumnOfN(5, "X") || playerCaptures == 5){
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
            // take win
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(playerCanWin()){
            // block win
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(playerHasThree()){
            // block three
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(playerCanCapture()){
            // block capture
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(computerCanCapture()){
            // make capture
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
            this.board[this.captivePieceOne[0]][this.captivePieceOne[1]] = "-";
            this.board[this.captivePieceTwo[0]][this.captivePieceTwo[1]] = "-";
            computerCaptures++;
        } else if(computerHasThree()){
            // make four
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } // else {
            // check senarios O-O-O, OOO--, -OOO-, --OOO, OO-O-, O-OO-, -O-OO, -OO-O
            // -O-O-, OO---, O-O--, O--O-, O---O, -OO--, -O--O, --OO-, --O-O, ---OO
        //}
    }

    public boolean playerMove(String columnLetter, int row){
        // determine the row
        int column = 0;
        for(int i = 0; i < 19; i++){
            if(columnLabels[i].equals(columnLetter)) column = i;
        }

        //checkForCapture(row-1, column);

        if(this.board[row-1][column].equals("X") || this.board[row-1][column].equals("O")){
            return false;
        } else {
            this.board[row-1][column] = "X";
            return true;
        }
    }

// Private methods 
    private boolean playerCanCapture(){ 
        // check for two "O" in a row with an "X" beside it
        if(findRowOfN(2, "O")){
            return true;
        }
        return false;
    }

    private boolean computerCanCapture(){ 
        // check for two "X" in a row with an "O" beside it
        if(findRowOfN(2, "X")){
            return true;
        }else if(findColumnOfN(2, "X")){
            return true;
        } else if(findDiagonalDownOfN(2, "X")){
            return true;
        //} else if(findDiagonalUpOfN(4, "X")){
        //    return true;
        } else {
            return false;
        }
    }

    private boolean computerCanWin(){ 
        // check for four "O" in a row with a free space on one side
        return findRowOfN(4, "O"); 
    }

    private boolean playerCanWin(){ // non functional
        // check for four "X" in a row with a free space on one side
        if(findRowOfN(4, "X")){
            return true;
        } else if(findColumnOfN(4, "X")){
            return true;
        } else if(findDiagonalDownOfN(4, "X")){
            return true;
        //} else if(findDiagonalUpOfN(4, "X")){
        //    return true;
        } else {
            return false;
        }
    }

    private boolean playerHasThree(){ // non functional
        // check for three "X" in a row with free space on both sides
        if(findRowOfN(3, "X")){
            return true;
        } else if(findColumnOfN(3, "X")){
            return true;
        } else if(findDiagonalDownOfN(3, "X")){
            return true;
        //} else if(findDiagonalUpOfN(3, "X")){
        //    return true;
        } else {
            return false;
        }
    }

    private boolean computerHasThree(){ // non functional
        // check for three "O" in a row with free space on both sides
        return findRowOfN(3, "O"); 
    }

    private boolean findRowOfN(int n, String piece){
        this.freeSpace[0] = 100;
        for(int i = 0; i < 19; i++){
            if(rowOfN(getRow(i), n, piece)){
                this.freeSpace[0] = i;
                this.captivePieceOne[0] = i;
                this.captivePieceTwo[0] = i;
                return true;
            } 
        }

        return false;
    }

    private String[] getRow(int n){
        return this.board[n];
    }

    private boolean rowOfN(String[] row, int n, String piece){
        String opposingPiece;
        if(piece == "X"){
            opposingPiece = "O";
        } else {
            opposingPiece = "X";
        }

        int numInRow = 0;
        for(int i = 1; i < 19; i++){
            if(n == 5 && numInRow == n){ // win condition
                return true;
            } else if(n == 3 && numInRow == n && row[i] == "-" && row[i-(n+1)] == "-"){ // 3 in a row with a free space on both sides
                if(random.nextInt(2) == 0){
                    this.freeSpace[1] = i;
                } else {
                    this.freeSpace[1] = i-(n+1);
                }
                return true;
            } else if(n == 4 && numInRow == n && row[i] == "-"){ // four in a row with a free space to the right
                this.freeSpace[1] = i;
                return true;
            } else if(n == 4 && numInRow == n && row[i-(n+1)] == "-"){ // four in a row with a free space to the left
                this.freeSpace[1] = i-(n+1);
                return true;
            }  else if(n == 2 && numInRow == n && row[i] == "-" && row[i-(n+1)] == opposingPiece){ // two in a row with a free space to the right and the opposing peice to the left
                this.freeSpace[1] = i;
                this.captivePieceOne[1] = i-1;
                this.captivePieceTwo[1] = i-2;
                return true;
            } else if(n == 2 && numInRow == n && row[i] == opposingPiece && row[i-(n+1)] == "-"){ // two in a row with the opposing peice to the right and a free space to the left
                this.freeSpace[1] = i-(n+1);
                this.captivePieceOne[1] = i-1;
                this.captivePieceTwo[1] = i-2;
                return true;
            } else if(row[i] == piece){
                numInRow++;
            } else if(row[i] != piece){
                numInRow = 0;
            }
        }
        return false;
    }

    private boolean findColumnOfN(int n, String piece){ 
        this.freeSpace[0] = 100;
        for(int i = 0; i < 19; i++){
            if(columnOfN(getColumn(i), n, piece)){
                this.freeSpace[1] = i;
                this.captivePieceOne[1] = i;
                this.captivePieceTwo[1] = i;
                return true;
            } 
        }

        return false;
    }

    private String[] getColumn(int n){
        String[] column = new String[19];
        for(int i = 0; i < 19; i++){
            column[i] = this.board[i][n];
        }
        return column;
    }

    private boolean columnOfN(String[] column, int n, String piece){
        String opposingPiece;
        if(piece == "X"){
            opposingPiece = "O";
        } else {
            opposingPiece = "X";
        }

        int numInColumn = 0;
        for(int i = 1; i < 19; i++){
            if(n == 5 && numInColumn == n){ // win condition
                return true;
            } else if(n == 3 && numInColumn == n && column[i] == "-" && column[i-(n+1)] == "-"){ // 3 in a column with a free space on both sides
                if(random.nextInt(2) == 0){
                    this.freeSpace[0] = i;
                } else {
                    this.freeSpace[0] = i-(n+1);
                }
                return true;
            } else if(n == 4 && numInColumn == n && column[i] == "-"){ // 4 in a column with a free space on the bottom
                this.freeSpace[0] = i;
                return true;
            } else if(n == 4 && numInColumn == n && column[i-(n+1)] == "-"){ // 4 in a column with a free space on the top
                this.freeSpace[0] = i-(n+1);
                return true;
            }  else if(n == 2 && numInColumn == n && column[i] == "-" && column[i-(n+1)] == opposingPiece){ // two in a column with a free space on the bottom and the opposing peice on top
                this.freeSpace[0] = i;
                this.captivePieceOne[0] = i-1;
                this.captivePieceTwo[0] = i-2;
                return true;
            } else if(n == 2 && numInColumn == n && column[i] == opposingPiece && column[i-(n+1)] == "-"){ // two in a column with the opposing peice on the bottom and a free space on top
                this.freeSpace[0] = i-(n+1);
                this.captivePieceOne[0] = i-1;
                this.captivePieceTwo[0] = i-2;
                return true;
            }  else if(column[i] == piece){
                numInColumn++;
            } else if(column[i] != piece){
                numInColumn = 0;
            }
        }
        return false;
    }

    private boolean findDiagonalDownOfN(int n, String piece){ 
        this.freeSpace[0] = 100;
        for(int i = 0; i <= 28; i++){ // there are 28 possible diagonals of 5 or more
            int rowIdx;
            int columnIdx;
            if(i <= 14){
                rowIdx = 14-i;
                columnIdx = 0;
            } else {
                rowIdx = 0;
                columnIdx = i-14;
            }
            if(diagonalDownOfN(getDiagonalDown(i), n, piece, rowIdx, columnIdx)){
                return true;
            } 
        }

        return false;
    }

    // the downward diagonals are numbered in the following way
    // diagonals with less than 5 spots are not counted
    //    15 16 17 18 19 20 21 22 23 24 25 26 27 28
    // 14 -  -  -  -  -  -  -  -  -  -  -  -  -  -  -    
    // 13 -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  
    // 12 -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  
    // 11 -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  
    // 10 -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 9
    // 8
    // 7
    // 6
    // 5
    // 4
    // 3
    // 2
    // 1
    // 0  -
    //    -  -  
    //    -  -  -
    //    -  -  -  -
    //    -  -  -  -  -

    private String[] getDiagonalDown(int n){
        String[] diagonalDown;
        if(n <= 14){
            diagonalDown = new String[n+5];
            int i = 14-n;
            int j = 0;
            int h = 0;
            while(h < n+5){
                diagonalDown[h] = this.board[i][j];
                j++;
                i++;
                h++;
            }
        } else {
            diagonalDown = new String[28-n+5];
            int i = 0;
            int j = n-14;
            int h = 0;
            while(h < 28-n+5){
                diagonalDown[h] = this.board[i][j];
                j++;
                i++;
                h++;
            }
        } 
        return diagonalDown;
    }

    private boolean diagonalDownOfN(String[] diagonal, int n, String piece, int rowIdx, int columnIdx){
        String opposingPiece;
        if(piece == "X"){
            opposingPiece = "O";
        } else {
            opposingPiece = "X";
        }

        int numInDiagonal = 0;
        for(int i = 1; i < diagonal.length; i++){
            if(n == 5 && numInDiagonal == n){ // win condition
                return true;
            } else if(n == 3 && numInDiagonal == n && diagonal[i] == "-" && diagonal[i-4] == "-"){ // 3 in a downward diagonal with a free space on both sides
                if(random.nextInt(2) == 0){
                    this.freeSpace[0] = rowIdx+i+1;
                    this.freeSpace[1] = columnIdx+i;
                } else {
                    this.freeSpace[0] = rowIdx+i-4;
                    this.freeSpace[1] = columnIdx+i-4;
                }
                return true;
            } else if(n == 4 && numInDiagonal == n && diagonal[i] == "-"){ // 4 in a diagonal with a free space on the bottom right
                this.freeSpace[0] = rowIdx+i+1;
                this.freeSpace[1] = columnIdx+i;
                return true;
            } else if(n == 4 && numInDiagonal == n && diagonal[i-5] == "-"){ // 4 in a column with a free space on the top left
                this.freeSpace[0] = rowIdx+i-5;
                this.freeSpace[1] = columnIdx+i-5;
                return true;
            }  else if(n == 2 && numInDiagonal == n && diagonal[i] == "-" && diagonal[i-3] == opposingPiece){ // two in a column with a free space on the bottom right and the opposing peice on top left
                this.freeSpace[0] = rowIdx+i+1;
                this.freeSpace[1] = columnIdx+i;
                this.captivePieceOne[0] = rowIdx+i-1;
                this.captivePieceOne[1] = columnIdx+i-1;
                this.captivePieceTwo[0] = rowIdx+i-2;
                this.captivePieceTwo[1] = columnIdx+i-2;
                return true;
            } else if(n == 2 && numInDiagonal == n && diagonal[i] == opposingPiece && diagonal[i-3] == "-"){ // two in a column with the opposing peice on the bottom and a free space on top
                this.freeSpace[0] = rowIdx+i-3;
                this.freeSpace[1] = columnIdx+i-3;
                this.captivePieceOne[0] = rowIdx+i-1;
                this.captivePieceOne[1] = columnIdx+i-1;
                this.captivePieceTwo[0] = rowIdx+i-2;
                this.captivePieceTwo[1] = columnIdx+i-2;
                return true;
            }  else if(diagonal[i] == piece){
                numInDiagonal++;
            } else if(diagonal[i] != piece){
                numInDiagonal = 0;
            }
        }
        return false;
    }
}
