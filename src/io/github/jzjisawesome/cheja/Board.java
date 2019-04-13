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
    //todo: make private
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
    
    boolean whiteTurn = true;//white starts first in chess
    
    //constructors
    Board() {}//fixme probably should initilize board array here
    
    Board(Piece brd[][])//fixme ensure only 8 * 8 sized arrays can be assigned
    {
        this.board = brd;
    }
    
    //public functions
    
    public void print()
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
    }
    
    public void printFlipped()
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
    
    //will probably just run over the whole board checking which tiles would be valid to move to
    //effecience can be improved later
    //will be array of Moves
    public static ArrayList validMoves(byte y, byte x)//todo
    {
        return new ArrayList();//placeholder
    }

    //can move any piece and follow any Move.MoveType
    //smart; returns if move was sucessfull
    public boolean move(byte fromY, byte fromX, byte toY, byte toX)
    {
        return false;//placeholder
    }
    
    //can move any piece and follow any Move.MoveType
    //smart; returns if move was sucessfull
    public boolean move(Move move)
    {
        return false;//placeholder
    }
        
    public static class Move//by convention this class should always contain info about a move that is valid
    {
        public Move(int fromX, int fromY, int toX, int toY, MoveType moveType)//constructor
        {
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
            this.moveType = moveType;
        }
        
        public static enum MoveType//move function will decide how it will work based on this
        {
            reg, castle,//todo: add other move types
        }
        
        //coordinates to and from
        public int fromX, fromY, toX, toY;
        public MoveType moveType;//type of move this will be
    }
    
    
    
    
    //these should be unfavoured over smarter move functions above
        
    //will need to try all move types to and from that coodinate and see if it is possible
    public boolean validMove(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        return false;//placeholder
    }
    
    //probably should work on this instead
    public boolean validMove(Move move)//todo
    {
        return false;//placeholder
    }
    
    //function does not check if the move would be valid
    public void regMove(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        //placeholder
    }
    
    //smarter move function that checks if move is valid before moving
    //note: won't work until regMove and validMove are implemented
    //only regular moves
    public boolean safeRegMove(byte fromY, byte fromX, byte toY, byte toX)
    {
        //check whether move is valid once
        boolean moveIsValid = this.validMove(fromY, fromX, toY, toX);
        
        if (moveIsValid)//move if it is valid
            this.regMove(fromY, fromX, toY, toX);
        
        return moveIsValid;//return whether the move was valid and therefore that it was moved
    }
    
    //will look at 4 coordinates and create a Move, detecting it's type in the process
    //todo: throw exception if move would be invalid
    public Move createMove(byte fromY, byte fromX, byte toY, byte toX)
    {
        return new Move(0, 0, 0, 0, Move.MoveType.reg);//placeholder
    }
}
