public class AES {
  
  public static void main(String[] args){
    test_shift_rows();
  }
  
  public static void shift_rows(int[][] block){
    if (block.length!=4 || block[0].length!=4){
      System.out.println("shift_rows: invalid input block. Must be 4x4 (is " + block.length + "x" + block[0].length + "). Block unchanged");
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
  
  public static void inverse_shift_rows(int[][] block){ // causes error for now. will be fixed
    if (block.length!=4 || block[0].length!=4){
      System.out.println("shift_rows: invalid input block. Must be 4x4 (is " + block.length + "x" + block[0].length + "). Block unchanged");
    }
    for(int i = 1; i < block.length; i++){
      int temp0 = block[i][i%4];
      int temp1 = block[i][(-i+1)%4];
      int temp2 = block[i][(-i+2)%4];
      int temp3 = block[i][(-i+3)%4];
      block[i][0] = temp0;
      block[i][1] = temp1;
      block[i][2] = temp2;
      block[i][3] = temp3;
    }
  }
  
  public static void test_shift_rows(){
    int[][] block = {
      {1,5,3,10},
      {4,11,1,8},
      {15,3,14,2},
      {7,8,6,9}
    };
    System.out.println("Block before shift_rows:");
    hex_print(block);
    shift_rows(block);
    System.out.println("Block after shift_rows:");
    hex_print(block);
   int[][] block2 = {
      {1,5,3,10},
      {4,11,1,8},
      {15,3,14,2},
      {7,8,6,9}
    };
    inverse_shift_rows(block2);
    System.out.println("Block after inverse_shift_rows:");
    hex_print(block2);
  }
  
  public static void mix_columns(int[][] block){ // a work in progress. Does nothing yet
    if (block.length!=4 || block[0].length!=4){
      System.out.println("shift_rows: invalid input block. Must be 4x4 (is " + block.length + "x" + block[0].length + "). Block unchanged");
    }
    int[][] mix_constant = {
      {2,3,1,1},
      {1,2,3,1},
      {1,1,2,3},
      {3,1,1,2}
    };
  }
  
  public static void inverse_mix_columns(int[][] block){ // a work in progress. Does nothing yet
    if (block.length!=4 || block[0].length!=4){
      System.out.println("shift_rows: invalid input block. Must be 4x4 (is " + block.length + "x" + block[0].length + "). Block unchanged");
    }
    int[][] mix_constant = {
      {14,11,13,9},
      {9,14,11,13},
      {13,9,14,11},
      {11,13,14,14}
    };
  }
  
  public static void hex_print(int[][] block){// prints in base 10 for now. should eventually print in hex
    System.out.println("{");
    for(int i = 0; i < block.length; i++){
      for(int j = 0; j < block[0].length; j++){
        System.out.print(block[i][j] + ", ");
      }
      System.out.println();
    }
    System.out.println("}");
  }
  
}
