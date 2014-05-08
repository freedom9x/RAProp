package dataset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;

public class Vali {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Vali vali = new Vali();
//		vali.Analysis("tweet2011/Analysis/MB01_AG.txt");
//		vali.Analysis("tweet2011/Analysis/MB02_AG.txt");
		vali.ScaleDataSet(200, "tweet2011/tweetN/MB01_AG.txt");
		vali.ScaleDataSet(200, "tweet2011/tweetN/MB02_AG.txt");
		System.out.println("----done----");
	}
	public void ScaleDataSet(int n, String path_file) throws IOException
	{
		String nameID = path_file.split("_")[0];
		nameID = nameID.split("/")[2];
		BufferedReader reader = new BufferedReader(new FileReader("tweet2011/Analysis/vali.txt"));
		String term;
		int c_2 = 0;
		int c0 = 0;
		int c1 = 0;
		int c2 = 0;
		n = n-c_2;
		//ouble rate_cale = 1.0 - 
		// {{ tinh so luong twett can lay
		while((term = reader.readLine())!=null)
		{
			String[] parts = term.split("\t");
			String id = parts[0];
			if(nameID.equals(id))
			{
				c_2 = Integer.parseInt(parts[1]);
				c0 = Integer.parseInt(parts[2]);
				c1 = Integer.parseInt(parts[3]);
				c2 = Integer.parseInt(parts[4]);
			}
		}
		reader.close();
		int sum = c_2 + c0+c1+c2;
		double rate_scale = n/(double)sum;
//		c0 = (int)(c0 * rate_scale);
//		c1 = (int)(c1 * rate_scale);
//		c2 = (int)(c2 * rate_scale);
		c0 = (int)Math.round(c0 * rate_scale);
		c1 = (int)Math.round(c1 * rate_scale);
		c2 = (int)Math.round(c2 * rate_scale);
		int sum2 = c0 + c1 + c2;
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println(c0+"\t"+c1+"\t"+c2+"\t"+sum2+"\t"+df.format(rate_scale));
		if(sum2<n)
		{
			int bu = n - sum;
			c1 = c1 + bu;
		}
		int sum3 = c0 + c1 + c2;
		System.out.println(c0+"\t"+c1+"\t"+c2+"\t"+sum2+ df.format(rate_scale));
		// }}
		//da tinh dc so luong tweet can lay

		//doc file lay 200 tweet theo ti le da scalde
		BufferedReader reader2 = new BufferedReader(new FileReader(path_file));
		String term1;
		int cc0 = 0;
		int cc1 = 0;
		int cc2 = 0;
		int dem = 0;
		while((term1 = reader2.readLine())!=null)
		{
			PrintWriter pw = new PrintWriter(new 
					FileWriter("tweet2011/Available/"+nameID+"_"+n+".txt", true));
			String[] parts = term1.split(" ");
			if(!parts[2].equals("-2") && dem != n)
			{
				int key = Integer.parseInt(parts[2]);
				switch (key) {
				case 0:
					if(cc0>=c0) break;
					pw.println(term1);
					System.out.println(path_file+"\t"+term1);
					cc0++;
					dem++;
					System.out.println(dem);
					break;
				case 1:
					if(cc1>=c1) break;
					pw.println(term1);
					System.out.println(path_file+"\t"+term1);
					cc1++;
					dem++;
					
					break;
				case 2:
					if(cc2>=c2) break;
					pw.println(term1);
					System.out.println(path_file+"\t"+term1);
					cc2++;
					dem++;
					break;
				default:
					break;
				}
			}
			pw.close();
		}
		reader.close();
	}
	public void Analysis(String path_file) throws IOException
	{
		String nameID = path_file.split("_")[0];
		nameID = nameID.split("/")[2];
		BufferedReader reader = new BufferedReader(new FileReader(path_file));
		//PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/vali.txt", true));
		String term;
		int c0 = 0;
		int c1 = 0;
		int c2 = 0;
		int c_2 = 0;
		while((term=reader.readLine())!=null)
		{
			String[] parts = term.split(" ");
			int val;
			switch (val= Integer.parseInt(parts[2])) {
			case 0:
				c0++;
				break;
			case 1:
				c1++;
				break;
			case 2:
				c2++;				
				break;
			case -2:
				c_2++;
				break;
			default:
				break;
			}			
		}
		reader.close();
		int sum = c0 + c1 + c2 + c_2;
		double rate0 = c0/(double)sum;
		double rate1 = c1/(double)sum;
		double rate2 = c2/(double)sum;
		double rate_2 = c_2/(double)sum;
		DecimalFormat df = new DecimalFormat("#.####");
		PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/Analysis/vali.txt", true));
		pw.println(nameID+"\t"+c_2+"\t"+c0+"\t"+c1+"\t"+c2+
				"\t"+df.format(rate_2)+"\t"+df.format(rate0)
				+"\t"+df.format(rate1)+"\t"+df.format(rate2));
		pw.close();
		
		//pw.close();
	}
}
