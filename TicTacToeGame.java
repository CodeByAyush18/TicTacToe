import java.util.*;

public class TicTacToe {
    private char[][] board;
    private char currentPlayerMark;
    private boolean gameWon;
    private boolean isTournamentOver;
    private int boardSize;
    private List<String> players;
    private Map<String, Integer> scores;
    private List<List<String>> schedule;
    private int currentRound;
    private int currentMatch;

    public TicTacToe(int boardSize) {
        this.boardSize = boardSize;
        board = new char[boardSize][boardSize];
        currentPlayerMark = 'X';
        gameWon = false;
        isTournamentOver = false;
        scores = new HashMap<>();
        schedule = new ArrayList<>();
        currentRound = 0;
        currentMatch = 0;
    }

    private void initializeBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = '-';
            }
        }
    }

    private void displayBoard() {
        StringBuilder horizontalLine = new StringBuilder(" ");
        for (int i = 0; i < boardSize; i++) {
            horizontalLine.append("----");
        }
        horizontalLine.append("-");

        System.out.println(horizontalLine);
        for (int row = 0; row < boardSize; row++) {
            System.out.print("| ");
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == '-') {
                    System.out.print("_ | ");
                } else {
                    System.out.print(board[row][col] + " | ");
                }
            }
            System.out.println();
            System.out.println(horizontalLine);
        }
    }

    private boolean isBoardFull() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == '-')
                    return false;
            }
        }
        return true;
    }

    private boolean isWinner() {
        for (int i = 0; i < boardSize; i++) {
            if (board[i][0] == currentPlayerMark && board[i][1] == currentPlayerMark && board[i][2] == currentPlayerMark) {
                return true;
            }
            if (board[0][i] == currentPlayerMark && board[1][i] == currentPlayerMark && board[2][i] == currentPlayerMark) {
                return true;
            }
        }
        boolean diagonal1 = true;
        boolean diagonal2 = true;
        for (int i = 0; i < boardSize; i++) {
            diagonal1 &= (board[i][i] == currentPlayerMark);
            diagonal2 &= (board[i][boardSize - 1 - i] == currentPlayerMark);
        }
        if (diagonal1 || diagonal2) {
            return true;
        }
        return false;
    }

    private void switchPlayer() {
        currentPlayerMark = (currentPlayerMark == 'X') ? 'O' : 'X';
    }

    private void makeMove(int row, int col) {
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize && board[row][col] == '-') {
            board[row][col] = currentPlayerMark;
            gameWon = isWinner();
            switchPlayer();
        } else {
            System.out.println("Invalid move! Please try again.");
        }
    }

    public void startTournament(List<String> players) {
        this.players = players;
        initializeScores();
        generateSchedule();
        printSchedule();
        while (!isTournamentOver) {
            List<String> match = schedule.get(currentRound);
            String player1 = match.get(0);
            String player2 = match.get(1);
            System.out.println("\nRound " + (currentRound + 1) + ", Match " + (currentMatch + 1) + ": " + player1 + " vs " + player2);
            startMatch(player1, player2);
            currentMatch++;
            if (currentMatch >= schedule.get(currentRound).size() / 2) {
                currentMatch = 0;
                currentRound++;
                if (currentRound >= schedule.size()) {
                    isTournamentOver = true;
                }
            }
        }
        System.out.println("Tournament over!");
        displayScores();
    }

    private void initializeScores() {
        for (String player : players) {
            scores.put(player, 0);
        }
    }

    private void generateSchedule() {
        int numPlayers = players.size();
        int totalRounds = numPlayers - 1;
        int matchesPerRound = numPlayers / 2;
        List<String> rotatedPlayers = new ArrayList<>(players);
        rotatedPlayers.remove(0);
        for (int round = 0; round < totalRounds; round++) {
            List<List<String>> roundSchedule = new ArrayList<>();
            for (int match = 0; match < matchesPerRound; match++) {
                List<String> matchPair = new ArrayList<>();
                matchPair.add(players.get(0));
                matchPair.add(rotatedPlayers.get(match));
                roundSchedule.add(matchPair);
            }
            schedule.addAll(roundSchedule);
            Collections.rotate(rotatedPlayers, 1);
        }
    }

    private void printSchedule() {
        System.out.println("Tournament Schedule:");
        for (int i = 0; i < schedule.size(); i++) {
            List<String> match = schedule.get(i);
            String player1 = match.get(0);
            String player2 = match.get(1);
            System.out.println("Round " + (i + 1) + ": " + player1 + " vs " + player2);
        }
    }

    private void startMatch(String player1, String player2) {
        Scanner scanner = new Scanner(System.in);
        initializeBoard();
        gameWon = false;
        currentPlayerMark = 'X';
        while (!gameWon && !isBoardFull()) {
            displayBoard();
            System.out.println("It's " + ((currentPlayerMark == 'X') ? player1 : player2) + "'s turn (" + currentPlayerMark + ")");
            System.out.print("Enter row number (0-" + (boardSize - 1) + "): ");
            int row = scanner.nextInt();
            System.out.print("Enter column number (0-" + (boardSize - 1) + "): ");
            int col = scanner.nextInt();
            makeMove(row, col);
        }
        displayBoard();
        announceResult(player1, player2);
    }

    private void announceResult(String player1, String player2) {
        if (gameWon) {
            String winner = (currentPlayerMark == 'X') ? player1 : player2;
            System.out.println(winner + " wins!");
            scores.put(winner, scores.getOrDefault(winner, 0) + 1);
        } else {
            System.out.println("It's a draw!");
        }
    }

    private void displayScores() {
        System.out.println("Final Scores:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " points");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        int numPlayers = scanner.nextInt();
        List<String> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name of player " + i + ": ");
            String playerName = scanner.next();
            players.add(playerName);
        }
        System.out.print("Enter the size of the Tic Tac Toe board (e.g., 3 for 3x3, 4 for 4x4, etc.): ");
        int boardSize = scanner.nextInt();
        TicTacToe tournament = new TicTacToe(boardSize);
        tournament.startTournament(players);
        scanner.close();
    }
}
