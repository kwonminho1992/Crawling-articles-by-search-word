package com.sbs.selenium.exam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import com.opencsv.CSVWriter;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// crawling project
		
		ContentExtracting contentExtracting = new ContentExtracting();
				
		//arraylist of the articles' title, press name, date, link, naver news link, content
		ArrayList<ArrayList<String>> infos = contentExtracting.wordExtractingFromContent();
		
		// write to csv file
		try {
			CSVWriter write = new CSVWriter(new FileWriter("articles.csv"));
			Charset.forName("UTF-8");

			// Header column value
			String[] header = { "Title", "Press name", "Date", "Link", "Naver news link" , "Content", "Words" };
			write.writeNext(header);
			// Value
			for (int i = 0; i < infos.size(); i++) {
				String[] contents = new String[infos.get(i).size()];
				for (int j = 0; j < infos.get(i).size(); j++) {
					contents[j] = infos.get(i).get(j);
				}
				write.writeNext(contents);
			}
			write.close();
			System.out.println("complete to write a csv file!");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}	        
	}
	
}

