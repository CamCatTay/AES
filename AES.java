public class AES {
  
  public static void main(String[] args){
    test_shift_rows();
    test_mix_columns();
  }

  // perform AES shift rows given a 4x4 block of input
  public static void shift_rows(int[][] block){
    if (block.length!=4 || block[0].length!=4){
      System.out.println("shift_rows: invalid input block. Must be 4x4 (is " + block.length + "x" + block[0].length + "). Block unchanged");
      return;
    }
    for(int i = 1; i < block.length; i++){
      int temp0 = block[i][i];
      int temp1 = block[i][(i+1)%4];
      int temp2 = block[i][(i+2)%4];
      int temp3 = block[i][(i+3)%4];
      block[i][0] = temp0;
      block[i][1] = temp1;
      block[i][2] = temp2;
      block[i][3] = temp3;
    }
  }

  // perform AES inverse of shift rows given a 4x4 input block
  public static void inverse_shift_rows(int[][] block){
    if (block.length!=4 || block[0].length!=4){
      System.out.println("shift_rows: invalid input block. Must be 4x4 (is " + block.length + "x" + block[0].length + "). Block unchanged");
      return;
    }
    for(int i = 1; i < block.length; i++){
      int temp0 = block[i][(-i+4)%4];
      int temp1 = block[i][(-i+5)%4];
      int temp2 = block[i][(-i+6)%4];
      int temp3 = block[i][(-i+7)%4];
      block[i][0] = temp0;
      block[i][1] = temp1;
      block[i][2] = temp2;
      block[i][3] = temp3;
    }
  }

  // a test to observe output for shift_rows and inverse_shift_rows
  public static void test_shift_rows(){
    int[][] block = {
      {0xf3,0x44,0x19,0x59},
      {0x7b,0xa2,0x16,0x02},
      {0xba,0xf1,0x11,0x0f},
      {0x37,0x80,0x9d,0xcb}
    };
    System.out.println("Original block before shift_rows:");
    hex_print(block);
    shift_rows(block);
    System.out.println("Block after shift_rows:");
    hex_print(block);
    inverse_shift_rows(block);
    System.out.println("Block after inverse_shift_rows (should be original):");
    hex_print(block);
  }

  // perform AES mix columns given a 4x4 block of input
  public static void mix_columns(int[][] block){
    if (block.length!=4 || block[0].length!=4){
      System.out.println("shift_rows: invalid input block. Must be 4x4 (is " + block.length + "x" + block[0].length + "). Block unchanged");
      return;
    }
    int mx = 0x11b;
    int[][] mix_constant = {
      {0x2,0x3,0x1,0x1},
      {0x1,0x2,0x3,0x1},
      {0x1,0x1,0x2,0x3},
      {0x3,0x1,0x1,0x2}
    };
    for (int j = 0; j < block[0].length; j++){ // loop through columns
      int[] temps = new int[block.length];
      for (int i = 0; i < block.length; i++){ // loop through rows
        temps[i] = GaloisMath.multiply_galois(mix_constant[i][0],block[0][j],mx) ^ GaloisMath.multiply_galois(mix_constant[i][1],block[1][j],mx) ^ GaloisMath.multiply_galois(mix_constant[i][2],block[2][j],mx) ^ GaloisMath.multiply_galois(mix_constant[i][3],block[3][j],mx);
      }
      block[0][j] = temps[0];
      block[1][j] = temps[1];
      block[2][j] = temps[2];
      block[3][j] = temps[3];
    }
  }

  // perform AES inverse of mix columns given a 4x4 input block
  public static void inverse_mix_columns(int[][] block){
    if (block.length!=4 || block[0].length!=4){
      System.out.println("shift_rows: invalid input block. Must be 4x4 (is " + block.length + "x" + block[0].length + "). Block unchanged");
      return;
    }
    int mx = 0x11b;
    int[][] mix_constant = {
      {0xe,0xb,0xd,0x9},
      {0x9,0xe,0xb,0xd},
      {0xd,0x9,0xe,0xb},
      {0xb,0xd,0x9,0xe}
    };
    for (int j = 0; j < block[0].length; j++){ // loop through columns
      int[] temps = new int[block.length];
      for (int i = 0; i < block.length; i++){ // loop through rows
        temps[i] = GaloisMath.multiply_galois(mix_constant[i][0],block[0][j],mx) ^ GaloisMath.multiply_galois(mix_constant[i][1],block[1][j],mx) ^ GaloisMath.multiply_galois(mix_constant[i][2],block[2][j],mx) ^ GaloisMath.multiply_galois(mix_constant[i][3],block[3][j],mx);
      }
      block[0][j] = temps[0];
      block[1][j] = temps[1];
      block[2][j] = temps[2];
      block[3][j] = temps[3];
    }
  }

  // a test to observe output for mix_columns and inverse_mix_columns
  public static void test_mix_columns(){
    int[][] block = {
      {0x87,0xf2,0x4d,0x97},
      {0x6e,0x4c,0x90,0xec},
      {0x46,0xe7,0x4a,0xc3},
      {0xa6,0x8c,0xd8,0x95}
    };
    System.out.println("Original Block before mix_columns:");
    hex_print(block);
    mix_columns(block);
    System.out.println("Block after mix_columns:");
    hex_print(block);
    inverse_mix_columns(block);
    System.out.println("Block after inverse_mix_columns (should be original):");
    hex_print(block);
  }
  
  // prints out an int[][] array in hex
  public static void hex_print(int[][] block){
    System.out.println("{");
    for(int i = 0; i < block.length; i++){
      for(int j = 0; j < block[0].length; j++){
        System.out.print(Integer.toHexString(block[i][j]) + ", ");
      }
      System.out.println();
    }
    System.out.println("}");
  }
  
}
