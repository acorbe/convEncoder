/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

import javax.swing.JPanel;

public class Encoder {
	private int totStatesLog;
	private Trellis myTrellis;
	private State myState;
	
	
	
	public Encoder(String path){
		this.myTrellis = new Trellis(path);
		this.myState = new State(new Integer(0));
		this.totStatesLog = myTrellis.getTotStatesLog();
		
	}
	
	public JPanel linkToTrellisPanel(){
		return this.myTrellis.rentMyrellisPanel();
	}
	
	public String encode(int infoBit){
		infoBit = ((infoBit != 0)?  1 :  0);
		CodeWordAndFinalState outWord = myTrellis.codedOut(myState, new Integer(infoBit));
		this.myState = outWord.myState;
		return outWord.myCodeWord;
	}
	public void reset(){
		this.myState = new State(new Integer(0));
	}
	
	
	

}
