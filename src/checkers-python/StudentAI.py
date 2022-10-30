from random import randint
from BoardClasses import Move
from BoardClasses import Board
#The following part should be completed by students.
#Students can modify anything except the class name and exisiting functions and varibles.
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
        
        
# Alpha-beta pruning:
#   def ab_pruning(moves, move):
#       max = max_val(moves, move, -inf, +inf)
#       return move with max value
#   def max_val(moves, move, alpha, beta):
#       if end of game: return current move
#       for i in moves:
#           min = min_val(moves, i, alpha, beta)
#           if min >= beta
#               return +inf
#           alpha = max(alpha, min)
#       return alpha
#   def min_val(moves, move, alpha, beta):
#       if end of game: return current move
#       for i in moves:
#           min = max_val(moves, i, alpha, beta)
#           if alpha >= min
#               return -inf
#           beta = min(beta, min)
#       return beta


    def get_move(self,move):
        if len(move) != 0:
            self.board.make_move(move,self.opponent[self.color])
        else:
            self.color = 1
        moves = self.board.get_all_possible_moves(self.color)
        index = randint(0,len(moves)-1)
        inner_index =  randint(0,len(moves[index])-1)
        move = moves[index][inner_index]
        self.board.make_move(move,self.color)
        return move
