import sys
sys.path.append("../")
from Communicator import Communicator
from BoardClasses import Move


def get_prefix(ai):
    if ai.endswith('.exe'):
        ai = './'+ai
    elif ai.endswith('.py') or ai.endswith('.pyc') :
        ai = 'python3 '+ai
    elif ai.endswith('.jar'):
        ai = 'java -jar ' + ai

    return ai


class IOAI():
    def __init__(self,col,row,k,**kwargs):
        command = kwargs['ai_path']
        command = get_prefix(command)
        command = command + " " + str(col) + " " + str(row) + " " + str(k) + " " + " t"
        self.communicator = Communicator(command,kwargs['time'])

    def get_move(self,move):
        self.communicator.send(str(move).encode())
        ai_move = self.communicator.recv()
        ai_move = ai_move.decode().split("\n")[-1].rstrip()
        return Move.from_str(ai_move)