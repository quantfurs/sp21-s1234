
array = [
    [4, 4, 2, 0],
    [4, 2, 0, 2],
    [2, 4, 2, 4],
    [0, 0, 0, 0]
]

def get_value(board, row, col):
    return board[row][col]

def get_column(board, col_index):
    return [row[col_index] for row in board]

def board_move(board, c, r, t):
    """
    Move the tile with value 't' to position (r, c).
    Update the 'board' accordingly.
    """
    board[r][c] = t  # Place the tile at the target position

def merge_tiles(tiles):
    """
    Merge a list of tiles according to 2048 rules.
    Returns the merged list and the score gained from merges.
    """
    merged_tiles = []
    score = 0
    idx = 0
    while idx < len(tiles):
        current_value = tiles[idx]
        if idx + 1 < len(tiles) and current_value == tiles[idx + 1]:
            # Merge the tiles
            new_value = current_value * 2
            merged_tiles.append(new_value)
            score += new_value
            idx += 2  # Skip the next tile since it's merged
        else:
            merged_tiles.append(current_value)
            idx += 1
    return merged_tiles, score

def tilt_up(board, score=0):
    """
    Tilt the board upward, combining like numbers and moving tiles up.
    Each tile calls 'board_move' once to move to its target position.
    """
    changed = False
    size = len(board)
    
    for c in range(size):
        # Step 1: Get the column using 'get_column'
        column = get_column(board, c)
        
        # Step 2: Extract non-zero tiles from the column
        tiles = [tile for tile in column if tile != 0]
        
        # Step 3: Merge tiles using 'merge_tiles' helper function
        merged_tiles, merge_score = merge_tiles(tiles)
        if merge_score > 0:
            score += merge_score
            changed = True
        
        # Step 4: Clear the original column in 'board'
        for r in range(size):
            if get_value(board, r, c) != 0:
                board[r][c] = 0
                changed = True
        
        # Step 5: Move merged tiles to the top using 'board_move'
        for idx, tile_value in enumerate(merged_tiles):
            board_move(board, c, idx, tile_value)  # Move tile to its new position
            changed = True  # Since we're moving tiles, we set changed to True

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

