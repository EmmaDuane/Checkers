from random import randint
from BoardClasses import Move
from BoardClasses import Board
#The following part should be completed by students.
#Students can modify anything except the class name and existing functions and variables.
class StudentAI():

    def __init__(self,col,row,p):
        self.col = col
        self.row = row
        self.p = p
        self.board = Board(col,row,p)
        self.board.initialize_game()
        self.color = ''
        self.opponent = {1:2,2:1}
        self.color = 2

    def get_move(self, move):
        if len(move) != 0:
            self.board.make_move(move, self.opponent[self.color])
        else:
            self.color = 1
        moves = self.board.get_all_possible_moves(self.color)
        # old code:
        #index = randint(0,len(moves)-1)
        #inner_index =  randint(0,len(moves[index])-1)
        #index represents a checker, inner_index represents its possible moves
        #move = moves[index][inner_index]
        #self.board.make_move(move, self.color)
        #return move
        move = ab_pruning(moves, move)
        self.board.make_move(move, self.color)
        return move

# Useful functions from boardclass:
#   is_valid_move(self, chess_row, chess_col, target_row, target_col, turn)
#   is_win(self,turn)

#minimax algorithm
def minimax(moves, move):
    if is_win(self, turn):
        return move
    # Alpha-beta pruning:
def ab_pruning(moves, move):
    max = max_val(moves, move, -sys.maxsize, maxsize)
    return move # return move with max value


def max_val(moves, move, alpha, beta):
        # if end of game: return (state of current move, move)
    if is_win(self, turn):
        return sys.maxsize
    return -sys.maxsize

    for i in moves:
        min = min_val(moves, i, alpha, beta)
        if min >= beta:
            return sys.maxsize
        alpha = max(alpha, min)
    return alpha


def min_val(moves, move, alpha, beta):
        # if end of game: return (state of current move, move)
    if is_win(self, turn):
        return -sys.maxsize
    else:
        return sys.maxsize
    for i in moves:
        min= max_val(moves, i, alpha, beta)
        if alpha >= min:
            return -sys.maxsize
    beta = min(beta, min)
    return beta



