# AES

---

### Compile

```bash
javac *.java
```
---

#### Encrypt

```bash
java Main "0123456787654321" input.txt output.txt encrypt
```

---

#### Decrypt

```bash
java Main "0123456787654321" input.txt output.txt decrypt
```

#### Test

```bash
javac *.java
echo "I love KSU!" > test_input.txt
java Main "0123456787654321" test_input.txt test_encrypted.txt encrypt
java Main "0123456787654321" test_encrypted.txt test_decrypted.txt decrypt
cat test_decrypted.txt
```

---

#### Demo

Prints the key (padded), all 11 round keys (W0–W43), and the full Plaintext → Ciphertext → Recovertext chain. Also writes the ciphertext to the output file.

```bash
java Main "0123456787654321" input.txt output.txt demo
```

