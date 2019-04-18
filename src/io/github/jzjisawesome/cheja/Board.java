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
        
        if (fromPiece.isWhite != this.whiteTurn)
            return false;//only can move pieces that are yours
        
        switch (move.moveType)
        {
            case reg:
            {
                //cannot attack one of your own pieces; ignores the colour of blank tiles
                if (toPiece.isWhite == fromPiece.isWhite && toPiece.type != PieceType.none)
                    return false;//may need to "move" to the location of the rook when casteling, so this applies only to regular moves
                
                switch (fromPiece.type)
                {
                    case pawn:
                    {
                        return this.regPawnMoveValid(fromY, fromX, toY, toX);
                        //break;
                    }
                    case king:
                    {
                        //king is moving up or down one
                        if ((toY == fromY - 1 || toY == fromY + 1) && toX == fromX)
                            return true;
                        //king is left or right one
                        else if (toY == fromY && (toX == fromX - 1 || toX == fromX + 1))
                            return true;
                        //if king is moving diagonally one
                        else if ((toY == fromY - 1 || toY == fromY + 1) && (toX == fromX - 1 || toX == fromX + 1 ))
                            return true;
                        
                        break;
                    }
                    case queen:
                    {
                        //queen can move either like a rook or a bishop
                        return this.regRookMoveValid(fromY, fromX, toY, toX) ||
                               this.regBishopMoveValid(fromY, fromX, toY, toX);
                        //break;
                    }
                    case knight:
                    {
                        //going two up or down; going one left or right
                        if ((toY == fromY - 2 || toY == fromY + 2) && (toX == fromX - 1 || toX == fromX + 1 ))
                            return true;
                        //going one up or down; going two left or right
                        else if ((toY == fromY - 1 || toY == fromY + 1) && (toX == fromX - 2 || toX == fromX + 2 ))
                            return true;
                        
                        break;
                    }
                    case rook:
                    {
                        return this.regRookMoveValid(fromY, fromX, toY, toX);
                        //break;
                    }
                    case bishop:
                    {
                        return this.regBishopMoveValid(fromY, fromX, toY, toX);
                        //break;
                    }
                    case none:
                    default:
                    {
                        return false;
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
        
        return false;//if true was not returned earlier
    }
    
    //will look at 4 coordinates and create a Move, detecting it's type in the process
    //throws exception if move would be invalid
    private Move createMove(byte fromY, byte fromX, byte toY, byte toX)
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
    
    //individual valilidy checkers for specific pieces and move types
    
    //assumes from coordinates are of a pawn
    private boolean regPawnMoveValid(byte fromY, byte fromX, byte toY, byte toX)
    {
        Board.Piece fromPiece = this.board[fromY][fromX];
        Board.Piece toPiece = this.board[toY][toX];
        
        if (fromPiece.isWhite)
        {
            //white pawn would be 1 lower than the tile it could move to; in same coloum; piece not in the way
            if (toY == fromY - 1 && toX == fromX && toPiece.type == PieceType.none)
                return true;
            //pawn has not moved yet (initial position); wants to move 2 ahead; no piece between from and to; no piece where pawn will move to; in same coloum
            else if (fromY == 6 && toY == 4 && (this.board[5][fromX].type == PieceType.none) && toPiece.type == PieceType.none && toX == fromX)
                return true;
            //pawn move one row up; pawn will also move one to the left or right; pawn will move onto a tile with a piece on it and capture it
            else if (toY == fromY - 1 && (fromX - 1 == toX || fromX + 1 == toX) && toPiece.type != PieceType.none)
                return true;
        }
        else
        {
            //black pawn would be 1 higher than the tile it could move to; in same coloum; piece not in the way
            if (toY == fromY + 1 && toX == fromX && toPiece.type == PieceType.none)
                return true;
            //pawn has not moved yet (initial position); wants to move 2 ahead; no piece between from and to; no piece where pawn will move to; in same coloum
            else if (fromY == 1 && toY == 3 && (this.board[2][fromX].type == PieceType.none) && toPiece.type == PieceType.none && toX == fromX)
                return true;
            //pawn move one row up; pawn will also move one to the left or right; pawn will move onto a tile with a piece on it and capture it
            else if (toY == fromY + 1 && (fromX + 1 == toX || fromX - 1 == toX) && toPiece.type != PieceType.none)
                return true;
        }
        
        return false;//by default
    }
    
    //assumes from coordinates are of a rook
    private boolean regRookMoveValid(byte fromY, byte fromX, byte toY, byte toX)
    {
        Board.Piece fromPiece = this.board[fromY][fromX];
        Board.Piece toPiece = this.board[toY][toX];
        
        //rook is in the same row or the same coloum
        if (!(toY == fromY || toX == fromX))
            return false;
        
        //rook in same row
        if (toY == fromY)
        {
            //rook is to the right of toPiece
            if (toX < fromX)
            {
                //check if there are any pieces in between where rook is and where it wants to go
                for (int i = toX + 1; i < fromX; ++i)
                {
                    //is there a piece?
                    if (this.board[fromY][i].type != PieceType.none)
                        return false;
                }
            }
            //rook is to the left of toPiece
            else if (toX > fromX)
            {
                //check if there are any pieces in between where rook is and where it wants to go
                for (int i = fromX + 1; i < toX; ++i)
                {
                    //is there a piece?
                    if (this.board[fromY][i].type != PieceType.none)
                        return false;
                }
            }
        }
        //rook in same coloum
        else if (toX == fromX)
        {
            //rook is below toPiece
            if (toY < fromY)
            {
                //check if there are any pieces in between where rook is and where it wants to go
                for (int i = toY + 1; i < fromY; ++i)
                {
                    //is there a piece?
                    if (this.board[i][fromX].type != PieceType.none)
                        return false;
                }
            }
            //rook is above toPiece
            else if (toY > fromY)
            {
                //check if there are any pieces in between where rook is and where it wants to go
                for (int i = fromY + 1; i < toY; ++i)
                {
                    //is there a piece?
                    if (this.board[i][fromX].type != PieceType.none)
                        return false;
                }
            }
        }
        
        return true;//if there were no pieces in the way
    }
    
    //assumes from coordinates are of a bishop
    private boolean regBishopMoveValid(byte fromY, byte fromX, byte toY, byte toX)
    {
        Board.Piece fromPiece = this.board[fromY][fromX];
        Board.Piece toPiece = this.board[toY][toX];
        
        
        /* Seperate for loops are not used for the j value. This is because
         * i and j must increase togeather, so that the bishop can only move diaganolly.
         * We must however sill start in the proper position and prevent oob,
         * so the guts of this for loop are spilt into several diffrent locations
         * for each diagonal direction.
         *
         * Side note:
         * This could have been done with significantly less code (both this and
         * the rook logic) but dividing the search into parts
         * (eg. toY < fromY && toX < fromX) makes us only have to step in
         * one diagonal direction, saving time.
         * Also I did not think about doing all directions with one loop at first,
         * but hey this is more efficient so yay i guess.
        */
        
        //location is higher than bishop
        if (toY < fromY)
        {
            //location is to left of bishop
            if (toX < fromX)
            {
                //cannot be its own for loop because we need to step with i
                int j = fromX - 1;
                
                for (int i = fromY - 1; i >= 0; --i)
                {
                    if (toY == i && toX == j)//if this is the location
                        return true;
                    else if (this.board[i][j].type != PieceType.none)//there is a piece in our way
                        return false;//give up; we're blocked
                    
                    
                    //cannot be its own for loop because we need to step with i
                    --j;
                    
                    if (!(j >= 0))
                        break;//prevent out of bounds
                }
            }
            //location is to right of bishop
            else if (toX > fromX)
            {
                //cannot be its own for loop because we need to step with i
                int j = fromX + 1;
                
                for (int i = fromY - 1; i >= 0; --i)
                {
                    if (toY == i && toX == j)//if this is the location
                        return true;
                    else if (this.board[i][j].type != PieceType.none)//there is a piece in our way
                        return false;//give up; we're blocked
                    
                    
                    //cannot be its own for loop because we need to step with i
                    ++j;
                    
                    if (!(j <= 7))
                        break;//prevent out of bounds
                }
            }
        }
        //location is lower than bishop
        else if (toY > fromY)
        {
            //location is to left of bishop
            if (toX < fromX)
            {
                //cannot be its own for loop because we need to step with i
                int j = fromX - 1;
                
                for (int i = fromY + 1; i <= 7; ++i)
                {
                    if (toY == i && toX == j)//if this is the location
                        return true;
                    else if (this.board[i][j].type != PieceType.none)//there is a piece in our way
                        return false;//give up; we're blocked
                    
                    
                    //cannot be its own for loop because we need to step with i
                    --j;
                    
                    if (!(j >= 0))
                        break;//prevent out of bounds
                }
            }
            //location is to right of bishop
            else if (toX > fromX)
            {
                //cannot be its own for loop because we need to step with i
                int j = fromX + 1;
                
                for (int i = fromY + 1; i <= 7; ++i)
                {
                    if (toY == i && toX == j)//if this is the location
                        return true;
                    else if (this.board[i][j].type != PieceType.none)//there is a piece in our way
                        return false;//give up; we're blocked
                    
                    
                    //cannot be its own for loop because we need to step with i
                    ++j;
                    
                    if (!(j <= 7))
                        break;//prevent out of bounds
                }
            }
        }
        
        return false;//if we find the location in our search
    }
}
