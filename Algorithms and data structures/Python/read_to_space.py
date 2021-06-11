import sys


def read_to_space(file=sys.stdin, _type=int, space_list=[' ', '\n', '\t']):
    buf = ''
    curr = ' '
    while curr in space_list and curr != '':
        curr = file.read(1)
    while curr not in space_list and curr != '':
        buf += curr
        curr = file.read(1)
    try:
        return _type(buf)
    except ValueError:
        return None


if __name__ == "__main__":
    read_to_space()
