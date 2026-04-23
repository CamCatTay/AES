public class AES {

    private final int[] s_box;
    private final int[] inverse_s_box;
    private final int[][][] round_keys;

    public AES(byte[] key) {
        if (key == null || key.length != 16) {
            throw new IllegalArgumentException("Key must be exactly 16 bytes");
        }
        s_box = SBox.generateSBox();
        inverse_s_box = SBox.generateInverseSBox(s_box);
        round_keys = new RoundKeyGenerator(s_box).generate_all_keys(key);
    }

    public byte[] encrypt(byte[] plaintext) {
        if (plaintext == null || plaintext.length != 16) {
            throw new IllegalArgumentException("Plaintext must be exactly 16 bytes");
        }
        int[][] block = bytes_to_block(plaintext);

        // Initial key addition before any rounds
        add_round_key(block, round_keys[0]);

        // Full round
        for (int round = 1; round <= 9; round++) {
            sub_bytes(block);
            Diffusion.shift_rows(block);
            Diffusion.mix_columns(block);
            add_round_key(block, round_keys[round]);
        }

        // Final round, no mix_columns
        sub_bytes(block);
        Diffusion.shift_rows(block);
        add_round_key(block, round_keys[10]);

        return block_to_bytes(block);
    }

    public byte[] decrypt(byte[] ciphertext) {
        if (ciphertext == null || ciphertext.length != 16) {
            throw new IllegalArgumentException("Ciphertext must be exactly 16 bytes");
        }
        int[][] block = bytes_to_block(ciphertext);

        // Undo final round key addition
        add_round_key(block, round_keys[10]);

        // Inverse full round
        for (int round = 9; round >= 1; round--) {
            Diffusion.inverse_shift_rows(block);
            inverse_sub_bytes(block);
            add_round_key(block, round_keys[round]);
            Diffusion.inverse_mix_columns(block);
        }

        // Final round undo initial key addition, no inverse mix_columns
        Diffusion.inverse_shift_rows(block);
        inverse_sub_bytes(block);
        add_round_key(block, round_keys[0]);

        return block_to_bytes(block);
    }

    // XOR each byte of the state block with the corresponding round key byte
    private void add_round_key(int[][] block, int[][] round_key) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                block[row][col] ^= round_key[row][col];
            }
        }
    }

    // Replace each byte in the state with its s_box substitution
    private void sub_bytes(int[][] block) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                block[row][col] = s_box[block[row][col]];
            }
        }
    }

    // Replace each byte in the state with its inverse s_box substitution
    private void inverse_sub_bytes(int[][] block) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                block[row][col] = inverse_s_box[block[row][col]];
            }
        }
    }

    // 16 bytes -> 4x4 state block because that's what our other classes use
    private int[][] bytes_to_block(byte[] input) {
        int[][] block = new int[4][4];
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                block[row][col] = input[col * 4 + row] & 0xFF;
            }
        }
        return block;
    }

    // 4x4 state block -> 16 bytes because that's what this class uses
    private byte[] block_to_bytes(int[][] block) {
        byte[] output = new byte[16];
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                output[col * 4 + row] = (byte) block[row][col];
            }
        }
        return output;
    }

}
