import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java Main <key> <input_file> <output_file> <encrypt|decrypt>");
            return;
        }

        String key_input   = args[0];
        String input_path  = args[1];
        String output_path = args[2];
        String mode        = args[3].toLowerCase();

        if (mode.equals("decrypt")) {
            decrypt_file(key_input, input_path, output_path);
        } else {
            encrypt_file(key_input, input_path, output_path);
        }
    }

    private static void encrypt_file(String key_string, String input_path, String output_path) {
        try {
            byte[] key    = Arrays.copyOf(key_string.getBytes("UTF-8"), 16);
            byte[] input  = Files.readAllBytes(Paths.get(input_path));
            byte[] padded = pkcs7_pad(input);
            AES    aes    = new AES(key);

            byte[] result = new byte[padded.length];
            for (int i = 0; i < padded.length; i += 16) {
                byte[] encrypted = aes.encrypt(Arrays.copyOfRange(padded, i, i + 16));
                System.arraycopy(encrypted, 0, result, i, 16);
            }

            Files.write(Paths.get(output_path), result);
            System.out.println("Encrypted " + input.length + " bytes -> " + output_path);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void decrypt_file(String key_string, String input_path, String output_path) {
        try {
            byte[] key    = Arrays.copyOf(key_string.getBytes("UTF-8"), 16);
            byte[] input  = Files.readAllBytes(Paths.get(input_path));

            if (input.length == 0 || input.length % 16 != 0) {
                System.out.println("Error: input file is not a valid AES-encrypted file (length must be a multiple of 16).");
                return;
            }

            AES    aes    = new AES(key);
            byte[] result = new byte[input.length];
            for (int i = 0; i < input.length; i += 16) {
                byte[] decrypted = aes.decrypt(Arrays.copyOfRange(input, i, i + 16));
                System.arraycopy(decrypted, 0, result, i, 16);
            }

            result = pkcs7_unpad(result);
            Files.write(Paths.get(output_path), result);
            System.out.println("Decrypted " + input.length + " bytes -> " + output_path);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // PKCS7 padding to the next 16-byte boundary
    private static byte[] pkcs7_pad(byte[] data) {
        int pad_len = 16 - (data.length % 16);
        byte[] padded = Arrays.copyOf(data, data.length + pad_len);
        for (int i = data.length; i < padded.length; i++) {
            padded[i] = (byte) pad_len;
        }
        return padded;
    }

    // PKCS7 unpad — strip the padding bytes added during encryption
    private static byte[] pkcs7_unpad(byte[] data) {
        int pad_len = data[data.length - 1] & 0xFF;
        return Arrays.copyOf(data, data.length - pad_len);
    }
}
