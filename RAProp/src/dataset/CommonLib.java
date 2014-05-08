package dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CommonLib {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	//	FindUserName("32899374300266497");
		GetALLUserName("tweet2011/qrels.microblog2011.txt");
	}
	public static void GetALLUserName(String path_file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(path_file));
		String term;
		while(( term = reader.readLine())!=null)
		{
			String[] parts = term.split(" ");
			
			if(!parts[0].equals("MB01") && !parts[0].equals("MB02"))
			{
				return;
			}
			else
			{
				String name = FindUserName(parts[2]);
				if(name!=null)
				{
					PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/tweet2Topic.txt", true));
					pw.println(parts[0]+" "+ parts[2]+ " "+parts[3]+" "+name);
					pw.close();
				}
				
				
			}
		}
		reader.close();
	}
	public static String FindUserName(String tweetID) throws IOException
	{
		File folder1 = new File("tweet2011");
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

}
