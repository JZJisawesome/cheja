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
        public Move(byte fromY, byte fromX, byte toY, byte toX, MoveType moveType)//constructor
        {
            this.fromY = fromY;
            this.fromX = fromX;
            this.toY = toY;
            this.toX = toX;
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
        if ((y <= 7) && (y >= 0) && (x <= 7) && (x >= 0))
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
                    break;//unreachable anaways
                }
                case castle:
                {
                    this.castle(move.fromY, move.fromX, move.toY, move.toX);//safty of move already checked at start of function
                    break;
                    //placeholder
                }
                default:
                    return false;//exit function here because somehow something went wrong
            }
            
            this.whiteTurn = !this.whiteTurn;//other person's turn now
            return true;
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
        
        //to save typing move later
        byte fromY = move.fromY;
        byte fromX = move.fromX;
        byte toY = move.toY;
        byte toX = move.toX;
        
        if (!((fromY <= 7) && (fromY >= 0) && (fromX <= 7) && (fromX >= 0) && (toY <= 7) && (toY >= 0) && (toX <= 7) && (toX >= 0)))
            return false;
        //if the coordinates are good, then we do deeper checks
        
        Board.Piece fromPiece = this.board[fromY][fromX];
        Board.Piece toPiece = this.board[toY][toX];
        
        if (fromPiece.type == PieceType.none)
            return false;//cannot move no piece
        
        switch (move.moveType)
        {
            case reg:
            {
                switch (fromPiece.type)
                {
                    case pawn:
                    {
                        //does not account for moving two spaces at start of the game or for attacking
                        if (fromPiece.isWhite)
                            if (toY == fromY - 1)//white pon would be 1 lower than the tile it could move to
                                return true;
                        else
                            if (toY == fromY + 1)//black pon would be 1 higher than the tile it could move to
                                return true;
                        
                        break;
                    }
                }
                break;
            }
            case castle:
            {
                if (fromPiece.type == PieceType.king)
                {
                    //placeholder//make sure the castle with the king will be valid
                }
                else
                    return false;
                break;
            }
            default:
                return false;
        }
        
        return false;
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
    
    //assumes move would be valid
    private void regMove(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        this.board[toY][toX] = this.board[fromY][fromX];
        this.board[fromY][fromX] = new Piece(PieceType.none, false);
    }
    
    //assumes move would be valid
    private void castle(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        //placeholder
    }
}
