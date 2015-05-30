/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

import java.util.Random;

public class BSChannel {
	private double myProbab;
	Random myRnd;
	

	public BSChannel(double myProbab) {
		//super();
		this.myProbab = myProbab;
		this.myRnd = new Random();
	}
	
	public int transition(int bit){
		double val;
		boolean boolBit = (bit!=0)? true : false;
		val = this.myRnd.nextDouble();
		if(val < myProbab)
			boolBit = !boolBit;
		
		return (boolBit == true) ? 1 : 0;
	}
	
	
	
	
	

}
