from random import randint
from BoardClasses import Move
from BoardClasses import Board
from Checker import Checker
import sys
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
        #black always moves first and is always player 1
        #self.color = 1 means AI is black
        #self.color = 2 mean AI is white

        # not sure what bottom code does
        if len(move) != 0:
            self.board.make_move(move, self.opponent[self.color])
        else:
            self.color = 1
        #############################
        moves = self.board.get_all_possible_moves(self.color)
        # loops through all moves, whichever moves jumps more than once, use that move
        jump = 2    # default is 2 since each move consists a list of at least length 2
        temp = 0
        king_flag = False   # set to true if a piece can be made a king
        for pieces in range(len(moves)):
            for positions in range(len(moves[pieces])):
                if len(moves[pieces][positions]) > jump:
                    temp = moves[pieces][positions]
                    jump = len(moves[pieces][positions])
                elif (len(moves[pieces][positions]) == jump) and self.canKing(moves[pieces][positions]):
                    temp = moves[pieces][positions]
                    jump = len(moves[pieces][positions])
                    king_flag = True
        mymove = self.minimax(moves)
        print("here", mymove)
        self.board.make_move(mymove, self.color)
        return mymove

        if temp != 0 or king_flag:
            move = temp
            self.board.make_move(move, self.color)
            return move
        else:
            # pick random move:
            index = randint(0,len(moves)-1)
            inner_index = randint(0, len(moves[index])-1)
            # index represents a checker, inner_index represents its possible moves
            move = moves[index][inner_index]
            self.board.make_move(move, self.color)
            return move
        ##############################
        # move = self.minimax(moves)
        # self.board.make_move(move, self.color)
        # return move

    def canKing(self, move):                # returns true if move will make checker king
        color = self.color
        checker = Checker(color, move[0])
        row = checker.row
        if checker.is_king:                  # return false if already king
            return False
        if checker.color == 'W':
            if row == 0:
                return True
            return False
        if row == self.row - 1:
            return True
        return False

    # Minimax:
    def minimax(self, moves):
        val, move = self.max_val(moves, -sys.maxsize, sys.maxsize)
        return move   # return move with max value along with its value

    def max_val(self, moves, a, b):
        # if end of game: return (win/lose/tie, move)
        if len(moves) == 0:
            if self.board.is_win(self.color) == self.color:
                return -sys.maxsize, ([])
            if self.board.is_win(self.color) != 0:
                return sys.maxsize, ([])
            return 0, ([])
        v = -sys.maxsize
        make_move = moves[0][0]
        pick_move, v = self.heuristic(moves,v)
        self.board.make_move(pick_move, self.color)
        temp_moves = self.board.get_all_possible_moves(self.opponent[self.color])
        min, min_move = self.min_val(temp_moves, a, b)
        print("Move returned:", pick_move)
        if min > v:
            v = min
            make_move = pick_move
            a = max(a, v)
        if a >= b:
            self.board.undo()
            return v, pick_move
        self.board.undo()
        return v, make_move

    def min_val(self, moves, a, b):
        # if end of game: return (win/lose/tie, move)
        print(self.board.black_count, self.board.white_count)
        if len(moves) == 0:
            if self.board.is_win(self.color) == self.color:
                return sys.maxsize, ([])
            if self.board.is_win(self.color) != 0:
                return -sys.maxsize, ([])
            return 0, ([])
        make_move = moves[0][0]
        v = sys.maxsize
        pick_move,v = self.heuristic(moves,v)
        self.board.make_move(pick_move, self.opponent[self.color])
        temp_moves = self.board.get_all_possible_moves(self.color)
        max, max_move = self.max_val(temp_moves, a, b)
        if max < v:
            v = max
            make_move = pick_move
            b = min(b, v)
        if a >= b:
            print(a,b)
            self.board.undo()
            return v, pick_move
        self.board.undo()
        return v, make_move


    def heuristic(self, moves,v):
        jump = 2  # default is 2 since each move consists a list of at least length 2
        temp = 0
        king_flag = False  # set to true if a piece can be made a king
        for pieces in range(len(moves)):
            for positions in range(len(moves[pieces])):
                if len(moves[pieces][positions]) > jump:
                    temp = moves[pieces][positions]
                    jump = len(moves[pieces][positions])
                elif (len(moves[pieces][positions]) == jump) and self.canKing(moves[pieces][positions]):
                    temp = moves[pieces][positions]
                    jump = len(moves[pieces][positions])
                    king_flag = True
        if temp != 0 or king_flag:
            v+=1
            move = temp
            return move, v
        else:
            # pick random move:
            index = randint(0, len(moves) - 1)
            inner_index = randint(0, len(moves[index]) - 1)
            # index represents a checker, inner_index represents its possible moves
            move = moves[index][inner_index]
            return move, v
