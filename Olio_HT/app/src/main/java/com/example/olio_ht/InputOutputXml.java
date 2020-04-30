package com.example.olio_ht;

import android.content.Context;
import android.os.Environment;
import android.renderscript.ScriptGroup;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//Source: https://stackoverflow.com/questions/13630844/read-write-to-external-xml-file-in-android
// https://stackoverflow.com/questions/32634240/how-to-append-new-nodes-to-an-existing-xml-file

public class InputOutputXml {
    private ArrayList<Transaction> transactionArrayList;
    Transaction newTrans;
    String fileName;

    public InputOutputXml() {
        transactionArrayList = new ArrayList<Transaction>();
        fileName = "transactions.xml";

    }


    public void writeTransaction(Context context, Transaction addTrans) {
        String fPath = context.getFilesDir().getPath() + "/" + fileName;
        File f = new File(fPath);

        XmlSerializer serializer = Xml.newSerializer();
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        StringWriter writer = new StringWriter();
        String result;

        //IF XML file already exists, appends the file
        if (f.exists() && !f.isDirectory()){
            System.out.println("APPENDING TRANSACTIONS FILE");

            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(f);

                //Get the root element from xml
                Element nList = doc.getDocumentElement();

                //Root item
                Element newItem = doc.createElement("item");

                //from
                Element from = doc.createElement("from");
                from.appendChild(doc.createTextNode(addTrans.getFrom()));
                newItem.appendChild(from);
                //to
                Element to = doc.createElement("to");
                to.appendChild(doc.createTextNode(addTrans.getTo()));
                newItem.appendChild(to);
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
                Element account_number = doc.createElement("account_number");
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
            } catch (TransformerException e) {
                e.printStackTrace();
            } finally {
                System.out.println("#################### APPEND SUCCESSFUL ####################");
            }
            //CREATE THE FILE
        }else{

            try {
                System.out.println("CREATING A NEW XML FILE");

                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);

                //Mainelement
                serializer.startTag(null, "transactions");

                serializer.startTag(null, "item");

                serializer.startTag(null, "from");
                serializer.text(addTrans.getFrom());
                serializer.endTag(null, "from");

                serializer.startTag(null, "to");
                serializer.text(addTrans.getTo());
                serializer.endTag(null, "to");

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

    public void readTransactionXml(Context context) {
        try {
            InputStream ins = context.openFileInput(fileName);

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document transXml = documentBuilder.parse(ins);

            NodeList nlist = transXml.getDocumentElement().getElementsByTagName("item");

            for (int i = 0; i < nlist.getLength(); i++) {
                Node node = nlist.item(i);

                if (node.getNodeType() == node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String from = element.getElementsByTagName("from").item(0).getTextContent();
                    String to = element.getElementsByTagName("to").item(0).getTextContent();
                    String date = element.getElementsByTagName("date").item(0).getTextContent();
                    String amount = element.getElementsByTagName("amount").item(0).getTextContent();
                    String balance = element.getElementsByTagName("balance").item(0).getTextContent();
                    String accNmbr = element.getElementsByTagName("acc_number").item(0).getTextContent();

                    //Finally create an object from these elements and add it to the arrayList
                    Transaction transaction = new Transaction(from, to, date, amount, balance, accNmbr);
                    transactionArrayList.add(transaction);
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
    }

}
