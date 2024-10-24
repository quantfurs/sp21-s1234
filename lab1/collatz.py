# Class that prints the Collatz sequence starting from a given number.
# Author: YOUR NAME HERE

def next_number(n):
    """Buggy implementation of next_number."""
    if n % 2 == 0:
        return n // 2
    else:
        return 3*n + 1

def main():
    n = 5
    print(n, end=" ")
    while n != 1:
        n = next_number(n)
        print(n, end=" ")
    print()

if __name__ == "__main__":
    main()
