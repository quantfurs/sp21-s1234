import java.util.Arrays;

class WindowPosSum {
    public static void windowPosSum(int[] a, int n) {
        int[] orig_a = Arrays.copyOf(a, a.length);
        for (int i = 0; i < a.length; i++) {
            if (orig_a[i] > 0) {
                int total = 0;
                int end = Math.min(i + n + 1, a.length);
                for (int j = i; j < end; j++) {
                    total += orig_a[j];
                }
                a[i] = total;
            }
        }
    }

    public static void main(String[] args) {
        // Example 1
        int[] a1 = {1, 2, -3, 4, 5, 4};
        int n1 = 3;
        windowPosSum(a1, n1);
        System.out.println(Arrays.toString(a1));  // Output: [4, 8, -3, 13, 9, 4]

        // Example 2
        int[] a2 = {-1, -1, -1, 10, 5, -1};
        int n2 = 2;
        windowPosSum(a2, n2);
        System.out.println(Arrays.toString(a2));  // Output: [-1, -1, -1, 14, 4, -1]
    }
}
