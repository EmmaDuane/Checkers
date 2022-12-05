from random import randint
from BoardClasses import Move
from BoardClasses import Board
from Checker import Checker
import sys
import math
import random

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
        temp = self.board.get_all_possible_moves(self.color)
        moves = []
        for pieces in range(len(temp)):
            for positions in range(len(temp[pieces])):
                moves.append(temp[pieces][positions])
        mcts = MCTS(self.board, self.color, moves)
        my_move = mcts.tree_search()
        my_move = Move(my_move.move)
        self.board.make_move(my_move, self.color)
        return my_move

    # MCTS algorithm

    #   Tree traversal:
    #       while node is not leaf:
    #           starting at initial node, calculate ucb and choose node with max ucb
    #       once leaf node is found:
    #           check how many times leaf node has been sampled
    #           if leaf node has never been sampled then rollout(node)
    #           if leaf node has been sampled then expand(node)
    #   Node expansion:
    #       add new node to tree for every available action
    #       current node = new node
    #       rollout(node)
    #   Simulation rollout:
    #       while True:
    #           if node is leaf: return value(node)
    #           randomly select child node to simulate
    #           return result of simulation

class TreeNode():

    def __init__(self, board, color, moves, last_move=None, parent=None):
        self.board = board
        self.color = color
        self.opponent_color = 1 if color == 2 else 2
        self.moves = moves
        self.move = last_move       # move that resulted in this TreeNode
        self.parent = parent
        self.children = []
        self.is_leaf = True if moves == 0 or len(moves) == 0 else False    # check if node is leaf/terminal
        self.samples = 0        # number of times node has been sampled
        self.wins = 0           # number of times node led to win
        self.is_expanded = False     # is node completely expanded

class MCTS():

    def __init__(self, board, color, moves):
        self.color = color #should stay consistent if working correctly
        self.opponent_color = 1 if color == 2 else 2
        self.board = board
        self.root = TreeNode(board, color,moves)

    def tree_search(self):
        #expand node for the root
        #node will always have at most 6 children initially
        self.expand_node(self.root)
        minWins = 0;
        bestMove = random.choice(self.root.children)
        for x in self.root.children:
            print(x.wins)
            if x.wins < minWins:
                minWins = x.wins
                bestMove = x
        print("Best Move is: ",bestMove.move, bestMove.wins)
        return bestMove

    def expand_node(self, node):
        for move in node.moves:
            self.board.make_move(move, node.color)
            temp = self.board.get_all_possible_moves(node.opponent_color) #get moves for the next player
            next_moves = []
            for pieces in range(len(temp)):
                for positions in range(len(temp[pieces])):
                    next_moves.append(temp[pieces][positions])
            child = TreeNode(self.board, node.opponent_color, next_moves, move, node)
            node.children.append(child)
            #iterations
            for i in range(50):
                child.wins+=self.expand_until_end(child,0)
            self.board.undo()
        return node
    def expand_until_end(self, chosenChild, ex_depth): #rollout function
        #check to see if at leaf node
        if(len(chosenChild.moves) == 0):
            if chosenChild.color == self.color and chosenChild.board.is_win(self.color) == self.color:
                for i in range(ex_depth):
                    self.board.undo()
                return 1
            elif chosenChild.color == self.opponent_color and chosenChild.board.is_win(self.opponent_color) == self.opponent_color:
                for i in range(ex_depth):
                    self.board.undo()
                return -1
            else:
                for i in range(ex_depth):
                    self.board.undo()
                return 0
        if ex_depth > 350:
            print("expansion depth reached")

            return 0

        #self.color should be the color of our AI, below checks to see whose turn it is
        if (chosenChild.color == self.color):
            #if AI turn, do heuristics to find next move
            #heuristic function should actually return a list of ranked moves
            rChoice = self.heuristic(chosenChild)
            #rChoice = random.choice(chosenChild.moves)
            chosenChild.board.make_move(rChoice, chosenChild.color)
            tempMoves = chosenChild.board.get_all_possible_moves(chosenChild.opponent_color)  # get moves for the next player
            nextMoves = []
            for pieces in range(len(tempMoves)):
                for positions in range(len(tempMoves[pieces])):
                    nextMoves.append(tempMoves[pieces][positions])
            #create a child for the current child
            tempChild = TreeNode(chosenChild.board, chosenChild.opponent_color, nextMoves, rChoice, chosenChild)
            chosenChild.children.append(tempChild)
            return self.expand_until_end(tempChild, ex_depth+1)
        else:
            #if enemy turn, do a random move for them
            rChoice = self.heuristic(chosenChild)
            #rChoice = random.choice(chosenChild.moves)
            chosenChild.board.make_move(rChoice, chosenChild.color)
            tempMoves = chosenChild.board.get_all_possible_moves(chosenChild.opponent_color)  # get moves for the next player
            nextMoves = []
            for pieces in range(len(tempMoves)):
                for positions in range(len(tempMoves[pieces])):
                    nextMoves.append(tempMoves[pieces][positions])
            #create a child for the current child
            tempChild = TreeNode(chosenChild.board, chosenChild.opponent_color, nextMoves, rChoice, chosenChild)
            chosenChild.children.append(tempChild)
            return self.expand_until_end(tempChild,ex_depth+1)

    def heuristic(self, chosenChild):
        jump = 2  # default is 2 since each move consists a list of at least length 2
        temp = 0
        king_flag = False  # set to true if a piece can be made a king
        safeMoves = []
        for move in chosenChild.moves:
            if len(move) > jump:
                temp = move
                jump = len(move)
            elif (len(move) == jump) and self.canKing(chosenChild,move):
                temp = move
                jump = len(move)
                king_flag = True
            if temp == 0:
                # check to make a defensive move
                eNextMoves = chosenChild.board.get_all_possible_moves(chosenChild.opponent_color)
                # do not move to places where the enemy can move to, avoid being eaten
                enemyMoves = []
                for pieces in range(len(eNextMoves)):
                    for positions in range(len(eNextMoves[pieces])):
                        enemyMoves.append(eNextMoves[pieces][positions])
                for eMove in enemyMoves:
                    if move[1] != eMove[1]:
                        safeMoves.append(move)
        if temp != 0 or king_flag: #if multiple kills or can king
            bestmove = temp
            return bestmove
        elif len(safeMoves) != 0: #play safe move
            bestmove = random.choice(safeMoves)
            return bestmove
        else:
            bestmove = random.choice(chosenChild.moves)
            return bestmove

    def canKing(self, chosenChild,move):  # returns true if move will make checker king
        color = self.color
        checker = Checker(color, move)
        row = checker.row
        if checker.is_king:  # return false if already king
            return False
        if checker.color == 'W':
            if row == 0:
                return True
            return False
        if row == chosenChild.board.row - 1:
            return True
        return False