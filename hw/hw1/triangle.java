class StarPattern {

    public static void main(String[] args) {
        drawTriangle(10);  // Call drawTriangle with N = 10
    }

    // New method to print a triangle of N asterisks tall
    public static void drawTriangle(int N) {
        String star = "*";

        for (int i = 0; i < N; i++) {
            System.out.println(star);
            star += "*";
        }
    }
}
