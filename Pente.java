import java.util.Random;

class Pente implements PenteInterface{

    private String[][] board = new String[19][19];
    public int computerCaptures = 0; // how many pairs has the computer captured 
    public int playerCaptures = 0; // how many pairs has the player captured
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
        System.out.println("");
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
        if(findRowOfN(5, "O") || findColumnOfN(5, "O") || findDiagonalDownOfN(5, "O") || findDiagonalUpOfN(5, "O") || computerCaptures == 5){
            return "computer";
        } else if(findRowOfN(5, "X") || findColumnOfN(5, "X") || findDiagonalDownOfN(5, "X") || findDiagonalUpOfN(5, "X") ||playerCaptures == 5){
            return "player";
        } else { 
            return "continue";
        }
    }

    public void computerMove(int round){
        if(round == 1){
            this.board[9][9] = "O";
        } else if(hasFourInFive("O") || hasSpecialWinScenario("O")){
            // check if the computer has four in a row
            // OR if the computer can satify a special win scenario
            // if true, take win
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
            checkIfCaptured(this.freeSpace[0], this.freeSpace[1], "O");
        } else if(hasFourInFive("X") || hasSpecialWinScenario("X")){
            // check if the player has four in a row
            // OR if the player can satify a special win scenario
            // if true, block win
            // check if a block via capture can be made, particularly if neither side is blocked NOT IMPLEMENTED
            // if the player has four uninterupted pieces, only block if one side is already blocked 
            // if the pieces are interrupted block within the interuption
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
            checkIfCaptured(this.freeSpace[0], this.freeSpace[1], "O");
        } else if(computerCanMakeFourUnblockedInARow()){
                // check if the computer has can make four unblocked in a row
                // if so, make four
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(hasThreeInFive("X")){
            // check if the player has three in a set of five
            // if unblocked, block three
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
            checkIfCaptured(this.freeSpace[0], this.freeSpace[1], "O");
        } else if(hasIntersectingTwos("O")){
            /* checks scenarios as such:
            * -O-O-
            * -OO--
            * -----
            */
            // create intersection
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(hasIntersectingTwos("X")){
            /* checks scenarios as such:
            * -O-O-
            * -OO--
            * -----
            */
            // block intersection
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(playerCanCapture()){
            // block capture
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
            checkIfCaptured(this.freeSpace[0], this.freeSpace[1], "O");
        } else if(computerCanCapture()){
            // make capture
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
            this.board[this.captivePieceOne[0]][this.captivePieceOne[1]] = "-";
            this.board[this.captivePieceTwo[0]][this.captivePieceTwo[1]] = "-";
            computerCaptures++;
        } else if(hasThreeInFive("O")){
                // check if the computer has three in a set of five
                // make four
                this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(computerHasTwo()){
            // check if the computer has two in a row
            // if not blocked, make three
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else if(computerHasOne()){
            // check if the computer has one piece
            // if not blocked, make two
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        } else {
            // if there is an empty five by five square, place a piece in the middle
            for(int i = 2; i < 18; i++){
                for(int j = 2; j < 18; j++){
                    if(isEmptyFivebyFive(i, j)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
                    }
                }
            }

            // if there is a space with 4 blank spaces around it then choose that space 
            for(int i = 0; i < 20; i++){
                for(int j = 0; j < 20; j++){
                    if(isEmptySetOfFive(i, j)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
                    }
                }
            }
            
            // otherwise choose a random empty space
            int randomRow = this.random.nextInt(20);
            int randomColumn = this.random.nextInt(20);
            while(this.board[randomRow][randomColumn] != "-"){
                randomRow = this.random.nextInt(20);
                randomColumn = this.random.nextInt(20);
            }   
            this.freeSpace[0] = randomRow;
            this.freeSpace[1] = randomColumn;
            this.board[this.freeSpace[0]][this.freeSpace[1]] = "O";
        }
    }

    public boolean playerMove(String columnLetter, int rowNumber){
        int row = rowNumber-1;
        // determine the column
        int column = 0;
        for(int i = 0; i < 19; i++){
            if(this.columnLabels[i].equals(columnLetter)) column = i;
        }

        if(this.board[row][column].equals("X") || this.board[row][column].equals("O")){
            return false;
        } else {
            this.board[row][column] = "X";
            checkIfCaptured(row, column, "X");
            return true;
        }
    }

// Private methods 
    private void checkIfCaptured(int row, int column, String piece){ // this does not work
        String opposingPiece;
        if(piece == "X"){
            opposingPiece = "O";
        } else {
            opposingPiece = "X";
        }

        if(column < 16 && this.board[row][column+1] == opposingPiece && this.board[row][column+2] == opposingPiece && this.board[row][column+3] == piece){ // check for row capture -OOX
            this.board[row][column+1] = "-";
            this.board[row][column+2] = "-";
            this.playerCaptures++;
        } else if(column > 2 && this.board[row][column-3] == piece && this.board[row][column-2] == opposingPiece && this.board[row][column-1] == opposingPiece){ // check for row capture XOO-
            this.board[row][column-1] = "-";
            this.board[row][column-2] = "-";
            this.playerCaptures++;
        } else if(row < 16 && this.board[row+1][column] == opposingPiece && this.board[row+2][column] == opposingPiece && this.board[row+3][column] == piece){ // check for column capture -OOX
            this.board[row+1][column] = "-";
            this.board[row+2][column] = "-";
            this.playerCaptures++;
        } else if(row > 2 && this.board[row-3][column] == piece && this.board[row-2][column] == opposingPiece && this.board[row-1][column] == opposingPiece){ // check for column capture XOO-
            this.board[row-1][column] = "-";
            this.board[row-2][column] = "-";
            this.playerCaptures++;
        } else if(column < 16 && row < 16 && this.board[row+1][column+1] == opposingPiece && this.board[row+2][column+2] == opposingPiece && this.board[row+3][column+3] == piece){ // check for diagonalDown capture -OOX
            this.board[row+1][column+1] = "-";
            this.board[row+2][column+2] = "-";
            this.playerCaptures++;
        } else if(column > 2 && row > 2 && this.board[row-3][column-3] == piece && this.board[row-2][column-2] == opposingPiece && this.board[row-1][column-1] == opposingPiece){ // check for diagonalDown capture XOO-
            this.board[row-1][column-1] = "-";
            this.board[row-2][column-2] = "-";
            this.playerCaptures++;
        } else if(column < 16 && row > 2 && this.board[row-1][column+1] == opposingPiece && this.board[row-2][column+2] == opposingPiece && this.board[row-3][column+3] == piece){ // check for diagonalUp capture -OOX
            this.board[row-1][column+1] = "-";
            this.board[row-2][column+2] = "-";
            this.playerCaptures++;
        } else if(column > 2 && row < 16 && this.board[row+3][column-3] == piece && this.board[row+2][column-2] == opposingPiece && this.board[row+1][column-1] == opposingPiece){ // check for diagonalUp capture XOO-
            this.board[row+1][column-1] = "-";
            this.board[row+2][column-2] = "-";
            this.playerCaptures++;
        }
    }

    private boolean playerCanCapture(){ 
        // check for two "O" in a row with an "X" beside it
        if(findRowOfN(2, "O")){
            return true;
        }else if(findColumnOfN(2, "O")){
            return true;
        } else if(findDiagonalDownOfN(2, "O")){
            return true;
        } else if(findDiagonalUpOfN(4, "O")){
            return true;
        } else {
            return false;
        }
    }

    private boolean computerCanCapture(){ 
        // check for two "X" in a row with an "O" beside it
        if(findRowOfN(2, "X")){
            return true;
        }else if(findColumnOfN(2, "X")){
            return true;
        } else if(findDiagonalDownOfN(2, "X")){
            return true;
        } else if(findDiagonalUpOfN(4, "X")){
            return true;
        } else {
            return false;
        }
    }

    private boolean hasSpecialWinScenario(String piece){
        if(specialWinScenario("row", piece)){
            return true;
        } else if(specialWinScenario("column", piece)){
            return true;
        } else if(specialWinScenario("diagonalDown", piece)){
            return true;
        } else if(specialWinScenario("diagonalUp", piece)){
            return true;
        } else {
            return false;
        }
    }

    private boolean specialWinScenario(String type, String piece){ 
        String[] fullSet;
        int rowIdx;
        int columnIdx;
        if(type == "row"){
            for(int j = 0; j < 19; j++){
                fullSet = getRow(j);
                this.freeSpace[0] = j;

                for(int i = 1; i < 18; i++){
                    this.freeSpace[1] = i;
                    if(i > 3 && i < 16 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check senario -OOO-OOO-
                        return true;
                    } else if (i == 3 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check senario OOO-OOO- where the left is the wall
                        return true;
                    } else if (i == 16 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check senario -OOO-OOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < 15 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OOOO-OOOO-
                        return true;
                    } else if(i == 4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OOOO-OOOO- where the left is the wall
                        return true;
                    } else if(i == 15 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OOOO-OOOO where the right is the wall
                        return true;
                    } else if(i > 3 && i < 15 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OOO-OOOO-
                        return true;
                    } else if(i == 3 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OOO-OOOO- where the left is the wall
                        return true;
                    } else if(i == 15 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OOO-OOOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < 16 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario -OOOO-OOO-
                        return true;
                    } else if(i == 4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario OOOO-OOO- where the left is the wall
                        return true;
                    } else if(i == 16 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check scenario -OOOO-OOO- where the right is the wall
                        return true;
                    } else if(i > 3 && i < 17 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario -OOO-OO-
                        return true;
                    } else if(i == 3 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario OOO-OO- where the left is the wall
                        return true;
                    } else if(i == 17 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece){ // check scenario -OOO-OO where the right is the wall
                        return true;
                    } else if(i > 2 && i < 16 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario -OO-OOO-
                        return true;
                    } else if(i == 2 && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario OO-OOO- where the left is the wall
                        return true;
                    } else if(i == 16 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check scenario -OO-OOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < 17 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario -OOOO-OO-
                        return true;
                    } else if(i == 4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario OOOO-OO- where the left is the wall
                        return true;
                    } else if(i == 17 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece){ // check scenario -OOOO-OO where the right is the wall
                        return true;
                    } else if(i > 2 && i < 15 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OO-OOOO-
                        return true;
                    } else if(i == 2 && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OO-OOOO- where the left is the wall
                        return true;
                    } else if(i == 15 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OO-OOOO where the right is the wall
                        return true;
                    } else if(i > 1 && i < 15 && fullSet[i-2] != piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -O-OOOO-
                        return true;
                    } else if(i == 1 && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario O-OOOO- where the left is the wall
                        return true;
                    } else if(i == 15 && fullSet[i-2] != piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -O-OOOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < 18 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] != piece){ // check scenario -OOOO-O-
                        return true;
                    } else if(i == 4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] != piece){ // check scenario OOOO-O- where the left is the wall
                        return true;
                    } else if(i == 18 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece){ // check scenario -OOOO-O where the right is the wall
                        return true;
                    }
                }
            }
            return false;
        } else if(type == "column"){
            for(int j = 0; j < 19; j++){
                fullSet = getColumn(j);
                this.freeSpace[1] = j;

                for(int i = 1; i < 18; i++){
                    this.freeSpace[0] = i;
                    if(i > 3 && i < 16 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check senario -OOO-OOO-
                        return true;
                    } else if (i == 3 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check senario OOO-OOO- where the left is the wall
                        return true;
                    } else if (i == 16 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check senario -OOO-OOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < 15 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OOOO-OOOO-
                        return true;
                    } else if(i == 4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OOOO-OOOO- where the left is the wall
                        return true;
                    } else if(i == 15 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OOOO-OOOO where the right is the wall
                        return true;
                    } else if(i > 3 && i < 15 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OOO-OOOO-
                        return true;
                    } else if(i == 3 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OOO-OOOO- where the left is the wall
                        return true;
                    } else if(i == 15 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OOO-OOOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < 16 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario -OOOO-OOO-
                        return true;
                    } else if(i == 4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario OOOO-OOO- where the left is the wall
                        return true;
                    } else if(i == 16 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check scenario -OOOO-OOO- where the right is the wall
                        return true;
                    } else if(i > 3 && i < 17 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario -OOO-OO-
                        return true;
                    } else if(i == 3 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario OOO-OO- where the left is the wall
                        return true;
                    } else if(i == 17 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece){ // check scenario -OOO-OO where the right is the wall
                        return true;
                    } else if(i > 2 && i < 16 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario -OO-OOO-
                        return true;
                    } else if(i == 2 && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario OO-OOO- where the left is the wall
                        return true;
                    } else if(i == 16 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check scenario -OO-OOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < 17 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario -OOOO-OO-
                        return true;
                    } else if(i == 4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario OOOO-OO- where the left is the wall
                        return true;
                    } else if(i == 17 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece){ // check scenario -OOOO-OO where the right is the wall
                        return true;
                    } else if(i > 2 && i < 15 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OO-OOOO-
                        return true;
                    } else if(i == 2 && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OO-OOOO- where the left is the wall
                        return true;
                    } else if(i == 15 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OO-OOOO where the right is the wall
                        return true;
                    } else if(i > 1 && i < 15 && fullSet[i-2] != piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -O-OOOO-
                        return true;
                    } else if(i == 1 && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario O-OOOO- where the left is the wall
                        return true;
                    } else if(i == 15 && fullSet[i-2] != piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -O-OOOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < 18 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] != piece){ // check scenario -OOOO-O-
                        return true;
                    } else if(i == 4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] != piece){ // check scenario OOOO-O- where the left is the wall
                        return true;
                    } else if(i == 18 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece){ // check scenario -OOOO-O where the right is the wall
                        return true;
                    } 
                }
            } 
            return false;   
        } else if(type == "diagonalDown"){ 
            for(int j = 0; j < 30; j++){
                fullSet = getDiagonalDown(j);
            
                if(j <= 14){
                    rowIdx = 14-j;
                    columnIdx = 0;
                } else {
                    rowIdx = 0;
                    columnIdx = j-14;
                }

                for(int i = 1; i < fullSet.length; i++){ 
                    this.freeSpace[0] = rowIdx+i;
                    this.freeSpace[1] = columnIdx+i;

                    if(i > 3 && i < fullSet.length-4 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check senario -OOO-OOO-
                        return true;
                    } else if (i == 3 && i < fullSet.length-4 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check senario OOO-OOO- where the left is the wall
                        return true;
                    } else if (i > 3 && i == fullSet.length-4 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check senario -OOO-OOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < fullSet.length-5 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OOOO-OOOO-
                        return true;
                    } else if(i == 4 && i < fullSet.length-5 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OOOO-OOOO- where the left is the wall
                        return true;
                    } else if(i > 4 && i == fullSet.length-5 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OOOO-OOOO where the right is the wall
                        return true;
                    } else if(i > 3 && i < fullSet.length-5 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OOO-OOOO-
                        return true;
                    } else if(i == 3 && i < fullSet.length-5  && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OOO-OOOO- where the left is the wall
                        return true;
                    } else if(i > 3 && i == fullSet.length-5 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OOO-OOOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < fullSet.length-4 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario -OOOO-OOO-
                        return true;
                    } else if(i == 4 && i < fullSet.length-4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario OOOO-OOO- where the left is the wall
                        return true;
                    } else if(i > 4 && i == fullSet.length-4 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check scenario -OOOO-OOO- where the right is the wall
                        return true;
                    } else if(i > 3 && i < fullSet.length-3 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario -OOO-OO-
                        return true;
                    } else if(i == 3 && i < fullSet.length-3 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario OOO-OO- where the left is the wall
                        return true;
                    } else if(i > 3 && i == fullSet.length-3 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece){ // check scenario -OOO-OO where the right is the wall
                        return true;
                    } else if(i > 2 && i < fullSet.length-4 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario -OO-OOO-
                        return true;
                    } else if(i == 2 && i < fullSet.length-4 && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario OO-OOO- where the left is the wall
                        return true;
                    } else if(i > 2 && i == fullSet.length-4 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check scenario -OO-OOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < fullSet.length-3 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario -OOOO-OO-
                        return true;
                    } else if(i == 4 && i < fullSet.length-3 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario OOOO-OO- where the left is the wall
                        return true;
                    } else if(i > 4 && i == fullSet.length-3 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece){ // check scenario -OOOO-OO where the right is the wall
                        return true;
                    } else if(i > 2 && i < fullSet.length-5 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OO-OOOO-
                        return true;
                    } else if(i == 2 && i < fullSet.length-5 && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OO-OOOO- where the left is the wall
                        return true;
                    } else if(i > 2 && i == fullSet.length-5 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OO-OOOO where the right is the wall
                        return true;
                    } else if(i > 1 && i < fullSet.length-5 && fullSet[i-2] != piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -O-OOOO-
                        return true;
                    } else if(i == 1 && i < fullSet.length-5 && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario O-OOOO- where the left is the wall
                        return true;
                    } else if(i > 1 && i == fullSet.length-5 && fullSet[i-2] != piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -O-OOOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < fullSet.length-2 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] != piece){ // check scenario -OOOO-O-
                        return true;
                    } else if(i == 4 && i < fullSet.length-2 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] != piece){ // check scenario OOOO-O- where the left is the wall
                        return true;
                    } else if(i > 4 && i == fullSet.length-2 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece){ // check scenario -OOOO-O where the right is the wall
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "diagonalUp"){ 
            for(int j = 0; j < 30; j++){
                fullSet = getDiagonalUp(j);
            
                if(j <= 14){
                    rowIdx = j+4;
                    columnIdx = 0;
                } else {
                    rowIdx = 18;
                    columnIdx = j-14;
                }
                
                for(int i = 1; i < fullSet.length; i++){ 
                    this.freeSpace[0] = rowIdx-i;
                    this.freeSpace[1] = columnIdx+i;

                    if(i > 3 && i < fullSet.length-4 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check senario -OOO-OOO-
                        return true;
                    } else if (i == 3 && i < fullSet.length-4 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check senario OOO-OOO- where the left is the wall
                        return true;
                    } else if (i > 3 && i == fullSet.length-4 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check senario -OOO-OOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < fullSet.length-5 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OOOO-OOOO-
                        return true;
                    } else if(i == 4 && i < fullSet.length-5 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OOOO-OOOO- where the left is the wall
                        return true;
                    } else if(i > 4 && i == fullSet.length-5 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OOOO-OOOO where the right is the wall
                        return true;
                    } else if(i > 3 && i < fullSet.length-5 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OOO-OOOO-
                        return true;
                    } else if(i == 3 && i < fullSet.length-5  && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OOO-OOOO- where the left is the wall
                        return true;
                    } else if(i > 3 && i == fullSet.length-5 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OOO-OOOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < fullSet.length-4 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario -OOOO-OOO-
                        return true;
                    } else if(i == 4 && i < fullSet.length-4 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario OOOO-OOO- where the left is the wall
                        return true;
                    } else if(i > 4 && i == fullSet.length-4 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check scenario -OOOO-OOO- where the right is the wall
                        return true;
                    } else if(i > 3 && i < fullSet.length-3 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario -OOO-OO-
                        return true;
                    } else if(i == 3 && i < fullSet.length-3 && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario OOO-OO- where the left is the wall
                        return true;
                    } else if(i > 3 && i == fullSet.length-3 && fullSet[i-4] != piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece){ // check scenario -OOO-OO where the right is the wall
                        return true;
                    } else if(i > 2 && i < fullSet.length-4 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario -OO-OOO-
                        return true;
                    } else if(i == 2 && i < fullSet.length-4 && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] != piece){ // check scenario OO-OOO- where the left is the wall
                        return true;
                    } else if(i > 2 && i == fullSet.length-4 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece){ // check scenario -OO-OOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < fullSet.length-3 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario -OOOO-OO-
                        return true;
                    } else if(i == 4 && i < fullSet.length-3 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] != piece){ // check scenario OOOO-OO- where the left is the wall
                        return true;
                    } else if(i > 4 && i == fullSet.length-3 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece){ // check scenario -OOOO-OO where the right is the wall
                        return true;
                    } else if(i > 2 && i < fullSet.length-5 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -OO-OOOO-
                        return true;
                    } else if(i == 2 && i < fullSet.length-5 && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario OO-OOOO- where the left is the wall
                        return true;
                    } else if(i > 2 && i == fullSet.length-5 && fullSet[i-3] != piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -OO-OOOO where the right is the wall
                        return true;
                    } else if(i > 1 && i < fullSet.length-5 && fullSet[i-2] != piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario -O-OOOO-
                        return true;
                    } else if(i == 1 && i < fullSet.length-5 && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check scenario O-OOOO- where the left is the wall
                        return true;
                    } else if(i > 1 && i == fullSet.length-5 && fullSet[i-2] != piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check scenario -O-OOOO where the right is the wall
                        return true;
                    } else if(i > 4 && i < fullSet.length-2 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] != piece){ // check scenario -OOOO-O-
                        return true;
                    } else if(i == 4 && i < fullSet.length-2 && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] != piece){ // check scenario OOOO-O- where the left is the wall
                        return true;
                    } else if(i > 4 && i == fullSet.length-2 && fullSet[i-5] != piece && fullSet[i-4] == piece && fullSet[i-3] == piece && fullSet[i-2] == piece && fullSet[i-1] == piece && fullSet[i] == "-" && fullSet[i+1] == piece){ // check scenario -OOOO-O where the right is the wall
                        return true;
                    } 
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean hasFourInFive(String piece){
        // checking for 4 in a row with one side free
        if(findRowOfN(4, piece)){
            return true;
        } else if(findColumnOfN(4, piece)){
            return true;
        } else if(findDiagonalDownOfN(4, piece)){
            return true;
        } else if(findDiagonalUpOfN(4, piece)){
            return true;
        // checking for four in a set of five
        } else if(setOfFourInFive("row", piece)){
            return true;
        } else if(setOfFourInFive("column", piece)){
            return true;
        } else if(setOfFourInFive("diagonalDown", piece)){
            return true;
        } else if(setOfFourInFive("diagonalUp", piece)){
            return true;
        } else {
            return false;
        }
    }

    private boolean setOfFourInFive(String type, String piece){ 
        String[] fullSet;
        int rowIdx;
        int columnIdx;
        if(type == "row"){
            for(int j = 0; j < 19; j++){
                fullSet = getRow(j);
                this.freeSpace[0] = j;

                for(int i = 0; i < 14; i++){
                    if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] =="-" && fullSet[i+4] == piece && fullSet[i+5] == piece){ // check senario -OO-OO where the right is the wall
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario OO-OO- where the left is the wall
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(i < 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -OO-OO-
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece){ // check senario -OOO-O where the right is the wall
                        this.freeSpace[1] = i+4;
                        return true;
                    } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario OOO-O- where the left is the wall
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(i < 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -OOO-O-
                        this.freeSpace[1] = i+4;
                        return true;
                    } else if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece){ // check senario -O-OOO right is the wall
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario O-OOO- left is the wall
                        this.freeSpace[1] = i+1;
                        return true;
                    } else if(i < 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -O-OOO-
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(i < 14 && piece == "O" && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-" && fullSet[i+6] != piece){ // check senario -OOOO-- 
                        this.freeSpace[1] = i+5;
                        return true;
                    }  else if(i < 14 && piece == "O" && fullSet[i] != piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario --OOOO- 
                        this.freeSpace[1] = i+1;
                        return true;
                    } else if((((i == 0 && piece == "O") || (i == 14 && piece == "O"))) && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OOOO- 
                        int[][] spots = {{freeSpace[0], i}, {freeSpace[0], i+5}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "column"){
            for(int j = 0; j < 19; j++){
                fullSet = getColumn(j);
                this.freeSpace[1] = j;

                for(int i = 0; i < 14; i++){
                    if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] =="-" && fullSet[i+4] == piece && fullSet[i+5] == piece){ // check senario -OO-OO where the top is the wall
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario OO-OO- where the bottom is the wall
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(i < 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -OO-OO-
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece){ // check senario -OOO-O where the top is the wall
                        this.freeSpace[0] = i+4;
                        return true;
                    } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario OOO-O- where the bottom is the wall
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(i < 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -OOO-O-
                        this.freeSpace[0] = i+4;
                        return true;
                    } else if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece){ // check senario -O-OOO top is the wall
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario O-OOO- bottom is the wall
                        this.freeSpace[0] = i+1;
                        return true;
                    } else if(i < 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -O-OOO-
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(i < 14 && piece == "O" && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-" && fullSet[i+6] != piece){ // check senario -OOOO-- 
                        this.freeSpace[0] = i+5; 
                        return true;
                    } else if(i < 14 && piece == "O" && fullSet[i] != piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario --OOOO-
                        this.freeSpace[0] = i+1;
                        return true;
                    } else if((((i == 0 && piece == "O") || (i == 14 && piece == "O"))) && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OOOO- 
                        int[][] spots = {{i, freeSpace[1]}, {i+5, freeSpace[1]}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    }
                }
            } 
            return false;   
        } else if(type == "diagonalDown"){ 
            for(int j = 0; j < 30; j++){
                fullSet = getDiagonalDown(j);
            
                if(j <= 14){
                    rowIdx = 14-j;
                    columnIdx = 0;
                } else {
                    rowIdx = 0;
                    columnIdx = j-14;
                }

                if(fullSet.length == 5 || fullSet.length == 6){
                    if(fullSet[0] == piece && fullSet[1] == piece && fullSet[2] == "-" && fullSet[3] == piece && fullSet[4] == piece){ // check senario OO-OO
                        this.freeSpace[0] = rowIdx+2;
                        this.freeSpace[1] = columnIdx+2;
                        return true;
                    } else if(fullSet[0] == piece && fullSet[1] == piece && fullSet[2] == piece && fullSet[3] == "-" && fullSet[4] == piece){ // check senario OOO-O
                        this.freeSpace[0] = rowIdx+3;
                        this.freeSpace[1] = columnIdx+3;
                        return true;
                    } else if(fullSet[0] == piece && fullSet[1] == "-" && fullSet[2] == piece && fullSet[3] == piece && fullSet[4] == piece){ // check senario O-OOO
                        this.freeSpace[0] = rowIdx+1;
                        this.freeSpace[1] = columnIdx+1;
                        return true;
                    } 
                } else {
                    for(int i = 0; i < fullSet.length-5; i++){ 
                        if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] =="-" && fullSet[i+4] == piece && fullSet[i+5] == piece){ // check senario -OO-OO where the bottom right is the wall
                            this.freeSpace[0] = rowIdx+i+3;
                            this.freeSpace[1] = columnIdx+i+3;
                            return true;
                        } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario OO-OO- where the top left is the wall
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                            return true;
                        } else if(i < fullSet.length-6 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -OO-OO-
                            this.freeSpace[0] = rowIdx+i+3;
                            this.freeSpace[1] = columnIdx+i+3;
                            return true;
                        } else if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece){ // check senario -OOO-O where the bottom right is the wall
                            this.freeSpace[0] = rowIdx+i+4;
                            this.freeSpace[1] = columnIdx+i+4;
                            return true;
                        } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario OOO-O- where the top left is the wall
                            this.freeSpace[0] = rowIdx+i+3;
                            this.freeSpace[1] = columnIdx+i+3;
                            return true;
                        } else if(i < fullSet.length-6 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -OOO-O-
                            this.freeSpace[0] = rowIdx+i+4;
                            this.freeSpace[1] = columnIdx+i+4;
                            return true;
                        } else if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece){ // check senario -O-OOO bottom right is the wall
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                            return true;
                        } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario O-OOO- top left is the wall
                            this.freeSpace[0] = rowIdx+i+1;
                            this.freeSpace[1] = columnIdx+i+1;
                            return true;
                        } else if(i < fullSet.length-6 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -O-OOO-
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                            return true;
                        } else if(i < fullSet.length-6 && piece == "O" && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-" && fullSet[i+6] != piece){ // check senario -OOOO-- 
                            this.freeSpace[0] = rowIdx+i+5;
                            this.freeSpace[1] = columnIdx+i+5; 
                            return true;
                        } else if(i < fullSet.length-6 && piece == "O" && fullSet[i] != piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario --OOOO-
                            this.freeSpace[0] = rowIdx+i+1;
                            this.freeSpace[1] = columnIdx+i+1;
                            return true;
                        } else if((((i == 0 && piece == "O") || (i == 14 && piece == "O"))) && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OOOO- 
                            int[][] spots = {{rowIdx+i, columnIdx+i}, {rowIdx+i+5, columnIdx+i+5}};
                            this.freeSpace = pickOptimalSpot(spots, piece);
                            return true;
                        } 
                    }
                }
            }
            return false;
        } else if(type == "diagonalUp"){ 
            for(int j = 0; j < 30; j++){
                fullSet = getDiagonalUp(j);
            
                if(j <= 14){
                    rowIdx = j+4;
                    columnIdx = 0;
                } else {
                    rowIdx = 18;
                    columnIdx = j-14;
                }
                
                if(fullSet.length == 5 || fullSet.length == 6){
                    if(fullSet[0] == piece && fullSet[1] == piece && fullSet[2] == "-" && fullSet[3] == piece && fullSet[4] == piece){ // check senario OO-OO
                        this.freeSpace[0] = rowIdx-2;
                        this.freeSpace[1] = columnIdx+2;
                        return true;
                    } else if(fullSet[0] == piece && fullSet[1] == piece && fullSet[2] == piece && fullSet[3] == "-" && fullSet[4] == piece){ // check senario OOO-O
                        this.freeSpace[0] = rowIdx-3;
                        this.freeSpace[1] = columnIdx+3;
                        return true;
                    } else if(fullSet[0] == piece && fullSet[1] == "-" && fullSet[2] == piece && fullSet[3] == piece && fullSet[4] == piece){ // check senario O-OOO
                        this.freeSpace[0] = rowIdx-1;
                        this.freeSpace[1] = columnIdx+1;
                        return true;
                    } 
                } else {
                    for(int i = 0; i < fullSet.length-5; i++){ 
                        if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == piece){ // check senario -OO-OO where the top right is the wall
                            this.freeSpace[0] = rowIdx-(i+3);
                            this.freeSpace[1] = columnIdx+i+3;
                            return true;
                        } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario OO-OO- where the bottom left is the wall
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                            return true;
                        } else if(i < fullSet.length-6 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -OO-OO-
                            this.freeSpace[0] = rowIdx-(i+3);
                            this.freeSpace[1] = columnIdx+i+3;
                            return true;
                        } else if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece){ // check senario -OOO-O where the top right is the wall
                            this.freeSpace[0] = rowIdx-(i+4);
                            this.freeSpace[1] = columnIdx+i+4;
                            return true;
                        } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario OOO-O- where the bottom left is the wall
                            this.freeSpace[0] = rowIdx-(i+3);
                            this.freeSpace[1] = columnIdx+i+3;
                            return true;
                        } else if(i < fullSet.length-6 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -OOO-O-
                            this.freeSpace[0] = rowIdx-(i+4);
                            this.freeSpace[1] = columnIdx+i+4;
                            return true;
                        } else if(i == 14 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece){ // check senario -O-OOO top right is the wall
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                            return true;
                        } else if(i == 0 && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] != piece){ // check senario O-OOO- bottom left is the wall
                            this.freeSpace[0] = rowIdx-(i+1);
                            this.freeSpace[1] = columnIdx+i+1;
                            return true;
                        } else if(i < fullSet.length-6 && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario -O-OOO-
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                            return true;
                        } else if(i < fullSet.length-6 && piece == "O" && fullSet[i] != piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-" && fullSet[i+6] != piece){ // check senario -OOOO-- 
                            this.freeSpace[0] = rowIdx-(i+5);
                            this.freeSpace[1] = columnIdx+i+5; 
                            return true;
                        } else if(i < fullSet.length-6 && piece == "O" && fullSet[i] != piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == piece && fullSet[i+6] != piece){ // check senario --OOOO-
                            this.freeSpace[0] = rowIdx-(i+1);
                            this.freeSpace[1] = columnIdx+i+1;
                            return true;
                        } else if((((i == 0 && piece == "O") || (i == 14 && piece == "O"))) && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OOOO- 
                            int[][] spots = {{rowIdx-i, columnIdx+i}, {rowIdx-(i+5), columnIdx+i+5}};
                            this.freeSpace = pickOptimalSpot(spots, piece);
                            return true;
                        } 
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean setOfThreeUnblockedInFive(String type, String piece){
        String[] fullSet;
        int rowIdx;
        int columnIdx;
        if(type == "row"){
            for(int j = 0; j < 19; j++){
                fullSet = getRow(j);
                this.freeSpace[0] = j;

                for(int i = 0; i < 14; i++){
                    if(piece == "X" && i < fullSet.length-6 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece && fullSet[i+6] == "-"){ // check senario -O-O-O-
                        int[][] spots = {{this.freeSpace[0], i}, {this.freeSpace[0], i+2}, {this.freeSpace[0], i+4}, {this.freeSpace[0], i+6}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario --OOO-
                        if(i == fullSet.length-6){
                            this.freeSpace[1] = i+1;
                        } else {
                            int[][] spots = {{this.freeSpace[0], i+1}, {this.freeSpace[0], i+5}};
                            this.freeSpace = pickOptimalSpot(spots, "O");
                        }
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == "-" ){ // check senario -OOO--
                        if(i == 0){
                            this.freeSpace[1] = i+4;
                        } else {
                            int[][] spots = {{this.freeSpace[0], i}, {this.freeSpace[0], i+4}};
                            this.freeSpace = pickOptimalSpot(spots, "O"); 
                        }
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -O-OO-
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OO-O-
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(piece == "X" && i < fullSet.length-5 && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario O--OO-,
                        int[][] spots = {{this.freeSpace[0], i+1}, {this.freeSpace[0], i+2}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } else if(piece == "X" && i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-" && fullSet[i+5] == piece){ // check senario -OO--O
                        int[][] spots = {{this.freeSpace[0], i+3}, {this.freeSpace[0], i+4}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "column"){
            for(int j = 0; j < 19; j++){
                fullSet = getColumn(j);
                this.freeSpace[1] = j;

                for(int i = 0; i < 14; i++){
                    if(piece == "X" && i < fullSet.length-6 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece && fullSet[i+6] == "-"){ // check senario -O-O-O-
                        int[][] spots = {{i, this.freeSpace[1]}, {i+2, this.freeSpace[1]}, {i+4, this.freeSpace[1]}, {i+6, this.freeSpace[1]}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario --OOO-
                        if(i == fullSet.length-6){
                            this.freeSpace[0] = i+1;
                        } else {
                            int[][] spots = {{i+1, this.freeSpace[1]}, {i+5, this.freeSpace[1]}};
                            this.freeSpace = pickOptimalSpot(spots, "O");
                        }
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == "-" ){ // check senario -OOO--
                        if(i == 0){
                            this.freeSpace[0] = i+4;
                        } else {
                            int[][] spots = {{i, this.freeSpace[1]}, {i+4, this.freeSpace[1]}};
                            this.freeSpace = pickOptimalSpot(spots, "O");
                        }
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -O-OO-
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OO-O-
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(piece == "X" && i < fullSet.length-5 && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario O--OO-
                        int[][] spots = {{i+1, this.freeSpace[1]}, {i+2, this.freeSpace[1]}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } else if(piece == "X" && i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-" && fullSet[i+5] == piece){ // check senario -OO--O
                        int[][] spots = {{i+3, this.freeSpace[1]}, {i+4, this.freeSpace[1]}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } 
                }
            } 
            return false;   
        } else if(type == "diagonalDown"){ 
            for(int j = 0; j < 30; j++){
                fullSet = getDiagonalDown(j);
            
                if(j <= 14){
                    rowIdx = 14-j;
                    columnIdx = 0;
                } else {
                    rowIdx = 0;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-4; i++){ 
                    if(piece == "X" && i < fullSet.length-6 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece && fullSet[i+6] == "-"){ // check senario -O-O-O-
                        int[][] spots = {{rowIdx+i, columnIdx+i}, {rowIdx+i+2, columnIdx+i+2}, {rowIdx+i+4, columnIdx+i+4}, {rowIdx+i+6, columnIdx+i+6}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario --OOO-
                        if(i == fullSet.length-6){
                            this.freeSpace[0] = rowIdx+i+1;
                            this.freeSpace[1] = columnIdx+i+1;
                        } else {
                            int[][] spots = {{rowIdx+i+1, columnIdx+i+1}, {rowIdx+i+5, columnIdx+i+5}};
                            this.freeSpace = pickOptimalSpot(spots, "O");
                        }
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == "-" ){ // check senario -OOO--
                        if(i == 0){
                            this.freeSpace[0] = rowIdx+i+4;
                            this.freeSpace[1] = columnIdx+i+4;
                        } else {
                            int[][] spots = {{rowIdx+i, columnIdx+i}, {rowIdx+i+4, columnIdx+i+4}};
                            this.freeSpace = pickOptimalSpot(spots, "O");
                        }
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -O-OO-
                        this.freeSpace[0] = rowIdx+i+2;
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OO-O-
                        this.freeSpace[0] = rowIdx+i+3;
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(piece == "X" && i < fullSet.length-5 && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario O--OO-
                        int[][] spots = {{rowIdx+i+1, columnIdx+i+1}, {rowIdx+i+2, columnIdx+i+2}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } else if(piece == "X" && i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-" && fullSet[i+5] == piece){ // check senario -OO--O
                        int[][] spots = {{rowIdx+i+3, columnIdx+i+3}, {rowIdx+i+4, columnIdx+i+4}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "diagonalUp"){
            for(int j = 0; j < 30; j++){
                fullSet = getDiagonalUp(j);
            
                if(j <= 14){
                    rowIdx = j+4;
                    columnIdx = 0;
                } else {
                    rowIdx = 18;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-4; i++){ 
                    if(piece == "X" && i < fullSet.length-6 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == piece && fullSet[i+6] == "-"){ // check senario -O-O-O-
                        int[][] spots = {{rowIdx-i, columnIdx+i}, {rowIdx-(i+2), columnIdx+i+2}, {rowIdx-(i+4), columnIdx+i+4}, {rowIdx-(i+6), columnIdx+i+6}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario --OOO-
                        if(i == fullSet.length-6){
                            this.freeSpace[0] = rowIdx-(i+1);
                            this.freeSpace[1] = columnIdx+i+1;
                        } else {
                            int[][] spots = {{rowIdx-(i+1), columnIdx+i+1}, {rowIdx-(i+5), columnIdx+i+5}};
                            this.freeSpace = pickOptimalSpot(spots, "O");
                        }
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-" && fullSet[i+5] == "-" ){ // check senario -OOO--
                        if(i == 0){
                            this.freeSpace[0] = rowIdx-(i+4);
                            this.freeSpace[1] = columnIdx+i+4;
                        } else {
                            int[][] spots = {{rowIdx-i, columnIdx+i}, {rowIdx-(i+4), columnIdx+i+4}};
                            this.freeSpace = pickOptimalSpot(spots, "O");
                        }
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -O-OO-
                        this.freeSpace[0] = rowIdx-(i+2);
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OO-O-
                        this.freeSpace[0] = rowIdx-(i+3);
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(piece == "X" && i < fullSet.length-5 && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario O--OO-
                        int[][] spots = {{rowIdx-(i+1), columnIdx+i+1}, {rowIdx-(i+2), columnIdx+i+2}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } else if(piece == "X" && i < fullSet.length-5 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-" && fullSet[i+5] == piece){ // check senario -OO--O
                        int[][] spots = {{rowIdx-(i+3), columnIdx+i+3}, {rowIdx-(i+4), columnIdx+i+4}};
                        this.freeSpace = pickOptimalSpot(spots, "O");
                        return true;
                    } 
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean hasThreeInFive(String piece){
        if(piece == "O"){
            // check senarios O-O-O, OOO--, -OOO-, --OOO, OO-O-, O-OO-, -O-OO, -OO-O for rows, columns, and diagonals
            if(setOfThreeComputerPieceInFive("row")){
                return true;
            } else if(setOfThreeComputerPieceInFive("column")){
                return true;
            } else if(setOfThreeComputerPieceInFive("diagonalDown")){
                return true;
            } else if(setOfThreeComputerPieceInFive("diagonalUp")){
                return true;
            } else {
                return false;
            }
        } else {
            // don't prevent 4 from being formed if one side is blocked 
            // this should check the following cases
            // -X-X-X-, --XXX-, -XXX--, -XX-X-, -X-XX- for rows, columns, and diagonals 
            if(setOfThreeUnblockedInFive("row", "X")){
                return true;
            } else if(setOfThreeUnblockedInFive("column", "X")){
                return true;
            } else if(setOfThreeUnblockedInFive("diagonalDown", "X")){
                return true;
            } else if(setOfThreeUnblockedInFive("diagonalUp", "X")){
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean setOfThreeComputerPieceInFive(String type){
        String[] fullSet;
        int rowIdx;
        int columnIdx;
        String piece = "O";
        if(type == "row"){
            for(int j = 0; j < 19; j++){
                fullSet = getRow(j);
                this.freeSpace[0] = j;

                for(int i = 0; i < 15; i++){
                    if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O-O-O
                        int[][] spots = {{this.freeSpace[0], i+1}, {this.freeSpace[0], i+3}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OOO--
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -OOO-
                        if(i == 0){
                            this.freeSpace[1] = i+4;
                        } else if(i == 15){
                            this.freeSpace[1] = i;
                        } else {
                            int[][] spots = {{this.freeSpace[0], i}, {this.freeSpace[0], i+4}};
                            this.freeSpace = pickOptimalSpot(spots, piece);
                            return true;
                        }
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario --OOO
                        this.freeSpace[1] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O-OO-
                        this.freeSpace[1] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario OO-O-
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario -O-OO,
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -OO-O
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O--OO,
                        int[][] spots = {{this.freeSpace[0], i+1}, {this.freeSpace[0], i+2}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OO--O
                        int[][] spots = {{this.freeSpace[0], i+2}, {this.freeSpace[0], i+3}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "column"){
            for(int j = 0; j < 19; j++){
                fullSet = getColumn(j);
                this.freeSpace[1] = j;

                for(int i = 0; i < 15; i++){
                    if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O-O-O
                        int[][] spots = {{i+1, this.freeSpace[1]}, {i+3, this.freeSpace[1]}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OOO--
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -OOO-
                        if(i == 0){
                            this.freeSpace[0] = i+4;
                        } else if(i == 15){
                            this.freeSpace[0] = i;
                        } else {
                            int[][] spots = {{i, this.freeSpace[1]}, {i+4, this.freeSpace[1]}};
                            this.freeSpace = pickOptimalSpot(spots, piece);
                            return true;
                        }
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario --OOO
                        this.freeSpace[0] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O-OO-
                        this.freeSpace[0] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario OO-O-
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario -O-OO,
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -OO-O
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O--OO
                        int[][] spots = {{i+1, this.freeSpace[1]}, {i+2, this.freeSpace[1]}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OO--O
                        int[][] spots = {{i+2, this.freeSpace[1]}, {i+3, this.freeSpace[1]}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } 
                }
            } 
            return false;   
        } else if(type == "diagonalDown"){
            for(int j = 0; j < 30; j++){
                fullSet = getDiagonalDown(j);
            
                if(j <= 14){
                    rowIdx = 14-j;
                    columnIdx = 0;
                } else {
                    rowIdx = 0;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-4; i++){ 
                    if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O-O-O
                        int[][] spots = {{rowIdx+i+1, columnIdx+i+1}, {rowIdx+i+3, columnIdx+i+3}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OOO--
                        this.freeSpace[0] = rowIdx+i+4;
                        this.freeSpace[1] = columnIdx+i+4;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -OOO-
                        if(i == 0){
                            this.freeSpace[0] = rowIdx+i+4;
                            this.freeSpace[1] = columnIdx+i+4;
                        } else if(i == fullSet.length-5){
                            this.freeSpace[0] = rowIdx+i;
                            this.freeSpace[1] = columnIdx+i;
                        } else {
                            int[][] spots = {{rowIdx+i, columnIdx+i}, {rowIdx+i+4, columnIdx+i+4}};
                            this.freeSpace = pickOptimalSpot(spots, piece);
                            return true;
                        }
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario --OOO
                        this.freeSpace[0] = rowIdx+i+1;
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O-OO-
                        this.freeSpace[0] = rowIdx+i+1;
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario OO-O-
                        this.freeSpace[0] = rowIdx+i+2;
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario -O-OO,
                        this.freeSpace[0] = rowIdx+i+2;
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -OO-O
                        this.freeSpace[0] = rowIdx+i+3;
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O--OO
                        int[][] spots = {{rowIdx+i+1, columnIdx+i+1}, {rowIdx+i+2, columnIdx+i+2}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OO--O
                        int[][] spots = {{rowIdx+i+2, columnIdx+i+2}, {rowIdx+i+3, columnIdx+i+3}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "diagonalUp"){
            for(int j = 0; j < 30; j++){
                fullSet = getDiagonalUp(j);
            
                if(j <= 14){
                    rowIdx = j+4;
                    columnIdx = 0;
                } else {
                    rowIdx = 18;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-4; i++){ 
                    if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O-O-O
                        int[][] spots = {{rowIdx-(i+1), columnIdx+i+1}, {rowIdx-(i+3), columnIdx+i+3}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OOO--
                        this.freeSpace[0] = rowIdx-(i+3);
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -OOO-
                        if(i == 0){
                            this.freeSpace[0] = rowIdx-(i+4);
                            this.freeSpace[1] = columnIdx+i+4;
                        } else if(i == fullSet.length-5){
                            this.freeSpace[0] = rowIdx-i;
                            this.freeSpace[1] = columnIdx+i;
                        } else {
                            int[][] spots = {{rowIdx-i, columnIdx+i}, {rowIdx-(i+4), columnIdx+i+4}};
                            this.freeSpace = pickOptimalSpot(spots, piece);
                            return true;
                        }
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario --OOO
                        this.freeSpace[0] = rowIdx-(i+1);
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O-OO-
                        this.freeSpace[0] = rowIdx-(i+1);
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario OO-O-
                        this.freeSpace[0] = rowIdx-(i+2);
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario -O-OO,
                        this.freeSpace[0] = rowIdx-(i+2);
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -OO-O
                        this.freeSpace[0] = rowIdx-(i+3);
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O--OO
                        int[][] spots = {{rowIdx-(i+1), columnIdx+i+1}, {rowIdx-(i+2), columnIdx+i+2}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OO--O
                        int[][] spots = {{rowIdx-(i+2), columnIdx+i+2}, {rowIdx-(i+3), columnIdx+i+3}};
                        this.freeSpace = pickOptimalSpot(spots, piece);
                        return true;
                    } 
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean hasIntersectingTwos(String piece){
        for(int i = 1; i < 18; i++){
            for(int j = 1; j < 18; j++){
                if(this.board[i][j] == "-"){
                    if(i > 2 && j > 2 && nNorth(2, i, j, piece) && nNorthWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && j < 16 && nNorth(2, i, j, piece) && nNorthEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && j < 16 && nNorth(2, i, j, piece) && nEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && j > 2 && nNorth(2, i, j, piece) && nWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && i < 16 && j > 2 && nNorth(2, i, j, piece) && nSouthWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && i < 16 && j < 16 && nNorth(2, i, j, piece) && nSouthEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && i < 16 && j > 2 && nSouth(2, i, j, piece) && nNorthWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && i < 16 && j < 16 && nSouth(2, i, j, piece) && nNorthEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i < 16 && j < 16 && nSouth(2, i, j, piece) && nEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i < 16 && j > 2 && nSouth(2, i, j, piece) && nWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i < 16 && j > 2 && nSouth(2, i, j, piece) && nSouthWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i < 16 && j < 16 && nSouth(2, i, j, piece) && nSouthEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && j > 2 && j < 16 && nEast(2, i, j, piece) && nNorthWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && j < 16 && nEast(2, i, j, piece) && nNorthEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i < 16 && j > 2 && j < 16 && nEast(2, i, j, piece) && nSouthWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i < 16 && j < 16 && nEast(2, i, j, piece) && nSouthEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && j > 2 && nWest(2, i, j, piece) && nNorthWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i > 2 && j > 2 && j < 16 && nWest(2, i, j, piece) && nNorthEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i < 16 && j > 2 && nWest(2, i, j, piece) && nSouthWest(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    } else if(i < 16 && j > 2 && j < 16 && nWest(2, i, j, piece) && nSouthEast(2, i, j, piece)){
                        this.freeSpace[0] = i;
                        this.freeSpace[1] = j;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean nNorth(int n, int rowIdx, int columnIdx, String piece){
        for(int i = 1; i <= n; i++){
            if(this.board[rowIdx-i][columnIdx] != piece) return false;
        }
        if(this.board[rowIdx-(n+1)][columnIdx] != "-") return false;
        
        return true;
    }

    private boolean nSouth(int n, int rowIdx, int columnIdx, String piece){
        for(int i = 1; i <= n; i++){
            if(this.board[rowIdx+i][columnIdx] != piece) return false;
        }
        if(this.board[rowIdx+(n+1)][columnIdx] != "-") return false;
        
        return true;
    }

    private boolean nEast(int n, int rowIdx, int columnIdx, String piece){
        for(int i = 1; i <= n; i++){
            if(this.board[rowIdx][columnIdx+i] != piece) return false;
        }
        if(this.board[rowIdx][columnIdx+(n+1)] != "-") return false;
        
        return true;
    }

    private boolean nWest(int n, int rowIdx, int columnIdx, String piece){
        for(int i = 1; i <= n; i++){
            if(this.board[rowIdx][columnIdx-i] != piece) return false;
        }
        if(this.board[rowIdx][columnIdx-(n+1)] != "-") return false;
        
        return true;
    }

    private boolean nNorthEast(int n, int rowIdx, int columnIdx, String piece){
        for(int i = 1; i <= n; i++){
            if(this.board[rowIdx-i][columnIdx+i] != piece) return false;
        }
        if(this.board[rowIdx-(n+1)][columnIdx+(n+1)] != "-") return false;

        return true;
    }
    
    private boolean nNorthWest(int n, int rowIdx, int columnIdx, String piece){
        for(int i = 1; i <= n; i++){
            if(this.board[rowIdx-i][columnIdx-i] != piece) return false;
        }
        if(this.board[rowIdx-(n+1)][columnIdx-(n+1)] != "-") return false;
        
        return true;
    }
    
    private boolean nSouthEast(int n, int rowIdx, int columnIdx, String piece){
        for(int i = 1; i <= n; i++){
            if(this.board[rowIdx+i][columnIdx+i] != piece) return false;
        }
        if(this.board[rowIdx+(n+1)][columnIdx+(n+1)] != "-") return false;
        
        return true;
    }
    
    private boolean nSouthWest(int n, int rowIdx, int columnIdx, String piece){
        for(int i = 1; i <= n; i++){
            if(this.board[rowIdx+i][columnIdx-i] != piece) return false;
        }
        if(this.board[rowIdx+(n+1)][columnIdx-(n+1)] != "-") return false;
        
        return true;
    }

    private int[] pickOptimalSpot(int[][] spots, String piece){
        int bestScore = score(spots[0], piece);
        int[] bestSpot = spots[0];

        for(int i = 1; i < spots.length; i++){
            int newScore = score(spots[i], piece);
            if(bestScore < newScore || this.random.nextInt(2) == 1){
                bestScore = newScore;
                bestSpot = spots[i];
            } 
        }

        return bestSpot;
    }

    private int score(int[] spot, String piece){
        String opposingPiece;
        if(piece == "X"){
            opposingPiece = "O";
        } else {
            opposingPiece = "X";
        }
        int score = 0; // the score is the number of piece around that spot
        int rowIdx = spot[0];
        int columnIdx = spot[1];

        //check north
        int r = rowIdx;
        int c = columnIdx;
        while(r >= 0){
            if(this.board[r][c] == piece){
                score++;
            } else if(this.board[r][c] == opposingPiece){
                break;
            }
            r--;
        }
        //check north east
        r = rowIdx;
        c = columnIdx;
        while(r >= 0 && c < 19){
            if(this.board[r][c] == piece){
                score++;
            } else if(this.board[r][c] == opposingPiece){
                break;
            }
            r--;
            c++;
        }
        //check east
        r = rowIdx;
        c = columnIdx;
        while(c < 19){
            if(this.board[r][c] == piece){
                score++;
            } else if(this.board[r][c] == opposingPiece){
                break;
            }
            c++;
        }
        //check south east
        r = rowIdx;
        c = columnIdx;
        while(r < 19 && c < 19){
            if(this.board[r][c] == piece){
                score++;
            } else if(this.board[r][c] == opposingPiece){
                break;
            }
            r++;
            c++;
        }
        //check south
        r = rowIdx;
        c = columnIdx;
        while(r < 19){
            if(this.board[r][c] == piece){
                score++;
            } else if(this.board[r][c] == opposingPiece){
                break;
            }
            r++;
        }
        //check south west
        r = rowIdx;
        c = columnIdx;
        while(r < 19 && c >= 0){
            if(this.board[r][c] == piece){
                score++;
            } else if(this.board[r][c] == opposingPiece){
                break;
            }
            r++;
            c--;
        }
        //check west
        r = rowIdx;
        c = columnIdx;
        while(c >= 0){
            if(this.board[r][c] == piece){
                score++;
            } else if(this.board[r][c] == opposingPiece){
                break;
            }
            c--;
        }
        //check north west
        r = rowIdx;
        c = columnIdx;
        while(r >= 0 && c >= 0){
            if(this.board[r][c] == piece){
                score++;
            } else if(this.board[r][c] == opposingPiece){
                break;
            }
            r--;
            c--;
        }

        return score;
    }

    private boolean computerCanMakeFourUnblockedInARow(){ 
        // This checks the following cases
        // --OOO-, -OOO--, -O-OO-, -OO-O-, O--OO-, -OO--O 
        if(setOfThreeUnblockedInFive("row", "O")){
            return true;
        } else if(setOfThreeUnblockedInFive("column", "O")){
            return true;
        } else if(setOfThreeUnblockedInFive("diagonalDown", "O")){
            return true;
        } else if(setOfThreeUnblockedInFive("diagonalUp", "O")){
            return true;
        } else {
            return false;
        }
    }

    private boolean computerHasThree(){ 
        // check senarios O-O-O, OOO--, -OOO-, --OOO, OO-O-, O-OO-, -O-OO, -OO-O for rows, columns, and diagonals
        if(setOfThreeinFive("row", "O")){ 
            return true;
        } else if(setOfThreeinFive("column", "O")){ 
            return true;
        } else if(setOfThreeinFive("diagonalDown", "O")){ 
            return true;
        } else if(setOfThreeinFive("diagonalUp", "O")){ 
            return true;
        } else {
            return false;
        }
    }

    private boolean computerHasTwo(){ 
        // check senarios -O-O-, OO---, O-O--, O--O-, O---O, -OO--, -O--O, --OO-, --O-O, ---OO
        if(setOfTwoinFive("row", "O")){
            return true;
        } else if(setOfTwoinFive("column", "O")){
            return true;
        } else if(setOfTwoinFive("diagonalDown", "O")){
            return true;
        } else if(setOfTwoinFive("diagonalUp", "O")){
            return true;
        } else {
            return false;
        }
    }

    private boolean computerHasOne(){ 
        // check senarios O----, -O---, --O--, ---O-, ----O
        if(setOfOneinFive("row", "O")){
            return true;
        } else if(setOfOneinFive("column", "O")){
            return true;
        } else if(setOfOneinFive("diagonalDown", "O")){
            return true;
        } else if(setOfOneinFive("diagonalUp", "O")){
            return true;
        } else {
            return false;
        }
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
        for(int i = 0; i < 19; i++){
            if(n == 5 && numInRow == n){ // win condition
                return true;
            } else if(i > 3 && n == 3 && numInRow == n && row[i] == "-" && row[i-4] == "-"){ // 3 in a row with a free space on both sides
                if(random.nextInt(2) == 0){
                    this.freeSpace[1] = i;
                } else {
                    this.freeSpace[1] = i-(n+1);
                }
                return true;
            } else if(i == 4 && n == 4 && numInRow == n && row[i] == "-"){ // four in a row with a free space to the right, left is the wall
                this.freeSpace[1] = i;
                return true;
            } else if(i == 18 && n == 4 && numInRow == 3 && row[i] == piece && row[i-4] == "-"){ // four in a row with a free space to the left, right is wall
                this.freeSpace[1] = i-4;
                return true;
            } else if(i > 4 && n == 4 && numInRow == n && row[i] == "-" && row[i-5] == opposingPiece){ // four in a row with a free space to the right, left is blocked
                this.freeSpace[1] = i;
                return true;
            } else if(i > 4 && n == 4 && numInRow == n && row[i] == opposingPiece && row[i-5] == "-"){ // four in a row with a free space to the left, right is blocked
                this.freeSpace[1] = i-5;
                return true;
            }  else if(i > 2 && n == 2 && numInRow == n && row[i] == "-" && row[i-3] == opposingPiece){ // two in a row with a free space to the right and the opposing peice to the left
                this.freeSpace[1] = i;
                this.captivePieceOne[1] = i-1;
                this.captivePieceTwo[1] = i-2;
                return true;
            } else if(i > 2 && n == 2 && numInRow == n && row[i] == opposingPiece && row[i-3] == "-"){ // two in a row with the opposing peice to the right and a free space to the left
                this.freeSpace[1] = i-3;
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
        for(int i = 0; i < 19; i++){
            if(n == 5 && numInColumn == n){ // win condition
                return true;
            } else if(i > 3 && n == 3 && numInColumn == n && column[i] == "-" && column[i-4] == "-"){ // 3 in a column with a free space on both sides
                if(random.nextInt(2) == 0){
                    this.freeSpace[0] = i;
                } else {
                    this.freeSpace[0] = i-(n+1);
                }
                return true;
            } else if(i == 4 && n == 4 && numInColumn == n && column[i] == "-"){ // 4 in a column with a free space on the bottom, the top is the wall
                this.freeSpace[0] = i;
                return true;
            } else if(i == 18 && n == 4 && numInColumn == 3 && column[i] == piece && column[i-4] == "-"){ // 4 in a column with a free space on the top, the bottom is the wall
                this.freeSpace[0] = i-4;
                return true;
            } else if(i > 4 && n == 4 && numInColumn == n && column[i] == "-" && column[i-5] == opposingPiece){ // 4 in a column with a free space on the bottom, the top the opposing piece
                this.freeSpace[0] = i;
                return true;
            } else if(i > 4 && n == 4 && numInColumn == n && column[i] == opposingPiece && column[i-5] == "-"){ // 4 in a column with a free space on the top and an opposing piece on bottom
                this.freeSpace[0] = i-5;
                return true;
            } else if(i > 2 && n == 2 && numInColumn == n && column[i] == "-" && column[i-3] == opposingPiece){ // two in a column with a free space on the bottom and the opposing peice on top
                this.freeSpace[0] = i;
                this.captivePieceOne[0] = i-1;
                this.captivePieceTwo[0] = i-2;
                return true;
            } else if(i > 2 && n == 2 && numInColumn == n && column[i] == opposingPiece && column[i-3] == "-"){ // two in a column with the opposing peice on the bottom and a free space on top
                this.freeSpace[0] = i-3;
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
    // 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28
    // 13 -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -    
    // 12 -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  
    // 11 -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  
    // 10 -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 9  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 8  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 7  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 6  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 5  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 4  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 3  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 2  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 1  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // 0  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    //    -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    //    -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  
    //    -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    //    -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    //    -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -

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
        for(int i = 0; i < diagonal.length; i++){
            if(n == 5 && numInDiagonal == n){ // win condition
                return true;
            } else if(i > 3 && n == 3 && numInDiagonal == n && diagonal[i] == "-" && diagonal[i-4] == "-"){ // 3 in a downward diagonal with a free space on both sides
                if(random.nextInt(2) == 0){
                    this.freeSpace[0] = rowIdx+i;
                    this.freeSpace[1] = columnIdx+i;
                } else {
                    this.freeSpace[0] = rowIdx+i-4;
                    this.freeSpace[1] = columnIdx+i-4;
                }
                return true;
            } else if(i == 4 && n == 4 && numInDiagonal == n && diagonal[i] == "-"){ // 4 in a diagonal with a free space on the bottom right, top left is the wall
                this.freeSpace[0] = rowIdx+i;
                this.freeSpace[1] = columnIdx+i;
                return true;
            } else if(i > 4 && n == 4 && numInDiagonal == n && diagonal[i] == "-" && diagonal[i-5] == opposingPiece){ // 4 in a diagonal with a free space on the bottom right, top left is blocked
                this.freeSpace[0] = rowIdx+i;
                this.freeSpace[1] = columnIdx+i;
                return true;
            } else if(i == diagonal.length-1 && n == 4 && numInDiagonal == 3 && diagonal[i] == piece && diagonal[i-4] == "-"){ // 4 in a downward diagonal with a free space on the top left, bottom right is the wall
                this.freeSpace[0] = rowIdx+i-4;
                this.freeSpace[1] = columnIdx+i-4;
                return true;
            } else if(i > 4 && n == 4 && numInDiagonal == n && diagonal[i] == opposingPiece && diagonal[i-5] == "-"){ // 4 in a downward diagonal with a free space on the top left, bottom right is blocked
                this.freeSpace[0] = rowIdx+i-5;
                this.freeSpace[1] = columnIdx+i-5;
                return true;
            }  else if(i > 2 && n == 2 && numInDiagonal == n && diagonal[i] == "-" && diagonal[i-3] == opposingPiece){ // two in a downward diagonal with a free space on the bottom right and the opposing peice on top left
                this.freeSpace[0] = rowIdx+i;
                this.freeSpace[1] = columnIdx+i;
                this.captivePieceOne[0] = rowIdx+i-1;
                this.captivePieceOne[1] = columnIdx+i-1;
                this.captivePieceTwo[0] = rowIdx+i-2;
                this.captivePieceTwo[1] = columnIdx+i-2;
                return true;
            } else if(i > 2 && n == 2 && numInDiagonal == n && diagonal[i] == opposingPiece && diagonal[i-3] == "-"){ // two in a downward diagonal with the opposing peice on the bottom and a free space on top
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

    private boolean findDiagonalUpOfN(int n, String piece){ 
        this.freeSpace[0] = 100;
        for(int i = 0; i <= 28; i++){ // there are 28 possible diagonals of 5 or more
            int rowIdx;
            int columnIdx;
            if(i <= 14){
                rowIdx = i+4;
                columnIdx = 0;
            } else {
                rowIdx = 18;
                columnIdx = i-14;
            }
            if(diagonalUpOfN(getDiagonalUp(i), n, piece, rowIdx, columnIdx)){
                return true;
            } 
        }

        return false;
    }

    // the upwards diagonals are numbered in the following way
    // diagonals with less than 5 spots are not counted
    //                0  1  2  3  4  5  6  7  8  9 10 11 12 13 14
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 15
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 16
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 17
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 18
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 19  
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 20
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 21
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 22
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 23
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 24
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 25
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 26
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 27
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 28
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  

    private String[] getDiagonalUp(int n){ 
        String[] diagonalUp;
        if(n <= 14){
            diagonalUp = new String[n+5];
            int i = n+4;
            int j = 0;
            int h = 0;
            while(h < n+5){
                diagonalUp[h] = this.board[i][j];
                j++;
                i--;
                h++;
            }
        } else {
            diagonalUp = new String[28-n+5];
            int i = 18;
            int j = n-14;
            int h = 0;
            while(h < 28-n+5){
                diagonalUp[h] = this.board[i][j];
                j++;
                i--;
                h++;
            }
        } 
        return diagonalUp;
    }

    private boolean diagonalUpOfN(String[] diagonal, int n, String piece, int rowIdx, int columnIdx){ 
        String opposingPiece;
        if(piece == "X"){
            opposingPiece = "O";
        } else {
            opposingPiece = "X";
        }

        int numInDiagonal = 0;
        for(int i = 0; i < diagonal.length; i++){
            if(n == 5 && numInDiagonal == n){ // win condition
                return true;
            } else if(i > 3 && n == 3 && numInDiagonal == n && diagonal[i] == "-" && diagonal[i-4] == "-"){ // 3 in a upward diagonal with a free space on both sides
                if(random.nextInt(2) == 0){
                    this.freeSpace[0] = rowIdx-i;
                    this.freeSpace[1] = columnIdx+i;
                } else {
                    this.freeSpace[0] = rowIdx-(i-4);
                    this.freeSpace[1] = columnIdx+(i-4);
                }
                return true;
            } else if(i == 4 && n == 4 && numInDiagonal == n && diagonal[i] == "-"){ // 4 in a diagonal with a free space on the top right, bottom left is the wall
                this.freeSpace[0] = rowIdx-i;
                this.freeSpace[1] = columnIdx+i;
                return true;
            } else if(i > 4 && n == 4 && numInDiagonal == n && diagonal[i] == "-" && diagonal[i-5] == opposingPiece){ // 4 in a diagonal with a free space on the top right, bottom left is blocked
                this.freeSpace[0] = rowIdx-i;
                this.freeSpace[1] = columnIdx+i;
                return true;
            } else if(i == diagonal.length-1 && n == 4 && numInDiagonal == 3 && diagonal[i] == piece && diagonal[i-4] == opposingPiece){ // 4 in a downward diagonal with a free space on the bottom left, top right is the wall
                this.freeSpace[0] = rowIdx-(i-4);
                this.freeSpace[1] = columnIdx+(i-4);
                return true;
            } else if(i > 4 && n == 4 && numInDiagonal == n && diagonal[i] == opposingPiece && diagonal[i-5] == "-"){ // 4 in a downward diagonal with a free space on the bottom left, top right is blocked
                this.freeSpace[0] = rowIdx-(i-5);
                this.freeSpace[1] = columnIdx+(i-5);
                return true;
            } else if(i > 2 && n == 2 && numInDiagonal == n && diagonal[i] == "-" && diagonal[i-3] == opposingPiece){ // two in a upward diagonal with a free space on the bottom left and the opposing peice on top right
                this.freeSpace[0] = rowIdx-i;
                this.freeSpace[1] = columnIdx+i;
                this.captivePieceOne[0] = rowIdx-(i-1);
                this.captivePieceOne[1] = columnIdx+(i-1);
                this.captivePieceTwo[0] = rowIdx-(i-2);
                this.captivePieceTwo[1] = columnIdx+(i-2);
                return true;
            } else if(i > 2 && n == 2 && numInDiagonal == n && diagonal[i] == opposingPiece && diagonal[i-3] == "-"){ // two in a upward diagonal with the opposing peice on the bottom left and a free space on top right
                this.freeSpace[0] = rowIdx-(i-3);
                this.freeSpace[1] = columnIdx+(i-3);
                this.captivePieceOne[0] = rowIdx-(i-1);
                this.captivePieceOne[1] = columnIdx+(i-1);
                this.captivePieceTwo[0] = rowIdx-(i-2);
                this.captivePieceTwo[1] = columnIdx+(i-2);
                return true;
            }  else if(diagonal[i] == piece){
                numInDiagonal++;
            } else if(diagonal[i] != piece){
                numInDiagonal = 0;
            }
        }
        return false;
    }

    private boolean setOfFourinFive(String type, String piece){ 
        String[] fullSet;
        int rowIdx;
        int columnIdx;
        if(type == "row"){
            for(int j = 0; j < 19; j++){
                fullSet = getRow(j);
                this.freeSpace[0] = j;

                for(int i = 0; i < 14; i++){
                    if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario OO-OO
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OOO-O
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O-OOO
                        this.freeSpace[1] = i+1;
                        return true;
                    } else if(i < 13 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OOOO- 
                        if(random.nextInt(2) == 0){
                            this.freeSpace[1] = i;
                        } else {
                            this.freeSpace[1] = i+5;
                        }
                        return true;
                    }
                }
            }
            return false;
        } else if(type == "column"){
            for(int j = 0; j < 19; j++){
                fullSet = getColumn(j);
                this.freeSpace[1] = j;

                for(int i = 0; i < 14; i++){
                    if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario OO-OO
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OOO-O
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O-OOO
                        this.freeSpace[0] = i+1;
                        return true;
                    } else if(i < 13 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OOOO- 
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = i;
                        } else {
                            this.freeSpace[0] = i+5;
                        }
                        return true;
                    }
                }
            } 
            return false;   
        } else if(type == "diagonalDown"){ 
            for(int j = 0; j < 28; j++){
                fullSet = getDiagonalDown(j);
            
                if(j <= 14){
                    rowIdx = 14-j;
                    columnIdx = 0;
                } else {
                    rowIdx = 0;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-5; i++){ 
                    if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario OO-OO
                        this.freeSpace[0] = rowIdx+i+2;
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OOO-O
                        this.freeSpace[0] = rowIdx+i+3;
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O-OOO
                        this.freeSpace[0] = rowIdx+i+1;
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(i < 14 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OOOO- 
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx+i;
                            this.freeSpace[1] = columnIdx+i;
                        } else {
                            this.freeSpace[0] = rowIdx+i+5;
                            this.freeSpace[1] = columnIdx+i+5;
                        }
                        return true;
                    }
                }
            }
            return false;
        } else if(type == "diagonalUp"){ 
            for(int j = 0; j < 28; j++){
                fullSet = getDiagonalUp(j);
            
                if(j <= 14){
                    rowIdx = j+4;
                    columnIdx = 0;
                } else {
                    rowIdx = 18;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-5; i++){ 
                    if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario OO-OO
                        this.freeSpace[0] = rowIdx-(i+2);
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OOO-O
                        this.freeSpace[0] = rowIdx-(i+3);
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O-OOO
                        this.freeSpace[0] = rowIdx-(i+1);
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(i < 14 && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario -OOOO- 
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx-i;
                            this.freeSpace[1] = columnIdx+i;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+5);
                            this.freeSpace[1] = columnIdx+i+5;
                        }
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean setOfThreeinFive(String type, String piece){
        String[] fullSet;
        int rowIdx;
        int columnIdx;
        if(type == "row"){
            for(int j = 0; j < 19; j++){
                fullSet = getRow(j);
                this.freeSpace[0] = j;

                for(int i = 0; i < 14; i++){
                    if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O-O-O
                        if(random.nextInt(2) == 0){
                            this.freeSpace[1] = i+1;
                        } else {
                            this.freeSpace[1] = i+3;
                        }
                        return true;
                    } else if(piece == "O" && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OOO--
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -OOO-
                        if(random.nextInt(2) == 0){
                            this.freeSpace[1] = i;
                        } else {
                            this.freeSpace[1] = i+4;
                        }
                        return true;
                    } else if(piece == "O" && fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario --OOO
                        this.freeSpace[1] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O-OO-
                        this.freeSpace[1] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario OO-O-
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario -O-OO,
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -OO-O
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O--OO,
                        if(random.nextInt(2) == 0){
                            this.freeSpace[1] = i+1;
                        } else {
                            this.freeSpace[1] = i+2;
                        }
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OO--O
                        if(random.nextInt(2) == 0){
                            this.freeSpace[1] = i+2;
                        } else {
                            this.freeSpace[1] = i+3;
                        }
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "column"){
            for(int j = 0; j < 19; j++){
                fullSet = getColumn(j);
                this.freeSpace[1] = j;

                for(int i = 0; i < 14; i++){
                    if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O-O-O
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = i+1;
                        } else {
                            this.freeSpace[0] = i+3;
                        }
                        return true;
                    } else if(piece == "O" && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OOO--
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(piece == "O" && fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -OOO-
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = i;
                        } else {
                            this.freeSpace[0] = i+4;
                        }
                        return true;
                    } else if(piece == "O" && fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario --OOO
                        this.freeSpace[0] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O-OO-
                        this.freeSpace[0] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario OO-O-
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario -O-OO,
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -OO-O
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O--OO
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = i+1;
                        } else {
                            this.freeSpace[0] = i+2;
                        }
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OO--O
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = i+2;
                        } else {
                            this.freeSpace[0] = i+3;
                        }
                        return true;
                    } 
                }
            } 
            return false;   
        } else if(type == "diagonalDown"){
            for(int j = 0; j < 28; j++){
                fullSet = getDiagonalDown(j);
            
                if(j <= 14){
                    rowIdx = 14-j;
                    columnIdx = 0;
                } else {
                    rowIdx = 0;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-5; i++){ 
                    if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O-O-O
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx+i+1;
                            this.freeSpace[1] = columnIdx+i+1;
                        } else {
                            this.freeSpace[0] = rowIdx+i+3;
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } else if(piece == "O" && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OOO--
                        this.freeSpace[0] = rowIdx+i+4;
                        this.freeSpace[1] = columnIdx+i+4;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -OOO-
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx+i;
                            this.freeSpace[1] = columnIdx+i;
                        } else {
                            this.freeSpace[0] = rowIdx+i+4;
                            this.freeSpace[1] = columnIdx+i+4;
                        }
                        return true;
                    } else if(piece == "O" && fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario --OOO
                        this.freeSpace[0] = rowIdx+i+1;
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O-OO-
                        this.freeSpace[0] = rowIdx+i+1;
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario OO-O-
                        this.freeSpace[0] = rowIdx+i+2;
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario -O-OO,
                        this.freeSpace[0] = rowIdx+i+2;
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -OO-O
                        this.freeSpace[0] = rowIdx+i+3;
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O--OO
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx+i+1;
                            this.freeSpace[1] = columnIdx+i+1;
                        } else {
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                        }
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OO--O
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                        } else {
                            this.freeSpace[0] = rowIdx+i+3;
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "diagonalUp"){
            for(int j = 0; j < 28; j++){
                fullSet = getDiagonalUp(j);
            
                if(j <= 14){
                    rowIdx = j+4;
                    columnIdx = 0;
                } else {
                    rowIdx = 18;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-5; i++){ 
                    if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O-O-O
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx-(i+1);
                            this.freeSpace[1] = columnIdx+i+1;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+3);
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } else if(piece == "O" && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OOO--
                        this.freeSpace[0] = rowIdx-(i+3);
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -OOO-
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx-i;
                            this.freeSpace[1] = columnIdx+i;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+4);
                            this.freeSpace[1] = columnIdx+i+4;
                        }
                        return true;
                    } else if(piece == "O" && fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario --OOO
                        this.freeSpace[0] = rowIdx-(i+1);
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O-OO-
                        this.freeSpace[0] = rowIdx-(i+1);
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario OO-O-
                        this.freeSpace[0] = rowIdx-(i+2);
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario -O-OO,
                        this.freeSpace[0] = rowIdx-(i+2);
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -OO-O
                        this.freeSpace[0] = rowIdx-(i+3);
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece){ // check senario O--OO
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx-(i+1);
                            this.freeSpace[1] = columnIdx+i+1;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                        }
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario OO--O
                        if(random.nextInt(2) == 0){
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+3);
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } 
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean setOfTwoinFive(String type, String piece){
        String[] fullSet;
        int rowIdx;
        int columnIdx;
        int randomValue = random.nextInt(3);
        if(type == "row"){  
            for(int j = 0; j < 19; j++){
                fullSet = getRow(j);
                this.freeSpace[0] = j;

                for(int i = 0; i < 14; i++){
                    if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OO---, making sure the left is not blocked
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -O-O-
                        this.freeSpace[1] = i+2;
                        return true;
                    } else if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario O-O--, making sure the left is not blocked
                        this.freeSpace[1] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O--O-
                        if(randomValue == 0){
                            this.freeSpace[1] = i+1;
                        } else if(randomValue == 1){
                            this.freeSpace[1] = i+2;
                        } else {
                            this.freeSpace[1] = i+4;
                        }
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O---O
                        if(randomValue == 0){
                            this.freeSpace[1] = i+1;
                        } else if(randomValue == 1){
                            this.freeSpace[1] = i+2;
                        } else {
                            this.freeSpace[1] = i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario -OO--
                        if(i > 0 && fullSet[i-1] == "-"){ // only choose this spot if the left is not blocked
                            this.freeSpace[1] = i;
                        } else if(randomValue == 1){
                            this.freeSpace[1] = i+3;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -O--O
                        if(randomValue == 0){
                            this.freeSpace[1] = i;
                        } else if(randomValue == 1){
                            this.freeSpace[1] = i+2;
                        } else {
                            this.freeSpace[1] = i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario --OO-
                        if(fullSet[i+5] == "-") { // only choose this spot if the right is not blocked
                            this.freeSpace[1] = i+4;
                        } else {
                            this.freeSpace[1] = i+1;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario --O-O
                        this.freeSpace[1] = i+3;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario ---OO, making sure the right is not blocked
                        this.freeSpace[1] = i+2;
                        return true;
                    }
                }
            }
            return false;
        } else if(type == "column"){ 
            for(int j = 0; j < 19; j++){
                fullSet = getColumn(j);
                this.freeSpace[1] = j;

                for(int i = 0; i < 14; i++){
                    if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OO---, making sure the left is not blocked
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -O-O-
                        this.freeSpace[0] = i+2;
                        return true;
                    } else if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario O-O--, making sure the left is not blocked
                        this.freeSpace[0] = i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O--O-
                        if(randomValue == 0){
                            this.freeSpace[0] = i+1;
                        } else if(randomValue == 1){
                            this.freeSpace[0] = i+2;
                        } else {
                            this.freeSpace[0] = i+4;
                        }
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O---O
                        if(randomValue == 0){
                            this.freeSpace[0] = i+1;
                        } else if(randomValue == 1){
                            this.freeSpace[0] = i+2;
                        } else {
                            this.freeSpace[0] = i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario -OO--
                        if(i > 0 && fullSet[i-1] == "-"){ // only choose this spot if the left is not blocked
                            this.freeSpace[0] = i;
                        } else {
                            this.freeSpace[0] = i+3;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -O--O
                        if(randomValue == 0){
                            this.freeSpace[0] = i;
                        } else if(randomValue == 1){
                            this.freeSpace[0] = i+2;
                        } else {
                            this.freeSpace[0] = i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario --OO-
                        if(fullSet[i+5] == "-"){ // only choose this spot if the right is not blocked
                            this.freeSpace[0] = i+4;
                        } else {
                            this.freeSpace[0] = i+1;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario --O-O, making sure the right is not blocked
                        this.freeSpace[0] = i+3;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario ---OO, making sure the right is not blocked
                        this.freeSpace[0] = i+2;
                        return true;
                    } 
                }
            } 
            return false;   
        } else if(type == "diagonalDown"){
            for(int j = 0; j < 28; j++){
                fullSet = getDiagonalDown(j);
            
                if(j <= 14){
                    rowIdx = 14-j;
                    columnIdx = 0;
                } else {
                    rowIdx = 0;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-5; i++){ 
                    if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OO---, making sure the left is not blocked
                        this.freeSpace[0] = rowIdx+i+2;
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -O-O-
                        this.freeSpace[0] = rowIdx+i+2;
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario O-O--, making sure the left is not blocked
                        this.freeSpace[0] = rowIdx+i+1;
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O--O-
                        if(randomValue == 0){
                            this.freeSpace[0] = rowIdx+i+1;
                            this.freeSpace[1] = columnIdx+i+1;
                        } else if(randomValue == 1){
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                        } else {
                            this.freeSpace[0] = rowIdx+i+4;
                            this.freeSpace[1] = columnIdx+i+4;
                        }
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O---O
                        if(randomValue == 0){
                            this.freeSpace[0] = rowIdx+i+1;
                            this.freeSpace[1] = columnIdx+i+1;
                        } else if(randomValue == 1){
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                        } else {
                            this.freeSpace[0] = rowIdx+i+3;
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario -OO--
                        if(i > 0 && fullSet[i-1] == "-"){ // only choose this spot if the left is not blocked
                            this.freeSpace[0] = rowIdx+i;
                            this.freeSpace[1] = columnIdx+i;
                        } else {
                            this.freeSpace[0] = rowIdx+i+3;
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -O--O
                        if(randomValue == 0){
                            this.freeSpace[0] = rowIdx+i;
                            this.freeSpace[1] = columnIdx+i;
                        } else if(randomValue == 1){
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                        } else {
                            this.freeSpace[0] = rowIdx+i+3;
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario --OO-
                        if(fullSet[i+5] == "-"){ // only choose this spot if the right is not blocked
                            this.freeSpace[0] = rowIdx+i+4;
                            this.freeSpace[1] = columnIdx+i+4;
                        } else {
                            this.freeSpace[0] = rowIdx+i+1;
                            this.freeSpace[1] = columnIdx+i+1;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario --O-O, making sure the right is not blocked
                        this.freeSpace[0] = rowIdx+i+3;
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario ---OO, making sure the right is not blocked
                        this.freeSpace[0] = rowIdx+i+2;
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "diagonalUp"){ 
            for(int j = 0; j < 28; j++){
                fullSet = getDiagonalUp(j);
            
                if(j <= 14){
                    rowIdx = j+4;
                    columnIdx = 0;
                } else {
                    rowIdx = 18;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-5; i++){ 
                    if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario OO---, making sure the left is not blocked
                        this.freeSpace[0] = rowIdx-(i+2);
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario -O-O-
                        this.freeSpace[0] = rowIdx-(i+2);
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } else if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario O-O--, checking that the left is not blocked
                        this.freeSpace[0] = rowIdx-(i+1);
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario O--O-
                        if(randomValue == 0){
                            this.freeSpace[0] = rowIdx-(i+1);
                            this.freeSpace[1] = columnIdx+i+1;
                        } else if(randomValue == 1){
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+4);
                            this.freeSpace[1] = columnIdx+i+4;
                        }
                        return true;
                    } else if(fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario O---O
                        if(randomValue == 0){
                            this.freeSpace[0] = rowIdx-(i+1);
                            this.freeSpace[1] = columnIdx+i+1;
                        } else if(randomValue == 1){
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+3);
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario -OO--
                        if(randomValue == 0){
                            this.freeSpace[0] = rowIdx-i;
                            this.freeSpace[1] = columnIdx+i;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+3);
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece){ // check senario -O--O
                        if(randomValue == 0){
                            this.freeSpace[0] = rowIdx-i;
                            this.freeSpace[1] = columnIdx+i;
                        } else if(randomValue == 1){
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+3);
                            this.freeSpace[1] = columnIdx+i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario --OO-
                        if(fullSet[i+5] == "-"){ // only choose this spot if the right is not blocked
                            this.freeSpace[0] = rowIdx-(i+4);
                            this.freeSpace[1] = columnIdx+i+4;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+1);
                            this.freeSpace[1] = columnIdx+i+1;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario --O-O, making sure the right is not blocked
                        this.freeSpace[0] = rowIdx-(i+3);
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario ---OO, making sure the right is not blocked
                        this.freeSpace[0] = rowIdx-(i+2);
                        this.freeSpace[1] = columnIdx+i+2;
                        return true;
                    } 
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean setOfOneinFive(String type, String piece){ 
        String[] fullSet;
        int rowIdx;
        int columnIdx;
        int randomValue = random.nextInt(4);
        if(type == "row"){  
            for(int j = 0; j < 19; j++){
                fullSet = getRow(j);
                this.freeSpace[0] = j;

                for(int i = 0; i < 14; i++){
                    if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario O----, making sure the left is not blocked
                        this.freeSpace[1] = i+1;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario -O---
                        if(i > 0 && fullSet[i-1] == "-"){ // only place a piece here if the left is not blocked
                            this.freeSpace[1] = i;
                        } else {
                            this.freeSpace[1] = i+2;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario --O--
                        if(randomValue == 1){
                            this.freeSpace[1] = i+1;
                        } else {
                            this.freeSpace[1] = i+3;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario ---O-
                        if(fullSet[i+5] == "-"){ // only choose this spot if the right is not blocked
                            this.freeSpace[1] = i+4;
                        } else {
                            this.freeSpace[1] = i+2;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario ----O, making sure the right is not blocked
                        this.freeSpace[1] = i+3;
                        return true;
                    } 
                }
            }
            return false;
        } else if(type == "column"){ 
            for(int j = 0; j < 19; j++){
                fullSet = getColumn(j);
                this.freeSpace[1] = j;

                for(int i = 0; i < 14; i++){
                    if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario O----, making sure the left is not blocked
                        this.freeSpace[0] = i+1;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario -O---
                        if(i > 0 && fullSet[i-1] == "-"){ // only place a piece here if the left is not blocked
                            this.freeSpace[0] = i;
                        } else {
                            this.freeSpace[0] = i+2;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario --O--
                        if(randomValue == 1){
                            this.freeSpace[0] = i+1;
                        } else {
                            this.freeSpace[0] = i+3;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario ---O-
                        if(fullSet[i+5] == "-"){ // only choose this spot if the right is not blocked
                            this.freeSpace[0] = i+4;
                        } else {
                            this.freeSpace[0] = i+2;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario ----O, making sure the right is not blocked
                        this.freeSpace[0] = i+3;
                        return true;
                    }  
                }
            } 
            return false;   
        } else if(type == "diagonalDown"){
            for(int j = 0; j < 28; j++){
                fullSet = getDiagonalDown(j);
            
                if(j <= 14){
                    rowIdx = 14-j;
                    columnIdx = 0;
                } else {
                    rowIdx = 0;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-5; i++){ 
                    if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario O----, making sure the left is not blocked
                        this.freeSpace[0] = rowIdx+i+1;
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario -O---
                        if(i > 0 && fullSet[i-1] == "-"){ // only place a piece here if the left is not blocked
                            this.freeSpace[0] = rowIdx+i;
                            this.freeSpace[1] = columnIdx;
                        } else {
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                        }  
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario --O--
                        if(randomValue == 1){
                            this.freeSpace[0] = rowIdx+i+1;
                            this.freeSpace[1] = columnIdx+i+1;
                        } else {
                            this.freeSpace[0] = rowIdx+i+3;
                            this.freeSpace[1] = columnIdx+i+3;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario ---O-
                        if(fullSet[i+5] == "-"){ // only choose this spot if the right is not blocked
                            this.freeSpace[0] = rowIdx+i+4;
                            this.freeSpace[1] = columnIdx+i+4;
                        } else {
                            this.freeSpace[0] = rowIdx+i+2;
                            this.freeSpace[1] = columnIdx+i+2;
                        }  
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario ----O, making sure the right is not blocked
                        this.freeSpace[0] = rowIdx+i+3;
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    }  
                }
            }
            return false;
        } else if(type == "diagonalUp"){ 
            for(int j = 0; j < 28; j++){
                fullSet = getDiagonalUp(j);
            
                if(j <= 14){
                    rowIdx = j+4;
                    columnIdx = 0;
                } else {
                    rowIdx = 18;
                    columnIdx = j-14;
                }

                for(int i = 0; i < fullSet.length-5; i++){ 
                    if(i > 0 && fullSet[i-1] == "-" && fullSet[i] == piece && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario O----, making sure the left is not blocked
                        this.freeSpace[0] = rowIdx-(i+1);
                        this.freeSpace[1] = columnIdx+i+1;
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == piece && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario -O---
                        if(i > 0 && fullSet[i-1] == "-"){ // only place a piece here if the left is not blocked
                            this.freeSpace[0] = rowIdx-i;
                            this.freeSpace[1] = columnIdx+i;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == piece && fullSet[i+3] == "-" && fullSet[i+4] == "-"){ // check senario --O--
                        if(randomValue == 1){
                            this.freeSpace[0] = rowIdx-(i+1);
                            this.freeSpace[1] = columnIdx+i+1;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+3);
                            this.freeSpace[1] = columnIdx+i+3;
                        } 
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == piece && fullSet[i+4] == "-"){ // check senario ---O-
                        if(i > fullSet.length-5 && fullSet[i+5] == "-"){ // only place a piece here if the right is not blocked
                            this.freeSpace[0] = rowIdx-(i+4);
                            this.freeSpace[1] = columnIdx+i+4;
                        } else {
                            this.freeSpace[0] = rowIdx-(i+2);
                            this.freeSpace[1] = columnIdx+i+2;
                        }
                        return true;
                    } else if(fullSet[i] == "-" && fullSet[i+1] == "-" && fullSet[i+2] == "-" && fullSet[i+3] == "-" && fullSet[i+4] == piece && fullSet[i+5] == "-"){ // check senario ----O, making sure the right is not blocked
                        this.freeSpace[0] = rowIdx-(i+3);
                        this.freeSpace[1] = columnIdx+i+3;
                        return true;
                    }  
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean isEmptyFivebyFive(int row, int column){
        if(this.board[row][column] != "-"){ // check the space
            return false;
        } else if(this.board[row-2][column-2] != "-"){
            return false;
        } else if(this.board[row-1][column-2] != "-"){
            return false;
        } else if(this.board[row][column-2] != "-"){
            return false;
        } else if(this.board[row+1][column-2] != "-"){
            return false;
        } else if(this.board[row+2][column-2] != "-"){
            return false;
        } else if(this.board[row-2][column-1] != "-"){
            return false;
        } else if(this.board[row-1][column-1] != "-"){
            return false;
        } else if(this.board[row][column-1] != "-"){
            return false;
        } else if(this.board[row+1][column-1] != "-"){
            return false;
        } else if(this.board[row+2][column-1] != "-"){
            return false;
        } else if(this.board[row-2][column] != "-"){
            return false;
        } else if(this.board[row-1][column] != "-"){
            return false;
        } else if(this.board[row+1][column] != "-"){
            return false;
        } else if(this.board[row+2][column] != "-"){
            return false;
        } else if(this.board[row-2][column+1] != "-"){
            return false;
        } else if(this.board[row-1][column+1] != "-"){
            return false;
        } else if(this.board[row][column+1] != "-"){
            return false;
        } else if(this.board[row+1][column+1] != "-"){
            return false;
        } else if(this.board[row+2][column+1] != "-"){
            return false;
        } else if(this.board[row-2][column+2] != "-"){
            return false;
        } else if(this.board[row-1][column+2] != "-"){
            return false;
        } else if(this.board[row][column+2] != "-"){
            return false;
        } else if(this.board[row+1][column+2] != "-"){
            return false;
        } else if(this.board[row+2][column+2] != "-"){
            return false;
        } else {
            this.freeSpace[0] = row;
            this.freeSpace[1] = column;
            return true;
        }
    }

    private boolean isEmptySetOfFive(int row, int column){
        if(this.board[row][column].equals("X") || this.board[row][column].equals("O")){
            return false;
        } else if(row < 15 && this.board[row+1][column].equals("-") && this.board[row+2][column].equals("-") && this.board[row+3][column].equals("-") && this.board[row+4][column].equals("-")){ // row: checking for four empty spots after the piece
            return true;
        } else if(column < 15 && this.board[row][column+1].equals("-") && this.board[row][column+2].equals("-") && this.board[row][column+3].equals("-") && this.board[row][column+4].equals("-")){ // column: checking for four empty spots after the piece
            return true;
        } else if(row < 15 && column < 15 && this.board[row+1][column+1].equals("-") && this.board[row+2][column+2].equals("-") && this.board[row+3][column+3].equals("-") && this.board[row+4][column+4].equals("-")){ // diagonalUp: checking for four empty spots after the piece
            return true;
        } else if(row > 3 && column < 15 && this.board[row-1][column+1].equals("-") && this.board[row-2][column+2].equals("-") && this.board[row-3][column+3].equals("-") && this.board[row-4][column+4].equals("-")){ // diagonalDown: checking for four empty spots after the piece
            return true;
        } else if(row > 0 && row < 16 && this.board[row-1][column].equals("-") && this.board[row+1][column].equals("-") && this.board[row+2][column].equals("-") && this.board[row+3][column].equals("-")){ // row: checking for one empty spot before and three empty spots after the piece
            return true;
        } else if(column > 0 && column < 16 && this.board[row][column-1].equals("-") && this.board[row][column+1].equals("-") && this.board[row][column+2].equals("-") && this.board[row][column+3].equals("-")){ // column: checking for one empty spot before and three empty spots after the piece
            return true;
        } else if(row > 0 && row < 16 && column > 0 && column < 16 && this.board[row-1][column-1].equals("-") && this.board[row+1][column+1].equals("-") && this.board[row+2][column+2].equals("-") && this.board[row+3][column+3].equals("-")){ // diagonalUp: checking one empty spot before and three empty spots after the piece
            return true;
        } else if(row > 2 && row < 16 && column > 0 && column < 16 && this.board[row+1][column-1].equals("-") && this.board[row-1][column+1].equals("-") && this.board[row-2][column+2].equals("-") && this.board[row-3][column+3].equals("-")){ // diagonalDown: checking one empty spot before and three empty spots after the piece
            return true;
        } else if(row > 1 && row < 17 && this.board[row-2][column].equals("-") && this.board[row-1][column].equals("-") && this.board[row+1][column].equals("-") && this.board[row+2][column].equals("-")){ // row: checking for two empty spots before and two empty spots after the piece
            return true;
        } else if(column > 1 && column < 17 && this.board[row][column-2].equals("-") && this.board[row][column-1].equals("-") && this.board[row][column+1].equals("-") && this.board[row][column+2].equals("-")){ // column: checking for two empty spots before and two empty spots after the piece
            return true;
        } else if(row > 1 && row < 17 && column > 1 && column < 17 && this.board[row-2][column-2].equals("-") && this.board[row-1][column-1].equals("-") && this.board[row+1][column+1].equals("-") && this.board[row+2][column+2].equals("-")){ // diagonalUp: checking for two empty spots before and two empty spots after the piece
            return true;
        } else if(row > 1 && row < 17 && column > 1 && column < 17 && this.board[row+2][column-2].equals("-") && this.board[row+1][column-1].equals("-") && this.board[row-1][column+1].equals("-") && this.board[row-2][column+2].equals("-")){ // diagonalDown: checking for two empty spots before and two empty spots after the piece
            return true;
        } else if(row > 2 && row < 18 && this.board[row-3][column].equals("-") && this.board[row-2][column].equals("-") && this.board[row-1][column].equals("-") && this.board[row+1][column].equals("-")){ // row: checking for three empty spots before and one empty spot after the piece
            return true;
        } else if(column > 2 && column < 18 && this.board[row][column-3].equals("-") && this.board[row][column-2].equals("-") && this.board[row][column-1].equals("-") && this.board[row][column+1].equals("-")){ // column: checking for three empty spots before and one empty spot after the piece
            return true;
        } else if(row > 2 && row < 18 && column > 2 && column < 18 && this.board[row-3][column-3].equals("-") && this.board[row-2][column-2].equals("-") && this.board[row-1][column-1].equals("-") && this.board[row+1][column+1].equals("-")){ // diagonalUp: checking for three empty spots before and one empty spot after the piece
            return true;
        } else if(row > 0 && row < 16 && column > 2 && column < 18 && this.board[row+3][column-3].equals("-") && this.board[row+2][column-2].equals("-") && this.board[row+1][column-1].equals("-") && this.board[row-1][column+1].equals("-")){ // diagonalDown: checking for three empty spots before and one empty spot after the piece
            return true;
        } else if(row > 3 && this.board[row-4][column].equals("-") && this.board[row-3][column].equals("-") && this.board[row-2][column].equals("-") && this.board[row-1][column].equals("-")){ // row: checking for four empty spots before the piece
            return true;
        } else if(column > 3 && this.board[row][column-4].equals("-") && this.board[row][column-3].equals("-") && this.board[row][column-2].equals("-") && this.board[row][column-1].equals("-")){ // column: checking for four empty spots before the piece the piece
            return true;
        } else if(row > 3 && column > 3 && this.board[row-4][column-4].equals("-") && this.board[row-3][column-3].equals("-") && this.board[row-2][column-2].equals("-") && this.board[row-1][column-1].equals("-")){ // diagonalUp: checking for four empty spots before the piece
            return true;
        } else if(row < 15 && column > 3 && this.board[row+4][column-4].equals("-") && this.board[row+3][column-3].equals("-") && this.board[row+2][column-2].equals("-") && this.board[row+1][column-1].equals("-")){ // diagonalDown: checking for four empty spots before the piece
            return true;
        } else {
            return false;
        }
    }
}