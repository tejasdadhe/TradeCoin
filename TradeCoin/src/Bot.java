import java.util.concurrent.TimeUnit;

public class Bot 
{
	
	static double max,min,lim,buyingPrice,sellingPrice;
	static float max_loss;
	static String pair="xrpxbt";
	
	static String domain = "https://api.kraken.com";
	static String path,data;

	
		
	
	static float PercentageDifference(double x,double basePrice)
	{
		return Math.abs((float) (((x-basePrice)/basePrice)*100));
	}
	
	static void SellAndTrade() throws InterruptedException
	{
		min=Api.CurrentPrice(pair);
		Api.SellCoin(pair,min);
		sellingPrice=min;
		double cp=min;
		while(true)
		{
			if(cp<=min)
			{
				min=cp;
			}
			else
			{
				System.out.println("Curent Price: " + cp + " | diff(current_price,anchor): "+PercentageDifference(cp,max)+ "% | diff(current_price,buying_price): " + PercentageDifference(cp,buyingPrice)+ "% | limit: " + lim + " | Anchor: " + min);
				if(PercentageDifference(cp,min)>=lim && PercentageDifference(cp,sellingPrice)>=lim)
				{
				return;	
				}		
			}
			TimeUnit.SECONDS.sleep(5);
			cp=Api.CurrentPrice(pair);
		}
		
	}
	
	static void BuyAndTrade() throws InterruptedException
	{	
		max=Api.CurrentPrice(pair);
		Api.BuyCoin(pair,max);
		buyingPrice=max;
		double cp=max;
		while(true)
		{
			if(cp>=max)
			{
				max=cp;
			}
			else
			{
				System.out.println("Curent Price: " + cp + " | diff(current_price,anchor): "+PercentageDifference(cp,max)+ "% | diff(current_price,buying_price): " + PercentageDifference(cp,buyingPrice)+ "% | limit: " + lim + " | Anchor: " + max);
				if(PercentageDifference(cp,max)>=lim && PercentageDifference(cp,buyingPrice)>=lim)
				{
				return;	
				}		
			}
			TimeUnit.SECONDS.sleep(5);
			cp=Api.CurrentPrice(pair);
		}
	}
	
	
	public static void main(String args[]) throws InterruptedException
	{
		max_loss=4;
		lim=0.5*max_loss;
		
		while(true)
		{	
			BuyAndTrade();
			SellAndTrade();
		}
		
	}
	
}
