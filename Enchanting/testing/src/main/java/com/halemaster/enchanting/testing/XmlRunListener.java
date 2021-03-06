package com.halemaster.enchanting.testing;

import java.io.File;
import java.time.Instant;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlRunListener  extends RunListener
{
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Document document;
	private Element rootElement;
	private String location;
	
	public XmlRunListener(String loc)
	{
		location = loc;
	}
	
	private Element findTest(Description description)
	{
		Element test = null;
		
		for(int i = 0; i < rootElement.getChildNodes().getLength(); i++)
		{
			Node node = rootElement.getChildNodes().item(i);
			NamedNodeMap map = node.getAttributes();
			if(null != map.getNamedItem("classname") && null != map.getNamedItem("name") &&
					description.getClassName().equals(((Attr)map.getNamedItem("classname")).getValue()) &&
					description.getMethodName().equals(((Attr)map.getNamedItem("name")).getValue()))
			{
				test = (Element) node;
			}
		}
		
		return test;
	}
	
	public void testStarted(Description description) 
	{
		Element test = document.createElement("testcase");	
		test.setAttribute("classname", description.getClassName());
		test.setAttribute("name", description.getMethodName());
		rootElement.appendChild(test);
	}

	public void testFailure(Failure failure)
	{
		Element test = findTest(failure.getDescription());
		Element fail = document.createElement("failure");
		fail.setAttribute("message", failure.getMessage());
		fail.appendChild(document.createTextNode(failure.getTrace()));
		test.appendChild(fail);
	}

	public void testRunStarted(Description description)
	{
		try
		{
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			document = docBuilder.newDocument();
			
			rootElement = document.createElement("testsuite");
			document.appendChild(rootElement);
			rootElement.setAttribute("name", "test.integration");
			rootElement.setAttribute("timestamp", String.valueOf(Date.from(Instant.now())));
			rootElement.setAttribute("hostname","localhost");
			
			Element properties = document.createElement("properties");		
			rootElement.appendChild(properties);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
	}
	
	public void testRunFinished(Result result)
	{
		rootElement.setAttribute("tests", String.valueOf(result.getRunCount()));
		rootElement.setAttribute("failures", String.valueOf(result.getFailureCount()));
		rootElement.setAttribute("ignores", String.valueOf(result.getIgnoreCount()));
		rootElement.setAttribute("time", String.valueOf(result.getRunTime()));
		try
		{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(location + "/testResults.xml"));
	 
			transformer.transform(source, streamResult);
	 
			System.out.println("File saved!");
		}
		catch (TransformerConfigurationException e) 
		{
			e.printStackTrace();
			System.out.println("Failed save!");
		} 
		catch (TransformerException e) 
		{
			e.printStackTrace();
			System.out.println("Failed save!");
		}
	}
}
