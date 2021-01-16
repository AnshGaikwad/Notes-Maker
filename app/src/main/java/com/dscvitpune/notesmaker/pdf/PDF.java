package com.dscvitpune.notesmaker.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


//Use this class to create a PDF object.
//Use addPage() command to add pages and addParagraph(text) command to add a paragraph to the document.
//One page is generated by default
//Use makeDocument(path) to create the document and save it to memory
public class PDF {
    Document document;
    int i = 0;
    int page = 0;
    //Array to save Actions
    String[][] data = new String[100][2];

    //Add a paragraph
    public void addParagraph(String text) {
        data[i][0] = "Para";
        data[i][1] = text;
        i++;
    }

    //Add a page
    public void addPage() {
        data[i][0] = "page";
        data[i][1] = String.valueOf(page);
        i++;
        page++;
    }

    //Code to make the document
    public File makeDocument(File file) {

        document = new Document();
        File dir;

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        try {
            PdfWriter.getInstance(document, fOut);
        } catch (DocumentException documentException) {
            documentException.printStackTrace();
        }

        //open the document
        document.open();
        document.newPage();
        page = 1;

        //Create a Font Object
        Font paraFont = new Font();
        paraFont.setFamily(String.valueOf(Font.FontFamily.TIMES_ROMAN));

        //Adding data from array to document
        for (int j = 0; j < data.length; j++) {
            if (data[j][0] != null) {
                switch (data[j][0]) {
                    case "Para": {
                        Paragraph paragraph = new Paragraph(data[j][1], paraFont);
                        try {
                            document.add(paragraph);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                    case "page": {
                        document.newPage();
                        break;
                    }
                }
            }
        }
        document.close();
        return file;
    }
}
