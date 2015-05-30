/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

import javax.swing.JPanel;

public class Joint {
	private Encoder myEncoder;
	private BSChannel myCh;
	private VitDecoder myDec;
	private int totInfoBit;
	private int totCodedBit;
	
	public int getTotCodedBit() {
		return totCodedBit;
	}

	public Joint(String path,double probab){
		this.myEncoder = new Encoder(path);
		this.myCh = new BSChannel(probab);
		this.myDec = new VitDecoder(path);
	}
	
	public JPanel linkToTrellisPanel(){
		return this.myEncoder.linkToTrellisPanel(); 
	}
	public JPanel linkToTrellisShow(){
		return this.myDec.rentTrellisShow();
	}
	
	public int getTotalInfobit(){
		return this.totInfoBit;
	}
	
	public String CodeSeq(String seq){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < seq.length(); i++){
			sb.append(this.myEncoder.encode(Integer.parseInt(Character.toString( seq.charAt(i)))));
		}
		return new String(sb);
		
	}
	public String CodeSeqAndSend(String seq){
		StringBuffer sb = new StringBuffer();
		StringBuffer cw = new StringBuffer();
		String codedWord;
		for(int i = 0 ; i < seq.length(); i++){
			codedWord = this.myEncoder.encode(Integer.parseInt(Character.toString( seq.charAt(i))));
			cw.append(codedWord);
			for(int j = 0 ; j <codedWord.length();j++){
				sb.append(this.myCh.transition(Integer.parseInt((Character.toString(codedWord.charAt(j))))));
			}
			
		}
		System.out.println("Information sequence: \t" + seq);
		System.out.println("Coded word: \t\t" + new String(cw));
		System.out.println("Received word: \t\t" + new String(sb));
		return new String(sb);
		
	}
	private int hammingWeight(String a, String b){
		
		int weight =0;
		/*for(int i=0;i<this.myDec.getCodeWordBit();i++){
			weight += c%2;
			c >>=1;
		}*/
		
		for(int i = 0; i<a.length();i++){
			if(a.charAt(i) != b.charAt(i)) weight++;
		}
		
		return weight;
	}
	public String CodeTransfDecode(String seq){
		
		
		String trans = this.CodeSeqAndSend(seq);
		
		//System.out.println("Received word: " + trans);
		
		StringBuffer sb = new StringBuffer("Decoded...\n");
		
		for(int i = 0; i< trans.length()/this.myDec.getCodeWordBit()-1;i++){
			this.myDec.addTransmittedWord(trans.substring(this.myDec.getCodeWordBit()*i,this.myDec.getCodeWordBit()*(i+1)));
		}
		
		int i = trans.length()/this.myDec.getCodeWordBit()-1;
		String decOutput = this.myDec.addTransmittedWord(trans.substring(this.myDec.getCodeWordBit()*i,this.myDec.getCodeWordBit()*(i+1)));
		String output = new String("Decoded: "+ decOutput  +"\n");// +
			//"Transmission Errors: " + Integer.toString(hammingWeight(Integer.parseInt(this.CodeSeq(seq),2),Integer.parseInt(decOutput,2))));
		
		return output;
	}
	
	public InterexchangeSeq CodeTransfDecode4Win(String seq){
		
		this.totInfoBit = seq.length();
		
		
		
		StringBuffer sb = new StringBuffer();
		StringBuffer cw = new StringBuffer();
		String codedWord;
		for(int i = 0 ; i < seq.length(); i++){
			codedWord = this.myEncoder.encode(Integer.parseInt(Character.toString( seq.charAt(i))));
			cw.append(codedWord);
			for(int j = 0 ; j <codedWord.length();j++){
				sb.append(this.myCh.transition(Integer.parseInt((Character.toString(codedWord.charAt(j))))));
			}
			
		}
		String codeString = new String(cw);
		this.totCodedBit = codeString.length();
		
		
		String trans = new String(sb);
		
		sb = new StringBuffer("Decoded...\n");
		
		for(int i = 0; i< trans.length()/this.myDec.getCodeWordBit()-1;i++){
			this.myDec.addTransmittedWord(trans.substring(this.myDec.getCodeWordBit()*i,this.myDec.getCodeWordBit()*(i+1)));
		}
		
		int i = trans.length()/this.myDec.getCodeWordBit()-1;
		String decOutput = this.myDec.addTransmittedWord(trans.substring(this.myDec.getCodeWordBit()*i,this.myDec.getCodeWordBit()*(i+1)));
		String output = new String("Decoded: "+ decOutput  +"\n");// +
			//"Transmission Errors: " + Integer.toString(hammingWeight(Integer.parseInt(this.CodeSeq(seq),2),Integer.parseInt(decOutput,2))));
		
		
		int codingErr, bitErr;
		
		codingErr = this.hammingWeight(codeString, trans);
		bitErr = this.hammingWeight(seq, decOutput);
		
		
		return new InterexchangeSeq(codeString,trans,decOutput, codingErr,bitErr);
		
		
	}
	
	public void resetEnc(){
		this.myEncoder.reset();
		this.myDec.reset();
	}

}
