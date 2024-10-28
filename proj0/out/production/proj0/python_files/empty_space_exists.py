board = [
    [2, 4, 2, 4],
    [4, 2, 4, 2],
    [2, 4, 2, 4],
    [0, 0, 0, 0]
]

def empty_space_exists():
    for x in board:
        for y in x:
            if y == 0:
                return True
    return False

print(empty_space_exists())