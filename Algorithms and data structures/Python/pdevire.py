from math import factorial


def pdevire():
    n, k, x0 = map(int, input().split())
    an = input().split()
    sum = 0
    for i in range(n-k+1):
        sum += factorial(n-i) / factorial(n-i-k) * (int(an[i])) * (x0 ** (n-i))
    print(int(sum))
    return


pdevire()
