/* MIT License
 *
 * Copyright (c) 2019 John Jekel
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

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class CLI//will eventually take over from main function with actual user interaction
{
    CLI(Board brd)
    {
        this.board = brd;//the board will be copied by refrence, so changes here will be reflected in the Cheja main class
    }
    
    private Board board;
    private boolean byeByeSeeYouLater = false;
    
    private static enum Command
    {
        invalid, print, exit
    };
    
    public void begin()
    {
        Scanner input = new Scanner(System.in);
        String enteredLine;
        String commandString;
        Command command;
        
        while (!byeByeSeeYouLater)
        {
            System.out.print("cheja▹");//command prompt
            
            enteredLine = input.nextLine();
            input = new Scanner(enteredLine);
            
            commandString = input.next();
            
            try
            {
                command = Command.valueOf(commandString);
            }
            catch (IllegalArgumentException e)
            {
                command = Command.invalid;
            }
            
            switch (command)
            {
                case print:
                {
                    this.printBrd();
                    break;
                }
                case exit:
                {
                    byeByeSeeYouLater = true;
                    break;
                }
                case invalid:
                default:
                {
                    System.out.println("Invalid command: \"" + commandString + "\"");
                    System.out.println("Type \"help\" for help");//ha ha no help function yet
                    break;
                }
            }
            
            input = new Scanner(System.in);//so we can read new user input
        }
    }     
        
    public static void printBoard(Board board)
    {
        System.out.println("   a   b   c   d   e   f   g   h");//print letters for coloums
        System.out.println(" ┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓");//start of the board
        for (int i = 0; i < 8; ++i)//loop for rows
        {
            System.out.print(8 - i);//print numbers for rows
            System.out.print("┃ ");//print the left side of the board
            for (int j = 0; j < 8; ++j)//loop for coloums
            {
                char piece;//stores character to print to represent a chess piece
                
                //checks type of piece here and varies it depending on its colour later
                switch (board.getPiece(i, j).type)
                {
                    case bishop:
                    {
                        //eg.   if white  ................. w else b coloured tile
                        piece = board.getPiece(i, j).isWhite ? '♗' : '♝';
                        break;
                    }
                    case king:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♔' : '♚';
                        break;
                    }
                    case knight:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♘' : '♞';
                        break;
                    }
                    case pawn:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♙' : '♟';
                        break;
                    }
                    case queen:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♕' : '♛';
                        break;
                    }
                    case rook:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♖' : '♜';
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
    
    public static void printBoardFlipped(Board board)
    {
        System.out.println("   h   g   f   e   d   c   b   a");//print letters for coloums
        System.out.println(" ┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓");//start of the board
        for (int i = 7; i >= 0; --i)//loop for rows
        {
            System.out.print(8 - i);//print numbers for rows
            System.out.print("┃ ");//print the left side of the board
            for (int j = 7; j >= 0; --j)//loop for coloums
            {
                char piece;//stores character to print to represent a chess piece
                
                //checks type of piece here and varies it depending on its colour later
                switch (board.getPiece(i, j).type)
                {
                    case bishop:
                    {
                        //eg.   if white  ................. w else b coloured tile
                        piece = board.getPiece(i, j).isWhite ? '♗' : '♝';
                        break;
                    }
                    case king:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♔' : '♚';
                        break;
                    }
                    case knight:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♘' : '♞';
                        break;
                    }
                    case pawn:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♙' : '♟';
                        break;
                    }
                    case queen:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♕' : '♛';
                        break;
                    }
                    case rook:
                    {
                        piece = board.getPiece(i, j).isWhite ? '♖' : '♜';
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
    
    /*never mind this is unnessary and does not work with parameters
    just going to use an enum and .valueOf
    //no shame
    //https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
    //https://stackoverflow.com/questions/8261075/adding-multiple-entries-to-a-hashmap-at-once-in-one-statement
    //https://stackoverflow.com/questions/924285/efficiency-of-java-double-brace-initialization
    private static final Map<String, Runnable> COMMANDS = new HashMap<String, Runnable>()
    {   //double brace initilization
        {//() -> is fancy lambda stuffs
            put("print", () -> this.printBrd);
        }
    };
    */
    
    private void printBrd()
    {
        if (this.board.isWhiteTurn())//so both players can play from their side of the board
            printBoard(this.board);
        else
            printBoardFlipped(this.board);
    }
}
