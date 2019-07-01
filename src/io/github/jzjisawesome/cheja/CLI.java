/* MIT License
 *
 * Copyright (c) 2019 John Jekel, Rowan Massad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/
package io.github.jzjisawesome.cheja;

//for user input
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Terminal frontend for cheja
 */
public class CLI
{
    /**
     * Constructor that copies a Board object by refrence to use for the game
     * @param brd The board to use
     */
    CLI(Board brd)
    {
        this.board = brd;//the board will be copied by refrence, so changes here will be reflected in the original object
    }
    
    /**
     * Refrence to board for game
     */
    private Board board;
    
    /**
     * Whether to display black pieces for instead of white and vice-versa for proper colour with dark terminals
     */
    private static boolean invertPieceColour = true;//to make pieces proper colour
    
    /**
     * Start accepting user input in an interactive fashion
    */
    public void begin()
    {
        Scanner input = new Scanner(System.in);
        String enteredLine;
        String commandString = "";
        
        boolean byeByeSeeYouLater = false;
        
        while (!byeByeSeeYouLater)
        {
            System.out.print("cheja▹");//command prompt
            
            try
            {
                enteredLine = input.nextLine();
                input = new Scanner(enteredLine);

                commandString = input.next();
            }
            catch (NoSuchElementException e)//happens if user presses enter key without entering anything
            {
                input = new Scanner(System.in);//so we can read new user input again
                continue;//restart prompt
            }
            
            switch (commandString)
            {
                case "print":
                case "pr":
                case "p":
                {
                    this.printBrd();
                    break;
                }
                
                case "move":
                case "mv":
                case "m":
                {
                    this.move(input);
                    break;
                }
                case "list":
                case "l":
                {
                    this.list(input);
                    break;
                }
                case "save":
                case "sv":
                {
                    try
                    {
                        String filename = input.nextLine().substring(1);//cut off initial space but get rest of line
                        
                        System.out.println("Saving board to: \"" + filename + "\"...");
                        if (this.board.save(filename))//attempt to save file
                            System.out.println("Save complete!");
                        else
                            System.out.println("Save failed!");
                    }
                    catch (NoSuchElementException e)
                    {
                        System.out.println("Invalid syntax");
                        System.out.println("Type \"help\" for help");
                    }

                    break;
                }
                case "load":
                case "ld":
                {
                    try
                    {
                        String filename = input.nextLine().substring(1);//cut off initial space but get rest of line
                        
                        System.out.println("Loading board from: \"" + filename + "\"...");
                        if (this.board.load(filename))//attempt to load file
                            System.out.println("Load complete!");
                        else
                            System.out.println("Load failed!");
                    }
                    catch (NoSuchElementException e)
                    {
                        System.out.println("Invalid syntax");
                        System.out.println("Type \"help\" for help");
                    }
                    
                    break;
                }
                case "exit":
                {
                    byeByeSeeYouLater = true;
                    break;
                }
                case "help":
                case "h":
                case "?":
                {
                    saveTheUserFromHeadaches();
                    break;
                }
                case "about":
                case "cheja":
                {
                    displayAbout();
                    break;
                }
                case "hello":
                case "Hello":
                case "HELLO":
                {
                    //good ol easter egg
                    try
                    {
                        String plzBeThere = input.next();
                        
                        if (plzBeThere.equals("there") || plzBeThere.equals("There") || plzBeThere.equals("THERE"))
                        {
                            System.out.println("GENERAL KENOBI!");
                            continue;
                        }
                    }
                    catch (NoSuchElementException e) {}
                }
                default:
                {
                    System.out.println("Invalid command: \"" + commandString + "\"");
                    System.out.println("Type \"help\" for help");
                    break;
                }
            }
            
            input = new Scanner(System.in);//so we can read new user input
        }
    }     
    
    /**
     * Print the board to System.out in the same orientation of the board array (black at top, white at bottom)
     * @param board The board to print
     */
    public static void printBoard(Board board)
    {
        //invert the colour of the piece if the boolean in the class is enabled
        boolean originalClr = !invertPieceColour;
        
        System.out.println("   a   b   c   d   e   f   g   h");//print letters for coloums
        System.out.println(" ┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓");//start of the board
        for (int i = 0; i < 8; ++i)//loop for rows
        {
            System.out.print(8 - i);//print numbers for rows
            System.out.print("┃ ");//print the left side of the board
            for (int j = 0; j < 8; ++j)//loop for coloums
            {
                char piece;//stores character to print to represent a chess piece
                
                boolean pieceIsWhite = board.getPiece(i, j).isWhite;
                //invert the colour of the piece if the boolean in the class is enabled
                boolean displayAsWhite = originalClr ? pieceIsWhite : !pieceIsWhite;
                
                //checks type of piece here and varies it depending on its colour later
                switch (board.getPiece(i, j).type)
                {
                    case bishop:
                    {
                        //eg.   if white  ................. w else b coloured tile
                        piece = displayAsWhite ? '♗' : '♝';
                        break;
                    }
                    case king:
                    {
                        piece = displayAsWhite ? '♔' : '♚';
                        break;
                    }
                    case knight:
                    {
                        piece = displayAsWhite ? '♘' : '♞';
                        break;
                    }
                    case pawn:
                    {
                        piece = displayAsWhite ? '♙' : '♟';
                        break;
                    }
                    case queen:
                    {
                        piece = displayAsWhite ? '♕' : '♛';
                        break;
                    }
                    case rook:
                    {
                        piece = displayAsWhite ? '♖' : '♜';
                        break;
                    }
                    case none:
                    {
                        piece = ' ';
                        break;
                    }
                    default:
                    {
                        piece = '?';
                        break;
                    }
                }

                System.out.print(piece);
                
                System.out.print(" ┃");//close the right side of the tile
                
                if (j < 7)
                    System.out.print(" ");
                else
                    System.out.print(8 - i);//print numbers for rows
            }
            
            if (i < 7)//all except before last line where special bottom characters will be used
            {
                System.out.println();
                System.out.print(" ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫");//seperate new rows of tiles
            }
            
            
            System.out.println();
        }
        
        System.out.println(" ┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛");//end of the board
        System.out.println("   a   b   c   d   e   f   g   h");//print letters for coloums
        System.out.println("\t   " + (board.isWhiteTurn() ? "White" : "Black") + "'s turn!");//print who's turn it is
    }
    
    /**
     * Print the board to System.out but rotated upside-down from the board array (black at bottom, white at top)
     * @param board The board to print
     */
    public static void printBoardFlipped(Board board)
    {
        //invert the colour of the piece if the boolean in the class is enabled
        boolean originalClr = !invertPieceColour;
        
        System.out.println("   h   g   f   e   d   c   b   a");//print letters for coloums
        System.out.println(" ┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓");//start of the board
        for (int i = 7; i >= 0; --i)//loop for rows
        {
            System.out.print(8 - i);//print numbers for rows
            System.out.print("┃ ");//print the left side of the board
            for (int j = 7; j >= 0; --j)//loop for coloums
            {
                char piece;//stores character to print to represent a chess piece
                
                boolean pieceIsWhite = board.getPiece(i, j).isWhite;
                //invert the colour of the piece if the boolean in the class is enabled
                boolean displayAsWhite = originalClr ? pieceIsWhite : !pieceIsWhite;
                
                //checks type of piece here and varies it depending on its colour later
                switch (board.getPiece(i, j).type)
                {
                    case bishop:
                    {
                        //eg.   if white  ................. w else b coloured tile
                        piece = displayAsWhite ? '♗' : '♝';
                        break;
                    }
                    case king:
                    {
                        piece = displayAsWhite ? '♔' : '♚';
                        break;
                    }
                    case knight:
                    {
                        piece = displayAsWhite ? '♘' : '♞';
                        break;
                    }
                    case pawn:
                    {
                        piece = displayAsWhite ? '♙' : '♟';
                        break;
                    }
                    case queen:
                    {
                        piece = displayAsWhite ? '♕' : '♛';
                        break;
                    }
                    case rook:
                    {
                        piece = displayAsWhite ? '♖' : '♜';
                        break;
                    }
                    case none:
                    {
                        piece = ' ';
                        break;
                    }
                    default:
                    {
                        piece = '?';
                        break;
                    }
                }

                System.out.print(piece);
                
                System.out.print(" ┃");//close the right side of the tile
                
                if (j > 0)
                    System.out.print(" ");
                else
                    System.out.print(8 - i);//print numbers for rows
            }
            
            if (i > 0)//all except before last line where special bottom characters will be used
            {
                System.out.println();
                System.out.print(" ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫");//seperate new rows of tiles
            }
            
            
            System.out.println();
        }
        
        System.out.println(" ┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛");//end of the board
        System.out.println("   h   g   f   e   d   c   b   a");//print letters for coloums
        System.out.println("\t   " + (board.isWhiteTurn() ? "White" : "Black") + "'s turn!");//print who's turn it is
    }
    
    //used internally
    
    /**
     * Parse input text to find coordinates to move between and move the piece.
     * 
     * eg. "a2 a3"
     * @param input The input to parse
     */
    private void move(Scanner input)
    {
        boolean parseWorked = false;//of both two tokens and individual 
        String from = null, to = null;//so compiler does not complain about uninitilization

        if (input.hasNext())
        {
            from = input.next();

            if (input.hasNext())
            {
                to = input.next();
                
                try
                {
                    byte fromY = (byte) (7 - (Character.getNumericValue(from.charAt(1)) - 1));//value of 0 to 7 from top
                    byte fromX = (byte) (from.charAt(0) - 'a');//value of 0 to 7
                    byte toY =   (byte) (7 - (Character.getNumericValue(to.charAt(1)) - 1));//value of 0 to 7 from bottom
                    byte toX =   (byte) (to.charAt(0) - 'a');//value of 0 to 7
                    
                    parseWorked = true;

                    if (!board.move(fromY, fromX, toY, toX))//moving piece was not valid
                        System.out.println("Invalid move");
                    else
                        this.printBrd();//show movement
                }
                catch (StringIndexOutOfBoundsException e)
                {
                    //parseWorked is already false, has not been set to true
                }
            }
        }
        
        if (!parseWorked)
        {
            System.out.println("Invalid syntax");
            System.out.println("Type \"help\" for help");
        }
    }
        
    //list moves
    /**
     * Parses the input for coordinates of a piece and prints all of the valid moves a piece can make
     * @param input The input to parse
     */
    private void list(Scanner input)
    {
        boolean parseWorked = false;
        String from;

        if (input.hasNext())
        {
            from = input.next();
            
            try
            {
                byte fromY = (byte) (7 - (Character.getNumericValue(from.charAt(1)) - 1));//value of 0 to 7 from top
                byte fromX = (byte) (from.charAt(0) - 'a');//value of 0 to 7

                System.out.print("Valid moves from \"" + from + "\" to: ");

                //loop through all coordinates to find valid moves
                for (byte i = 0; i < 8; ++i)//loop for rows
                {
                    for (byte j = 0; j < 8; ++j)//loop for coloums
                    {
                        if(board.validMove(fromY, fromX, i, j))
                            System.out.print(chessCoordinatesOf(i, j) + " ");
                    }
                }
                System.out.println();
            }
            catch (StringIndexOutOfBoundsException e)
            {
                System.out.println("Invalid syntax");
                System.out.println("Type \"help\" for help");
            }
        }
        else
        {
            System.out.println("Invalid syntax");
            System.out.println("Type \"help\" for help");
        }
    }
    
    /**
     * Convenience function to print the board oriented for the player that has to move
     */
    private void printBrd()
    {
        if (this.board.isWhiteTurn())//so both players can play from their side of the board
            printBoard(this.board);
        else
            printBoardFlipped(this.board);
    }
    
    //todo error checking
    /**
     * Helper function to convert board array coordinates to 
     */
    private static String chessCoordinatesOf(byte y, byte x)
    {
        return Character.toString((char) ('a' + x)) + (8 - y);
    }
    
    //does not make sense to use as returning a "pair" sucks in java. Written in line in list() and move()
    /*
     * Helper function to convert chess coordinates to board array coordinates. Not implemented yet
     */
    /*
    private static ??? boardCoordinatesOf(String coordinates)
    {
        byte fromY = (byte) (7 - (Character.getNumericValue(coordinates.charAt(1)) - 1));//value of 0 to 7 from top
        byte fromX = (byte) (coordinates.charAt(0) - 'a');//value of 0 to 7
        return "";//placeholder
    }
    */
    
    /**
     * Print command help text to the screen
     */
    private static void saveTheUserFromHeadaches()
    {
        
        System.out.println("\nCommands");
        
        System.out.println("╔══════╦═══════════════════════════════════════════════════╗");
        System.out.println("║print ║Print the chess board to the terminal again.       ║");
        System.out.println("║move  ║Move a piece to another location. (eg. move a2 a3) ║");
        System.out.println("║list  ║List the valid moves of a piece.                   ║");
        System.out.println("║save  ║Save the board to disk.      (eg. save foobar.brd) ║");
        System.out.println("║load  ║Load the board from disk.    (eg. load foobar.brd) ║");
        System.out.println("║help  ║Display information about various cheja commands.  ║");
        System.out.println("║about ║Display information about cheja itself cheja       ║");
        System.out.println("║exit  ║Quit cheja (be sure to save first).                ║");
        System.out.println("╚══════╩═══════════════════════════════════════════════════╝");
    }
    
    /**
     * Display information about cheja
    */
    private static void displayAbout()
    {
        System.out.println("\ncheja " + Cheja.VERSION);
        System.out.println("Monolithic chess game for the terminal and the desktop!");
        System.out.println("Built from the ground up by Rowan Massad and John Jekel\n");
        
        System.out.println("Licencing Information");
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║MIT License                                                                     ║");
        System.out.println("║                                                                                ║");
        System.out.println("║Copyright (c) 2019 John Jekel, Rowan Massad                                     ║");
        System.out.println("║                                                                                ║");
        System.out.println("║Permission is hereby granted, free of charge, to any person obtaining a copy    ║");
        System.out.println("║of this software and associated documentation files (the \"Software\"), to deal   ║");//to componsate for escape characters
        System.out.println("║in the Software without restriction, including without limitation the rights    ║");
        System.out.println("║to use, copy, modify, merge, publish, distribute, sublicense, and/or sell       ║");
        System.out.println("║copies of the Software, and to permit persons to whom the Software is           ║");
        System.out.println("║furnished to do so, subject to the following conditions:                        ║");
        System.out.println("║                                                                                ║");
        System.out.println("║The above copyright notice and this permission notice shall be included in all  ║");
        System.out.println("║copies or substantial portions of the Software.                                 ║");
        System.out.println("║                                                                                ║");
        System.out.println("║THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR      ║");//to componsate for escape characters
        System.out.println("║IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,        ║");
        System.out.println("║FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE     ║");
        System.out.println("║AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER          ║");
        System.out.println("║LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,   ║");
        System.out.println("║OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE   ║");
        System.out.println("║SOFTWARE.                                                                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
    }
}
