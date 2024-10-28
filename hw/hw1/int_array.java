class ClassNameHere {
    /** Returns the maximum value from m. */
    public static int max(int[] m) {
        int maxVal = m[0]; // Start with the first element as the maximum
        for (int i = 1; i < m.length; i++) {
            if (m[i] > maxVal) {
                maxVal = m[i]; // Update maxVal if a larger element is found
            }
        }
        return maxVal;
    }

    public static void main(String[] args) {
        int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};
        System.out.println(max(numbers)); // Call the max function and print the result
    }
}
