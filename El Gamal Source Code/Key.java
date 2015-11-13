public class Key
{
	public PublicKey pub;
	public SecretKey sec;
	
	public Key()
	{
	}
	
	public Key(PublicKey pub, SecretKey sec)
	{
		this.pub=pub;
		this.sec=sec;
	}
}
