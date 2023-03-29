public interface PenteInterface{
    public void startBoard();
    public void printBoard();
    public void printRules(); // print the game rules
    public boolean checkReady(String n); // check if the player is ready to start
    public String checkWin(); // returns "player" "computer" or "continue" depending on if the win condition has been met
    public void computerMove(int round); // determines the computers turn
    public boolean playerMove(String columnLetter, int row); // places the player's piece
}