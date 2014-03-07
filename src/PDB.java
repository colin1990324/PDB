import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * personal information database. for personal use.
 * 
 * @author ColinMac
 * 
 */
public class PDB {

	public static final String instructionString = "please choose Command: add, search, save, exit";
	public static final String instructionAdd1 = "please input: firstName family gender\n you can specify -m to add more items";
	public static final String instructionAdd2 = "add successful";
	// attributes
	public static final String ATTRIBUTE_PERSON = "person";
	public static final String ATTRIBUTE_FRIENDS = "friends";
	public static final String ATTRIBUTE_USERID = "userID";
	public static final String ATTRIBUTE_FIRSTNAME = "firstName";
	public static final String ATTRIBUTE_LASTNAME = "lastName";
	public static final String ATTRIBUTE_NICKNAME = "nickname";
	public static final String ATTRIBUTE_COMPANY = "company";
	public static final String ATTRIBUTE_TITLE = "title";
	public static final String ATTRIBUTE_BIRTHDATE = "birthday";
	public static final String ATTRIBUTE_BACHELOR = "bachelor";
	public static final String ATTRIBUTE_MASTER = "master";
	public static final String ATTRIBUTE_HIGHSCHOOL = "highSchool";
	public static final String ATTRIBUTE_MIDDLESCHOOL = "middleSchool";
	public static final String ATTRIBUTE_QQ = "qq";
	public static final String ATTRIBUTE_CELL = "cell";
	public static final String ATTRIBUTE_EMAIL = "email";
	public static final String ATTRIBUTE_ADDRESS = "address";
	public static final String ATTRIBUTE_CREATIONTIME = "creationTime";
	public static final String ATTRIBUTE_LASTMODIFYTIME = "lastModificationTime";
	public static final String ATTRIBUTE_GENDER = "gender";
	public static final String ATTRIBUTE_COUNTER = "counter";
	public static final String FOLDER_ADDRESS = "/Users/ColinMac/Google Drive/MyDesign";

	public static void main(String[] args) {
		System.out.println("Welcome to PDB");
		// read file
		Document document = readDocument();
		// get command buffer from console
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// commandParser
		commandParser(br, document);
	}

	/**
	 * read xml with inner direct address
	 * 
	 * @return
	 */
	private static Document readDocument() {
		// load xml file
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			// document = saxReader.read(new File(FOLDER_ADDRESS +
			// "/people.xml"));
			document = saxReader
					.read(new File(
							"/Users/ColinMac/Documents/JavaWorkSpace/PeopleDataBase/src/people.xml"));
			System.out.println("people.xml load successful!");
		} catch (DocumentException e) {
			System.out.println("people.xml load failed, check data location!");
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * save current document and replace older one. previews xml will be stored
	 * as people.xml.old
	 * 
	 * @param br
	 * @param document
	 */
	public static void save(Document document) {
		// rename older document
		// File newFile = new File(FOLDER_ADDRESS + "/people.xml.old"), oldFile
		// = new File(
		// FOLDER_ADDRESS + "/people.xml");
		File newFile = new File(
				"/Users/ColinMac/Documents/JavaWorkSpace/PeopleDataBase/src/people.xml.old"), oldFile = new File(
				"/Users/ColinMac/Documents/JavaWorkSpace/PeopleDataBase/src/people.xml");
		oldFile.renameTo(newFile);
		// System.out.println(document.asXML());
		Writer fileWriter;
		try {
			OutputFormat xmlFormat = OutputFormat.createCompactFormat();
			xmlFormat.setIndent("   ");
			xmlFormat.setNewlines(true);
			// fileWriter = new FileWriter(FOLDER_ADDRESS + "/people.xml");
			fileWriter = new FileWriter(
					"/Users/ColinMac/Documents/JavaWorkSpace/PeopleDataBase/src/people.xml");
			XMLWriter xmlWriter = new XMLWriter(fileWriter, xmlFormat);
			xmlWriter.write(document);
			xmlWriter.flush();
			xmlWriter.close();
			System.out.println("save successful!");
			System.out.println("exit!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addPerson(String[] strings, Document document) {
		Element rootElement = document.getRootElement();
		// new Person
		Element element = rootElement.addElement(ATTRIBUTE_PERSON);
		// set ID
		int id = Integer.valueOf(rootElement.attributeValue(ATTRIBUTE_COUNTER)) + 1;
		element.addAttribute(ATTRIBUTE_USERID, String.valueOf(id));
		// base personal information
		Element firstNameElement = element.addElement(ATTRIBUTE_FIRSTNAME);
		firstNameElement.addText(strings[1]);
		Element lastNameElement = element.addElement(ATTRIBUTE_LASTNAME);
		lastNameElement.addText(strings[2]);
		Element genderElement = element.addElement(ATTRIBUTE_GENDER);
		genderElement.addText(strings[3]);
		Element creationTimeElement = element
				.addElement(ATTRIBUTE_CREATIONTIME);
		creationTimeElement.addText(new Date().toString());
		Element lastModifyTimeElement = element
				.addElement(ATTRIBUTE_LASTMODIFYTIME);
		lastModifyTimeElement.addText(new Date().toString());
		rootElement.setAttributeValue(ATTRIBUTE_COUNTER, String.valueOf(id));
		// more specific item
		if (strings.length > 4) {
			for (int i = 4; i < strings.length;) {
				if (!validAttribute(strings[i].substring(1))) {
					System.out
							.println(strings[i]
									+ ": is not valid element name. use add -help for instruction");
					return;
				}
				Element tempElement = element.addElement(strings[i]
						.substring(1));
				i++;
				if (strings[i] == null)
					break;
				String tmpString = "";
				while (!strings[i].substring(0, 1).equals("-")) {
					tmpString += strings[i] + " ";
					i++;
					if (i == strings.length)
						break;
				}
				tempElement.addText(tmpString);
			}
		}
		print(element);
	}

	/**
	 * 
	 * @param br
	 * @param document
	 */
	public static boolean searchPerson(String[] strings, Document document) {
		Element rootElement = document.getRootElement();
		Iterator<Element> iterator = rootElement.elements(ATTRIBUTE_PERSON)
				.iterator();
		int counter = 0;
		while (iterator.hasNext()) {
			Element rElement = iterator.next();
			if (rElement.element(ATTRIBUTE_FIRSTNAME).getText()
					.equals(strings[1])
					&& rElement.element(ATTRIBUTE_LASTNAME).getText()
							.equals(strings[2])) {
				print(rElement);
				counter++;
			}
		}
		if (counter == 0)
			return false;
		else
			return true;
	}

	public static void commandParser(BufferedReader br, Document document) {
		String[] strings = null;
		do {
			try {

				System.out.println(instructionString);
				System.out.print("->");
				strings = br.readLine().split("\\s+");
			} catch (IOException e) {
				e.printStackTrace();
			}
			// add more specific item by given User_ID
			if (strings[0].equals("add") && strings[1].equals("-id")) {
				addPerson(strings[2], strings, document);
			}
			// add a person information
			if (strings[0].equals("add") && !strings[1].equals("-id")) {
				addPerson(strings, document);
			}
			//
			else if (strings[0].equals("remove")) {
			}
			// search
			else if (strings[0].equals("search")) {
				if (!searchPerson(strings, document))
					System.out.println("no such person");
			}
			// save
			else if (strings[0].equals("save")) {
				save(document);
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			// exit
			else if (strings[0].equals("exit")) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			// // invalid command
			// else {
			// System.out.println("command not valid, please use 'help' command");
			// }
		} while (true);
	}

	/**
	 * given userID, add more specific items
	 * 
	 * @param string
	 * @param strings
	 * @param document
	 */
	private static void addPerson(String userID, String[] strings,
			Document document) {
		// get person
		Element rootElement = document.getRootElement();
		Element element = null;
		Iterator<Element> iterator = rootElement.elements(ATTRIBUTE_PERSON)
				.iterator();
		while (iterator.hasNext()) {
			Element rElement = iterator.next();
			if (rElement.attributeValue(ATTRIBUTE_USERID).equals(userID)) {
				element = rElement;
				break;
			}
		}
		if (element == null) {
			System.out.print("UserID: " + userID + " no found!");
			return;
		}
		// set last modify time
		Element lastModifyTimeElement = element
				.element(ATTRIBUTE_LASTMODIFYTIME);
		lastModifyTimeElement.setText(new Date().toString());
		// add more specific items
		if (strings.length > 3) {
			for (int i = 3; i < strings.length;) {
				if (!validAttribute(strings[i].substring(1))) {
					System.out
							.println(strings[i]
									+ ": is not valid element name. use add -help for instruction");
					return;
				}
				Element tempElement = element.addElement(strings[i]
						.substring(1));
				i++;
				if (strings[i] == null)
					break;
				String tmpString = "";
				while (!strings[i].substring(0, 1).equals("-")) {
					tmpString += strings[i] + " ";
					i++;
					if (i == strings.length)
						break;
				}
				tempElement.addText(tmpString);
			}
		}
		print(element);
	}

	/**
	 * print element 1. element attributes 2. children elements
	 * 
	 * @param element
	 */
	public static void print(Element element) {
		String string = "";
		for (int i = 0; i < element.attributeCount(); i++) {
			string += element.attribute(i).getName() + ": "
					+ element.attribute(i).getText() + "\n";
		}
		Iterator<Element> iterator = element.elementIterator();
		while (iterator.hasNext()) {
			Element r = iterator.next();
			string += r.getName() + ": " + r.getText() + "\n";
		}
		System.out.println(string);
	}

	/**
	 * check if input is valid element name
	 * 
	 * @param string
	 * @return
	 */
	public static boolean validAttribute(String string) {
		if (string.equals(ATTRIBUTE_PERSON))
			return true;
		else if (string.equals(ATTRIBUTE_FRIENDS))
			return true;
		else if (string.equals(ATTRIBUTE_USERID))
			return true;
		else if (string.equals(ATTRIBUTE_FIRSTNAME))
			return true;
		else if (string.equals(ATTRIBUTE_LASTNAME))
			return true;
		else if (string.equals(ATTRIBUTE_NICKNAME))
			return true;
		else if (string.equals(ATTRIBUTE_COMPANY))
			return true;
		else if (string.equals(ATTRIBUTE_TITLE))
			return true;
		else if (string.equals(ATTRIBUTE_BIRTHDATE))
			return true;
		else if (string.equals(ATTRIBUTE_BACHELOR))
			return true;
		else if (string.equals(ATTRIBUTE_MASTER))
			return true;
		else if (string.equals(ATTRIBUTE_HIGHSCHOOL))
			return true;
		else if (string.equals(ATTRIBUTE_QQ))
			return true;
		else if (string.equals(ATTRIBUTE_CELL))
			return true;
		else if (string.equals(ATTRIBUTE_EMAIL))
			return true;
		else if (string.equals(ATTRIBUTE_ADDRESS))
			return true;
		else if (string.equals(ATTRIBUTE_CREATIONTIME))
			return true;
		else if (string.equals(ATTRIBUTE_LASTMODIFYTIME))
			return true;
		else if (string.equals(ATTRIBUTE_GENDER))
			return true;
		else if (string.equals(ATTRIBUTE_COUNTER))
			return true;
		else if (string.equals(ATTRIBUTE_MIDDLESCHOOL))
			return true;
		else
			return false;
	}
}
