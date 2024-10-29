
array = [
    [4, 4, 2, 0],
    [4, 2, 0, 2],
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

def board_move(current_row, current_col, target_row, target_col, tile_value):
    """
    Move the tile 'tile_value' from (current_row, current_col) to (target_row, target_col).
    Update the 'array' accordingly.
    """
    # Place the tile at the target position
    array[target_row][target_col] = tile_value
    # Set the original position to zero
    array[current_row][current_col] = 0



def tilt_up(board, score=0):
    """
    Tilt the board upward, combining like numbers and moving tiles up.
    Returns a tuple of (changed, score) where:
    - changed: boolean indicating if the board was modified
    - score: the score accumulated from combining tiles
    """
    changed = False
    size = len(array)
    
    for c in range(size):
        # Step 1: Extract non-zero tiles in the column
        tiles = [array[r][c] for r in range(size) if array[r][c] != 0]
        
        # Step 2: Merge tiles
        merged_tiles = []
        r = 0
        while r < len(tiles):
            if r + 1 < len(tiles) and tiles[r] == tiles[r + 1]:
                # Merge tiles
                new_value = tiles[r] * 2
                merged_tiles.append(new_value)
                score += new_value
                changed = True
                r += 2  # Skip the next tile, as it's merged
            else:
                merged_tiles.append(tiles[r])
                r += 1

        # Step 3: Pad the merged tiles with zeros to match the size
        merged_tiles.extend([0] * (size - len(merged_tiles)))
        
        # Step 4: Update the array and check for changes
        for r in range(size):
            if array[r][c] != merged_tiles[r]:
                changed = True
                array[r][c] = merged_tiles[r]
    
    return changed, score



print("Before tilt up:")
for row in array:
    print(row)

changed, score = tilt_up(array)

print("\nAfter tilt up:")
for row in array:
    print(row)

print(f"\nChanged: {changed}")
print(f"Score: {score}")

