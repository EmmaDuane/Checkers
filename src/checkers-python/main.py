"""
This module is where the game is run from.

We are following the javadoc docstring format which is:
@param tag describes the input parameters of the function
@return tag describes what the function returns
@raise tag describes the errors this function can raise
"""

from GameLogic import GameLogic
import sys

from socket import *
def network_init():
    """
    This function sets up a network connection to the ICS servers incase you want to play against another AI connected to the network.
    @return response, mode: a tuple that returns the response from the ICS servers, and sends a string with either host or client to indicate whether this AI is hosting the game session or joining a session.
    """
    serverPort = 12002
    clientSocket = socket(AF_INET, SOCK_STREAM)
    clientSocket.connect(('syn2-1.ics.uci.edu', serverPort))

    sentence = "REQUEST_NUM"
    clientSocket.send(sentence.encode())
    result = int(clientSocket.recv(1024).decode())
    clientSocket.close()

    clientSocket = socket(AF_INET, SOCK_STREAM)
    clientSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
    clientSocket.connect(('syn2-1.ics.uci.edu', serverPort))

    print("There are currently",result,"rooms.")
    print("Enter which room you want to join, or create a new room.")
    while True:
        command = input('{# of room/create}')
        try:
            int(command)
            sentence = "REQUEST_JOIN|"+command
            mode = 'client'
        except:
            if command != "create":
                print("Unknown Command")
                continue
            else:
                sentence = "REQUEST_OPEN"
                mode = 'host'

        clientSocket.send(sentence.encode())

        response = eval(clientSocket.recv(1024).decode())
        clientSocket.close()
        return response,mode


if __name__ == "__main__":
    # To run under manual mode, please use this command "python3 main.py {row} {col} {k} m {order}"
    # e.g. "python3 main.py 7 7 2 m 0"
    # e.g. "python3 main.py 7 7 2 l {AI_path 1} {AI_path 2}"
    # e.g. "python3 main.py 7 7 2 n {AI_path}"
    if len(sys.argv) < 5:
        print("Invalid Parameters")
        sys.exit(-1)

    col = int(sys.argv[1])
    row = int(sys.argv[2])
    k = int(sys.argv[3])
    mode = sys.argv[4]

    main = GameLogic(col,row,k,mode,debug=True)
    if mode == 'n' or mode == 'network':
        ai_path =  sys.argv[5]
        response, mode = network_init()

        main.Run(mode=mode,ai_path=ai_path,info=response,time=1200)
    elif mode == 'm' or mode == 'manual':
        order = sys.argv[5]
        order =main.Run(mode=mode,order=order)

    elif mode == 't':
        main.Run(mode=mode)

    elif mode == 'l':
        ai_path_1,ai_path_2 =  sys.argv[5],sys.argv[6]
        main.Run(mode=mode,ai_path_1=ai_path_1,ai_path_2=ai_path_2,time=1200)