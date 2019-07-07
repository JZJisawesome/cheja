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
 * <p>
 * Throuought the class, coordinates are backwards (y, x) as this makes working with arrays easier.<br>
 * Also a higher y value represents a lower tile on the board<br>
 * </p>
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
        public boolean hasMoved = false;//TODO: to be used if casteling as valid and TODO: to more quickly determine if pawn can move two spaces up
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
            pawn_upgrade, reg, castle,//todo: add other move types
        }
        
        //coordinates to and from
        public byte fromY, fromX, toY, toX;
        public MoveType moveType;//type of move this will be
    }
    
    /**
     * A board consists of an 8 by 8 array of pieces.
     * 
     * <p>
     * Note: when accessing this array coordinates are [y][x], backwards to normal notation
     * </p>
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
    
    /**
     * Currently in the middle of upgrading a pawn to another piece
     * 
     * <p>
     * Cannot move any piece if this is true; must change pawn first
     * </p>
     * 
     */
    private boolean inPawnUpgrade = false;
    
    //constructors
    Board() {}//fixme probably should initilize board array here insted of above
    
    Board(Board newBoard)
    {
        this.board = new Piece[8][8];
        
        //copy board
        for (int i = 0; i < 8; ++i)
        {
            for (int j = 0; j < 8; ++j)
            {
                //copy the piece
                this.board[i][j] = new Piece(newBoard.board[i][j].type, newBoard.board[i][j].isWhite);
                this.board[i][j].hasMoved = newBoard.board[i][j].hasMoved;
            }
        }
        
        this.inPawnUpgrade = newBoard.inPawnUpgrade;
        this.whiteTurn = newBoard.whiteTurn;
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
        if ((y <= 7) && (y >= 0) && (x <= 7) && (x >= 0))//in bounds
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
     * <p>
     * File format is as follows<br>
     * Line 1: "w" or "b" for current turn<br>
     * Line 2: "piece" which is the name of the enum of the piece | "w" or "b" for colour | "y" or "n" if has moved or not<br>
     * Line 3-65: repeat the above line for all pieces in first row, then second row and so on<br>
     * </p>
     * 
     * @param saveFile The file name
     * @return Whether the file was saved sucessfully
     */
    public boolean save(String saveFile)
    {
        //standard
        try
        {
            //attempt to open file
            FileWriter fileWriter = new FileWriter(saveFile);
            
            //write whos turn it is
            fileWriter.write(this.whiteTurn ? "w" : "b");
            fileWriter.write("\n");//end line
            
            //loop through the board array to save each piece
            for (int i = 0; i < 8; ++i)//loop for rows
            {
                for (int j = 0; j < 8; ++j)//loop for coloums
                {
                    Piece currentPiece = this.board[i][j];
                    
                    fileWriter.write(currentPiece.type.toString());
                    fileWriter.write(" ");//space between piece type and colour
                    fileWriter.write(currentPiece.isWhite ? "w" : "b");
                    fileWriter.write(" ");//space between colour and hasMoved
                    fileWriter.write(currentPiece.hasMoved ? "y" : "n");
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
     * <p>
     * File format is as follows<br>
     * Line 1: "w" or "b" for current turn<br>
     * Line 2: "piece" which is the name of the enum of the piece | "w" or "b" for colour | "y" or "n" if has moved or not<br>
     * Line 3-65: repeat the above line for all pieces in first row, then second row and so on<br>
     * </p>
     * 
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
            
            //load current turn
            boolean isWhiteTurn;
            char turnChar = fileReader.next().charAt(0);//save only the first character from the next string
            
            if (turnChar == 'w' || turnChar == 'b')//has to be one or the other colour
                isWhiteTurn = turnChar == 'w';//whites turn or not
            else
                return false;//has to be one or the other colour
            
            
            //loop through the array to load each piece
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
                    
                    char hasMovedChar = fileReader.next().charAt(0);//save only the first character from the next string
                    
                    //has to have moved or not
                    if (hasMovedChar == 'y' || hasMovedChar == 'n')
                        newPiece.hasMoved = hasMovedChar == 'y';//the piece has moved or not
                    else
                        return false;//has to be one or the other
                    
                    
                    temp[i][j] = newPiece;//fill in the board
                }
            }
            
            //todo verify board is valid here
            
            //copy good loaded board to current board and current turn
            this.whiteTurn = isWhiteTurn;
            this.board = temp;
            
            return true;//everything worked
        }
        catch (Exception e)//just want to catch anything
        {
            return false;//if something went wrong
        }
    }
    
    /**
     * Determine if a player has won: incomplete
     * 
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
            byte fromY = move.fromY;
            byte fromX = move.fromX;
            byte toY = move.toY;
            byte toX = move.toX;
            
            switch (move.moveType)
            {
                case reg:
                {
                    this.regMove(fromY, fromX, toY, toX);//safty of move already checked at start of function
                    break;//unreachable anaways
                }
                case castle:
                {
                    this.castle(fromY, fromX, toY, toX);//safty of move already checked at start of function
                    break;
                    //placeholder
                }
                case pawn_upgrade:
                {
                    this.regMove(fromY, fromX, toY, toX);//safty of move already checked at start of function
                    inPawnUpgrade = true;
                    return true;//it's still white's turn; must upgrade pawn now
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
    public boolean validMove(byte fromY, byte fromX, byte toY, byte toX)
    {
        //have to check every kind of move with those coordinates to see if one would work
        boolean pawnUpgradeValid = this.validMove(new Move(fromY, fromX, toY, toX, Move.MoveType.pawn_upgrade));
        boolean regMoveValid = this.validMove(new Move(fromY, fromX, toY, toX, Move.MoveType.reg));
        boolean castleValid = this.validMove(new Move(fromY, fromX, toY, toX, Move.MoveType.castle));
        
        return pawnUpgradeValid || regMoveValid || castleValid;//as more move types are added, this function will have to check more
    }
    
    /**
     * Determine whether the information in a Move object is valid
     * 
     * <p>
     * Also checks for if a move would end in a check
     * </p>
     * 
     * @param move The move object
     * @return Whether the move is valid or not
     */
    //can be used repititively on all tiles of board to find all valid places to move
    public boolean validMove(Move move)
    {
        if (validMoveIgnoreCheck(move))//if the move is valid otherwise
            return !wouldBeInCheck(move);//make sure we would not be in check if we did it
        else
            return false;//not even valid
    }
    
    /**
     * Creates a move from the sets of coordinates given in the following priority:
     * 
     * Pawn upgrade, regular, castle
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
        Move pawnUpgradeMove = new Move(fromY, fromX, toY, toX, Move.MoveType.pawn_upgrade);//pawn upgrade move
        if (this.validMove(pawnUpgradeMove))
        {
            return pawnUpgradeMove;
        }
        
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
    private void regMove(byte fromY, byte fromX, byte toY, byte toX)
    {
        this.board[toY][toX] = this.board[fromY][fromX];//copy piece
        this.board[fromY][fromX] = new Piece(PieceType.none, false);//delete original
        
        this.board[toY][toX].hasMoved = true;//piece now in new location has moved
    }
    
    //assumes move would be valid and piece is king
    /**
     * Castling move of a king
     * @param fromY The y coordinate of the king
     * @param fromX The x coordinate of the king
     * @param toY The y coordinate of the new location
     * @param toX The x coordinate of the new location
     */
    private void castle(byte fromY, byte fromX, byte toY, byte toX)
    {
        byte row = fromY;//only diffrence between white and black is the row
        
        this.regMove(fromY, fromX, toY, toX);//move king
        
        //queen side castle
        if (toX == 2)
        {
            this.regMove(row, (byte) 0, row, (byte) 3);//move rook to right of king
        }
        //king side castle
        else if (toX == 6)
        {
            this.regMove(row, (byte) 7, row, (byte) 5);//move rook to left of king
        }
    }
    
    //individual valilidy checkers for specific pieces and move types
    
    //assumes from coordinates are of a pawn and is right turn
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
            else if (!fromPiece.hasMoved && toY == 4 && (this.board[5][fromX].type == PieceType.none) && toPiece.type == PieceType.none && toX == fromX)
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
            else if (!fromPiece.hasMoved && toY == 3 && (this.board[2][fromX].type == PieceType.none) && toPiece.type == PieceType.none && toX == fromX)
                return true;
            //pawn move one row up; pawn will also move one to the left or right; pawn will move onto a tile with a piece on it and capture it
            else if (toY == fromY + 1 && (fromX + 1 == toX || fromX - 1 == toX) && toPiece.type != PieceType.none)
                return true;
        }
        
        return false;//by default
    }
    
    //assumes from coordinates are of a rook and is right turn
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
    
    //assumes from coordinates are of a bishop and is right turn
    /**
     * Determines whether the regular movement of a bishop would be valid or not
     * 
     * <p>
     * Cascading for loops are not used for the j value. This is because<br>
     * i and j must increase together, so that the bishop can only move diagonally.<br>
     * We must however sill start in the proper position and prevent out of bounds,<br>
     * so the guts of this for loop are spilt into several different locations<br>
     * for each diagonal direction.<br>
     * <br>
     * Side note:<br>
     * This could have been done with significantly less code (both this and<br>
     * the rook logic) but dividing the search into parts (4 surrounding quadrants)<br>
     * (eg. toY < fromY && toX < fromX) makes us only have to step in<br>
     * one diagonal direction, saving time.<br>
     * Also I did not think about doing all directions with one loop at first,<br>
     * but hey this is more efficient so yay i guess.<br>
     * </p>
     * 
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
    
    /**
     * Determines whether a king that is attempting to check would be a valid move or not
     * @param fromY The y coordinate of the king
     * @param fromX The x coordinate of the king
     * @param toY The y coordinate where the king will end up after checking
     * @param toX The x coordinate where the king will end up after checking
     * @return Whether the move is valid or not
     */
    //asssumes it is right turn
    //todo make sure king is not in check before moving
    //todo make sure tiles king would have to cross would are not under attack and would place him in check
    private boolean castleValid(byte fromY, byte fromX, byte toY, byte toX)
    {
        Board.Piece fromPiece = this.board[fromY][fromX];
        Board.Piece toPiece = this.board[toY][toX];
        
        //only valid if we're moving a king, in the same row, and king has not moved
        if (fromPiece.type == PieceType.king && fromY == toY && !fromPiece.hasMoved)
        {
            if (fromPiece.isWhite)
            {
                //queen side castle
                if (toX == 2)
                {
                    //a1 is a rook and no pieces at b1 c1 or d1
                    if (this.board[7][0].type == PieceType.rook && this.board[7][1].type == PieceType.none && this.board[7][2].type == PieceType.none && this.board[7][3].type == PieceType.none)
                    {
                        return !this.board[7][0].hasMoved && this.board[7][0].isWhite;//rook must have not moved also and must be white
                    }
                    else
                        return false;
                }
                //king side castle
                else if (toX == 6)
                {
                    //f1 and g1 have no pieces; h1 is a rook
                    if (this.board[7][5].type == PieceType.none && this.board[7][6].type == PieceType.none && this.board[7][7].type == PieceType.rook)
                    {
                        return !this.board[7][7].hasMoved && this.board[7][7].isWhite;//rook must have not moved also and must be white
                    }
                    else
                        return false;
                }
                else
                    return false;//no other way to castle
            }
            else//piece is black
            {
                //queen side castle
                if (toX == 2)
                {
                    //a8 is a rook and no pieces at b8 c8 or d8
                    if (this.board[0][0].type == PieceType.rook && this.board[0][1].type == PieceType.none && this.board[0][2].type == PieceType.none && this.board[0][3].type == PieceType.none)
                    {
                        return !this.board[0][0].hasMoved && !this.board[0][0].isWhite;//rook must have not moved also and must be black
                    }
                    else
                        return false;
                }
                //king side castle
                else if (toX == 6)
                {
                    //f8 and g8 have no pieces; h8 is a rook
                    if (this.board[0][5].type == PieceType.none && this.board[0][6].type == PieceType.none && this.board[0][7].type == PieceType.rook)
                    {
                        return !this.board[0][7].hasMoved && !this.board[0][7].isWhite;//rook must have not moved also and must be black
                    }
                    else
                        return false;
                }
                else
                    return false;//no other way to castle
            }
        }
        else
            return false;
    }
    
    //fixme do not depend on validMove so it can depend on this function; otherwise we get bad recursion

    /**
     *
     * @param y
     * @param x
     * @return
     */
    public boolean inCheck(byte y, byte x)
    {
        if (this.board[y][x].type == PieceType.king && this.board[y][x].isWhite == this.whiteTurn)
        {
            //fixme should not modify original board, instead make copy and test that
            this.whiteTurn = !this.whiteTurn;//pretend its the other persons turn for the moment

            //loop through entire board to see if anything can attack the king
            for (byte i = 0; i < 8; ++i)
            {
                for (byte j = 0; j < 8; ++j)
                {
                    //a piece can attack the king with one of these three kinds of moves
                    if (this.validMoveIgnoreCheck(new Move(i, j, y, x, Move.MoveType.reg)) ||
                        this.validMoveIgnoreCheck(new Move(i, j, y, x, Move.MoveType.castle)) ||
                        this.validMoveIgnoreCheck(new Move(i, j, y, x, Move.MoveType.pawn_upgrade)))
                    {
                        this.whiteTurn = !this.whiteTurn;//put the current turn back
                        return true;//king is in danger
                    }
                }
            }

            this.whiteTurn = !this.whiteTurn;//put the current turn back
            return false;//if no threats
        }
        else
        {
            throw new IllegalArgumentException("inCheck not king or piece colour != turn colour");
        }
    }
    
    /**
     *
     * @return
     */
    public boolean pawnUpgradeWaiting()
    {
        return inPawnUpgrade;
    }
    
    /** Upgrade a pawn to another piece if the pawn reaches the first or last row
     *
     * @param y The y coordinate of the pawn
     * @param x The x coordinate of the pawn
     * @param newType The piece to upgrade to
     * @return If the upgrade was sucessfull or not
     */
    public boolean upgradePawnTo(byte y, byte x, PieceType newType)
    {
        if (this.inPawnUpgrade && this.board[y][x].type == PieceType.pawn)
        {
            //in first or last row
            if (y == 0 || y == 7)
            {
                //we cant turn into a king or delete the piece
                if (newType != PieceType.king && newType != PieceType.none)
                {
                    this.board[y][x].type = newType;//transform the piece
                    this.inPawnUpgrade = false;//we did it
                    this.whiteTurn = !this.whiteTurn;//finally other person's turn now
                    return true;
                }
                else
                    return false;
            }
            else
                return false;
        }
        else
            return false;
    }
    
    
    private boolean wouldBeInCheck(Move moveToDo)
    {
        Board testbrd = new Board(this);//make a copy of the board to test and not affect the original
        
        if (testbrd.validMoveIgnoreCheck(moveToDo))//this move is valid
            testbrd.moveNoValidation(moveToDo);
        else
            throw new IllegalArgumentException("wouldBeInCheck move invalid");
        
        //after we move the piece, the turn changes, so change the turn back to the original
        //as we want to know if our own move will expose our own king
        testbrd.whiteTurn = !testbrd.whiteTurn;
        
        byte y = 0, x = 0;
        
        //loop through entire board to find king
        for (byte i = 0; i < 8; ++i)
        {
            for (byte j = 0; j < 8; ++j)
            {
                //right colour and is a king
                if (testbrd.board[i][j].isWhite == testbrd.whiteTurn && testbrd.board[i][j].type == Board.PieceType.king)
                {
                    y = i;
                    x = j;
                    
                    //no need to keep looping, we found the king
                    i = 8;
                    j = 8;
                }
            }
        }
        
        return testbrd.inCheck(y, x);
    }
    
    //completly independent from all functions except other valid move functions
    //eg regPawnMoveValid, etc, not either of the other "validMove" ones
    //used in the inCheck function as the this cannot depend on inCheck
    private boolean validMoveIgnoreCheck(Move move)
    {
        //to make things easier to read
        byte fromY = move.fromY;
        byte fromX = move.fromX;
        byte toY = move.toY;
        byte toX = move.toX;
        
        if ( !( (fromY <= 7) && (fromY >= 0) && (fromX <= 7) && (fromX >= 0) && (toY <= 7) && (toY >= 0) && (toX <= 7) && (toX >= 0) ) )
            return false;
        //if the coordinates are in bounds, then we do deeper checks
        
        if (this.inPawnUpgrade)
            return false;//we can never move if a pawn in the last or first row has not upgraded yet
        
        Board.Piece fromPiece = this.board[fromY][fromX];
        Board.Piece toPiece = this.board[toY][toX];
        
        if (fromPiece.type == PieceType.none)
            return false;//cannot move no piece
        
        if (fromPiece.isWhite != this.whiteTurn)
            return false;//only can move pieces that are yours
        
        //cannot attack one of your own pieces; ignores the colour of blank tiles as they might be black or white but it is meaningless for them
        if (toPiece.isWhite == fromPiece.isWhite && toPiece.type != PieceType.none)
            return false;
        
        switch (move.moveType)
        {
            case reg:
            {
                switch (fromPiece.type)
                {
                    case pawn:
                    {
                        //regular moves to the first and last row are replaced by pawn upgrades; if a pawn upgrade is valid then a regular is not
                        if (!(toY == 0 || toY == 7))//if we're not moving to the first or last row
                        {
                            return this.regPawnMoveValid(fromY, fromX, toY, toX);//check if a standard pawn move is valid
                        }
                        else
                            return false;
                        
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
                        //start of new rook moving implementation, not working yet but should be better
                        //Moves laterally
                        /*
                        if ((toY == fromY - 7 || toY == fromY + 7 || toX == fromX - 7 || toX == fromX + 7))
                            return true;
                        */
                        
                        //old one to be used for now until the new one works
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
                return this.castleValid(fromY, fromX, toY, toX);
                //break;
            }
            case pawn_upgrade:
            {
                if (this.regPawnMoveValid(fromY, fromX, toY, toX))
                {
                    if (toY == 0 || toY == 7)
                        return true;//pawn is either moving to top or bottom row; does not need to disguish between colours because they cannot move backwards
                }
                break;
            }
            default:
                return false;
        }
        
        return false;//if true was not returned earlier
    }
    
    //does not depend on a valid move at all
    private boolean moveNoValidation(Move move)
    {
        byte fromY = move.fromY;
        byte fromX = move.fromX;
        byte toY = move.toY;
        byte toX = move.toX;

        switch (move.moveType)
        {
            case reg:
            {
                this.regMove(fromY, fromX, toY, toX);//safty of move already checked at start of function
                break;//unreachable anaways
            }
            case castle:
            {
                this.castle(fromY, fromX, toY, toX);//safty of move already checked at start of function
                break;
                //placeholder
            }
            case pawn_upgrade:
            {
                this.regMove(fromY, fromX, toY, toX);//safty of move already checked at start of function
                inPawnUpgrade = true;
                return true;//it's still white's turn; must upgrade pawn now
            }
            default:
                return false;//exit function here if somehow something went wrong
        }

        this.whiteTurn = !this.whiteTurn;//other person's turn now
        return true;
    }
}