/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

public class InterexchangeSeq {
	
	private String codeS,trasS,decS;
	private int codingErr, bitErr;
	

	public InterexchangeSeq(String codeS, String trasS, String decS, int codingErr, int bitErr) {
		super();
		this.codeS = codeS;
		this.trasS = trasS;
		this.decS = decS;
		this.codingErr = codingErr;
		this.bitErr = bitErr;
		
	}
	
	

	public int getCodingErr() {
		return codingErr;
	}



	public void setCodingErr(int codingErr) {
		this.codingErr = codingErr;
	}



	public int getBitErr() {
		return bitErr;
	}



	public void setBitErr(int bitErr) {
		this.bitErr = bitErr;
	}



	public String getCodeS() {
		return codeS;
	}

	public void setCodeS(String codeS) {
		this.codeS = codeS;
	}

	public String getTrasS() {
		return trasS;
	}

	public void setTrasS(String trasS) {
		this.trasS = trasS;
	}

	public String getDecS() {
		return decS;
	}

	public void setDecS(String decS) {
		this.decS = decS;
	}

}
