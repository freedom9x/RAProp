package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import dataset.CreateData;

import rank.AG_graph;

public class MainAG {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//CreateData.main("tweet2011/");// lay tweet, bo reteet, lu trong test folder
//		AG_graph.main("BBC World Service staff cuts", "MB01", "tweet2011/test/MB01_AG.txt");
//		TopK_result.main("tweet2011/test/MB01_AG.txt", "tweet2011/result/AG/MB01_ag.txt");
		//AG_graph.main("MB02", "tweet2011/test/MB02_AG.txt");
////		TopK_result.main("tweet2011/test/MB02_AG.txt", "tweet2011/result/AG/MB02_ag.txt");
//		TopK_result.TopK("tweet2011/result/AG/MB01_result.txt");
//		TopK_result.TopK("tweet2011/result/AG/MB02_result.txt");
		
		//CommonLib.DevideFile("")
		ComputeAG("tweet2011/test/49QueryforTest/");
	}
	public static void ComputeAG(String path_folder) throws Exception
	{
		File folder = new File(path_folder);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles)
		{
			String path = path_folder+file.getName();
			String queryID=file.getName().split("_")[0];
			//String query = CreateData.FindQuery(queryID);
			
			System.out.println(file.getName()+"------------------");
			AG_graph.main(queryID, path);
			System.out.println(file.getName()+"done-------------");
		}
	}

}
