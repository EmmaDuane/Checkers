class Move:

    """
    @:param l: a sequence of position.
      |  |
    --------
      | X|
    --------
      |  |
    --------
      | X|
    ________
    O |  |

    In the example above, l should be [(0,0),(2,2),(0,4)]

    """
    def __init__(self,l):
        self.seq = list(l)

    @classmethod
    def from_str(cls,s:str):
        return cls(list(map(lambda x:eval(x),s.split('-'))))

    """
    :return self.seq = [(0,0),(2,2),(0,4)] -> '(0,0)-(2,2)-(0,4)'
    """
    def __str__(self):
        result = ''
        if len(self.seq) == 0:
            return '-1'
        for e in self.seq:
            result += str(e)
            result += '-'
        return result[:-1].replace(" ","")
    def __len__(self):
        return len(self.seq)
    def __repr__(self):
        return str(self)

    def __getitem__(self,i):
        return self.seq[i]

    def __setitem__(self, index, value):
        self.seq[index] = value
