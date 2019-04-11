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

public class BoardHelper//everything in here should be static; only local vars
{
    public static void printBoard(Board board)
    {
        for (int i = 0; i < 8; ++i)
        {
            for (int j = 0; j < 8; ++j)
            {
                char piece;

                switch (board.board[i][j].type)
                {
                    case bishop:
                    {
                        piece = 'b';
                        break;
                    }
                    case king:
                    {
                        piece = 'K';
                        break;
                    }
                    case knight:
                    {
                        piece = 'k';
                        break;
                    }
                    case pawn:
                    {
                        piece = 'p';
                        break;
                    }
                    case queen:
                    {
                        piece = 'Q';
                        break;
                    }
                    case rook:
                    {
                        piece = 'r';
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
            }

            System.out.println();
        }
    }
    
    public static void save(String saveFile, Board board)
    {
        
    }
    
    public static void load(String saveFile, Board board)
    {
        
    }
    
    public static boolean hasWon(Board board, boolean white)
    {
        return false;
    }
    
    public static ArrayList validMoves(Board board, byte y, byte x)
    {
        return new ArrayList();
    }
}
