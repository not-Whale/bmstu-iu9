from read_to_space import read_to_space


def rangeset():
    n = int(read_to_space())
    result = []

    if n == 0:
        return None

    l, r = int(read_to_space()), int(read_to_space())

    for i in range(n - 1):
        new_l, new_r = int(read_to_space()), int(read_to_space())
        if new_l <= r + 1:
            r = max(new_r, r)
        else:
            result.append([l, r])
            l = new_l
            r = new_r

    result.append([l, r])

    return result


for x in rangeset():
    print(x[0], x[1])
