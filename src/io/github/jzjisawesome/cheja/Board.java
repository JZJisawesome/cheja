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

public class Board
{
    public enum PieceType
    {
        none, pawn, knight, rook, bishop, queen, king
    };
    
    public class Piece
    {
        Piece(PieceType t, boolean isW)
        {
            this.type = t;
            this.isWhite = isW;
        }
        
        public PieceType type;
        public boolean isWhite;
    }
    
    Piece board[][] = 
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
}
