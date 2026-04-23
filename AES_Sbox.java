public class AES_Sbox {

    //gf(2^8) multiplication for table generation
    public static int xtime(int x) {
        x <<= 1;
        if ((x & 0x100) != 0) x ^= 0x11B;
        return x & 0xFF;
    }

    public static int gfMulNoMod(int a, int b) {
        int result = 0;

        while (b != 0) {
            if ((b & 1) != 0) result ^= a;
            a = xtime(a);
            b >>= 1;
        }

        return result;
    }


    static int[] logTable = new int[256];
    static int[] expTable = new int[512];

    static {
        int x = 1;

        // generates tables
        for (int i = 0; i < 255; i++) {
            expTable[i] = x;
            logTable[x] = i;

            x = gfMulNoMod(x, 0x03); // generator for AES field
        }

        // extend exp table
        for (int i = 255; i < 512; i++) {
            expTable[i] = expTable[i - 255];
        }
    }


    public static int findInverse(int a) {
        if (a == 0) return 0;
        return expTable[255 - logTable[a]];
    }


    public static int affineTransform(int x) {
        int result = 0;

        for (int i = 0; i < 8; i++) {
            int bit =
                    ((x >> i) & 1) ^
                            ((x >> ((i + 4) % 8)) & 1) ^
                            ((x >> ((i + 5) % 8)) & 1) ^
                            ((x >> ((i + 6) % 8)) & 1) ^
                            ((x >> ((i + 7) % 8)) & 1) ^
                            ((0x63 >> i) & 1);

            result |= (bit << i);
        }

        return result & 0xFF;
    }


    public static int[] generateSBox() {
        int[] sbox = new int[256];

        for (int x = 0; x < 256; x++) {
            int inv = findInverse(x);
            sbox[x] = affineTransform(inv);
        }

        return sbox;
    }


    public static int[] generateInverseSBox(int[] sbox) {
        int[] inv = new int[256];

        for (int x = 0; x < 256; x++) {
            inv[sbox[x]] = x;
        }

        return inv;
    }

    //this prints the 16x16 table
    public static void printBox(int[] box) {
        System.out.print("    ");
        for (int i = 0; i < 16; i++) {
            System.out.printf("%X  ", i);
        }
        System.out.println();

        for (int row = 0; row < 16; row++) {
            System.out.printf("%X | ", row);
            for (int col = 0; col < 16; col++) {
                System.out.printf("%02X ", box[row * 16 + col]);
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {

        int[] sbox = generateSBox();
        int[] invSbox = generateInverseSBox(sbox);

        System.out.println("AES S-Box:");
        printBox(sbox);

        System.out.println("\nInverse S-Box:");
        printBox(invSbox);

        System.out.println("\nCheck S-Box[0x53] = " +
                String.format("%02X", sbox[0x53]));
    }
}