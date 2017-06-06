import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;


public class ChartPlotter extends ApplicationFrame 
{

    private static final int COUNT=2 * 60;
	private static final long serialVersionUID = 1L;
	private static final String TITLE = null;
    private static int time = 10000;
    
    
    private Timer timer;
    
    
    static JLabel cp=new JLabel("Current Price :");
    static JLabel bp=new JLabel("Buying Price :");
    static JLabel sp=new JLabel("Selling Price : Not sold Yet");
    static JLabel anc=new JLabel("Anchored at : ");
    static JLabel profit=new JLabel("Max Profit : ");
    static JButton sellButton=new JButton(" Sell ");
    static JButton buyButton=new JButton(" Buy ");
    static String strategy[]={"Manual","Auto : Anchor","Auto : Scripted trade"};
    static JComboBox st=new JComboBox(strategy);
    static JLabel st_label=new JLabel("Strategy");
    
    Border border = cp.getBorder();
    Border margin = new EmptyBorder(10,10,10,50);
    
    

    public ChartPlotter(final String title,float range ) 
    {
        super(title);
        final DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(4, COUNT, new Second());
        
        dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2017));
        dataset.addSeries(primaryData(), 0, "Current Price");
        dataset.addSeries(primaryData(), 1, "Anchor");
        dataset.addSeries(primaryData(), 2, "Buying Price");
        dataset.addSeries(primaryData(), 3, "Selling Price");
        
        JFreeChart chart = createChart(dataset,range);       
        ChartPanel graph=new ChartPanel(chart);
        
        JPanel pricePanel = new JPanel();
        JPanel controlPanel = new JPanel();
       
        
        pricePanel.setBounds(0, 0, 500, 500);
        controlPanel.setBounds(0, 0, 100, MAXIMIZED_VERT);
        
        
        pricePanel.add(cp);
        pricePanel.add(bp);
        pricePanel.add(sp);
        
        
        
		sellButton.addActionListener(new ConfirmOrder());
		buyButton.addActionListener(new ConfirmOrder());
        
        controlPanel.add(st_label);
        controlPanel.add(st);
        controlPanel.add(anc);
        controlPanel.add(profit);
        controlPanel.add(sellButton);
        controlPanel.add(buyButton);
        
        
        cp.setBorder(new CompoundBorder(border, margin));
        bp.setBorder(new CompoundBorder(border, margin));
        sp.setBorder(new CompoundBorder(border, margin));
        anc.setBorder(new CompoundBorder(border, margin));
        profit.setBorder(new CompoundBorder(border, margin));
        
        
        controlPanel.setSize(500, 1000);
        st.setBounds(300,300,500,3000);
        anc.setBounds(0,100,50,300);
        profit.setBounds(0,100,50,300);
        st_label.setBounds(0,0,50,600);
        
        
        
        this.add(graph, BorderLayout.CENTER);
        this.add(pricePanel, BorderLayout.SOUTH);
        this.add(controlPanel, BorderLayout.EAST);
        

        timer = new Timer(time, new ActionListener() {

            float[] newData = new float[4];
            

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					newData[0] = (float) Api.CurrentPrice("xrpusd");
					newData[1] = (float) Bot.anchor;
					newData[2] = (float) Bot.buyingPrice;
					newData[3] = (float) Bot.sellingPrice;
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
                dataset.advanceTime();
                dataset.appendData(newData);
            }
        });
    }

    private float[] primaryData() {
        float [] a = new float[COUNT];
        float cp=0;
        try {
			cp=(float) Api.CurrentPrice("xrpusd");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        for(int i=0;i<a.length;i++)
        	a[i]=cp;
        return a;
    }

    private JFreeChart createChart(final XYDataset dataset,float r) 
    {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(TITLE, "time", "price", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(r*0.9, r*1.1);
        return result;
    }

    public void start() {
        timer.start();
    }
    
    
    public void ConfirmSell()
    {
    	
    }


}