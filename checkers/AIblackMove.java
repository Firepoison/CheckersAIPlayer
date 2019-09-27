
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author phil
 */
public class AIblackMove 
{

    //This is the current state of the game
    CheckersGame currentGame;
    //This array contains the legal moves at this point in the game for black.
    CheckersMove legalMoves[];
    int best_index;
    int calls;

    // The constructor.
    public AIblackMove(CheckersGame game, CheckersMove moves[]) 
    {
        currentGame = game;
        legalMoves = moves;
    }

    // This is where your logic goes to make a move.
    public CheckersMove nextMove() 
    {
        boolean firstcall = true;
        long start = System.currentTimeMillis();
        calls = 0;
        minimax(currentGame.boardData, CheckersData.BLACK,11, firstcall, -100000, 100000);
        System.out.println("Thinking time: "+(System.currentTimeMillis() - start)+"ms  MiniMax Calls: "+calls);
        return legalMoves[best_index];
        
        
        
       /* for(int i = 0; i < legalMoves.length; i++)
        {
            CheckersData new_board = new CheckersData(currentGame.boardData);
            currentGame.simulateMove(new_board, legalMoves[i], CheckersData.BLACK);
            int val = evaluate(new_board);
            if(val > best) 
            {
                best = val;
                best_index = 1;
            }
        }
        return(legalMoves[best_index]); */
        //Or you can create a copy of the current board like this:
        //CheckersData new_board = new CheckersData(currentGame.boardData);
        //You can then simulate a move on this new board like this:
        //currentGame.simulateMove(new_board, legalMoves[0],CheckersData.BLACK); 
        //After you simulate the move you can evaluate the state of the board
        //after the move and see how it looks.  You can evaluate all the 
        //currently legal moves using a loop and select the best one.
    }

    // One thing you will probably want to do is evaluate the current
    // goodness of the board.  This is a toy example, and probably isn't
    // very good, but you can tweak it in any way you want.  Not only is
    // number of pieces important, but board position could also be important.
    // Also, are kings more valuable than regular pieces?  How much?
    int evaluate(CheckersData board) 
    {
        int val= board.numBlack()+ (10*board.numBlackKing()) - board.numRed() - (5*board.numRedKing());
        if ((board.pieceAt(6,0) > 2) || (board.pieceAt(5,7) > 2) || (board.pieceAt(4,0) > 2) || (board.pieceAt(3,7) > 2))
            val+=2;
        if ((board.pieceAt(0,0) > 2) ||  (board.pieceAt(0,2) > 2) || (board.pieceAt(0,4) > 2) || (board.pieceAt(0,6) > 2))
            val+=6;
        if ((board.pieceAt(6,0) == 1) || (board.pieceAt(5,7) == 1) || (board.pieceAt(4,0) == 1) || (board.pieceAt(3,7) == 1))
            val-=2;
        //if ((board.pieceAt(0,0) == 2) ||  (board.pieceAt(0,2) == 2) || (board.pieceAt(0,4) == 2) || (board.pieceAt(0,6) == 2))
          //  val-=6;
        
        return val;
    }
    
    int minimax(CheckersData board, int color, int depth, boolean top, int alpha, int beta)
    {
        calls++;
        CheckersMove cur_legalMoves[] = board.getLegalMoves(color);
        if (top) 
           cur_legalMoves = legalMoves;
        //top = false;
        boolean isBlack  = (color == CheckersData.BLACK ? true : false);
        int best = (isBlack ? -234234 : 235235);
        if(board.getLegalMoves(color) == null)
            return best;
        int val;
        for(int i = 0; i < cur_legalMoves.length; i++)
        {
            CheckersData new_board = new CheckersData(board);
            currentGame.simulateMove(new_board, cur_legalMoves[i], color);
            if (depth == 0)
                val = evaluate(new_board);
            else val = minimax(new_board, (isBlack ? CheckersData.RED : CheckersData.BLACK), depth - 1, false, alpha, beta);
            if (isBlack)
            {
                if(cur_legalMoves[i].isJump())
                    val += 3;
                if(val > best) 
                {
                    best = val;
                    if(top)
                        best_index = i;
                }
                alpha = alpha > best ? alpha : best;
            }
            else 
            {
                if(cur_legalMoves[i].isJump())
                    val -= 3;
                if (val < best) 
                {
                    best = val;
                    if(top)
                        best_index = i;
                }
                beta = beta < best ? beta : best;
            }
            if (beta <= alpha)
                break;
             if (top) System.out.println("Index: "+i+" best_index= "+best_index);
        }
        return best;
    }
}
