board = [
    [2, 4, 2, 4],
    [4, 2, 4, 2],
    [2, 4, 2, 4],
    [0, 0, 0, 0]
]

def empty_space_exists():
    for i in range(len(board)):
        for j in range(len(board[i])):
            # Check if the current space is empty
            if board[i][j] == 0:
                return True
            # Check right neighbor
            if j < len(board[i]) - 1 and board[i][j + 1] == 0:
                return True
            # Check bottom neighbor
            if i < len(board) - 1 and board[i + 1][j] == 0:
                return True
    return False

def check_if_same_val():
    for i in range(len(board)):
        for j in range(len(board[i])):
            # Check right neighbor
            if j < len(board[i]) - 1 and board[i][j] == board[i][j + 1]:
                return True
            # Check bottom neighbor
            if i < len(board) - 1 and board[i][j] == board[i + 1][j]:
                return True
    return False

if empty_space_exists() == True or check_if_same_val() == True:
    print("At least one move exists")
else:
    print("No moves exist")