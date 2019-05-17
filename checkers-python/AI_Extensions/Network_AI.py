from socket import *
import sys
sys.path.append('../')
from BoardClasses import Move
from time import sleep
class NetworkAI():
    def __init__(self,col,row,k,**kwargs):
        self.topSocket = socket(AF_INET, SOCK_STREAM)
        self.mode = kwargs['mode']
        serverName, serverPort = kwargs['info']
        if self.mode == 'host':
            self.topSocket.bind((serverName, serverPort))
            self.topSocket.listen(1)
            self.topSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
            self.connectionSocket, _ = self.topSocket.accept()
            if self.connectionSocket.recv(1024).decode() != 'OK':
                raise SO_ERROR
            else:
                print('OK')
        else:
            self.topSocket.connect((serverName, serverPort))
            self.topSocket.send("OK".encode())
            print('OK')
    def sent_final_result(self,move):
        if self.mode == 'host':
            sentence = str((move.col, move.row)).encode()
            self.connectionSocket.send(sentence)
        else:
            sentence = str((move.col, move.row)).encode()
            self.topSocket.send(sentence)

    def get_move(self,move):
        print('SENT:',move.col,move.row)
        sleep(0.3)
        if self.mode == 'host':
            if move.col != -1:
                sentence = str((move.col,move.row)).encode()
                self.connectionSocket.send(sentence)

            response = self.connectionSocket.recv(1024).decode().rstrip()
            try:
                res_move = Move(response)
            except:
                print("You win. Your peer crashed.")
                raise Exception
            print('GET:', res_move)
            return res_move
        else:
            if move.col != -1:
                sentence = str((move.col,move.row)).encode()
                self.topSocket.send(sentence)
            response = self.topSocket.recv(1024).decode().rstrip()
            try:
                res_move = Move(response)
            except:
                print("You win. Your peer crashed.")
                raise Exception
            print('GET:', res_move)
            return res_move

    def __del__(self):
        self.topSocket.close()
