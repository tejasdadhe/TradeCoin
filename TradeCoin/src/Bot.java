import java.awt.EventQueue;
import java.util.concurrent.TimeUnit;

import org.jfree.ui.RefineryUtilities;

public class Bot 
{
	
	protected static final String TITLE = "TEJAS";
	static double max,min,lim,buyingPrice,sellingPrice,buyingLimit,sellingLimit;
	static float max_loss;
	static String pair="xrpusd";
	
	static String domain = "https://api.kraken.com";
	static String path,data;
	static float achg=0;

	
		
	
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
				if(PercentageDifference(cp,min)>=sellingLimit && PercentageDifference(cp,sellingPrice)>=lim)
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
				if(PercentageDifference(cp,max)>=buyingLimit && PercentageDifference(cp,buyingPrice)>=lim)
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
		
		achg=(float) Api.CurrentPrice("xrpusd");
		
		EventQueue.invokeLater(new Runnable() 
		{   
            public void run() {
            	ChartPlotter demo = new ChartPlotter(TITLE,achg);
                demo.pack();
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
                demo.start();
            }
        });
		
		
		max_loss=4;
		
		buyingLimit=0.2*max_loss;
		sellingLimit=0.2*max_loss;
		lim=0.5*max_loss;
		
		while(true)
		{	
			System.out.println("this executed 1");
			BuyAndTrade();
			System.out.println("this executed 2");
			SellAndTrade();
			System.out.println("this executed 3");
		}
		
		
	}
	
}
