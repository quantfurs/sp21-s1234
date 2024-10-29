from board import Board

array = [
    [2, 4, 2, 4],
    [4, 2, 4, 2],
    [2, 4, 2, 4],
    [0, 0, 0, 0]
]

def get_value(row, col):
    return array[row][col]

def get_column(col_index):
    return [row[col_index] for row in array]

def check_if_same_val_above(row, col):
    """Check if the value at (row, col) has the same value above it"""
    current_value = get_value(row, col)
    if row > 0:  # Check if there is a row above
        value_above = get_value(row - 1, col)
        return current_value == value_above
    return False

def check_if_zero_above(row, col):
    """Check if the value at (row, col) has a zero above it"""
    if row > 0:  # Check if there is a row above
        value_above = get_value(row - 1, col)
        return value_above == 0
    return False

def sum_with_above(row, col):
    """Sum the value at (row, col) with the entry above it in the column"""
    current_value = get_value(row, col)
    if row > 0:  # Check if there is a row above (not in first row)
        value_above = get_value(row - 1, col)
        return current_value + value_above
    return current_value  # Return original value if in first row



def tilt_up(board, score=0):
    """
    Tilt the board upward, combining like numbers and moving tiles up.
    Returns a tuple of (changed, score) where:
    - changed: boolean indicating if the board was modified
    - score: the score accumulated from combining tiles
    """
    changed = False
    
    for r in range(len(array)):
        for c in range(len(array[0])):
            t = board.tile(c, r)
            if t != 0:
                while check_if_zero_above(r, c):
                    board.move(c, r-1, t)
                    changed = True
                if check_if_same_val_above(r, c):
                    t = sum_with_above(r, c)
                    board.move(c, r-1, t)
                    changed = True
                    score += sum_with_above(r, c)
    
    return changed, score




