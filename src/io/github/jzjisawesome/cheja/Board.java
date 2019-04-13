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
            
    public static class Move//by convention this class should always contain info about a move that is valid
    {
        public Move(byte fromX, byte fromY, byte toX, byte toY, MoveType moveType)//constructor
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
        public byte fromY, fromX, toY, toX;
        public MoveType moveType;//type of move this will be
    }
    
    //8 by 8 array of pieces
    //note when accessing this array coordinates are [y][x]
    //todo: make private
    private Piece board[][] =// new Piece[8][8]//fixme ensure dimentions can only be 8 * 8
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
    
    private boolean whiteTurn = true;//white starts first in chess
    
    //constructors
    Board() {}//fixme probably should initilize board array here
    
    Board(Piece brd[][])//fixme ensure only 8 * 8 sized arrays can be assigned
    {
        this.board = brd;
    }

    public Piece[][] getBoard()
    {
        return board;
    }
    
    public Piece getPiece(int y, int x)
    {
        if (y <= 7 || y >= 0 || x <= 7 || x >= 0)
        {
            return board[y][x];
        }
        else
            throw new IllegalArgumentException("coordinates out of bounds");
    }

    public boolean isWhiteTurn()
    {
        return whiteTurn;
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
    
    public void save(String saveFile)
    {
        //placeholder
    }
    
    public void load(String saveFile)
    {
        //placeholder
    }
    
    public boolean hasWon(boolean checkWhite)
    {
        return false;//placeholder
    }

    //can move any piece and follow any Move.MoveType
    //smart; returns if move was sucessfull
    public boolean move(byte fromY, byte fromX, byte toY, byte toX)
    {
        try
        {
            return this.move(this.createMove(fromY, fromX, toY, toX));//create the move type and run the other move overload
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
    }
    
    //can move any piece and follow any Move.MoveType
    //smart; returns if move was sucessfull
    public boolean move(Move move)
    {
        if (this.validMove(move))
        {
            switch (move.moveType)
            {
                case reg:
                {
                    this.regMove(move.fromY, move.fromX, move.toY, move.toX);//safty of move already checked at start of function
                    return true;
                    //break;//unreachable anaways
                }
                case castle:
                {
                    this.castle(move.fromY, move.fromX, move.toY, move.toX);//safty of move already checked at start of function
                    return true;
                    //placeholder
                }
                default:
                    return false;
            }
        }
        else
            return false;
    }
    
    //can be used repititively on all tiles of board to find all valid places to move
    public boolean validMove(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        //note; cannot depend on any other function as almost all others depend on it
        
        //have to check every kind of move with those coordinates to see if one would work
        boolean regMoveValid = validMove(new Move(fromY, fromX, toY, toX, Move.MoveType.reg));
        boolean castleValid = validMove(new Move(fromY, fromX, toY, toX, Move.MoveType.castle));
        
        return regMoveValid || castleValid;//as more move types are added, this function will have to check more
    }
    
    //can be used repititively on all tiles of board to find all valid places to move
    public boolean validMove(Move move)//todo
    {
        //note; cannot depend on any other function as almost all others depend on it
        
        if (move.fromY <= 7 || move.fromY >= 0 || move.fromX <= 7 || move.fromX >= 0 || move.toY <= 7 || move.toY >= 0 || move.toX <= 7 || move.toX >= 0)
            return false;
        //if the coordinates are good, then we do deeper checks
        
        switch (move.moveType)
        {
            case reg:
            {
                //placeholder
            }
            case castle:
            {
                if ((this.board[move.fromX][move.fromY].type) == PieceType.king)
                {
                    //placeholder//make sure the castle with the kingwill be valid
                }
                else
                    return false;
                
            }
            default:
                return false;
        }
    }
    
    //will look at 4 coordinates and create a Move, detecting it's type in the process
    //throws exception if move would be invalid
    public Move createMove(byte fromY, byte fromX, byte toY, byte toX)
    {
        Move newMove = new Move(fromY, fromX, toY, toX, Move.MoveType.reg);//placeholder, not all moves are regular but this is only reg
        
        if (this.validMove(newMove))
        {
            return newMove;
        }
        else
            throw new IllegalArgumentException("createMove(move) invalid");
    }
    
    
    //private functions
    
    private void regMove(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        //placeholder
    }
    
    private void castle(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        //placeholder
    }
}
