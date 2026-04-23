
public class RoundKeyGenerator {

    private final int[] s_box;

    public RoundKeyGenerator(int[] s_box) {
        this.s_box = s_box;
    }

    // Returns input word rotated 1 byte to the left
    private int get_rotated_word(int word) {
        return Integer.rotateLeft(word, 8);
    }

    // Returns substituted input word using s_box
    // Unsigned right shift >>> and mask with & 0xFF to ensure sign bits don't interfere
    // << to put back into position and | to combine back into a single word
    private int get_substituted_word(int word) {
        return (s_box[(word >>> 24) & 0xFF] << 24) |
               (s_box[(word >>> 16) & 0xFF] << 16) |
               (s_box[(word >>> 8)  & 0xFF] << 8)  |
               (s_box[ word & 0xFF]);
    }

    // Pre-Calculated Round constants for rounds 1-10
    // rc(1) = 1
    // rc(i) = 2 * rc(i-1)             if rc(i-1) < 0x80
    // rc(i) = (2 * rc(i-1)) ^ 0x11B   if rc(i-1) >= 0x80
    private static final int[] ROUND_CONSTANTS = {
        0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000,
        0x20000000, 0x40000000, 0x80000000, 0x1b000000, 0x36000000
    };

    private int get_round_constant(int round) {
        return ROUND_CONSTANTS[round - 1];
    }

    // Input: 16 key bytes (k0..k15).
    // Output: int[11][4][4] -> 11 4x4 round key blocks
    // round 0 = initial key, rounds 1-10 = expanded keys
    // add_round_key can XOR block[row][col] ^= round_keys[r][row][col]
    public int[][][] generate_all_keys(int[] key_bytes) {
        // Pack 16 input bytes into 4 words
        int[] words = new int[44];
        for (int i = 0; i < 4; i++) {
            words[i] = ((key_bytes[i * 4]     & 0xFF) << 24) |
                       ((key_bytes[i * 4 + 1] & 0xFF) << 16) |
                       ((key_bytes[i * 4 + 2] & 0xFF) << 8)  |
                        (key_bytes[i * 4 + 3] & 0xFF);
        }

        // Generate the remaining 40 words
        for (int i = 4; i < 44; i++) {
            int temp = words[i - 1];

            // XOR every 4th word with round constant
            if (i % 4 == 0) {
                temp = get_substituted_word(get_rotated_word(temp)) ^ get_round_constant(i / 4);
            }
            words[i] = words[i - 4] ^ temp;
        }

        // Unpack 44 words into 11 round key blocks of int[4][4]
        int[][][] round_keys = new int[11][4][4];
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 4; col++) {
                int word = words[row * 4 + col];
                round_keys[row][0][col] = (word >>> 24) & 0xFF;
                round_keys[row][1][col] = (word >>> 16) & 0xFF;
                round_keys[row][2][col] = (word >>> 8)  & 0xFF;
                round_keys[row][3][col] =  word         & 0xFF;
            }
        }
        return round_keys;
    }

    // === Main function for testing and debugging ===
    public static void main(String[] args) {
        int[] s_box = SBox.generateSBox();
        RoundKeyGenerator rkg = new RoundKeyGenerator(s_box);

        // Textbook pages 193-194
        int[] key_bytes = {
            0x0f, 0x15, 0x71, 0xc9,   // W0
            0x47, 0xd9, 0xe8, 0x59,   // W1
            0x0c, 0xb7, 0xad, 0xd6,   // W2
            0xaf, 0x7f, 0x67, 0x98    // W3
        };

        int[][][] round_keys = rkg.generate_all_keys(key_bytes);

        for (int r = 0; r < round_keys.length; r++) {
            System.out.println("Round " + r + ":");
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    System.out.printf("%02x ", round_keys[r][row][col]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
