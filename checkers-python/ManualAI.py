from Move import Move
from BoardClasses import Board

class ManualAI():
    def __init__(self,col,row,k):
        self.col = col
        self.row = row
        self.k = k
        self.board = Board(col,row,k)
        self.board.initialize_game()
        self.color = 2
        self.oppoent = {1:2,2:1}
    def get_move(self,move):
        if move is not None:
            self.board.make_move(move,self.oppoent[self.color])
        else:
            self.color = 1
        moves = self.board.get_all_possible_moves(self.color)
        while True:
            try:
                for i,checker_moves in enumerate(moves):
                    print(i,':[',end="")
                    for j, move in enumerate(checker_moves):
                        print(j,":",move,end=", ")
                    print("]")
                index,inner_index = map(lambda x: int(x), input("Select Move {int} {int}: ").split())
                res_move = moves[index][inner_index]
            except KeyboardInterrupt:
                raise KeyboardInterrupt
            except:
                print('invalid move')
                continue
            else:
                break
        self.board.make_move(res_move, self.color)
        return res_move
