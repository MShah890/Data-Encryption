import java.math.BigInteger;
import java.util.Random;

public class ElGamal
{
	int bits;
	Random rand;
	
	public ElGamal()
	{
	}
	
	public ElGamal(int b, Random r)
	{
		bits=b;
		rand=r;
	}
	
	public CipherText Encrypt(PublicKey pubkey,String msg)
	{
		BigInteger k=new BigInteger(bits,rand);
		k=k.mod(pubkey.p);
		k=k.add(BigInteger.ONE);							
		
		BigInteger gk=pubkey.g.modPow(k, pubkey.p);
		BigInteger[] cipher=new BigInteger[msg.length()+1];
		cipher[0]=gk;
		for(int i=0;i<msg.length();i++)
		{
			int x=msg.charAt(i);
			BigInteger tmp=BigInteger.valueOf(x);
			tmp=tmp.multiply(pubkey.ga.modPow(k, pubkey.p));
			tmp=tmp.mod(pubkey.p);
			cipher[i+1]=tmp;
		}
        return new CipherText(cipher);
	}
	
	public String Decrypt(SecretKey sk, String cipher)
	{
		String[] encrypted=cipher.split(" ");
		BigInteger gk=new BigInteger(encrypted[0]);
		String ans="";
		for(int i=1;i<encrypted.length;i++)
		{
			BigInteger c=new BigInteger(encrypted[i]);
			BigInteger gab=gk.modPow(sk.a, sk.p);
	        BigInteger inv=gab.modInverse(sk.p);
	        BigInteger m=inv.multiply(c).mod(sk.p);
			ans+=(char)m.intValue();
		}
		return ans;
	}
	
	public Key Generate()
	{
		BigInteger p,q;
		while(true)
		{
			p=getPrime(bits, 1, rand);
			BigInteger p1=p.subtract(new BigInteger("1"));
			q=p1.divide(new BigInteger("2"));
			if(q.isProbablePrime(1))
				break;
		}
		
		BigInteger g=new BigInteger(bits,1,rand);
		g=g.mod(p);
		while(g.modPow(q,p).compareTo(new BigInteger("1"))!=0)
		{
			g=new BigInteger(bits,1,rand);
			g.mod(p);
		}
		
		BigInteger a=new BigInteger(bits,1,rand);
		while(a.compareTo(p)>=0)
			a=new BigInteger(bits,1,rand);
		a.add(new BigInteger("1"));
		
		BigInteger ga=g.modPow(a,p);
		
		Key key=new Key(new PublicKey(p, g, ga), new SecretKey(p,a));
		return key;
	}
	
	public static BigInteger getPrime(int bits, int certainty, Random r)
	{
		while(true)
		{
			BigInteger p=new BigInteger(bits, certainty, r);
			if(p.signum()==-1)
				p = p.negate();
			if(p.isProbablePrime(certainty))
				return p;
		}
	}
}
