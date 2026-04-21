# Generate Round Keys Planning Document

Q-4 Implement the Key Expansion function, see section 6.4 for a description.  It will accept an input array of bytes of key:

K = (k0, k1,  k2,  k3,    = W0 the first column word of W
         k4, k5,  k6,  k7,    = W1 the second column word of W
         k8, k9,  k10, k11,  =  W2 the third column word of W
         k12,k13,k14, k15)  =  W3 the fourth column word of W
Use this initial four W0 to W4 values to produce W5 to W43 to complete W the 176 byte array to be used as keys for initial round and 10 rounds of AES.

You can also verify your results by testing key gen from the table in book p.193/194 (7th).

Key Expansion, Slide 10 Lecture 14 and Figure 6.9 of textbook

Round Count for Key Expansion

## Overview of how key generation works:

- Initial key (user selected) of 4 words (128 bits) is input. Produces linear array of 44 words.

- Key is copied into the first 4 words of the expanded key
    - The remainder of th expanded key is filled in 4 words at a time

- Each added word w[i] depends on the immediately preceding word, w[i-1], and the word 4 positions back, w[i-4]
    - In three out of four cases a simple XOR is used
    - FOr a word whose position in the w array is a multiple of 4, a more complex function is used

## Notes:

Visualization of what needs to happen:
https://en.wikipedia.org/wiki/AES_key_schedule#/media/File:AES-Key_Schedule_128-bit_key.svg

- 128 bit (4 words 32 bits each) key so only 10 rounds
    - larger keys can be used but since it's not a requirement I won't worry about that rn

## Functions

### RotWord
- rotate left by 1 byte
w[0] = w[1]
w[1] = w[2]
w[2] = w[3]
w[3] = w[0]

- Use Integer.rotateLeft(int i, int distance)
Example: Integer.rotateLeft(0xABCD, 1) = 0xBCDA

### SubWord
- Substitutes 32 bit word using the AES S-Box
w[0] = S-box(w[0])
w[1] = S-box(w[1])
w[2] = S-box(w[2])
w[3] = S-box(w[3])

### Round Constants (Rcon)
- Round constants are generated using a recursive function
- This is XOR with one of our round keys
rc(1) = 1
rc(i) = 2 * rc(i-1)                 if rc(i-1) < 0x80
rc(i) = (2 * rc(i-1)) XOR 0x11B     if rc(i-1) >= 0x80

### How to XOR in Java
- Use ^ operator to XOR
- Example: 0101 ^ 0011 = 0110

## System Architecture:

Class Round_Key_Generator

// Return input word rotated 1 byte to the left
private int rotate_word(word)
    int distance = 1
    return Integer.rotateLeft(word, distance)

// Returns substituted input word using s_box
// Unsigned right shift >>> and mask with & 0xFF to ensure that any sign bits don't interfere with the value
// << to put back into position and | to combine back into a single word
private int substitute_word(int word) {
    return (s_box[(word >>> 24) & 0xFF] << 24) |
           (s_box[(word >>> 16) & 0xFF] << 16) |
           (s_box[(word >>> 8)  & 0xFF] << 8)  |
           (s_box[word & 0xFF]);so how is the w
}

// Returns calculated round constant
// Calculates:
// rc(1) = 1
// rc(i) = 2 * rc(i-1)                 if rc(i-1) < 0x80
// rc(i) = (2 * rc(i-1)) XOR 0x11B     if rc(i-1) >= 0x80
private int round_constant(int round) {

}

// input 4 words outputs 44 words. All keys for 10 rounds and the initial key
// May need to update this to actually output matrix
public int generate_all_keys(int[] initial_key) {
    int[] w = new int[44];

        w[0] = initialKey[0];
        w[1] = initialKey[1];
        w[2] = initialKey[2];
        w[3] = initialKey[3];

        for (int i = 4; i < 44; i++) {
            int temp = w[i - 1];
            if (i % 4 == 0) {
                temp = subWord(rotateWord(temp)) ^ RCON[i / 4];
            }
            w[i] = w[i - 4] ^ temp;
        }
        return w;
    }