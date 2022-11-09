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
                # curr_move = [pieces, temp[pieces][positions]]
                # moves.append(curr_move)
                moves.append(temp[pieces][positions])
        print(moves)
        mcts = MCTS(self.board, moves, self.color)
        my_move = mcts.tree_search()
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

    def __init__(self, board, moves, last_move=None, parent=None):
        self.board = board
        self.moves = moves          # move that resulted in this TreeNode
        self.move = last_move
        self.parent = parent
        self.children = []
        moves = moves
        if len(moves) == 0:         # check if node is leaf/terminal
            self.is_leaf = True
        else:
            self.is_leaf = False
        self.samples = 0        # number of times node has been sampled
        self.wins = 0           # number of times node led to win
        self.is_expanded = self.is_leaf     # is node completely expanded

class MCTS():

    def __init__(self, board, moves, color):
        self.color = color
        self.board = board
        self.root = TreeNode(board, moves)

    def tree_search(self):
        for i in range(100):                 # change, need to decide on stopping condition
            curr_node = self.select_node(self.root)         # select next node
            if curr_node is None:
                curr_node = random.choice(self.root.children)
            wins = self.rollout(curr_node)  # perform rollout on node
            self.backpropogate(curr_node, wins)   # backpropogate
        return self.best_move(curr_node, 0)

    def select_node(self, node):
        while node is not None and len(node.moves) != 0:         # while node is not a leaf
            if not node.is_expanded:        # if node is not expanded then expand it
                self.expand_node(node)
            else:                           # if node is expanded then get the best move
                node = self.best_move(node, 2)      # need to decide on exportation constant
        return node

    def expand_node(self, node):
        if len(node.moves) == 0:
            return None
        for move in node.moves:    # for each move in node.moves:
            try:
                self.board.make_move(move, self.color)
            except:
                continue
            temp = self.board.get_all_possible_moves(self.color)
            self.board.undo()
            next_moves = []
            for pieces in range(len(temp)):
                for positions in range(len(temp[pieces])):
                    # curr_move = [pieces, temp[pieces][positions]]
                    # next_moves.append(curr_move)
                    next_moves.append(temp[pieces][positions])
            child = TreeNode(self.board, next_moves, move, node)
            if child not in node.children:      # check if child in node.children, add if not
                node.children.append(child)
                if len(node.moves) == len(node.children):   # if node fully expanded,
                    node.is_expanded = True                 # then set node.is_expanded = True
                return None

    def rollout(self, node):
        if len(node.children) == 0:     # base case: check if game over
            if node.board.is_win(self.color) == self.color:
                return 1
            return 0
        # randomly select move for self and for opponent
        child = random.choice(node.children)
        node.board.make_move(child.move, self.color)
        opponent_color = 1 if self.color == 2 else 1
        opponent_moves = []
        temp = self.board.get_all_possible_moves(opponent_color)
        for pieces in range(len(temp)):
            for positions in range(len(temp[pieces])):
                # curr_move = [pieces, temp[pieces][positions]]
                # opponent_moves.append(curr_move)
                opponent_moves.append(temp[pieces][positions])
        opponent_move = random.choice(opponent_moves)
        node.board.make_move(opponent_move, opponent_color)
        result = self.rollout(child)
        self.board.undo()
        return result

    def backpropogate(self, node, wins):
        while node is not None:     # update node.samples & node.wins
            node.samples += 1
            node.wins += wins
            node = node.parent

    def best_move(self, node, exploration_const):
        # return node with highest win/sample ratio
        max = -sys.maxsize
        if len(node.children) == 0:
            return None
        max_child = None
        for child in node.children:
            if child.samples == 0:
                continue
            # calculate UCT
            uct = (child.wins / child.samples)
            uct += exploration_const * math.sqrt(math.log(node.samples) / child.samples)
            if uct > max:
                max = uct
                max_child = child
        return max_child
