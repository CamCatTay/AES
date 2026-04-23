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
echo "I love KSU!" > test_input.txt
java Main "0123456787654321" test_input.txt test_encrypted.bin encrypt
java Main "0123456787654321" test_encrypted.bin test_decrypted.txt decrypt
cat test_decrypted.txt
```

