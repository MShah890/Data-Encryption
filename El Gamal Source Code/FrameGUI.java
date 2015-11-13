import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FrameGUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JLabel pubkey;
	private JTextField pk;
	private JLabel publickey;
	private JTextField pkg;
	private JLabel publickeyexp;
	private JTextField pkga;
	private JLabel privatekey;
	private JTextField prvkey;
	private JLabel plain;
	private JTextField plainText;
	private JLabel cipher;
	private JTextField cipherText;
	private JButton encr;
	private JButton decr;
	private JButton gen;
	private JTextField bitlen;
	private JLabel bits;
	private JLabel tgen;
	private JTextField ttgen;
	private JLabel tenc;
	private JTextField ttenc;
	private JLabel tdec;
	private JTextField ttdec;
	
	public FrameGUI()
	{
		super("ElGamal");
		setLayout(new FlowLayout());
		
		JPanel gui=new JPanel(new BorderLayout(12, 3));
		this.setContentPane(gui);
		
		JPanel labels=new JPanel(new GridLayout(0,1));
		JPanel fields=new JPanel(new GridLayout(0,1));
        gui.add(labels, BorderLayout.WEST);
        gui.add(fields, BorderLayout.EAST);
		
		pubkey=new JLabel("Public Key p : ");
		labels.add(pubkey);
		
		pk=new JTextField(50);
		fields.add(pk);
		
		publickey=new JLabel("Public Key g : ");
		labels.add(publickey);
		
		pkg=new JTextField(50);
		fields.add(pkg);
		
		publickeyexp=new JLabel("Public Key g^a : ");
		labels.add(publickeyexp);
		
		pkga=new JTextField(50);
		fields.add(pkga);
		
		privatekey=new JLabel("Private Key a : ");
		labels.add(privatekey);
		
		prvkey=new JTextField(50);
		fields.add(prvkey);
		
		plain=new JLabel("Plain Text : ");
		labels.add(plain);
		
		plainText=new JTextField(50);
		fields.add(plainText);
		
		cipher=new JLabel("Cipher Text : ");
		labels.add(cipher);
		
		cipherText=new JTextField(50);
		fields.add(cipherText);
		
		bits=new JLabel("Bits : ");
		labels.add(bits);
		
		bitlen=new JTextField("128",10);
		fields.add(bitlen);
		
		tgen=new JLabel("Gen Time : ");
		labels.add(tgen);
		
		ttgen=new JTextField(10);
		fields.add(ttgen);
		
		tenc=new JLabel("Enc Time : ");
		labels.add(tenc);
		
		ttenc=new JTextField(10);
		fields.add(ttenc);
		
		tdec=new JLabel("Dec Time : ");
		labels.add(tdec);
		
		ttdec=new JTextField(10);
		fields.add(ttdec);
		
		encr=new JButton("Encrypt");
		labels.add(encr);
		encr.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String msg=plainText.getText();
				if(msg.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Plain Text can't be empty");
					return;
				}
				String pubp=pk.getText();
				String pubg=pkg.getText();
				String pubga=pkga.getText();
				if(pubp.isEmpty() || pubg.isEmpty() || pubga.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Public can't be empty");
					return;
				}
				BigInteger p=new BigInteger(pubp);
				BigInteger g=new BigInteger(pubg);
				BigInteger ga=new BigInteger(pubga);
				PublicKey pub=new PublicKey(p, g, ga);
				
				ElGamal crypto=new ElGamal();
				long time=System.nanoTime();
				CipherText cipher=crypto.Encrypt(pub, msg);
				long enctime=System.nanoTime()-time;
				String x="";
				for(int i=0;i<cipher.cipher.length;i++)
					x+=cipher.cipher[i].toString()+" ";
				
				ttenc.setText(enctime + " ns");
				cipherText.setText(x);
			}
		});
		
		decr=new JButton("Decrypt");
		fields.add(decr);
		decr.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String cipher=cipherText.getText();
				if(cipher.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Cipher Text can't be empty");
					return;
				}
				
				SecretKey sec=new SecretKey(new BigInteger(pk.getText()),new BigInteger(prvkey.getText()));
				ElGamal crypto=new ElGamal();
				long time=System.nanoTime();
				String plain=crypto.Decrypt(sec,cipher);
				long dectime=System.nanoTime()-time;
				
				ttdec.setText(dectime + " ns");
				plainText.setText(plain);
			}
		});
		
		gen=new JButton("Generate");
		gui.add(gen, BorderLayout.SOUTH);
		gen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Random rand=new Random();
				String bit=bitlen.getText();
				if(bit.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Bit Length can't be empty");
					return;
				}
				int bits=Integer.parseInt(bit);
				ElGamal crypto=new ElGamal(bits,rand);
				long time=System.nanoTime();
				Key key=crypto.Generate();
				long gentime=System.nanoTime()-time;
				
				ttgen.setText(gentime + " ns");
				pk.setText(key.pub.p.toString());
				pkg.setText(key.pub.g.toString());
				pkga.setText(key.pub.ga.toString());
				prvkey.setText(key.sec.a.toString());
			}
		});
		
		this.pack();
	}
}
