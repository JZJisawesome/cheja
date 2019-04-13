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

import java.util.ArrayList;


//decepreated; use Board member functions instead (yes i know i spelt that d word wrong)
//In retrospect, it makes more sense to have helper/management functions for an
//class to be part of that class; a mistake I made here and in 15Slide.
//Lucically there has been minimal development of cheja so far so this move should be relatively painless


public class BoardHelper//everything in here should be static; only local vars
{
    public static void printBoard(Board board)
    {
        System.out.println("┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓");//start of the board
        for (int i = 0; i < 8; ++i)//loop for coloums
        {
            System.out.print("┃ ");//print the left side of the board
            for (int j = 0; j < 8; ++j)//loop for rows
            {
                char piece;//stores character to print to represent a chess piece
                
                //checks type of piece here and varies it depending on its colour later
                switch (board.board[i][j].type)
                {
                    case bishop:
                    {
                        //eg.   if white  ................. w else b coloured tile
                        piece = board.board[i][j].isWhite ? '♗' : '♝';
                        break;
                    }
                    case king:
                    {
                        piece = board.board[i][j].isWhite ? '♔' : '♚';
                        break;
                    }
                    case knight:
                    {
                        piece = board.board[i][j].isWhite ? '♘' : '♞';
                        break;
                    }
                    case pawn:
                    {
                        piece = board.board[i][j].isWhite ? '♙' : '♟';
                        break;
                    }
                    case queen:
                    {
                        piece = board.board[i][j].isWhite ? '♕' : '♛';
                        break;
                    }
                    case rook:
                    {
                        piece = board.board[i][j].isWhite ? '♖' : '♜';
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
                
                System.out.print(" ┃ ");//close the right side of the tile
            }
            
            if (i < 7)//all except before last line where special bottom characters will be used
            {
                System.out.println();
                System.out.print("┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫");//seperate new rows of tiles
            }
            
            
            System.out.println();
        }
        
        System.out.println("┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛");//end of the board
    }
    
    public static void save(String saveFile, Board board)//todo
    {
        //placeholder
    }
    
    public static void load(String saveFile, Board board)//todo
    {
        //placeholder
    }
    
    public static boolean hasWon(Board board, boolean white)//todo
    {
        return false;//placeholder
    }
    
    public static ArrayList validMoves(Board board, Board.Piece piece, byte y, byte x)//todo
    {
        return new ArrayList();//placeholder
    }
    
    public static boolean validMove(Board board, Board.Piece piece, byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        return false;//placeholder
    }
    
    //not whether a move would be smart or not; just the function does not check if the move would be valid
    public static void dumbMove(Board board, Board.Piece piece, byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        //placeholder
    }
    
    //smarter move function that checks if move is valid before moving
    public static boolean safeMove(Board board, Board.Piece piece, byte fromY, byte fromX, byte toY, byte toX)
    {
        //check whether move is valid once
        boolean moveIsValid = validMove(board, piece, fromY, fromX, toY, toX);
        
        if (moveIsValid)//move if it is valid
            dumbMove(board, piece, fromY, fromX, toY, toX);
        
        return moveIsValid;//return whether the move was valid and therefore that it was moved
    }
}