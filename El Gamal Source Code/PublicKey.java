import java.math.BigInteger;

public class PublicKey
{
	BigInteger p,g,ga;
	
	public PublicKey(BigInteger p, BigInteger g, BigInteger ga)
	{
		this.p=p;
		this.g=g;
		this.ga=ga;
	}
}
