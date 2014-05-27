package rank;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import dataset.ReadFile;


public class AG_graph {

	//public static String query = "nexus 5";
	public static void main(String query,String queryID, String path) throws Exception {
		// TODO Auto-generated method stub
		//int N = 200; so luong twwet
		//String queryID="MB01";
		//String query= "BBC World Service staff cuts";
		int N = ReadFile.CountTweet(path);
		double[][] Agreement_Graph = new double[N+1][N];
		Tweet[] tweets = new Tweet[200];
		//query = "nexus 5";
		//lay 200 tweet
		try {
			tweets=ReadFile.GetNtweet(path, N);
			ReadFile.WriteResult(tweets, "tweet2011/result/AG/"+queryID +"_rm.txt", true, query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println(e.toString());
		}
		System.out.println("-------------------");	
//		double agc;
//		agc = TFIDF.Agreement(tweets[18].content, tweets[17].content, tweets, N, query);
//		System.out.println(agc);
		 //tinh agreemnet graph
		for(int i = 0; i < N; i++)
		{
			
			for(int j = i+1; j <N; j++)
			{		
				if(i==j)
				{
					Agreement_Graph[i][j]=0.0;
					continue;
				}
				Agreement_Graph[i][j]=TFIDF.Agreement(tweets[i].content,
						tweets[j].content, tweets, N, query);
				System.out.print(i+"\\"+j+"="+Agreement_Graph[i][j]+"  ");					
					
					
			}
			System.out.println();
			
		}//copy ji ji
		System.out.println("coping");
		for(int i = 0; i<N; i++)
		{
			for(int j = 0; j<N; j++)
			{
				if(i==j) continue;
				if(Agreement_Graph[i][j]>0) Agreement_Graph[j][i]=Agreement_Graph[i][j];
				if(Agreement_Graph[j][i]>0) Agreement_Graph[i][j]= Agreement_Graph[j][i];
			}
		}
		System.out.println("done");
		PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/result/AG/"+queryID+"_ag.txt"));
		for(int i = 0; i<N; i++)
		{
			for(int j = 0; j <N; j++)
			{
				pw.print(i+"\\"+j+"="+Agreement_Graph[i][j]+"  ");
			}
			pw.println();
		}
		
		pw.close();
		System.out.println("-------------------");	
		
	}
}
