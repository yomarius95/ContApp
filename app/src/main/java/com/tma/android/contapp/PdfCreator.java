package com.tma.android.contapp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tma.android.contapp.data.Nir;
import com.tma.android.contapp.data.Produs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tma.android.contapp.data.Produs.COTA_TVA_0;
import static com.tma.android.contapp.data.Produs.COTA_TVA_19;
import static com.tma.android.contapp.data.Produs.COTA_TVA_20;
import static com.tma.android.contapp.data.Produs.COTA_TVA_24;
import static com.tma.android.contapp.data.Produs.COTA_TVA_5;
import static com.tma.android.contapp.data.Produs.COTA_TVA_9;
import static com.tma.android.contapp.data.Produs.UNITATE_MASURA_BUC;
import static com.tma.android.contapp.data.Produs.UNITATE_MASURA_KG;
import static com.tma.android.contapp.data.Produs.UNITTE_MASURA_M;

public class PdfCreator {

    private Nir mNir;
    private Context mContext;
    private String mUnitateMasura;
    private String mCategorieTVA;
    private double mTotalIntrare;
    private double mTotalIesire;

    public PdfCreator (Context context, Nir nir) {
        mNir = nir;
        mContext = context;
    }

    public void makePdf() throws IOException, DocumentException {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("MainActivity.java", "Pdf Directory created");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(date);

        File myFile = new File(pdfFolder, mNir.getNumar() + "_" + mNir.getNumeFurnizor() + "_" + timeStamp + ".pdf");
        OutputStream output = new FileOutputStream(myFile);

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, output);
        document.open();
        addNirInformation(document);
        PdfPTable table = new PdfPTable(44);
        table.setTotalWidth(document.getPageSize().getWidth() - 80);
        table.setLockedWidth(true);
        addTableHeaders(table);
        for(Produs p : mNir.getListaProduse()) {
            int i = 1;
            mTotalIntrare += p.getValoareTotalIntrare();
            mTotalIesire += p.getValoareTotalIesire();
            setUnitateMasuraAndCategorieTVA(p);
            PdfPCell nrCrt = new PdfPCell(new Phrase(String.valueOf(i)));
            nrCrt.setColspan(2);
            PdfPCell nume = new PdfPCell(new Phrase(p.getNume()));
            nume.setColspan(10);
            PdfPCell unitateMasura = new PdfPCell(new Phrase(mUnitateMasura));
            unitateMasura.setColspan(2);
            PdfPCell cantitate = new PdfPCell(new Phrase(Double.toString(p.getCantitate())));
            cantitate.setColspan(4);
            PdfPCell pretIntrare = new PdfPCell(new Phrase(Double.toString(p.getPretIntrare())));
            pretIntrare.setColspan(4);
            PdfPCell valoareIntrare = new PdfPCell(new Phrase(Double.toString(p.getValoareTotalIntrare())));
            valoareIntrare.setColspan(4);
            PdfPCell valoareTva = new PdfPCell(new Phrase(Double.toString(p.getValoareTotalTVA())));
            valoareTva.setColspan(4);
            PdfPCell catTva = new PdfPCell(new Phrase(mCategorieTVA));
            catTva.setColspan(2);
            PdfPCell pretIesire = new PdfPCell(new Phrase(Double.toString(p.getPretIesire())));
            pretIesire.setColspan(4);
            PdfPCell valoareIesire = new PdfPCell(new Phrase(Double.toString(p.getValoareTotalIesire())));
            valoareIesire.setColspan(4);
            PdfPCell procentAdaos = new PdfPCell(new Phrase(Double.toString(p.getProcentAdaos())));
            procentAdaos.setColspan(4);
            table.addCell(nrCrt);
            table.addCell(nume);
            table.addCell(unitateMasura);
            table.addCell(cantitate);
            table.addCell(pretIntrare);
            table.addCell(valoareIntrare);
            table.addCell(valoareTva);
            table.addCell(catTva);
            table.addCell(pretIesire);
            table.addCell(valoareIesire);
            table.addCell(procentAdaos);
            i++;
        }
        addTotalFinal(table);
        document.add(table);
        document.close();
    }

    private void addTotalFinal(PdfPTable table) {
        PdfPCell nrCrt = new PdfPCell(new Phrase());
        nrCrt.setColspan(2);
        PdfPCell nume = new PdfPCell(new Phrase());
        nume.setColspan(10);
        PdfPCell unitateMasura = new PdfPCell(new Phrase());
        unitateMasura.setColspan(2);
        PdfPCell cantitate = new PdfPCell(new Phrase());
        cantitate.setColspan(4);
        PdfPCell pretIntrare = new PdfPCell(new Phrase("Total intrare"));
        pretIntrare.setColspan(4);
        PdfPCell valoareIntrare = new PdfPCell(new Phrase(String.valueOf(mTotalIntrare)));
        valoareIntrare.setColspan(4);
        PdfPCell valoareTva = new PdfPCell(new Phrase());
        valoareTva.setColspan(4);
        PdfPCell catTva = new PdfPCell(new Phrase());
        catTva.setColspan(2);
        PdfPCell pretIesire = new PdfPCell(new Phrase("Total iesire"));
        pretIesire.setColspan(4);
        PdfPCell valoareIesire = new PdfPCell(new Phrase(String.valueOf(mTotalIesire)));
        valoareIesire.setColspan(4);
        PdfPCell procentAdaos = new PdfPCell(new Phrase());
        procentAdaos.setColspan(4);
        table.addCell(nrCrt);
        table.addCell(nume);
        table.addCell(unitateMasura);
        table.addCell(cantitate);
        table.addCell(pretIntrare);
        table.addCell(valoareIntrare);
        table.addCell(valoareTva);
        table.addCell(catTva);
        table.addCell(pretIesire);
        table.addCell(valoareIesire);
        table.addCell(procentAdaos);
    }

    private void addNirInformation(Document document) throws DocumentException {
        Phrase nrNir = new Phrase("Nr. Nir: " + mNir.getNumar());
        Phrase dataNir = new Phrase("Data Nir: " + mNir.getData());
        Phrase furnizor = new Phrase("Furnizor: " + mNir.getNumeFurnizor());
        Phrase localitate = new Phrase("Localitate: " + mNir.getLocalitateFurnizor());
        Phrase serieNrFactura = new Phrase("Serie Act: " + mNir.getSerieAct() + " Nr. Act: " + mNir.getNumarAct());
        Phrase dataFactura = new Phrase("Data Act: " + mNir.getDataAct());

        document.add(nrNir);
        document.add(Chunk.TABBING);
        document.add(dataNir);
        document.add(Chunk.NEWLINE);
        document.add(furnizor);
        document.add(Chunk.TABBING);
        document.add(localitate);
        document.add(Chunk.NEWLINE);
        document.add(serieNrFactura);
        document.add(Chunk.TABBING);
        document.add(dataFactura);
        document.add(Chunk.NEWLINE);
    }

    private void addTableHeaders(PdfPTable table) {
        PdfPCell nrCrt = new PdfPCell(new Phrase("Nr. Crt."));
        nrCrt.setColspan(2);
        PdfPCell nume = new PdfPCell(new Phrase("Nume produs"));
        nume.setColspan(10);
        PdfPCell unitateMasura = new PdfPCell(new Phrase("UM"));
        unitateMasura.setColspan(2);
        PdfPCell cantitate = new PdfPCell(new Phrase("Cantitate"));
        cantitate.setColspan(4);
        PdfPCell pretIntrare = new PdfPCell(new Phrase("Pret intrare"));
        pretIntrare.setColspan(4);
        PdfPCell valoareIntrare = new PdfPCell(new Phrase("Total intrare"));
        valoareIntrare.setColspan(4);
        PdfPCell valoareTva = new PdfPCell(new Phrase("Total TVA"));
        valoareTva.setColspan(4);
        PdfPCell catTva = new PdfPCell(new Phrase("Cat. TVA"));
        catTva.setColspan(2);
        PdfPCell pretIesire = new PdfPCell(new Phrase("Pret iesire"));
        pretIesire.setColspan(4);
        PdfPCell valoareIesire = new PdfPCell(new Phrase("Total iesire"));
        valoareIesire.setColspan(4);
        PdfPCell procentAdaos = new PdfPCell(new Phrase("Procent Adaos"));
        procentAdaos.setColspan(4);
        table.addCell(nrCrt);
        table.addCell(nume);
        table.addCell(unitateMasura);
        table.addCell(cantitate);
        table.addCell(pretIntrare);
        table.addCell(valoareIntrare);
        table.addCell(valoareTva);
        table.addCell(catTva);
        table.addCell(pretIesire);
        table.addCell(valoareIesire);
        table.addCell(procentAdaos);
    }

    private void setUnitateMasuraAndCategorieTVA(Produs produs) {
        switch (produs.getUnitateMasura()) {
            case UNITATE_MASURA_BUC:
                mUnitateMasura = mContext.getString(R.string.unitate_masura_buc);
                break;
            case UNITATE_MASURA_KG:
                mUnitateMasura = mContext.getString(R.string.unitate_masura_kg);
                break;
            case UNITTE_MASURA_M:
                mUnitateMasura = mContext.getString(R.string.unitate_masura_metru);
                break;
        }

        switch (produs.getCategorieTVA()) {
            case COTA_TVA_0:
                mCategorieTVA = mContext.getString(R.string.categorie_tva_0);
                break;
            case COTA_TVA_5:
                mCategorieTVA = mContext.getString(R.string.categorie_tva_5);
                break;
            case COTA_TVA_9:
                mCategorieTVA = mContext.getString(R.string.categorie_tva_9);
                break;
            case COTA_TVA_19:
                mCategorieTVA = mContext.getString(R.string.categorie_tva_19);
                break;
            case COTA_TVA_20:
                mCategorieTVA = mContext.getString(R.string.categorie_tva_20);
                break;
            case COTA_TVA_24:
                mCategorieTVA = mContext.getString(R.string.categorie_tva_24);
                break;
        }
    }
}
