/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

public class LaunchB1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Trellis tr = new Trellis("CodifEsempioLibro540.txt");
		//tr.trallisWindowShow();
		//Encoder enc = new Encoder("CodifEsempioLibro540.txt");
		//System.out.println(enc.encode(0)+ enc.encode(1) + enc.encode(0));
		
		Joint jt = new Joint("CodifEsempioLibro540.txt", 0.2);
		String seq = new String("0111101010101011");
		//System.out.println(jt.CodeSeq(seq));
		
		jt.resetEnc();
		
		System.out.println(jt.CodeTransfDecode(seq));
		//jt.CodeTransfDecode(seq);

	}

}
