package com.halemaster.enchanting.testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class OutputRunListener extends RunListener
{
	private File folderLocation;
	
	public OutputRunListener(String folderLocation)
	{
		this.folderLocation = new File(folderLocation);
	}
	
	public String getFolderLocation()
	{
		return folderLocation.getAbsolutePath();
	}

	public void testStarted(Description description) 
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFolderLocation() + 
				File.separator + "AllTests.txt", true))) 
	    {
			writer.write("Beginning test:" + description.getClassName() + "." + 
				   description.getMethodName() + "\n");
			System.out.println("Beginning test:" + description.getClassName() + "." + 
				   description.getMethodName());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void testFinished(Description description)
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFolderLocation() + 
				File.separator + "AllTests.txt", true))) 
	    {
			writer.write("Finished test:" + description.getClassName() + "." + 
				   description.getMethodName() + "\n");
			System.out.println("Finished test:" + description.getClassName() + "." + 
				   description.getMethodName());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void testFailure(Failure failure)
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFolderLocation() + 
				File.separator + "AllTests.txt", true))) 
	    {
			writer.write("Test Failure:" + failure.getTestHeader() +"\n");
			System.out.println("Test Failure:" + failure.getTestHeader());
			if(failure.getMessage() != null)
			{
				writer.write(failure.getMessage() + "\n");
				System.out.println(failure.getMessage());
			}
			writer.write(failure.getTrace() + "\n");
			System.out.println(failure.getTrace());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void testRunStarted(Description description)
	{
		if(folderLocation.exists() && folderLocation.isDirectory())
		{
			deleteFolder(folderLocation);
		}
		folderLocation.mkdirs();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFolderLocation() + 
				File.separator + "AllTests.txt", true))) 
	    {
			writer.write("Beginning test run:\n");
			System.out.println("Beginning test run:");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void testRunFinished(Result result)
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFolderLocation() + 
				File.separator + "AllTests.txt", true))) 
	    {
			writer.write("Ending test run\n");
			System.out.println("Ending test run");
			writer.write("tests run: " + result.getRunCount() + "\n");
			System.out.println("tests run: " + result.getRunCount());
			writer.write("tests failed: " + result.getFailureCount() + "\n");
			System.out.println("tests failed: " + result.getFailureCount());
			writer.write("tests ignored: " + result.getIgnoreCount() + "\n");
			System.out.println("tests ignored: " + result.getIgnoreCount());
			writer.write("Running time: " + (result.getRunTime() / 1000.0) + "s\n");
			System.out.println("Running time: " + (result.getRunTime() / 1000.0) + "s");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void deleteFolder(File folder) 
	{
	    File[] files = folder.listFiles();
	    if(files!=null) 
	    {
	        for(File f: files)
	        {
	            if(f.isDirectory()) 
	            {
	                deleteFolder(f);
	            } 
	            else 
	            {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
}
