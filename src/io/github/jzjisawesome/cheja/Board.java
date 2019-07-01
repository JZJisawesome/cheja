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

//for saving and loading functions
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

/**
 * Manages a chess board including piece movement and the status of the game
 * Throuought the class, coordinates are backwards (y, x) as this makes working with arrays easier.
 * Also a higher y value represents a lower tile on the board
 */
public class Board
{
    /**
     * Different types of chess pieces
     */
    public static enum PieceType//todo move inside Piece class like MoveType inside of Move
    {
        none, pawn, knight, rook, bishop, queen, king
    };//todo: in future probably faster to replace none pieces with null for speed
    
    /**
     * A piece consists of a PieceType, a colour and a whether it has moved yet
     */
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
        public boolean hasMoved;//TODO: to be used if casteling as valid and TODO: to more quickly determine if pawn can move two spaces up
    }
            
    /**
     * A move consists of two pairs of coordinates and the kind of move it is
     */
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
        
        /**
         * Different types of moves
         */
        public static enum MoveType//move function will decide how it will work based on this
        {
            reg, castle,//todo: add other move types
        }
        
        //coordinates to and from
        public byte fromY, fromX, toY, toX;
        public MoveType moveType;//type of move this will be
    }
    
    /**
     * A board consists of an 8 by 8 array of pieces.
     * 
     * Note: when accessing this array coordinates are [y][x], backwards to normal notation
     */
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
    
    /**
     * In chess, the white pieces start first
     */
    private boolean whiteTurn = true;
    
    //constructors
    Board() {}//fixme probably should initilize board array here insted of above
    
    Board(Piece brd[][])//fixme ensure only 8 * 8 sized arrays can be assigned
    {
        this.board = brd;
    }

    /**
     * Returns a copy of the chess board
     * @return A copy of the chess board
     */
    public Piece[][] getBoard()
    {
        return board;//todo: ensure this is a copy not a pass by refrence
    }
    
    /**
     * Returns a copy of a piece at a specific coordinate
     * @param y The y coordinate
     * @param x The x coordinate
     * @return The Piece
     * @throws IllegalArgumentException If the coordinates are out of bounds
     */
    public Piece getPiece(int y, int x) throws IllegalArgumentException
    {
        if ((y <= 7) && (y >= 0) && (x <= 7) && (x >= 0))
        {
            return board[y][x];
        }
        else
            throw new IllegalArgumentException("coordinates out of bounds");
    }

    /**
     * Returns if it is whites turn
     * @return If it is whites turn
     */
    public boolean isWhiteTurn()
    {
        return whiteTurn;
    }
    
    /**
     * Save the board to a file on disk
     * 
     * Not functional yet
     * @param saveFile The file name
     * @return Whether the file was saved sucessfully
     */
    public boolean save(String saveFile)
    {
        try
        {
            //attempt to open file
            FileWriter fileWriter = new FileWriter(saveFile);
            
            //write whos turn it is
            fileWriter.write(this.whiteTurn ? "w" : "b");
            fileWriter.write("\n");//end line
            
            //loop through the board array
            for (int i = 0; i < 8; ++i)//loop for rows
            {
                for (int j = 0; j < 8; ++j)//loop for coloums
                {
                    Piece currentPiece = this.board[i][j];
                    
                    fileWriter.write(currentPiece.type.toString());
                    fileWriter.write(" ");//space between piece type and colour
                    fileWriter.write(currentPiece.isWhite ? "w" : "b");
                    fileWriter.write("\n");//end line
                }
            }
            
            fileWriter.close();
            return true;//if everything worked
        }
        catch (Exception e)//just want to catch anything
        {
            return false;//if something went wrong
        }
    }
    
    /**
     * Load the board from a file on disk
     * 
     * Not functional yet
     * @param saveFile The file name
     * @return Whether the file was loaded sucessfully
     */
    public boolean load(String saveFile)
    {
        try
        {
            //try to open the file
            //any missing strings during reads will throw exceptions to be caught by finally and return false
            Scanner fileReader = new Scanner(new File(saveFile));
            
            //temporary board
            Piece temp[][] = new Piece[8][8];
            
            char turnChar = fileReader.next().charAt(0);//save only the first character from the next string
            
            if (turnChar == 'w' || turnChar == 'b')//has to be one or the other colour
                this.whiteTurn = turnChar == 'w';//whites turn or not
            else
                return false;//has to be one or the other colour
            
            //loop through the board array
            for (int i = 0; i < 8; ++i)//loop for rows
            {
                for (int j = 0; j < 8; ++j)//loop for coloums
                {
                    //probably could just initalize directly in temp but this is easier to read
                    Piece newPiece = new Piece(PieceType.none, false);//placeholder to be filled
                    
                    newPiece.type = PieceType.valueOf(fileReader.next());//convert string to piece type
                    
                    
                    char colourChar = fileReader.next().charAt(0);//save only the first character from the next string
                    
                    //has to be one or the other colour
                    if (colourChar == 'w' || colourChar == 'b')
                        newPiece.isWhite = colourChar == 'w';//the piece is white or not
                    else
                        return false;//has to be one or the other colour
                    
                    temp[i][j] = newPiece;//fill in the board
                }
            }
            
            //todo verify board is valid here
            
            //loop through the board array
            for (int i = 0; i < 8; ++i)//loop for rows
            {
                for (int j = 0; j < 8; ++j)//loop for coloums
                {
                    this.board[i][j] = temp[i][j];//copy piece from temporary board
                }
            }
            
            return true;//everything worked
        }
        catch (Exception e)//just want to catch anything
        {
            return false;//if something went wrong
        }
    }
    
    /**
     * Determine if a player has won
     * 
     * Not functional yet
     * @param checkWhite The player colour
     * @return Whether the player has won or not
     */
    public boolean hasWon(boolean checkWhite)
    {
        return false;//placeholder
    }

    /**
     * Attempts to move a piece from one location to another using the most priortized move type detected in createMove
     * 
     * @see createMove
     * 
     * @param fromY The y coordinate of the piece
     * @param fromX The x coordinate of the piece
     * @param toY The y coordinate of the new location
     * @param toX The x coordinate of the new location
     * @return If the move was successfull and valid or not
     */
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
    
    /**
     * Follows the move information defined in a move
     * 
     * @param move The Move object to follow
     * @return True if the move is valid, otherwise false
     */
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
                    return false;//exit function here if somehow something went wrong
            }
            
            this.whiteTurn = !this.whiteTurn;//other person's turn now
            return true;
        }
        else
            return false;
    }
    
    /**
     * Determines whether one of any kind of move type from one location to another would be valid
     * 
     * @param fromY The y coordinate of the piece
     * @param fromX The x coordinate of the piece
     * @param toY The y coordinate of the new location
     * @param toX The x coordinate of the new location
     * @return Whether at least one kind of move would be valid or not
     */
    //can be used repititively on all tiles of board to find all valid places to move
    public boolean validMove(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        //note; cannot depend on any other function as almost all others depend on it
        
        //have to check every kind of move with those coordinates to see if one would work
        boolean regMoveValid = validMove(new Move(fromY, fromX, toY, toX, Move.MoveType.reg));
        boolean castleValid = validMove(new Move(fromY, fromX, toY, toX, Move.MoveType.castle));
        
        return regMoveValid || castleValid;//as more move types are added, this function will have to check more
    }
    
    /**
     * Determine whether the information in a Move object is valid
     * @param move The move object
     * @return Whether the move is valid or not
     */
    //can be used repititively on all tiles of board to find all valid places to move
    public boolean validMove(Move move)//todo
    {
        //note; cannot depend on any other function as almost all others depend on it
        //only depends of other valid move functions
        
        //to make things easier to read
        byte fromY = move.fromY;
        byte fromX = move.fromX;
        byte toY = move.toY;
        byte toX = move.toX;
        
        if (!((fromY <= 7) && (fromY >= 0) && (fromX <= 7) && (fromX >= 0) && (toY <= 7) && (toY >= 0) && (toX <= 7) && (toX >= 0)))
            return false;
        //if the coordinates are in bounds, then we do deeper checks
        
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
                //cannot attack one of your own pieces; ignores the colour of blank tiles as they might be black or white but it is meaningless for them
                if (toPiece.isWhite == fromPiece.isWhite && toPiece.type != PieceType.none)
                    return false;//may need to move to the location of the rook when casteling and then recreate the rook elsewhere, so this applies only to regular moves
                
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
                        //I did not think or-ing the rook and bishop togeather would accually work
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
                        //start of new rook moving implementation
                        //Moves laterally
                        if ((toY == fromY - 7 || toY == fromY + 7 || toX == fromX - 7 || toX == fromX + 7))
                            return true;
                        
                        //old one to be kept for now until the new one works and is faster
                        //return this.regRookMoveValid(fromY, fromX, toY, toX);
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
    
    /**
     * Creates a move from the sets of coordinates given in the following priority:
     * 
     * Pon upgrade (incomplete), regular, castle
     * 
     * @param fromY The y coordinate of the piece
     * @param fromX The x coordinate of the piece
     * @param toY The y coordinate of the new location
     * @param toX The x coordinate of the new location
     * @return A Move object of how the piece will move
     * @throws IllegalArgumentException If the coordinates give will produce no type of valid move
     */
    //will look at 4 coordinates and create a Move, detecting it's type in the process
    //throws exception if move would be invalid
    private Move createMove(byte fromY, byte fromX, byte toY, byte toX) throws IllegalArgumentException
    {
        //pawn upgrade first
        
        Move _regMove = new Move(fromY, fromX, toY, toX, Move.MoveType.reg);//a regular move
        if (this.validMove(_regMove))
        {
            return _regMove;
        }
        
        Move castleMove = new Move(fromY, fromX, toY, toX, Move.MoveType.castle);//a castle move
        if (this.validMove(castleMove))
        {
            return castleMove;
        }
        
        //we only get here if validGrid said none of the move types worked 
        throw new IllegalArgumentException("createMove coordinates invalid");
    }
    
    //private functions
    
    //assumes move would be valid
    /**
     * Standard regular move of a piece from one coordinate to another
     * @param fromY The y coordinate of the piece
     * @param fromX The x coordinate of the piece
     * @param toY The y coordinate of the new location
     * @param toX The x coordinate of the new location
     */
    private void regMove(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        this.board[toY][toX] = this.board[fromY][fromX];//copy piece
        this.board[fromY][fromX] = new Piece(PieceType.none, false);//delete original
    }
    
    //assumes move would be valid and piece is king
    /**
     * Casteling move of a king
     * @param fromY The y coordinate of the king
     * @param fromX The x coordinate of the king
     * @param toY The y coordinate of the new location
     * @param toX The x coordinate of the new location
     */
    private void castle(byte fromY, byte fromX, byte toY, byte toX)//todo
    {
        //placeholder
    }
    
    //individual valilidy checkers for specific pieces and move types
    
    //assumes from coordinates are of a pawn
    /**
     * Determines whether the regular movement of a pawn would be valid or not
     * @param fromY The y coordinate of the pawn
     * @param fromX The x coordinate of the pawn
     * @param toY The y coordinate of the new location
     * @param toX The x coordinate of the new location
     * @return Whether the movement of the pawn would be valid or not
     */
    private boolean regPawnMoveValid(byte fromY, byte fromX, byte toY, byte toX)
    {
        Board.Piece fromPiece = this.board[fromY][fromX];
        Board.Piece toPiece = this.board[toY][toX];
        
        //lower=physically lower index; higher=physically higher index
        //nothing to do with chess coordinates
        
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
        else//everything kind of flips because the the pawns move in the opposite direction
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
    /**
     * Determines whether the regular movement of a rook would be valid or not
     * @param fromY The y coordinate of the rook
     * @param fromX The x coordinate of the rook
     * @param toY The y coordinate of the new location
     * @param toX The x coordinate of the new location
     * @return Whether the movement of the rook would be valid or not
     */
    private boolean regRookMoveValid(byte fromY, byte fromX, byte toY, byte toX)
    {
        Board.Piece fromPiece = this.board[fromY][fromX];
        Board.Piece toPiece = this.board[toY][toX];
        
        
        //rook in same row
        if (toY == fromY)
        {
            //rook is to the right of (greater than) toPiece
            if (toX < fromX)
            {
                //check if there are any pieces in between where rook is and where it wants to go (not including destination)
                for (int i = toX + 1; i < fromX; ++i)
                {
                    //is there a piece?
                    if (this.board[fromY][i].type != PieceType.none)
                        return false;
                }
            }
            //rook is to the left of (less than) toPiece
            else if (toX > fromX)
            {
                //check if there are any pieces in between where rook is and where it wants to go (not including destination)
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
            //rook is below (greater than) toPiece
            if (toY < fromY)
            {
                //check if there are any pieces in between where rook is and where it wants to go (not including destination)
                for (int i = toY + 1; i < fromY; ++i)
                {
                    //is there a piece?
                    if (this.board[i][fromX].type != PieceType.none)
                        return false;
                }
            }
            //rook is above (less than) toPiece
            else if (toY > fromY)
            {
                //check if there are any pieces in between where rook is and where it wants to go (not including destination)
                for (int i = fromY + 1; i < toY; ++i)
                {
                    //is there a piece?
                    if (this.board[i][fromX].type != PieceType.none)
                        return false;
                }
            }
        }
        else
            return false;//rook was not in same row or the same coloum
        
        return true;//if there were no pieces in the way
    }
    
    //assumes from coordinates are of a bishop
    /**
     * Determines whether the regular movement of a bishop would be valid or not
     * @param fromY The y coordinate of the bishop
     * @param fromX The x coordinate of the bishop
     * @param toY The y coordinate of the new location
     * @param toX The x coordinate of the new location
     * @return Whether the movement of the bishop would be valid or not
     */
    private boolean regBishopMoveValid(byte fromY, byte fromX, byte toY, byte toX)
    {
        Board.Piece fromPiece = this.board[fromY][fromX];
        Board.Piece toPiece = this.board[toY][toX];
        
        
        /* Cascading for loops are not used for the j value. This is because
         * i and j must increase togeather, so that the bishop can only move diaganolly.
         * We must however sill start in the proper position and prevent out of bounds,
         * so the guts of this for loop are spilt into several diffrent locations
         * for each diagonal direction.
         *
         * Side note:
         * This could have been done with significantly less code (both this and
         * the rook logic) but dividing the search into parts (4 surronding quadrants)
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
        
        return false;//if we fail to find the location in our search or the piece is in the same vertical position as the bishop
    }
}
