#include "GameLogic.h"

GameLogic::GameLogic(int col, int row, int k, string mode,int order)
{
	this->col = col;
	this->row = row;
	this->k = k;
	this->mode = mode;
	this->order = order;
}

void GameLogic::Manual()
{
	int player = 1;
	int winPlayer = 0;
	bool init = true;
	Move move;
	Board board(col, row, k);
    board.initializeGame();
	while (true)
	{
		move = aiList[player - 1]->GetMove(move);
		try
		{
			board.makeMove(move, player);
		}
		catch (InvalidMoveError)
		{
			cout << "Invalid Move!" << endl;
			winPlayer = player == 1 ? 2 : 1;
			break;
		}
		winPlayer = board.isWin();
		board.showBoard();
		if (winPlayer != 0)
		{
			break;
		}
		player = player == 1 ? 2 : 1;

	}
	if (winPlayer == -1)
	{
	    cout<< "Tie"<<endl;
	}
	else
	{
	    cout << "Player " << winPlayer << " wins!" << endl;
	}

}

void GameLogic::TournamentInterface()
{
	StudentAI ai(col, row, k);
	while (true)
	{
		string instr;
		cin >> instr;
		Move result = ai.GetMove(Move(instr));
		cout << result.toString()<< endl;
	}
}

void GameLogic::Run()
{
	if (mode == "m" or mode == "manual")
	{
        AI* studentai = new StudentAI(col, row, k);
        AI* manualai = new ManualAI(col, row, k);
	    if (order == 0)
        {
            aiList.push_back(manualai);
            aiList.push_back(studentai);
        }
        else
        {
            aiList.push_back(studentai);
            aiList.push_back(manualai);

        }


		Manual();
	}
	else if (mode == "t")
	{
		TournamentInterface();
	}
}



GameLogic::~GameLogic()
{
	for (int i = 0; i < aiList.size(); i++)
	{
		delete aiList[i];
	}
}
