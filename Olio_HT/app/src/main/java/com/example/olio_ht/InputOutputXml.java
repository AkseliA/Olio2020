package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */
import android.content.Context;
import android.util.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//Source: https://stackoverflow.com/questions/13630844/read-write-to-external-xml-file-in-android
// https://stackoverflow.com/questions/32634240/how-to-append-new-nodes-to-an-existing-xml-file

public class InputOutputXml {
    private ArrayList<Transaction> transactionArrayList;
    String fileName;

    public InputOutputXml() {
        transactionArrayList = new ArrayList<Transaction>();
        fileName = "transactions.xml";
    }

    //Creates xml file and writes transaction or appends it, if file doesn't exist.
    public void writeTransaction(Context context, Transaction addTrans) {
        String fPath = context.getFilesDir().getPath() + "/" + fileName;
        File f = new File(fPath);

        XmlSerializer serializer = Xml.newSerializer();
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        StringWriter writer = new StringWriter();
        String result;

        //IF XML file already exists, appends the file
        if (f.exists() && !f.isDirectory()) {
            System.out.println("APPENDING TRANSACTIONS FILE");

            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(f);

                //Get the root element from xml
                Element nList = doc.getDocumentElement();

                //Root item
                Element newItem = doc.createElement("item");

                //action to display what happened
                Element action = doc.createElement("action");
                action.appendChild(doc.createTextNode(addTrans.getAction()));
                newItem.appendChild(action);

                //date
                Element date = doc.createElement("date");
                date.appendChild(doc.createTextNode(addTrans.getDate()));
                newItem.appendChild(date);
                //amount
                Element amount = doc.createElement("amount");
                amount.appendChild(doc.createTextNode(addTrans.getAmount()));
                newItem.appendChild(amount);
                //amount
                Element balance = doc.createElement("balance");
                balance.appendChild(doc.createTextNode(addTrans.getBalance()));
                newItem.appendChild(balance);
                //amount
                Element account_number = doc.createElement("acc_number");
                account_number.appendChild(doc.createTextNode(addTrans.getAccNmbr()));
                newItem.appendChild(account_number);

                //append item to nList
                nList.appendChild(newItem);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                //Initialize streamresult
                StreamResult streamResult = new StreamResult(new File(fPath));
                DOMSource source = new DOMSource(doc);
                transformer.transform(source, streamResult);

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } finally {
                System.out.println("#################### APPEND SUCCESSFUL ####################");
            }

            //CREATE THE FILE
        } else {

            try {
                System.out.println("CREATING A NEW XML FILE");

                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);

                //Mainelement
                serializer.startTag(null, "transactions");

                serializer.startTag(null, "item");

                serializer.startTag(null, "action");
                serializer.text(addTrans.getAction());
                serializer.endTag(null, "action");

                serializer.startTag(null, "date");
                serializer.text(addTrans.getDate());
                serializer.endTag(null, "date");

                serializer.startTag(null, "amount");
                serializer.text(addTrans.getAmount());
                serializer.endTag(null, "amount");

                serializer.startTag(null, "balance");
                serializer.text(addTrans.getBalance());
                serializer.endTag(null, "balance");

                serializer.startTag(null, "acc_number");
                serializer.text(addTrans.getAccNmbr());
                serializer.endTag(null, "acc_number");

                serializer.endTag(null, "item");

                serializer.endTag(null, "transactions");
                serializer.endDocument();
                serializer.flush();

                result = writer.toString();
                fos.write(result.getBytes());
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("#################### DONE ####################");
            }
        }
    }

    //This reads the transaction xml file, filters it by the account number and returns an arrayList, which contains transactions of that account number
    public ArrayList<Transaction> readTransactionXml(Context context, String accNumber) {
        try {
            transactionArrayList.clear();
            InputStream ins = context.openFileInput(fileName);

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document transXml = documentBuilder.parse(ins);

            NodeList nlist = transXml.getDocumentElement().getElementsByTagName("item");

            for (int i = 0; i < nlist.getLength(); i++) {
                Node node = nlist.item(i);

                if (node.getNodeType() == node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String action = element.getElementsByTagName("action").item(0).getTextContent();
                    String date = element.getElementsByTagName("date").item(0).getTextContent();
                    String amount = element.getElementsByTagName("amount").item(0).getTextContent();
                    String balance = element.getElementsByTagName("balance").item(0).getTextContent();
                    String accNmbr = element.getElementsByTagName("acc_number").item(0).getTextContent();

                    //If the account number matches, creates a transaction object and stores it into an arraylist
                    if (accNmbr.equals(accNumber)) {
                        Transaction transaction = new Transaction(action, date, amount, balance, accNmbr);
                        transactionArrayList.add(transaction);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            System.out.println("#################### DONE ####################");
        }
        return transactionArrayList;
    }
}
