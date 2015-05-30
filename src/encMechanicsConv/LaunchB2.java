/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */



package encMechanicsConv;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LaunchB2 {
	
	private Joint myFacadeClass;
	private Gui myGui;
	private double transProb;
	public static void main(String[] args) {
		String path = JOptionPane.showInputDialog("Please insert a correctly formatted trellis file:","CodifEsempioLibro540.txt");
		String bscTransProbab = JOptionPane.showInputDialog("Please insert the transition probability of the BSC", "0.1");
		
		new LaunchB2(path,bscTransProbab);
		
		
	}
	
	private LaunchB2(String path, String bscTransProbab){
		this.myFacadeClass = new Joint(path,Double.parseDouble(bscTransProbab));
		
		this.transProb = Double.parseDouble(bscTransProbab);
		
		
		this.myGui = new Gui(this.myFacadeClass.linkToTrellisPanel(),this.myFacadeClass.linkToTrellisShow());
	}
	
	
	
	class Gui extends JFrame{
		private JPanel trellisFr;
		private Container wordPn;
		private JPanel trellisShow;
		private Container globCont;
		
		
		private Container centerCont;
		private JPanel rightCont;
		
		public Gui(JPanel trellisFr, JPanel trellisShow){
			super("[Convol Enc - Trans - Dec] Alessandro Corbetta");
			//this.setLayout(new FlowLayout());
			
			this.trellisFr = trellisFr;
			this.trellisShow = trellisShow;
			
			this.globCont = this.getContentPane();
			this.globCont.setLayout(new FlowLayout());
			
			
			this.centerCont = new Container();
			this.centerCont.setLayout(new FlowLayout());
			
			this.centerCont.add(this.trellisFr);
			
			this.wordPn = new InputForm();
			this.centerCont.add(wordPn);
			
			
			this.rightCont = new JPanel();
			this.rightCont.add(trellisShow);
			
			this.centerCont.add(this.rightCont);
			
			this.globCont.add(this.centerCont);
			
			
			this.pack();
			this.setVisible(true);
			
		}
		
		private class InputForm extends Container{
			private JLabel inputLabel;
			private JTextField inputField;
			
			private JButton encodeAndSend;
			
			private JLabel encodedLabel;
			private JTextField encodedField;
			
			private JLabel TransProp;
			
			private JLabel arrivedLabel;
			private JTextField arrivedField;
			
			private JLabel decodedLabel;
			private JTextField decodedField;
			private JLabel decProp;
			
			private JButton buttonAbout;
			
			
			
			private InputForm(){
				super();
				this.setLayout(new GridLayout(0,1));
				
				this.inputLabel = new JLabel("Insert information sequence [ 0,1 array]: ");
				this.add(this.inputLabel);
				
				this.inputField = new JTextField(20);
				this.add(this.inputField);
				
				this.encodeAndSend = new JButton("Encode and Send!");
				this.encodeAndSend.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						myFacadeClass.resetEnc();
						trellisShow = myFacadeClass.linkToTrellisShow();
						//JOptionPane.showMessageDialog(null,  Integer.toString(trellisShow.getComponentCount()));
						
						rightCont.remove(0);
						rightCont.add(trellisShow);
						
											
						trellisShow.repaint();
						
						
						
						
						InterexchangeSeq seq =  myFacadeClass.CodeTransfDecode4Win(inputField.getText());
						encodedField.setText(seq.getCodeS());
						arrivedField.setText(seq.getTrasS());
						decodedField.setText(seq.getDecS());
						TransProp.setText("Transmission errors: " + Integer.toString(seq.getCodingErr()) + " over " + Integer.toString(myFacadeClass.getTotCodedBit()) + " simbols");
						
						decProp.setText("Decoding errors: " + Integer.toString(seq.getBitErr()) + " over " + Integer.toString(myFacadeClass.getTotalInfobit())+ " bit");
						// TODO Auto-generated method stub
						
						
					}
					
				});
				this.add(this.encodeAndSend);
				
				this.encodedLabel = new JLabel("Encoded String:");
				this.add(this.encodedLabel);
				this.encodedField = new JTextField(40);
				this.add(this.encodedField);
				
				JLabel BSCprop = new JLabel("Transmission through BSC with transition probability p=" + Double.toString(transProb),JLabel.RIGHT);
				BSCprop.setFont(new Font("sansserif", Font.ITALIC, 12));
				this.add(BSCprop);
				
				
				
				
				
				this.arrivedLabel = new JLabel("Received String:");
				this.add(this.arrivedLabel);
							
				this.arrivedField = new JTextField(40);
				this.add(this.arrivedField);
				
				TransProp = new JLabel("Transmission errors: N/A",JLabel.RIGHT);
				TransProp.setFont(new Font("sansserif", Font.ITALIC, 12));
				this.add(TransProp);
				
				this.decodedLabel = new JLabel("Decoded String:");
				this.add(this.decodedLabel);
				this.decodedField = new JTextField(20);
				this.add(this.decodedField);
				
				decProp = new JLabel("Decoding errors: N/A",JLabel.RIGHT);
				decProp.setFont(new Font("sansserif", Font.ITALIC, 12));
				this.add(decProp);
				
				this.buttonAbout = new JButton("About...");
				this.buttonAbout.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						JOptionPane.showMessageDialog(null,
								"A small program to simulate convolutional encoding...\n" +
								"Homework for the exam: Coding Theory and Structure of Convolutional Codes\n" +
								"at Politecnico di Torino (2011)\n"	+	
								"1,5K+ codelines and 6 days of hard work\n" +
								"\n" +
								"by Alessandro Corbetta\n" +
								"           alessandro.corbetta@hotmail.com\n" +
								
								"                                                                                 1/02/2011\n" +
								"\n" +
								"\n" +
								
								"P.S. try other trellis files!!\n " +
								"(the program is not protected against bad filling of the trellis file - follow the template!)\n\n" +
								"" +
								"peace",
								"About..",
								JOptionPane.INFORMATION_MESSAGE
								);
					}
					
				});
					
				this.add(buttonAbout);
				
				
				
			}
		}
	}

}
