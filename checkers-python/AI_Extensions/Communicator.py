from subprocess import Popen, PIPE
import select
import fcntl, os
import time

class Communicator(object):
    def __init__(self, command,timeout):
        self.timeout = timeout
        self.process = Popen(command, shell=True, stdin=PIPE, stdout=PIPE, stderr=PIPE)
        flags = fcntl.fcntl(self.process.stdout, fcntl.F_GETFL)
        fcntl.fcntl(self.process.stdout, fcntl.F_SETFL, flags | os.O_NONBLOCK)

    def send(self, data, tail = '\n'.encode()):
        self.process.stdin.write(data + tail)
        self.process.stdin.flush()
        time.sleep(0.01)

    def recv(self,t=0.2,time_already=0):
        r = ''
        pr = self.process.stdout
        bt = time.time()
        while ((time.time() - bt)+time_already < self.timeout):
            if not select.select([pr], [], [], 0)[0]:
                time.sleep(t)
                continue
            r = pr.read().rstrip()

            return r, time.time() - bt
        raise TimeoutError
    def __del__(self):
        try:
            self.process.terminate()
        except:
            pass
