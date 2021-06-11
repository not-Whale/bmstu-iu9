from math import factorial
from read_to_space import read_to_space


def pdevire():
    n, k, x0 = int(read_to_space()), int(read_to_space()), int(read_to_space())
    sum = 0
    for i in range(n-k+1):
        sum += factorial(n-i) / factorial(n-i-k) * (int(read_to_space())) * (x0 ** (n-i))
    return int(sum)


print(pdevire())
