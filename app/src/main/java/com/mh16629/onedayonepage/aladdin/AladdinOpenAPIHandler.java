package com.mh16629.onedayonepage.aladdin;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AladdinOpenAPIHandler extends DefaultHandler {

    private static final String TAG = "AladdinOpenAPIHandler";

    public ArrayList<AladdinBookSearchItem> Items;
    private AladdinBookSearchItem currentItem;
    private boolean inItemElement = false;
    private String tempValue;

    private static final String authorExceptTag = "지음";
    private static final String descriptionExceptTagStart = "<img";
    private static final String descriptionExceptTagEnd = "<br/>";

    //FIXME: 날짜 포맷 변경
//    private static final SimpleDateFormat pubdateParseFormat = new SimpleDateFormat("E, dd MMM ");
//    private static final SimpleDateFormat pubdateTransFormat = new SimpleDateFormat("");

    public AladdinOpenAPIHandler() {
        Items = new ArrayList<>();
    }

    public void startElement(String namespace, String localName, String qName, Attributes attributes) {
        if (localName.equals("item")) {
            currentItem = new AladdinBookSearchItem();

            //parse itemId
            int length = attributes.getLength();
            for (int i=0; i<length; i++) {
                currentItem.setItemId(attributes.getValue(i));
            }

            inItemElement = true;
        } else if (localName.equals("title")) {
            tempValue = "";
        } else if (localName.equals("link")) {
            tempValue = "";
        } else if (localName.equals("author")) {
            tempValue = "";
        }else if (localName.equals("pubDate")) {
            tempValue = "";
        }else if (localName.equals("description")) {
            tempValue = "";
        }else if (localName.equals("cover")) {
            tempValue = "";
        }else if (localName.equals("publisher")) {
            tempValue = "";
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException{
        tempValue = tempValue + new String(ch,start,length);
    }

    public void endElement(String namespaceURI, String localName, String qName) {
        if (inItemElement) {
            if (localName.equals("item")) {
                Items.add(currentItem);
                currentItem = null;
                inItemElement = false;

            } else if (localName.equals("title")) {
                currentItem.setTitle(tempValue);
            } else if (localName.equals("link")) {
                currentItem.setLink(tempValue);
            } else if (localName.equals("author")) {
                //MEMO: ~~"지음" 이라는 문자열 삭제할까? 말까?
                if (tempValue.contains(authorExceptTag)) {
                    String subAuthor = null;
                    subAuthor = tempValue.substring(tempValue.lastIndexOf(authorExceptTag) + 2);
                    currentItem.setAuthor(subAuthor);
                } else {
                    currentItem.setAuthor(tempValue);
                }
            } else if (localName.equals("pubDate")) {
                //FIXME: 날짜 포맷 변경
                currentItem.setPubDate(tempValue);
            } else if (localName.equals("description")) {
                if (tempValue.contains(descriptionExceptTagStart)) {
                    String subDescription = null;
                    subDescription = tempValue.substring(tempValue.lastIndexOf(descriptionExceptTagEnd) + 6);
                    currentItem.setDescription(subDescription);
                } else {
                    currentItem.setDescription(tempValue);
                }
            } else if (localName.equals("cover")) {
                currentItem.setImgUrlStr(tempValue);
            } else if (localName.equals("publisher")) {
                currentItem.setPublisher(tempValue);
            }
        }
    }

    public static void parseXml(String xmlUrl) throws Exception {
        ArrayList<AladdinBookSearchItem> list = new ArrayList<AladdinBookSearchItem>();
        new AladdinBookSearchTask().execute(xmlUrl);
    }

    public ArrayList<AladdinBookSearchItem> getResult(){ return (ArrayList<AladdinBookSearchItem>) this.Items; }

}

