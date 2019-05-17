#include "ManualAI.h"
#include <iostream>

ManualAI::ManualAI(int col, int row, int k)
	:AI(col, row, k)
{
    board = Board(col,row,k);
    board.initializeGame();
    player = 2;
}

Move ManualAI::GetMove(Move move)
{

    if (move.seq.empty())
    {
        player = 1;
    } else{
        board.makeMove(move,player == 1?2:1);
    }
    vector<vector<Move>> moves = board.getAllPossibleMoves(player);

    for (int i = 0; i < moves.size();i++)
    {
        cout<<i<<": [";
        for (int j = 0; j < moves[i].size();j++)
        {
            cout<<j<<": "<<moves[i][j].toString()<<",";
        }
        cout<<"]"<<endl;
    }
    cout<<"Waiting input {int} {int}: ";
    int n,m;
    cin>>n>>m;
    Move res = moves[n][m];
    board.makeMove(res,player);
    return res;
}

