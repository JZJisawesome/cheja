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

public class CLI//will eventually take over from main function with actual user interaction
{
    CLI(Board brd)
    {
        this.board = brd;//the board will be copied by refrence, so changes here will be reflected in the Cheja main class
    }
    
    private Board board;
    
    public void begin()
    {
        
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
    
    private void printBrd(boolean flipped)
    {
        if (flipped) 
            printBoardFlipped(this.board);
        else
            printBoard(this.board);
    }
}
