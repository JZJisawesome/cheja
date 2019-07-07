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

/** A monolithic chess game for the terminal and the desktop: main class
 */
public final class Cheja//main program
{
    /** Semantic versioning number for Cheja; helpfull for version tracking
    */
    public static final String VERSION = "0.0.1";
    
    private static Board board = new Board();//create a new chess board to play
    private static CLI cli = new CLI(board);//to be used later

    /** The main function. Yep, that's about it
     * @param args Command line arguments; unused at the moment
     */
    public static void main(String[] args)
    {
        System.out.println("\t     ♞Cheja♞\n");
        
        System.out.println(" Note: colours may look reversed\n  if your terminal is white themed");
        System.out.println();
        
        CLI.printBoard(board);
        System.out.println();
        
        cli.begin();//returns when finished
        
        //exiting
        System.out.println("\nGoodbye!");
    }
    
}
