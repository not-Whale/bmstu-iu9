from read_to_space import read_to_space


def fibinc():
    n = int(read_to_space())
    result = []
    sum = 0

    if n == 0:
        return 0

    x = int(read_to_space())

    if x == 0:
        result.append(1)
        if n > 1:
            result.append(int(read_to_space()))
    else:
        result.append(0)
        result.append(1)
        if n > 1:
            read_to_space()

    for i in range(n - 2):
        result.append(int(read_to_space()))
        if result[i] == 1 and result[i + 1] == 1 and result[i + 2] == 0:
            result[i], result[i + 1], result[i + 2] = 0, 0, 1

    for i in range(n):
        sum += result[i] * (10 ** i)

    return sum


print(fibinc())
