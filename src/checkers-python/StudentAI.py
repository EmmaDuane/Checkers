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
        self.moves = moves
        self.is_leaf = True if moves == 0 or len(moves) == 0 else False    # check if node is leaf/terminal
        self.samples = 0        # number of times node has been sampled
        self.wins = 0           # number of times node led to win
        self.is_expanded = self.is_leaf     # is node completely expanded

class MCTS():

    def __init__(self, board, color, moves):
        self.color = color
        self.opponent_color = 1 if color == 2 else 2
        self.board = board
        self.root = TreeNode(board, color,moves)

    def tree_search(self):
        for i in range(10):                 # change, need to decide on stopping condition
            curr_node = self.select_node(self.root)         # select next node
            if curr_node is None:
                curr_node = random.choice(self.root.children)
            wins = self.rollout(curr_node)  # perform rollout on node
            self.backpropogate(curr_node, wins)   # backpropogate
        return self.best_move(self.root, 2)

    def select_node(self, node):
        while node.is_expanded:
            node = self.best_move(node, 2)
            if node is None:
                return node
        return self.expand_node(node) or node

    def expand_node(self, node):
        for move in node.moves:    # for each move in node.moves:
            try:
                self.board.make_move(move, node.color)
                temp = self.board.get_all_possible_moves(node.opponent_color)
                self.board.undo()
                next_moves = []
                for pieces in range(len(temp)):
                    for positions in range(len(temp[pieces])):
                        next_moves.append(temp[pieces][positions])
                child = TreeNode(self.board, node.opponent_color, next_moves, move, node)
                if child not in node.children:  # check if child in node.children, add if not
                    node.children.append(child)
                    if len(node.moves) == len(node.children):  # if node fully expanded,
                        node.is_expanded = True  # then set node.is_expanded = True
                    return child
            except:
                continue

    def rollout(self, node):
        if len(node.children) == 0:     # base case: check if game over
            if node.board.is_win(self.color) == self.color:
                return 1
            elif node.board.is_win(self.color) == self.opponent_color:
                return -1
            return 0
        # randomly select move for self and for opponent
        child = random.choice(node.children)
        self.board.make_move(child.move, child.color)
        result = self.rollout(child)
        self.board.undo()
        return result

    def backpropogate(self, node, wins):
        if node is None:
            return
        # update node.samples & node.wins
        node.samples += 1
        node.wins += wins
        self.backpropogate(node.parent, wins)

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
        if max_child is None:
            max_child = random.choice(node.children)
        return max_child
