
def windowPosSum(a, n):
    orig_a = a.copy()
    for i in range(len(a)):
        if orig_a[i] > 0:
            total = 0
            for j in range(i, min(i + n + 1, len(a))):
                total += orig_a[j]
            a[i] = total

# Example 1:
a = [1, 2, -3, 4, 5, 4]
n = 3
windowPosSum(a, n)
print(a)  # Output: [4, 8, -3, 13, 9, 4]