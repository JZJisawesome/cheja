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

public class Board//chess board storage class
{
    public static enum PieceType
    {
        none, pawn, knight, rook, bishop, queen, king
    };
    
    public static class Piece
    {
        Piece(PieceType t, boolean isW)//constructor
        {
            this.type = t;
            this.isWhite = isW;
        }
        
        //a chess piece is both a certain type and a either black or white
        public PieceType type;
        public boolean isWhite;//should be ignored for the "none" PieceType
    }
    
    //8 by 8 array of pieces
    //note when accessing this array coordinates are [y][x]
    Piece board[][] =// new Piece[8][8]//fixme ensure dimentions can only be 8 * 8
    {
        {new Piece(PieceType.rook, false), new Piece(PieceType.knight, false), new Piece(PieceType.bishop, false), new Piece(PieceType.queen, false), new Piece(PieceType.king, false), new Piece(PieceType.bishop, false), new Piece(PieceType.knight, false), new Piece(PieceType.rook, false)},
        {new Piece(PieceType.pawn, false), new Piece(PieceType.pawn, false), new Piece(PieceType.pawn, false), new Piece(PieceType.pawn, false), new Piece(PieceType.pawn, false), new Piece(PieceType.pawn, false), new Piece(PieceType.pawn, false), new Piece(PieceType.pawn, false)},
        {new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false)},
        {new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false)},
        {new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false)},
        {new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false), new Piece(PieceType.none, false)},
        {new Piece(PieceType.pawn, true), new Piece(PieceType.pawn, true), new Piece(PieceType.pawn, true), new Piece(PieceType.pawn, true), new Piece(PieceType.pawn, true), new Piece(PieceType.pawn, true), new Piece(PieceType.pawn, true), new Piece(PieceType.pawn, true)},
        {new Piece(PieceType.rook, true), new Piece(PieceType.knight, true), new Piece(PieceType.bishop, true), new Piece(PieceType.queen, true), new Piece(PieceType.king, true), new Piece(PieceType.bishop, true), new Piece(PieceType.knight, true), new Piece(PieceType.rook, true)},
    };
    
    //constructors
    Board() {}//fixme probably should initilize board array here
    
    Board(Piece brd[][])//fixme ensure only 8 * 8 sized arrays can be assigned
    {
        this.board = brd;
    }
    
    //public functions
    
    public void print()
    {
        System.out.println("┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓");//start of the board
        for (int i = 0; i < 8; ++i)//loop for coloums
        {
            System.out.print("┃ ");//print the left side of the board
            for (int j = 0; j < 8; ++j)//loop for rows
            {
                char piece;//stores character to print to represent a chess piece
                
                //checks type of piece here and varies it depending on its colour later
                switch (this.board[i][j].type)
                {
                    case bishop:
                    {
                        //eg.   if white  ................. w else b coloured tile
                        piece = this.board[i][j].isWhite ? '♗' : '♝';
                        break;
                    }
                    case king:
                    {
                        piece = this.board[i][j].isWhite ? '♔' : '♚';
                        break;
                    }
                    case knight:
                    {
                        piece = this.board[i][j].isWhite ? '♘' : '♞';
                        break;
                    }
                    case pawn:
                    {
                        piece = this.board[i][j].isWhite ? '♙' : '♟';
                        break;
                    }
                    case queen:
                    {
                        piece = this.board[i][j].isWhite ? '♕' : '♛';
                        break;
                    }
                    case rook:
                    {
                        piece = this.board[i][j].isWhite ? '♖' : '♜';
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
    
    public void save(String saveFile)//todo
    {
        //placeholder
    }
    
    public void load(String saveFile)//todo
    {
        //placeholder
    }
    
    public boolean hasWon(boolean checkWhite)//todo
    {
        return false;//placeholder
    }
    
    public static ArrayList validMoves(byte y, byte x)//todo
    {
        return new ArrayList();//placeholder
    }
    
    public boolean validMove(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        return false;//placeholder
    }
    
    //not whether a move would be smart or not; just the function does not check if the move would be valid
    public void dumbMove(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        //placeholder
    }
    
    //smarter move function that checks if move is valid before moving
    public boolean safeMove(byte fromY, byte fromX, byte toY, byte toX)
    {
        //check whether move is valid once
        boolean moveIsValid = this.validMove(fromY, fromX, toY, toX);
        
        if (moveIsValid)//move if it is valid
            this.dumbMove(fromY, fromX, toY, toX);
        
        return moveIsValid;//return whether the move was valid and therefore that it was moved
    }
}
