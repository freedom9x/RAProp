package dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.math.ode.DerivativeException;

import com.temesoft.google.pr.PageRankService;

public class CommonLib {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	//	FindUserName("32899374300266497");
		//BBC World Service to 'cut up to 650 jobs' (Guardian) http://feedzil.la/gvJ6Gm
		//GetALLUserName("tweet2011/Available/qrels.microblog2011.txt");
		
//		String domain = "pr backlink backlinks service - TurboCommunity: pr backlink backlinks service Announcements.... http://bit.ly/guPFjf http://bit.ly/7JCDu";
//		int pr = PageRank(domain);
//		System.out.println(domain+" PG=" + pr);
//		String path_folder = "tweet2011/DataTest/"; 
//		File folder = new File(path_folder);
//		File[] listOfFiles = folder.listFiles();
//		//PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/TrainData/train.txt", true));
//		for (File file : listOfFiles)
//		{
//			String term = "";
//			int dem_s = 0;
//			int dem_t = 0;
//			BufferedReader reader_s = new BufferedReader(new FileReader("tweet2011/test/du phong/49QueryforTest/"+file.getName()));
//			while((term=reader_s.readLine())!=null)
//			{
//				dem_s++;
//			}
//			BufferedReader reader_t = new BufferedReader(new FileReader(path_folder+file.getName()));
//			while((term=reader_t.readLine())!=null)
//			{
//				dem_t++;
//			}
//			System.out.println(file.getName()+"s="+dem_s+"---t="+dem_t);
//			reader_s.close();
//			reader_t.close();
//		}
		String path_s= "tweet2011/test/du phong/49QueryforTest/MB15_AG.txt";
		String path_t= "tweet2011/DataTest/MB15_AG.txt" ;
		DuplicateDetec(path_s, path_t);
	}
	public static String NaiveURLExpander(String address){
        String result = address;
        URLConnection urlConn =  connectURL(address);
        
        if(urlConn!=null)
        {
        	urlConn.getHeaderFields();
        	result = urlConn.getURL().toString();
        }
		return result;
    }
	

	



/* connectURL - This function will take a valid url and return a 
URL object representing the url address. */
	public static URLConnection connectURL(String strURL) 
	{
		URLConnection conn =null;
        try {
        	URL inputURL = new URL(strURL);
        	conn = inputURL.openConnection();
            int test = 0;

        	}catch(MalformedURLException e) {
        		System.out.println("Please input a valid URL");
        	}catch(IOException ioe) {
        		System.out.println("Can not connect to the URL");
        	}
        return conn;
}


	public static int PageRank(String text2) throws IOException
	{
		
		int result = 0;
		String[] parts = text2.split(" ");
		PageRankService ps = new PageRankService();
		for (String string : parts)
		{
			if(string.contains("http://"))
			{
//				string = string.substring(string.indexOf("http"));
//				System.out.println(string);
//				string = NaiveURLExpander(string);
				//System.out.println(string);
				String domain = "";
				try {
					domain = NaiveURLExpander(string);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return -1;
				}
				if(domain.substring(8).contains("/"))
					domain = domain.substring(0,domain.indexOf("/",8));
				result =  result + ps.getPR(domain); 
			}
		}
		
		return result;
	}
	public static void GetALLUserName(String path_file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(path_file));
		String term;
		while(( term = reader.readLine())!=null)
		{
			String[] parts = term.split(" ");
//			
//			if(!parts[0].equals("MB01") && !parts[0].equals("MB02"))
//			{
//				return;
//			}
//			else
//			{
				String name = FindUserName(parts[2]);
				if(name!=null)
				{
					PrintWriter pw = new PrintWriter(new 
							FileWriter("tweet2011/49TrecData/"+parts[0]+"_AG.txt", true));
					pw.println(parts[0]+" "+ parts[2]+ " "+parts[3]+" "+name);
					pw.close();
				}
				
				
			//}
		}
		reader.close();
	}
	public static String FindUserName(String tweetID) throws IOException
	{
		File folder1 = new File("tweet2011/TRECdataset");
		File[] folder2 = folder1.listFiles();
		for (File folder : folder2) 
		{
			//System.out.println("searching in "+folder1.getName()+"/" +folder.getName());
			for (File file : folder.listFiles())
			{
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String term;
				while(( term = reader.readLine())!=null)
				{					
					String[] part = term.split("\t");
					if(tweetID.equals(part[0]))
					{
						System.out.println(folder1.getName()+"/" +folder.getName()+"/"+ file.getName()+"\t"+part[1]+"\t"+tweetID);
//						System.out.println(part[1]);
						reader.close();
						return part[1];
					}
				}
				reader.close();
			}
		}
		System.out.println("ko thay??"+tweetID);
		String term="null";
		return term;
	}
	public static void DevideFile(String path) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String term;
		while((term = reader.readLine())!=null)
		{
			String id = term.split("\t")[0];
			//String content = term.split("\t")[0];
			PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/tweetN/"+id+"_AG.txt"), true);
			pw.println(term);
		}
		reader.close();
		
	}
	public static void DuplicateDetec(String path_s, String path_t) throws IOException
	{
		String term;
		BufferedReader reader_s = new BufferedReader(new FileReader(path_s));
		int id= 1;
		while((term = reader_s.readLine())!=null)
		{
			int dem = 0;
			
			String ids = term.split("\t")[1];
			BufferedReader reader_t = new BufferedReader(new FileReader(path_t));
			String term_t;
			while((term_t = reader_t.readLine())!=null)
			{
				String idt = term_t.split("#")[1];
				if(ids.equals(idt))
					dem++;
			}
			System.out.println(id+".  "+ids+"--"+dem);
			if(dem==2) System.out.println("No o tren ne");
			reader_t.close();
			id++;
		}
		reader_s.close();
		
	}
}
