public class GaloisMath {
  public static void main(String[] args) {
    // testing functions
    System.out.println(get_degree(0x3CF0));
    System.out.println(get_degree(0x10000));
    System.out.println(get_degree(0x00));
    System.out.println(get_degree(0x01));
    System.out.println();
    System.out.println(divide_galois(0x1000, 0x11B));
    System.out.println(divide_galois(0xE1, 0x11B));
    System.out.println(divide_galois(0x32CFE1, 0x11B));
    System.out.println(divide_galois(0xE1, 0x11B));
    System.out.println();
    System.out.println(multiply_galois(0xD5, 0x61, 0x11B));
    System.out.println(multiply_galois(0x1E3C, 0x1E3C, 0x11B));
  }
  
  // returns degree of polynomial in base-10. (code structure based on pseudocode provided)
  public static int get_degree(int ax){
    int degree = -1 ;
    while( ax != 0) {
	    degree++;     // keep incrementing while there are 1’s in number
      ax = ax/2;    // divide by 2 which shifts out lowest bit value
    }
    return degree;
  }
    
  // returns galois division result in base-10. (code structure based on pseudocode provided)
  public static int divide_galois(int ax, int mx){
    int result = ax;
    while (get_degree(result) >= get_degree(mx)) {
		  result = result ^ (mx << (get_degree(result) - get_degree(mx)));
    }
	  return result;
  }
  
  // returns galois multiplication result in base-10. (code structure based on pseudocode provided)
  public static int multiply_galois(int ax, int bx, int mx){
    int index = 0;
    int result = 0;
    while(bx != 0) { // run until no bits left in multiplier
    int b0 = bx%2;
		  if ( b0 == 1) {    // Test the lowest bit of b(x)
			  result = result ^ (ax << index);   // index is the current degree of the multiplicand
		  }
		  bx = bx/2;    // SHIFT out the current b0 and bring new b0
		  index++;		// raise the degree of the multiplicand for next loop in the line above
    }
    return divide_galois( result, mx);
  }
}
