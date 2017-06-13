import java.awt.EventQueue;
import java.util.concurrent.TimeUnit;

import org.jfree.ui.RefineryUtilities;

public class Bot 
{
	
	protected static final String TITLE = "TradeCoin";
	static double anchor,lim,buyingPrice,sellingPrice,buyingLimit,sellingLimit,profit;
	static float max_loss;
	static String pair="xrpusd";
	
	static String domain = "https://api.kraken.com";
	static String path,data;
	static float achg=0;
	static float volume=653;
	static int strategy;
	
	
	

	
		
	
	static float PercentageDifference(double x,double basePrice)
	{
		return Math.abs((float) (((x-basePrice)/basePrice)*100));
	}
	
	static void SellAndTrade() throws InterruptedException
	{
		anchor=Api.CurrentPrice(pair);
		Api.SellCoin(pair,anchor);
		sellingPrice=anchor;
		ChartPlotter.sp.setText("Selling Price : "+ sellingPrice +"\t");
		double cp=anchor;
		while(true)
		{
			if(cp<=anchor)
			{
				anchor=cp;
				ChartPlotter.anc.setText("Anchored at : "+ anchor +"\t");
			}
			else
			{
				System.out.println("Curent Price: " + cp + " | diff(current_price,anchor): "+PercentageDifference(cp,anchor)+ "% | diff(current_price,buying_price): " + PercentageDifference(cp,buyingPrice)+ "% | limit: " + lim + " | Anchor: " + anchor);
				if(PercentageDifference(cp,anchor)>=sellingLimit && PercentageDifference(cp,sellingPrice)>=lim)
				{
				return;	
				}		
			}
			TimeUnit.SECONDS.sleep(5);
			cp=Api.CurrentPrice(pair);
			profit=(cp-sellingPrice);
			ChartPlotter.profit.setText("Max Profit : "+ String.format("%.2f",profit*volume) +" ("+ String.format("%.2f",(profit/sellingPrice)*100)+"%)\t");
			ChartPlotter.cp.setText("Current Price : "+ cp +"\t");
			ChartPlotter.tradePrice.setText(String.format("%.8f", cp));
		}
		
	}
	
	static void BuyAndTrade() throws InterruptedException
	{	
		anchor=Api.CurrentPrice(pair);
		Api.BuyCoin(pair,anchor);
		buyingPrice=anchor;
		ChartPlotter.bp.setText("Buying Price : "+ buyingPrice +"\t");
		double cp=anchor;
		while(true)
		{
			if(cp>=anchor)
			{
				anchor=cp;
				ChartPlotter.anc.setText("Anchored at : "+ anchor +"\t");
			}
			else
			{
				
				System.out.println("Curent Price: " + cp + " | diff(current_price,anchor): "+PercentageDifference(cp,anchor)+ "% | diff(current_price,buying_price): " + PercentageDifference(cp,buyingPrice)+ "% | limit: " + lim + " | Anchor: " + anchor);
				if(PercentageDifference(cp,anchor)>=buyingLimit && PercentageDifference(cp,buyingPrice)>=1)
				{
				return;	
				}		
			}
			TimeUnit.SECONDS.sleep(5);
			cp=Api.CurrentPrice(pair);
			profit=(cp-buyingPrice);
			ChartPlotter.profit.setText("Max Profit : "+ String.format("%.2f",profit*volume) +" ("+ String.format("%.2f",(profit/buyingPrice)*100)+"%)\t");
			ChartPlotter.cp.setText("Current Price : "+ cp +"\t");
			ChartPlotter.tradePrice.setText(String.format("%.8f", cp));
		}
	}
	
	
	public static void main(String args[]) throws InterruptedException
	{
		strategy=0;
		achg=(float) Api.CurrentPrice("xrpusd");
		anchor=achg;
		buyingPrice=anchor;
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
		sellingPrice=achg;
		ChartPlotter.cp.setText("Current Price : "+ achg);
		profit=0;
		
		
		
		while(true)
		{	
			double oldPrice= Api.CurrentPrice(pair);
			while(strategy==0)
			{
				System.out.println("Executing strategy 0");	//Manual
				TimeUnit.SECONDS.sleep(4);
			}
			while(strategy==1)
			{	
				System.out.println("this executed 1");	//Anchor
				BuyAndTrade();
				System.out.println("this executed 2");
				profit=0;
				SellAndTrade();
				System.out.println("this executed 3");
			}
			while(strategy==2)
			{
				System.out.println("Executing strategy 2");	//Scripted
				TimeUnit.SECONDS.sleep(4);
			}
			while(strategy==3)
			{
				TimeUnit.SECONDS.sleep(4);
				double newPrice=Api.CurrentPrice(pair);
				float pd=PercentageDifference(newPrice,oldPrice);
				if(pd>0.1)
				{
					Api.BuyCoin(pair, newPrice*0.995);
					Api.SellCoin(pair, newPrice*1.005);
					ChartPlotter.trailingTradeLog.append("\n");
					oldPrice=newPrice;
				}
				ChartPlotter.trailingTradeLog.append("Current Price : "+newPrice+" | Percentage Difference : " +pd+" \n");
			}	
		}
			
		
		
	}
	
}
