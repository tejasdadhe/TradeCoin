import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
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
    private static int time = 5000;
    private Timer timer;

    public ChartPlotter(final String title,float range ) {
        super(title);
        final DynamicTimeSeriesCollection dataset =
            new DynamicTimeSeriesCollection(2, COUNT, new Second());
        dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2017));
        dataset.addSeries(primaryData(), 0, "Current Price");
        dataset.addSeries(primaryData(), 1, "Anchor");
        JFreeChart chart = createChart(dataset,range);


        

        this.add(new ChartPanel(chart), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout());
        this.add(btnPanel, BorderLayout.SOUTH);

        timer = new Timer(time, new ActionListener() {

            float[] newData = new float[2];
            

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					newData[0] = (float) Api.CurrentPrice("xrpusd");
					newData[1] = (float) Bot.max;
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

    private JFreeChart createChart(final XYDataset dataset,float r) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            TITLE, "time", "price", dataset, true, true, false);
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


}