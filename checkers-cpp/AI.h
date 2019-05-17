#ifndef AI_H
#define AI_H
#include "Move.h"
#include "Board.h"
#pragma once

class AI
{
protected:
	int col, row, k, player;
public:

	AI(int col, int row, int k)
	{
		this->col = col;
		this->row = row;
		this->k = k;
	}
	virtual Move GetMove(Move move) = 0;
	virtual ~AI(){}
};

#endif //AI_H