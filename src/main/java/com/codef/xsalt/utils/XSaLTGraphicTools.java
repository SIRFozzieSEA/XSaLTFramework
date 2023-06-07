package com.codef.xsalt.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BarcodeInter25;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import multivalent.Behavior;
import multivalent.ParseException;
import multivalent.std.adaptor.pdf.PDF;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTGraphicTools {

	public PdfWriter ioPDFWriter = null;
	public Document ioPdfDocument = null;
	private PdfContentByte ioPDFContentByte = null;
	public Image ioBackgroundImage = null;

	public float ifMaxPageWidth = 0;
	public float ifMaxPageHeight = 0;

	public static final int XN_OBRIEN_BARCODE_XPOS = 589;
	public static final int XN_OBRIEN_BARCODE_YPOS = 620;

	// public BaseFont ioBaseFontDingbats = null;

	// public Font ioFontDingbatsRegular_6 = null;
	// public Font ioFontDingbatsBold_6 = null;
	// public Font ioFontDingbatsBoldItalic_6 = null;
	// public Font ioFontDingbatsItalic_6 = null;

	public BaseFont ioBaseFontTimesRegular = null;
	public BaseFont ioBaseFontTimesBold = null;
	public BaseFont ioBaseFontTimesBoldItalic = null;
	public BaseFont ioBaseFontTimesItalic = null;

	public BaseFont ioBaseFontHelveticaRegular = null;
	public BaseFont ioBaseFontHelveticaBold = null;
	public BaseFont ioBaseFontHelveticaBoldItalic = null;
	public BaseFont ioBaseFontHelveticaItalic = null;

	public Font ioFontHelveticaRegular_6 = null;
	public Font ioFontHelveticaBold_6 = null;
	public Font ioFontHelveticaBoldItalic_6 = null;
	public Font ioFontHelveticaItalic_6 = null;

	public Font ioFontHelveticaRegular_8 = null;
	public Font ioFontHelveticaBold_8 = null;
	public Font ioFontHelveticaBoldItalic_8 = null;
	public Font ioFontHelveticaItalic_8 = null;

	public Font ioFontHelveticaRegular_9 = null;
	public Font ioFontHelveticaBold_9 = null;
	public Font ioFontHelveticaBoldItalic_9 = null;
	public Font ioFontHelveticaItalic_9 = null;

	public Font ioFontHelveticaRegular_10 = null;
	public Font ioFontHelveticaBold_10 = null;
	public Font ioFontHelveticaBoldItalic_10 = null;
	public Font ioFontHelveticaItalic_10 = null;

	public Font ioFontHelveticaRegular_11 = null;
	public Font ioFontHelveticaBold_11 = null;
	public Font ioFontHelveticaBoldItalic_11 = null;
	public Font ioFontHelveticaItalic_11 = null;

	public Font ioFontHelveticaRegular_12 = null;
	public Font ioFontHelveticaBold_12 = null;
	public Font ioFontHelveticaBoldItalic_12 = null;
	public Font ioFontHelveticaItalic_12 = null;

	public Font ioFontHelveticaRegular_14 = null;
	public Font ioFontHelveticaBold_14 = null;
	public Font ioFontHelveticaBoldItalic_14 = null;
	public Font ioFontHelveticaItalic_14 = null;

	public Font ioFontHelveticaRegular_18 = null;
	public Font ioFontHelveticaBold_18 = null;
	public Font ioFontHelveticaBoldItalic_18 = null;
	public Font ioFontHelveticaItalic_18 = null;

	public Font ioFontHelveticaRegular_24 = null;
	public Font ioFontHelveticaBold_24 = null;
	public Font ioFontHelveticaBoldItalic_24 = null;
	public Font ioFontHelveticaItalic_24 = null;

	public Font ioFontHelveticaRegular_60 = null;
	public Font ioFontHelveticaBold_60 = null;
	public Font ioFontHelveticaBoldItalic_60 = null;
	public Font ioFontHelveticaItalic_60 = null;

	private HashMap<Integer, String> ioOBrienInserterFirstDigitMap = null;

	private static final Logger LOGGER = LogManager.getLogger(XSaLTGraphicTools.class.getName());

	/**
	 * Generic constructor.
	 */
	public XSaLTGraphicTools() {

	}
	
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	    graphics2D.dispose();
	    return resizedImage;
	}
	
	

	/**
	 * This method reads a PDF file and converts the file and writes it into a JPEG
	 * format.
	 * 
	 * @param _sPDFFilePath      Path for PDF file
	 * @param _sImageFilePathJPG Path to write JPEG file
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 */
	public static void convertPDFtoJPEG(String _sPDFFilePath, String _sImageFilePathJPG)
			throws IOException, DocumentException, ParseException {

		File oInputFile = new File(_sPDFFilePath);
		File oOutFile = new File(_sImageFilePathJPG);

		PDF oPDFInputFile = (PDF) Behavior.getInstance("AdobePDF", "AdobePDF", null, null, null);

		oPDFInputFile.setInput(oInputFile);

		multivalent.Document oDocument = new multivalent.Document("doc", null, null);
		oPDFInputFile.parse(oDocument);
		oDocument.clear();

		oDocument.putAttr(multivalent.Document.ATTR_PAGE, Integer.toString(1));
		oPDFInputFile.parse(oDocument);

		multivalent.Node oTopNode = oDocument.childAt(0);

		oDocument.formatBeforeAfter(612, 792, null);

		int w = oTopNode.bbox.width;
		int h = oTopNode.bbox.height;

		BufferedImage oBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D oGraphics2D = oBufferedImage.createGraphics();
		oGraphics2D.setClip(0, 0, w, h);

		oGraphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		oGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		oGraphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		multivalent.Context oContext = oDocument.getStyleSheet().getContext(oGraphics2D, null);
		oTopNode.paintBeforeAfter(oGraphics2D.getClipBounds(), oContext);

		ImageIO.write(oBufferedImage, "jpeg", oOutFile);

		oDocument.removeAllChildren();
		oContext.reset();
		oGraphics2D.dispose();

		oPDFInputFile.getReader().close();
		oOutFile = null;
		oDocument = null;

	}

	/**
	 * This method grabs a resource file and returns an Image
	 * 
	 * @param _oResourcePath Source path
	 * @return Image
	 * @throws Exception
	 */
	public static java.awt.Image loadImage(String _oResourcePath) {
		ClassLoader oloader = ClassLoader.getSystemClassLoader();
		try {
			Toolkit oToolkik = Toolkit.getDefaultToolkit();
			return oToolkik.getImage(oloader.getResource(_oResourcePath));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return null;
		}
	}

	/**
	 * This method grabs a resource file and returns an Icon
	 * 
	 * @param _oResourcePath Source path
	 * @return Icon
	 * @throws Exception
	 */
	public static ImageIcon loadIcon(String _oResourcePath) {
		ClassLoader oloader = ClassLoader.getSystemClassLoader();
		try {
			return new ImageIcon(oloader.getResource(_oResourcePath));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return null;
		}
	}

	// String sFieldToChange = "";
	//
	// try
	// {
	// PdfReader oReader = new PdfReader("C:/000005433_Des_Plaines_eDoc_3600.pdf");
	// AcroFields oForm = oReader.getAcroFields();
	// HashMap<String, Item> oFields = oForm.getFields();
	// for (Iterator<String> i = oFields.keySet().iterator(); i.hasNext();)
	// {
	// String sKey = i.next();
	// if (sKey.startsWith("psMailDate"))
	// {
	// sysout(sKey + " -- " + oForm.getField(sKey));
	// sFieldToChange = sKey;
	// }
	//
	// }
	//
	// PdfStamper oMyStamper = new PdfStamper(oReader, new
	// FileOutputStream("C:/Modified.pdf"));
	// oForm = oMyStamper.getAcroFields();
	// oForm.setField(sFieldToChange, "TODAY");
	// oMyStamper.setFormFlattening(false);
	// oMyStamper.close();
	//
	// }
	// catch (Exception e)
	// {
	// LOGGER.error(e.toString(), e);
	// }

	// public XSaLTPDFTools(String _sPDFFilePath, Rectangle _oPageSize) throws
	// DocumentException, IOException
	// {
	// ioPdfDocument = new Document(_oPageSize);
	// ioPDFWriter = PdfWriter.getInstance(ioPdfDocument, new
	// FileOutputStream(_sPDFFilePath));
	// ioPdfDocument.open();
	// ioPdfDocument.setMargins(0f, 0f, 0f, 0f);
	//
	// ifMaxPageWidth = _oPageSize.getWidth();
	// ifMaxPageHeight = _oPageSize.getHeight();
	//
	// ioPDFContentByte = ioPDFWriter.getDirectContent();
	//
	// setStandardBaseFonts();
	//
	// }

	/*
	 * This was custom modified in the iText classes
	 * 
	 * public void showTextAligned(int alignment, String text, float x, float y,
	 * float rotation) {
	 * 
	 * if (text.indexOf("$") != -1 && alignment == ALIGN_RIGHT &&
	 * text.endsWith(" CR")) { showTextAligned(alignment, text.substring(0,
	 * text.length() - 3), x, y, rotation, false); showTextAligned(ALIGN_LEFT, "CR",
	 * x + 4, y, rotation, false); } else { showTextAligned(alignment, text, x, y,
	 * rotation, false); }
	 * 
	 * }
	 * 
	 * try { Rectangle oPageSize = PageSize.LETTER; XSaLTPDFTools ioPDFTools = new
	 * XSaLTPDFTools("C:/bobo.pdf", oPageSize); ioPDFTools.ioPdfDocument.newPage();
	 * String sTextCopy =
	 * "<table><tr><td>This is Liz's customized message center that shows <B>bold</B>, <I>italic</I>, <U>underline</U> and <B><I>bold italic</I></B>.</td></tr></tbody></table>"
	 * ; ioPDFTools.generateFormattedMessageCenter(0, 0, oPageSize.getWidth(),
	 * oPageSize.getHeight(), sTextCopy, false, false, true);
	 * ioPDFTools.ioPdfDocument.close();
	 * 
	 * } catch (Exception e) { LOGGER.error(e.toString(), e); }
	 * 
	 * 
	 * 
	 * try { Rectangle oPageSize = PageSize.LEGAL; XSaLTPDFTools ioPDFTools = new
	 * XSaLTPDFTools("C:/TestSize.pdf", oPageSize);
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LETTER);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LETTER sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LEGAL);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LEGAL sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LETTER);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LETTER sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LETTER);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LETTER sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LETTER);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LETTER sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LEGAL);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LEGAL sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LETTER);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LETTER sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LEGAL);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LEGAL sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LETTER);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LETTER sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LEGAL);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LEGAL sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.setPageSize(PageSize.LETTER);
	 * ioPDFTools.ioPdfDocument.newPage();
	 * ioPDFTools.getPDFContentByte().beginText();
	 * ioPDFTools.getPDFContentByte().setFontAndSize(ioPDFTools.
	 * ioBaseFontHelveticaBold, 10);
	 * ioPDFTools.getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER,
	 * "This should print on LETTER sized paper.",
	 * ioPDFTools.ioPdfDocument.getPageSize().getWidth() / 2,
	 * ioPDFTools.ioPdfDocument.getPageSize().getHeight() / 2, 0);
	 * ioPDFTools.getPDFContentByte().endText();
	 * 
	 * ioPDFTools.ioPdfDocument.close();
	 * 
	 * } catch (Exception e) { LOGGER.error(e.toString(), e); }
	 * 
	 * 
	 */

	/**
	 * Main constructor for this method.
	 * 
	 * @param _sPDFFilePath Path to create PDF file
	 * @param _oPageSize    Rectangle object to determine page size
	 * @throws DocumentException
	 * @throws IOException
	 */
	public XSaLTGraphicTools(String _sPDFFilePath, Rectangle _oPageSize) throws DocumentException, IOException {
		ioPdfDocument = new Document(_oPageSize);
		ioPDFWriter = PdfWriter.getInstance(ioPdfDocument, new FileOutputStream(_sPDFFilePath));
		ioPdfDocument.open();
		ioPdfDocument.setMargins(0f, 0f, 0f, 0f);

		ifMaxPageWidth = _oPageSize.getWidth();
		ifMaxPageHeight = _oPageSize.getHeight();

		ioPDFContentByte = ioPDFWriter.getDirectContent();

		setStandardBaseFonts();
		loadOBrienInserterMap();
	}

	/**
	 * @return Contents of the created PDF
	 */
	public PdfContentByte getPDFContentByte() {
		return ioPDFContentByte;
	}

	/**
	 * This method initializes the base fonts to a previously declared standard.
	 * 
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void setStandardBaseFonts() throws DocumentException, IOException {

		ioBaseFontHelveticaRegular = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		ioBaseFontHelveticaBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		ioBaseFontHelveticaBoldItalic = BaseFont.createFont(BaseFont.HELVETICA_BOLDOBLIQUE, BaseFont.WINANSI,
				BaseFont.NOT_EMBEDDED);
		ioBaseFontHelveticaItalic = BaseFont.createFont(BaseFont.HELVETICA_OBLIQUE, BaseFont.WINANSI,
				BaseFont.NOT_EMBEDDED);

		ioBaseFontTimesRegular = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		ioBaseFontTimesBold = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		ioBaseFontTimesBoldItalic = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC, BaseFont.WINANSI,
				BaseFont.NOT_EMBEDDED);
		ioBaseFontTimesItalic = BaseFont.createFont(BaseFont.TIMES_ITALIC, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		// ioBaseFontDingbats = BaseFont.createFont(BaseFont.ZAPFDINGBATS,
		// BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		ioFontHelveticaRegular_6 = new Font(Font.getFamily("Helvetica"), 6, Font.NORMAL);
		ioFontHelveticaBold_6 = new Font(Font.getFamily("Helvetica"), 6, Font.BOLD);
		ioFontHelveticaBoldItalic_6 = new Font(Font.getFamily("Helvetica"), 6, Font.BOLDITALIC);
		ioFontHelveticaItalic_6 = new Font(Font.getFamily("Helvetica"), 6, Font.ITALIC);

		ioFontHelveticaRegular_8 = new Font(Font.getFamily("Helvetica"), 8, Font.NORMAL);
		ioFontHelveticaBold_8 = new Font(Font.getFamily("Helvetica"), 8, Font.BOLD);
		ioFontHelveticaBoldItalic_8 = new Font(Font.getFamily("Helvetica"), 8, Font.BOLDITALIC);
		ioFontHelveticaItalic_8 = new Font(Font.getFamily("Helvetica"), 8, Font.ITALIC);

		ioFontHelveticaRegular_9 = new Font(Font.getFamily("Helvetica"), 9, Font.NORMAL);
		ioFontHelveticaBold_9 = new Font(Font.getFamily("Helvetica"), 9, Font.BOLD);
		ioFontHelveticaBoldItalic_9 = new Font(Font.getFamily("Helvetica"), 9, Font.BOLDITALIC);
		ioFontHelveticaItalic_9 = new Font(Font.getFamily("Helvetica"), 9, Font.ITALIC);

		ioFontHelveticaRegular_10 = new Font(Font.getFamily("Helvetica"), 10, Font.NORMAL);
		ioFontHelveticaBold_10 = new Font(Font.getFamily("Helvetica"), 10, Font.BOLD);
		ioFontHelveticaBoldItalic_10 = new Font(Font.getFamily("Helvetica"), 10, Font.BOLDITALIC);
		ioFontHelveticaItalic_10 = new Font(Font.getFamily("Helvetica"), 10, Font.ITALIC);

		ioFontHelveticaRegular_11 = new Font(Font.getFamily("Helvetica"), 11, Font.NORMAL);
		ioFontHelveticaBold_11 = new Font(Font.getFamily("Helvetica"), 11, Font.BOLD);
		ioFontHelveticaBoldItalic_11 = new Font(Font.getFamily("Helvetica"), 11, Font.BOLDITALIC);
		ioFontHelveticaItalic_11 = new Font(Font.getFamily("Helvetica"), 11, Font.ITALIC);

		ioFontHelveticaRegular_12 = new Font(Font.getFamily("Helvetica"), 12, Font.NORMAL);
		ioFontHelveticaBold_12 = new Font(Font.getFamily("Helvetica"), 12, Font.BOLD);
		ioFontHelveticaBoldItalic_12 = new Font(Font.getFamily("Helvetica"), 12, Font.BOLDITALIC);
		ioFontHelveticaItalic_12 = new Font(Font.getFamily("Helvetica"), 12, Font.ITALIC);

		ioFontHelveticaRegular_14 = new Font(Font.getFamily("Helvetica"), 14, Font.NORMAL);
		ioFontHelveticaBold_14 = new Font(Font.getFamily("Helvetica"), 14, Font.BOLD);
		ioFontHelveticaBoldItalic_14 = new Font(Font.getFamily("Helvetica"), 14, Font.BOLDITALIC);
		ioFontHelveticaItalic_14 = new Font(Font.getFamily("Helvetica"), 14, Font.ITALIC);

		ioFontHelveticaRegular_18 = new Font(Font.getFamily("Helvetica"), 18, Font.NORMAL);
		ioFontHelveticaBold_18 = new Font(Font.getFamily("Helvetica"), 18, Font.BOLD);
		ioFontHelveticaBoldItalic_18 = new Font(Font.getFamily("Helvetica"), 18, Font.BOLDITALIC);
		ioFontHelveticaItalic_18 = new Font(Font.getFamily("Helvetica"), 18, Font.ITALIC);

		ioFontHelveticaRegular_24 = new Font(Font.getFamily("Helvetica"), 24, Font.NORMAL);
		ioFontHelveticaBold_24 = new Font(Font.getFamily("Helvetica"), 24, Font.BOLD);
		ioFontHelveticaBoldItalic_24 = new Font(Font.getFamily("Helvetica"), 24, Font.BOLDITALIC);
		ioFontHelveticaItalic_24 = new Font(Font.getFamily("Helvetica"), 24, Font.ITALIC);

		// ioFontDingbatsRegular_6 = new Font(Font.getFamily("Helvetica"), 6,
		// Font.NORMAL);
		// ioFontDingbatsBold_6 = new Font(Font.getFamily("Helvetica"), 6, Font.BOLD);
		// ioFontDingbatsBoldItalic_6 = new Font(Font.getFamily("Helvetica"), 6,
		// Font.BOLDITALIC);
		// ioFontDingbatsItalic_6 = new Font(Font.getFamily("Helvetica"), 6,
		// Font.ITALIC);

		ioFontHelveticaRegular_60 = new Font(Font.getFamily("Helvetica"), 60, Font.NORMAL);
		ioFontHelveticaBold_60 = new Font(Font.getFamily("Helvetica"), 60, Font.BOLD);
		ioFontHelveticaBoldItalic_60 = new Font(Font.getFamily("Helvetica"), 60, Font.BOLDITALIC);
		ioFontHelveticaItalic_60 = new Font(Font.getFamily("Helvetica"), 60, Font.ITALIC);

	}

	// public Chunk getCheckbox()
	// {
	// return new Chunk("o", ioFontDingbatsRegular_6);
	// }

	/**
	 * This method sets the back ground image for the PDF.
	 * 
	 * @param _sBackgroundImageFilePath File path to get image
	 * @param _fShimmyLeftRight         Absolute X-coordinate to anchor image at
	 * @param _fShimmyUpDown            Absolute Y-coordinate to anchor image at
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void setBackgroundImage(String _sBackgroundImageFilePath, float _fShimmyLeftRight, float _fShimmyUpDown)
			throws MalformedURLException, IOException, DocumentException {

		if (!_sBackgroundImageFilePath.endsWith("/")) {

			// All background images must be 144 DPI and 100% size
			ioBackgroundImage = Image.getInstance(_sBackgroundImageFilePath);
			ioBackgroundImage.setAlignment(Image.ALIGN_LEFT | Image.UNDERLYING);
			ioBackgroundImage.setAbsolutePosition(_fShimmyLeftRight, _fShimmyUpDown);
			ioBackgroundImage.scalePercent(50f);

		}

	}

	public void createLeftText(PdfContentByte _oPDFContentByte, String _text, BaseFont _baseFont, float _fontSize,
			float _fPositionX, float _fPositionY) {
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_baseFont, _fontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _text, _fPositionX, _fPositionY, 0);
		_oPDFContentByte.endText();
	}

	public void createCenterText(PdfContentByte _oPDFContentByte, String _text, BaseFont _baseFont, float _fontSize,
			float _fPositionX, float _fPositionY) {
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_baseFont, _fontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_CENTER, _text, _fPositionX, _fPositionY, 0);
		_oPDFContentByte.endText();
	}

	public void createRightText(PdfContentByte _oPDFContentByte, String _text, BaseFont _baseFont, float _fontSize,
			float _fPositionX, float _fPositionY) {
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_baseFont, _fontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _text, _fPositionX, _fPositionY, 0);
		_oPDFContentByte.endText();
	}

	/**
	 * This method aligns a label and value in the PDF (e.g. "NAME: TEST CUSTOMER").
	 * 
	 * @param _oPDFContentByte       PDF object where content will be rendered
	 * @param _sLeftSideDisplayText  Label text
	 * @param _oLeftBaseFont         Font for label text
	 * @param _fLeftFontSize         Font size for label text
	 * @param _sRightSideDisplayText Value text
	 * @param _oRightBaseFont        Font for value text
	 * @param _fRightFontSize        Font size for value text
	 * @param _fPositionX            X-coordinate to render text
	 * @param _fPositionY            Y-coordinate to render text
	 */
	public void createRightLeftColonValue(PdfContentByte _oPDFContentByte, String _sLeftSideDisplayText,
			BaseFont _oLeftBaseFont, float _fLeftFontSize, String _sRightSideDisplayText, BaseFont _oRightBaseFont,
			float _fRightFontSize, float _fPositionX, float _fPositionY) {

		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_oLeftBaseFont, _fLeftFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sLeftSideDisplayText, _fPositionX, _fPositionY,
				0);
		_oPDFContentByte.setFontAndSize(_oRightBaseFont, _fRightFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sRightSideDisplayText,
				_fPositionX + (_fLeftFontSize / 2), _fPositionY, 0);
		_oPDFContentByte.endText();
	}

	/**
	 * This method renders two sets of text on a line, the left text will be aligned
	 * left and the right text will be aligned right.
	 * 
	 * @param _oPDFContentByte       PDF object where content will be rendered
	 * @param _sLeftSideDisplayText  Text for left side
	 * @param _oLeftBaseFont         Font for left side
	 * @param _fLeftFontSize         Font size for left side
	 * @param _sRightSideDisplayText Text for right side
	 * @param _oRightBaseFont        Font for right side
	 * @param _fRightFontSize        Font size for right side
	 * @param _fPositionX1           X-coordinate for left text
	 * @param _fPositionX2           X-coordinate for right text
	 * @param _fPositionY            Y-coordinate for created text set
	 * @throws DocumentException
	 */
	public void createRightLeftValue(PdfContentByte _oPDFContentByte, String _sLeftSideDisplayText,
			BaseFont _oLeftBaseFont, float _fLeftFontSize, String _sRightSideDisplayText, BaseFont _oRightBaseFont,
			float _fRightFontSize, float _fPositionX1, float _fPositionX2, float _fPositionY) throws DocumentException {
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_oLeftBaseFont, _fLeftFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sLeftSideDisplayText, _fPositionX1, _fPositionY,
				0);
		float fLeftPhraseWidth = _oPDFContentByte.getEffectiveStringWidth(_sLeftSideDisplayText, false)
				+ _fLeftFontSize / 2;
		_oPDFContentByte.setFontAndSize(_oRightBaseFont, _fRightFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sRightSideDisplayText, _fPositionX2, _fPositionY,
				0);
		float fRightPhraseWidth = _oPDFContentByte.getEffectiveStringWidth(_sRightSideDisplayText, false)
				+ _fRightFontSize / 2;

		float fbobo = fLeftPhraseWidth + fRightPhraseWidth;
		fbobo = fbobo + fbobo;

		_oPDFContentByte.endText();

	}

	/**
	 * This method writes text to a document with a leader between the left and
	 * right text areas.
	 * 
	 * @param _oPDFContentByte       PDF object where content will be rendered
	 * @param _sLeftSideDisplayText  Text for left side
	 * @param _oLeftBaseFont         Font for left text
	 * @param _fLeftFontSize         Font size for left text
	 * @param _sRightSideDisplayText Text for right side
	 * @param _oRightBaseFont        Font for right text
	 * @param _fRightFontSize        Font size for right text
	 * @param _oLeaderFont           Font for leader
	 * @param _fPositionX1           X-coordinate for left text
	 * @param _fPositionX2           X-coordinate for right text
	 * @param _fPositionY            Y-coordinate for text line
	 * @throws DocumentException
	 */
	public void createRightLeftLeaderValue(PdfContentByte _oPDFContentByte, String _sLeftSideDisplayText,
			BaseFont _oLeftBaseFont, float _fLeftFontSize, String _sRightSideDisplayText, BaseFont _oRightBaseFont,
			float _fRightFontSize, Font _oLeaderFont, float _fPositionX1, float _fPositionX2, float _fPositionY)
			throws DocumentException {

		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_oLeftBaseFont, _fLeftFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sLeftSideDisplayText, _fPositionX1, _fPositionY,
				0);
		float fLeftPhraseWidth = _oPDFContentByte.getEffectiveStringWidth(_sLeftSideDisplayText, false)
				+ _fLeftFontSize / 2;
		_oPDFContentByte.setFontAndSize(_oRightBaseFont, _fRightFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sRightSideDisplayText, _fPositionX2, _fPositionY,
				0);

		float fRightPhraseWidth = 0;

		if (_sRightSideDisplayText.indexOf("$") != -1 && _sRightSideDisplayText.endsWith(" CR")) {
			fRightPhraseWidth = _oPDFContentByte.getEffectiveStringWidth(
					_sRightSideDisplayText.substring(0, _sRightSideDisplayText.length() - 3), false)
					+ _fRightFontSize / 2;
		} else {
			fRightPhraseWidth = _oPDFContentByte.getEffectiveStringWidth(_sRightSideDisplayText, false)
					+ _fRightFontSize / 2;
		}

		_oPDFContentByte.endText();

		ColumnText oColumnText = new ColumnText(_oPDFContentByte);
		Phrase oPhrase = new Phrase();
		oPhrase.setFont(_oLeaderFont);
		oPhrase.add(
				"................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................");
		oColumnText.addText(oPhrase);
		oColumnText.setSimpleColumn(_fPositionX1 + fLeftPhraseWidth, _fPositionY + _oLeaderFont.getCalculatedSize(),
				_fPositionX2 - fRightPhraseWidth, _fPositionY, _oLeaderFont.getSize(), Element.ALIGN_LEFT);
		oColumnText.go();

	}

	/**
	 * This method creates columns in the document with the given information.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _fPositionY      Y-coordinate for columns to be rendered
	 * @param _abfFonts        Array of fonts for text
	 * @param _anFontSizes     Array of font sizes for text
	 * @param _anAlignments    Array of alignment values
	 * @param _asTexts         Array of text to display in columns
	 * @param _afXPositions    Array of x-coordinates for columns
	 */
	public void createColumns(PdfContentByte _oPDFContentByte, float _fPositionY, BaseFont[] _abfFonts,
			int[] _anFontSizes, int[] _anAlignments, String[] _asTexts, float[] _afXPositions) {
		_oPDFContentByte.beginText();

		int nNumber = _asTexts.length;

		for (int i = 0; i < nNumber; i++) {
			if (_asTexts[i] == null) {
				continue;
			}

			_oPDFContentByte.setFontAndSize(_abfFonts[i], _anFontSizes[i]);
			_oPDFContentByte.showTextAligned(_anAlignments[i], _asTexts[i], _afXPositions[i], _fPositionY, 0);
		}

		_oPDFContentByte.endText();
	}

	/**
	 * This method creates and writes columns in a PDF document based on each
	 * columns percentage of the total line width.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _fPositionY      Y-coordinate for columns to be rendered
	 * @param _fLineWidth      Total line width
	 * @param _fLeftPosition   X-position for left of line
	 * @param _aoFonts         Array of fonts for text
	 * @param _anFontSizes     Array of font sizes for text
	 * @param _anAlignments    Array of alignments for text
	 * @param _asTexts         Array of texts
	 * @param _afColumnWidths  Array of percents of line width for each column (in
	 *                         decimal format)
	 */
	public void createColumnsByLineWidthPct(PdfContentByte _oPDFContentByte, float _fPositionY, float _fLineWidth,
			float _fLeftPosition, BaseFont[] _aoFonts, int[] _anFontSizes, int[] _anAlignments, String[] _asTexts,
			float[] _afColumnWidths) {
		_oPDFContentByte.beginText();

		float fCurrLeftEdge = _fLeftPosition;

		for (int i = 0; i < _asTexts.length; i++) {
			if (_asTexts[i] != null) {
				_oPDFContentByte.setFontAndSize(_aoFonts[i], _anFontSizes[i]);

				float fCurrXPos = fCurrLeftEdge;
				if (_anAlignments[i] == PdfContentByte.ALIGN_CENTER) {
					fCurrXPos += (_afColumnWidths[i] * _fLineWidth) / 2;
				} else if (_anAlignments[i] == PdfContentByte.ALIGN_RIGHT) {
					fCurrXPos += (_afColumnWidths[i] * _fLineWidth);
				}

				_oPDFContentByte.showTextAligned(_anAlignments[i], _asTexts[i], fCurrXPos, _fPositionY, 0);
			}
			fCurrLeftEdge += _afColumnWidths[i] * _fLineWidth;
		}

		_oPDFContentByte.endText();
	}

	/**
	 * This method renders columns based on the specified configuration with a
	 * leader for each text value.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _fPositionY      Y-coordinate for columns to be rendered
	 * @param _abfFonts        Array of fonts for text
	 * @param _anFontSizes     Array of font sizes for text
	 * @param _anAlignments    Array of alignment values
	 * @param _asTexts         Array of text to display in columns
	 * @param _afXPositions    Array of x-coordinates for columns
	 * @param _oLeaderFont     Font for leader (same for all columns)
	 * @throws DocumentException
	 */
	public void createColumnsWithLeaders(PdfContentByte _oPDFContentByte, float _fPositionY, BaseFont[] _abfFonts,
			int[] _anFontSizes, int[] _anAlignments, String[] _asTexts, float[] _afXPositions, Font _oLeaderFont)
			throws DocumentException {

		int nNumber = _asTexts.length;

		float fCurrentItemTabPositionRightEdge = 0;
		float fCurrentItemTabPositionLeftEdge = 0;
		float fLastItemTabPositionRightEdge = 0;

		for (int i = 0; i < nNumber; i++) {
			if (_asTexts[i] == null) {
				continue;
			}

			_oPDFContentByte.beginText();
			_oPDFContentByte.setFontAndSize(_abfFonts[i], _anFontSizes[i]);
			_oPDFContentByte.showTextAligned(_anAlignments[i], _asTexts[i], _afXPositions[i], _fPositionY, 0);
			_oPDFContentByte.endText();

			if (_anAlignments[i] == Element.ALIGN_LEFT) {
				fCurrentItemTabPositionRightEdge = _oPDFContentByte.getEffectiveStringWidth(_asTexts[i], false)
						+ _anFontSizes[i] + _afXPositions[i];
				fCurrentItemTabPositionLeftEdge = _afXPositions[i];
			} else if (_anAlignments[i] == Element.ALIGN_RIGHT) {
				fCurrentItemTabPositionRightEdge = _afXPositions[i] + _anFontSizes[i];
				fCurrentItemTabPositionLeftEdge = _afXPositions[i]
						- _oPDFContentByte.getEffectiveStringWidth(_asTexts[i], false) - _anFontSizes[i];
			} else {
				fCurrentItemTabPositionRightEdge = (_oPDFContentByte.getEffectiveStringWidth(_asTexts[i], false) / 2)
						+ _anFontSizes[i] + _afXPositions[i];
				fCurrentItemTabPositionLeftEdge = _afXPositions[i]
						- (_oPDFContentByte.getEffectiveStringWidth(_asTexts[i], false) / 2) - _anFontSizes[i];
			}

			if (i > 0) {

				ColumnText oColumnText = new ColumnText(_oPDFContentByte);
				Phrase oPhrase = new Phrase();
				oPhrase.setFont(_oLeaderFont);
				oPhrase.add(
						"................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................");
				oColumnText.addText(oPhrase);
				oColumnText.setSimpleColumn(fLastItemTabPositionRightEdge,
						_fPositionY + _oLeaderFont.getCalculatedSize(), fCurrentItemTabPositionLeftEdge, _fPositionY,
						_oLeaderFont.getSize(), Element.ALIGN_LEFT);
				oColumnText.go();

			}

			fLastItemTabPositionRightEdge = fCurrentItemTabPositionRightEdge;

		}

	}

	/**
	 * This method renders three text values, the first aligned left and the next
	 * two aligned right with the desired coordinates and fonts.
	 * 
	 * @param _oPDFContentByte        PDF object where content will be rendered
	 * @param _sLeftSideDisplayText   Text for left side
	 * @param _oLeftBaseFont          Font for left side
	 * @param _fLeftFontSize          Font size for left side
	 * @param _sRightSide1DisplayText 1st text for right side
	 * @param _oRight1BaseFont        Font for 1st right text
	 * @param _fRight1FontSize        Font size for 1st right text
	 * @param _sRightSide2DisplayText 2nd text for right side
	 * @param _oRight2BaseFont        Font for 2nd right text
	 * @param _fRight2FontSize        Font size for 2nd right text
	 * @param _fPositionX1            X-coordinate for left text
	 * @param _fPositionX2            X-coordinate for 1st right text
	 * @param _fPositionX3            X-coordinate for 2nd right text
	 * @param _fPositionY             Y coordinate for all text
	 * @throws DocumentException
	 */
	public void createDualRightLeftValue(PdfContentByte _oPDFContentByte, String _sLeftSideDisplayText,
			BaseFont _oLeftBaseFont, float _fLeftFontSize, String _sRightSide1DisplayText, BaseFont _oRight1BaseFont,
			float _fRight1FontSize, String _sRightSide2DisplayText, BaseFont _oRight2BaseFont, float _fRight2FontSize,
			float _fPositionX1, float _fPositionX2, float _fPositionX3, float _fPositionY) throws DocumentException {
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_oLeftBaseFont, _fLeftFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sLeftSideDisplayText, _fPositionX1, _fPositionY,
				0);

		_oPDFContentByte.setFontAndSize(_oRight1BaseFont, _fRight1FontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sRightSide1DisplayText, _fPositionX2, _fPositionY,
				0);

		if (_sRightSide2DisplayText != null && _sRightSide2DisplayText.length() > 0) {
			_oPDFContentByte.setFontAndSize(_oRight2BaseFont, _fRight2FontSize);
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sRightSide2DisplayText, _fPositionX3,
					_fPositionY, 0);
		}

		_oPDFContentByte.endText();

	}

	/**
	 * This method creates a column that will not go past the specified coordinate
	 * to the right.
	 * 
	 * @param _oPDFContentByte                           PDF object where content
	 *                                                   will be rendered
	 * @param _sLeftSideDisplayText_sLeftSideDisplayText Text for left side
	 * @param _oLeftBaseFont                             Font for left text
	 * @param _fLeftFontSize                             Font size for left text
	 * @param _sRightSideDisplayText                     Text for right side
	 * @param _oRightBaseFont                            Font for right text
	 * @param _fRightFontSize                            Font size for right text
	 * @param _oLeaderFont                               Font for leader
	 * @param _fPositionX1                               X-coordinate for left text
	 * @param _fPositionX2                               X-coordinate for right text
	 * @param _fPositionY                                Y-coordinate for text line
	 * @param _fPositionX3
	 * @throws DocumentException
	 */
	public void createRightLeftLeaderValueWithFixedRightStop(PdfContentByte _oPDFContentByte,
			String _sLeftSideDisplayText, BaseFont _oLeftBaseFont, float _fLeftFontSize, String _sRightSideDisplayText,
			BaseFont _oRightBaseFont, float _fRightFontSize, Font _oLeaderFont, float _fPositionX1, float _fPositionX2,
			float _fPositionY, float _fPositionX3) throws DocumentException {
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_oLeftBaseFont, _fLeftFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sLeftSideDisplayText, _fPositionX1, _fPositionY,
				0);
		float fLeftPhraseWidth = _oPDFContentByte.getEffectiveStringWidth(_sLeftSideDisplayText, false)
				+ _fLeftFontSize / 2;
		_oPDFContentByte.setFontAndSize(_oRightBaseFont, _fRightFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sRightSideDisplayText, _fPositionX2, _fPositionY,
				0);
		_oPDFContentByte.endText();

		ColumnText oColumnText = new ColumnText(_oPDFContentByte);
		Phrase oPhrase = new Phrase();
		oPhrase.setFont(_oLeaderFont);
		oPhrase.add(
				"................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................");
		oColumnText.addText(oPhrase);
		oColumnText.setSimpleColumn(_fPositionX1 + fLeftPhraseWidth, _fPositionY + _oLeaderFont.getCalculatedSize(),
				_fPositionX3, _fPositionY, _oLeaderFont.getSize(), Element.ALIGN_LEFT);
		oColumnText.go();

	}

	/**
	 * This method creates a label/value pair for rendering with a maximum right
	 * edge and a custom leader between the label and value.
	 * 
	 * @param _oPDFContentByte       PDF object where content will be rendered
	 * @param _sLeftSideDisplayText  Text for left side
	 * @param _oLeftBaseFont         Font for left text
	 * @param _fLeftFontSize         Font size for left text
	 * @param _sRightSideDisplayText Text for right side
	 * @param _oRightBaseFont        Font for right text
	 * @param _fRightFontSize        Font size for right text
	 * @param _sLeaderDisplayText    Text for leader
	 * @param _oLeaderBaseFont       Font for leader
	 * @param _fLeaderFontSize       Font size for leader
	 * @param _oLeaderFont           Font for leader dots
	 * @param _fPositionX1           X-coordinate for left text
	 * @param _fPositionX2           X-coordinate for custom leader
	 * @param _fPositionY            Y-coordinate for this display
	 * @param _fPositionX3           X-coordinate for right stop
	 * @param _fPositionX4           X-coordinate for right text
	 * @throws DocumentException
	 */
	public void createRightLeftLeaderValueWithFixedRightStopAndLeaderText(PdfContentByte _oPDFContentByte,
			String _sLeftSideDisplayText, BaseFont _oLeftBaseFont, float _fLeftFontSize, String _sRightSideDisplayText,
			BaseFont _oRightBaseFont, float _fRightFontSize, String _sLeaderDisplayText, BaseFont _oLeaderBaseFont,
			float _fLeaderFontSize, Font _oLeaderFont, float _fPositionX1, float _fPositionX2, float _fPositionY,
			float _fPositionX3, float _fPositionX4) throws DocumentException {
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_oLeftBaseFont, _fLeftFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sLeftSideDisplayText, _fPositionX1, _fPositionY,
				0);
		float fLeftPhraseWidth = _oPDFContentByte.getEffectiveStringWidth(_sLeftSideDisplayText, false)
				+ _fLeftFontSize / 2;

		_oPDFContentByte.setFontAndSize(_oLeaderBaseFont, _fLeaderFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sLeaderDisplayText, _fPositionX2, _fPositionY, 0);
		float fLeaderPhraseWidth = _oPDFContentByte.getEffectiveStringWidth(_sLeaderDisplayText, false)
				+ _fLeaderFontSize / 2;

		_oPDFContentByte.setFontAndSize(_oRightBaseFont, _fRightFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sRightSideDisplayText, _fPositionX4, _fPositionY,
				0);
		_oPDFContentByte.endText();

		ColumnText oColumnText = new ColumnText(_oPDFContentByte);
		Phrase oPhrase = new Phrase();
		oPhrase.setFont(_oLeaderFont);
		oPhrase.add(
				"................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................");
		oColumnText.addText(oPhrase);
		oColumnText.setSimpleColumn(_fPositionX1 + fLeftPhraseWidth, _fPositionY + _oLeaderFont.getCalculatedSize(),
				_fPositionX2, _fPositionY, _oLeaderFont.getSize(), Element.ALIGN_LEFT);
		oColumnText.go();

		ColumnText oColumnText2 = new ColumnText(_oPDFContentByte);
		Phrase oPhrase2 = new Phrase();
		oPhrase2.setFont(_oLeaderFont);
		oPhrase2.add(
				"................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................");
		oColumnText2.addText(oPhrase);
		oColumnText2.setSimpleColumn(_fPositionX2 + fLeaderPhraseWidth, _fPositionY + _oLeaderFont.getCalculatedSize(),
				_fPositionX3, _fPositionY, _oLeaderFont.getSize(), Element.ALIGN_LEFT);
		oColumnText2.go();

	}

	/**
	 * This method writes text to a document with a leader between the left and
	 * right text areas. In this method both texts (right & left) will be right
	 * aligned.
	 * 
	 * @param _oPDFContentByte       PDF object where content will be rendered
	 * @param _sLeftSideDisplayText  Text for left side
	 * @param _oLeftBaseFont         Font for left text
	 * @param _fLeftFontSize         Font size for left text
	 * @param _sRightSideDisplayText Text for right side
	 * @param _oRightBaseFont        Text for right side
	 * @param _fRightFontSize        Font size for right text
	 * @param _oLeaderFont           Font for leader
	 * @param _fPositionX1           X-coordinate for left text
	 * @param _fPositionX2           X-coordinate for left text
	 * @param _fPositionY            X-coordinate for left text
	 * @param _fPositionX3           X-coordinate for right stop
	 * @throws DocumentException
	 */
	public void createRightLeftLeaderValueRightJustifiedWithFixedRightStop(PdfContentByte _oPDFContentByte,
			String _sLeftSideDisplayText, BaseFont _oLeftBaseFont, float _fLeftFontSize, String _sRightSideDisplayText,
			BaseFont _oRightBaseFont, float _fRightFontSize, Font _oLeaderFont, float _fPositionX1, float _fPositionX2,
			float _fPositionY, float _fPositionX3) throws DocumentException {
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_oLeftBaseFont, _fLeftFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sLeftSideDisplayText, _fPositionX1, _fPositionY,
				0);
		_oPDFContentByte.setFontAndSize(_oRightBaseFont, _fRightFontSize);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sRightSideDisplayText, _fPositionX2, _fPositionY,
				0);
		_oPDFContentByte.endText();

		ColumnText oColumnText = new ColumnText(_oPDFContentByte);
		Phrase oPhrase = new Phrase();
		oPhrase.setFont(_oLeaderFont);
		oPhrase.add(
				"................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................");
		oColumnText.addText(oPhrase);
		oColumnText.setSimpleColumn(_fPositionX1 + _fLeftFontSize / 2, _fPositionY + _oLeaderFont.getCalculatedSize(),
				_fPositionX3, _fPositionY, _oLeaderFont.getSize(), Element.ALIGN_LEFT);
		oColumnText.go();

	}

	/**
	 * This method creates a "3 of 9" type bar code image and adds it to the PDF
	 * document.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @throws DocumentException
	 */
	public void create3of9Barcode(PdfContentByte _oPDFContentByte, String _sDisplayText, float _fPositionX,
			float _fPositionY) throws DocumentException {
		Barcode39 oCode39 = new Barcode39();
		oCode39.setCode(_sDisplayText);
		oCode39.setBarHeight(22);
		oCode39.setAltText("");

		Rectangle oRectangle = oCode39.getBarcodeSize();
		_oPDFContentByte.addImage(oCode39.createImageWithBarcode(_oPDFContentByte, null, null), oRectangle.getWidth(),
				0, 0, oRectangle.getHeight(), _fPositionX, _fPositionY);
	}

	/**
	 * This method creates a "3 of 9" type bar code image with a user-defined height
	 * and adds it to the PDF document.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @param _fBarHeight      Desired height for bar code
	 * @throws DocumentException
	 */
	public void create3of9BarcodeCustomHeight(PdfContentByte _oPDFContentByte, String _sDisplayText, float _fPositionX,
			float _fPositionY, float _fBarHeight) throws DocumentException {
		Barcode39 oCode39 = new Barcode39();
		oCode39.setCode(_sDisplayText);
		oCode39.setBarHeight(_fBarHeight);
		oCode39.setAltText("");
		Rectangle oRectangle = oCode39.getBarcodeSize();
		_oPDFContentByte.addImage(oCode39.createImageWithBarcode(_oPDFContentByte, null, null), oRectangle.getWidth(),
				0, 0, oRectangle.getHeight(), _fPositionX, _fPositionY);
	}

	/**
	 * This method creates a "3 of 9" type bar code image with a user-defined height
	 * and adds it to the PDF document.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @param _fBarHeight      Desired height for bar code
	 * @param _fRotateDegrees  Number of degrees to rotate image
	 * @throws DocumentException
	 */
	public void create3of9BarcodeCustomHeightRotateDegrees(PdfContentByte _oPDFContentByte, String _sDisplayText,
			float _fPositionX, float _fPositionY, float _fBarHeight, float _fRotateDegrees) throws DocumentException {
		Barcode39 oCode39 = new Barcode39();
		oCode39.setCode(_sDisplayText);
		oCode39.setBarHeight(_fBarHeight);
		oCode39.setAltText("");
		Image oBCImage = oCode39.createImageWithBarcode(_oPDFContentByte, null, null);
		oBCImage.setRotationDegrees(_fRotateDegrees);
		oBCImage.setAbsolutePosition(_fPositionX, _fPositionY);
		_oPDFContentByte.addImage(oBCImage);
	}

	/**
	 * This method creates a code 128 type bar code image and adds it to the PDF
	 * document.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @throws DocumentException
	 */
	public void create128Barcode(PdfContentByte _oPDFContentByte, String _sDisplayText, float _fPositionX,
			float _fPositionY) throws DocumentException {
		Barcode128 oCode128 = new Barcode128();
		oCode128.setCode(_sDisplayText);
		oCode128.setBarHeight(22);
		oCode128.setAltText("");
		Rectangle oRectangle = oCode128.getBarcodeSize();
		_oPDFContentByte.addImage(oCode128.createImageWithBarcode(_oPDFContentByte, null, null), oRectangle.getWidth(),
				0, 0, oRectangle.getHeight(), _fPositionX, _fPositionY);
	}

	/**
	 * This method creates a 128 type bar code image with a user-defined height and
	 * adds it to the PDF document.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @param _fBarHeight      Desired height for bar code
	 * @param _fRotateDegrees  Number of degrees to rotate image
	 * @param _fScale          Percent to scale the image by
	 * @throws DocumentException
	 */
	public void create128BarcodeCustomHeightRotationScale(PdfContentByte _oPDFContentByte, String _sDisplayText,
			float _fPositionX, float _fPositionY, float _fBarHeight, float _fRotateDegrees, float _fScale)
			throws DocumentException {
		Barcode128 oCode128 = new Barcode128();
		oCode128.setCode(_sDisplayText);
		oCode128.setBarHeight(_fBarHeight);
		oCode128.setAltText("");
		Image oBCImage = oCode128.createImageWithBarcode(_oPDFContentByte, null, null);
		oBCImage.setRotationDegrees(_fRotateDegrees);
		oBCImage.setAbsolutePosition(_fPositionX, _fPositionY);
		oBCImage.scalePercent(_fScale);
		_oPDFContentByte.addImage(oBCImage);
	}

	/**
	 * This method creates a "3 of 9" type bar code image and adds it to the PDF
	 * document- the image will be left aligned.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @throws DocumentException
	 */
	public void create3of9BarcodeLeftAlign(PdfContentByte _oPDFContentByte, String _sDisplayText, float _fPositionX,
			float _fPositionY) throws DocumentException {
		Barcode39 oCode39 = new Barcode39();
		oCode39.setCode(_sDisplayText);
		oCode39.setBarHeight(22);
		oCode39.setAltText("");
		Rectangle oRectangle = oCode39.getBarcodeSize();
		_oPDFContentByte.addImage(oCode39.createImageWithBarcode(_oPDFContentByte, null, null), oRectangle.getWidth(),
				0, 0, oRectangle.getHeight(), _fPositionX, _fPositionY);
	}

	/**
	 * This method creates a "3 of 9" type bar code image with a height of 16 and
	 * adds it to the PDF document.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @throws DocumentException
	 */
	public void create3of9Barcode16(PdfContentByte _oPDFContentByte, String _sDisplayText, float _fPositionX,
			float _fPositionY) throws DocumentException {
		Barcode39 oCode39 = new Barcode39();
		oCode39.setCode(_sDisplayText);
		oCode39.setBarHeight(16);
		oCode39.setAltText("");
		Rectangle oRectangle = oCode39.getBarcodeSize();
		_oPDFContentByte.addImage(oCode39.createImageWithBarcode(_oPDFContentByte, null, null), oRectangle.getWidth(),
				0, 0, oRectangle.getHeight(), _fPositionX, _fPositionY);
	}

	/**
	 * This method creates an Interleaved 2 of 5 bar code image with a custom height
	 * and adds it to the PDF document.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @param _fBarHeight      Height of bar code
	 * @throws DocumentException
	 */
	public void createInterleaved2of5BarcodeCustomHeight(PdfContentByte _oPDFContentByte, String _sDisplayText,
			float _fPositionX, float _fPositionY, float _fBarHeight) throws DocumentException {
		BarcodeInter25 oCode25 = new BarcodeInter25();
		oCode25.setCode(_sDisplayText);
		oCode25.setBarHeight(_fBarHeight);
		oCode25.setAltText("");
		oCode25.setGuardBars(true);
		Rectangle oRectangle = oCode25.getBarcodeSize();
		_oPDFContentByte.addImage(oCode25.createImageWithBarcode(_oPDFContentByte, null, null), oRectangle.getWidth(),
				0, 0, oRectangle.getHeight(), _fPositionX, _fPositionY);
	}

	public void createInterleaved2of5BarcodeRotated(PdfContentByte _oPDFContentByte, String _sDisplayText,
			float _fPositionX, float _fPositionY, float _fBarHeight, float _fRotateDegrees) throws DocumentException {
		BarcodeInter25 oCode25 = new BarcodeInter25();
		oCode25.setCode(_sDisplayText);
		oCode25.setBarHeight(_fBarHeight);
		oCode25.setAltText("");
		oCode25.setGuardBars(true);
		Image oBCImage = oCode25.createImageWithBarcode(_oPDFContentByte, null, null);
		oBCImage.setRotationDegrees(_fRotateDegrees);
		oBCImage.setAbsolutePosition(_fPositionX, _fPositionY);
		_oPDFContentByte.addImage(oBCImage);
	}

	/**
	 * This method creates an Interleaved 2 of 5 bar code image and adds it to the
	 * PDF document.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @throws DocumentException
	 */
	public void createInterleaved2of5Barcode(PdfContentByte _oPDFContentByte, String _sDisplayText, float _fPositionX,
			float _fPositionY) throws DocumentException {
		createInterleaved2of5BarcodeCustomHeight(_oPDFContentByte, _sDisplayText, _fPositionX, _fPositionY, 22);
	}

	/**
	 * This method creates an OCR scan line image and adds it to the PDF aligned
	 * right.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sPathToOCRFont  Path to font definition file
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createOCRScanlineRight(PdfContentByte _oPDFContentByte, String _sPathToOCRFont, String _sDisplayText,
			float _fPositionX, float _fPositionY) throws DocumentException, IOException {

		BaseFont oBaseFontOCRA = BaseFont.createFont(_sPathToOCRFont, BaseFont.CP1252, BaseFont.EMBEDDED);
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(oBaseFontOCRA, 12);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sDisplayText, _fPositionX, _fPositionY, 0);
		_oPDFContentByte.endText();
	}

	/**
	 * This method creates an OCR scan line image and adds it to the PDF aligned
	 * left.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sPathToOCRAFont Path to font definition file
	 * @param _sDisplayText    Text to be displayed
	 * @param _fPositionX      X-coordinate for image
	 * @param _fPositionY      Y-coordinate for image
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createOCRScanlineLeft(PdfContentByte _oPDFContentByte, String _sPathToOCRAFont, String _sDisplayText,
			float _fPositionX, float _fPositionY) throws DocumentException, IOException {

		BaseFont oBaseFontOCRA = BaseFont.createFont(_sPathToOCRAFont, BaseFont.CP1252, BaseFont.EMBEDDED);
		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(oBaseFontOCRA, 12);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sDisplayText, _fPositionX, _fPositionY, 0);
		_oPDFContentByte.endText();
	}

	/**
	 * This method creates a bar code to be read by O'Brien's multi-tray inserter to
	 * determine what will go into each envelope.
	 * 
	 * @param _oPDFContentByte    PDF object where content will be rendered
	 * @param _bCloseEnvelopeFlag
	 * @param _sRenderMode        Render mode (e.g. TEST for testing mode)
	 * @param _sBarcodeContent    Content to be rendered as bar code
	 * @throws DocumentException
	 */
	public void doOBrienBarCodeStandardLocation(PdfContentByte _oPDFContentByte, double _dPageShimmyLeftRight,
			double _dPageShimmyUpDown, boolean _bCloseEnvelopeFlag, int _nGroupSequence, int _nPageRollSequence,
			boolean _bHas9Envelope, boolean _bHasItemInPocketOne, boolean _bHasItemInPocketTwo,
			boolean _bHasItemInPocketThree, boolean _bHasItemInPocketFour, boolean _bHasItemInPocketSix,
			boolean _bUseInkMark, String _sRenderMode) throws DocumentException {
		doOBrienBarCode(_oPDFContentByte, XN_OBRIEN_BARCODE_XPOS, XN_OBRIEN_BARCODE_YPOS, _dPageShimmyLeftRight,
				_dPageShimmyUpDown, _bCloseEnvelopeFlag, _nGroupSequence, _nPageRollSequence, _bHas9Envelope,
				_bHasItemInPocketOne, _bHasItemInPocketTwo, _bHasItemInPocketThree, _bHasItemInPocketFour,
				_bHasItemInPocketSix, _bUseInkMark, _sRenderMode);
	}

	public void doOBrienBarCodeStandardLocationVehicle(PdfContentByte _oPDFContentByte, double _dPageShimmyLeftRight,
			double _dPageShimmyUpDown, String _sCloseEnvelopeFlag, int _nGroupSequence, int _nPageRollSequence,
			String _sHas9Envelope, String _sHasItemInPocketOne, String _sHasItemInPocketTwo,
			String _sHasItemInPocketThree, String _sHasItemInPocketFour, String _sHasItemInPocketSix,
			boolean _bUseInkMark, String _sRenderMode) throws DocumentException {

		boolean _bCloseEnvelopeFlag = false;
		if (_sCloseEnvelopeFlag.equalsIgnoreCase("Y")) {
			_bCloseEnvelopeFlag = true;
		}

		boolean _bHas9Envelope = false;
		if (_sHas9Envelope.equalsIgnoreCase("Y")) {
			_bHas9Envelope = true;
		}

		boolean _bHasItemInPocketOne = false;
		if (_sHasItemInPocketOne.equalsIgnoreCase("Y")) {
			_bHasItemInPocketOne = true;
		}

		boolean _bHasItemInPocketTwo = false;
		if (_sHasItemInPocketTwo.equalsIgnoreCase("Y")) {
			_bHasItemInPocketTwo = true;
		}

		boolean _bHasItemInPocketThree = false;
		if (_sHasItemInPocketThree.equalsIgnoreCase("Y")) {
			_bHasItemInPocketThree = true;
		}

		boolean _bHasItemInPocketFour = false;
		if (_sHasItemInPocketFour.equalsIgnoreCase("Y")) {
			_bHasItemInPocketFour = true;
		}

		boolean _bHasItemInPocketSix = false;
		if (_sHasItemInPocketSix.equalsIgnoreCase("Y")) {
			_bHasItemInPocketSix = true;
		}

		doOBrienBarCodeStandardLocation(_oPDFContentByte, _dPageShimmyLeftRight, _dPageShimmyUpDown,
				_bCloseEnvelopeFlag, _nGroupSequence, _nPageRollSequence, _bHas9Envelope, _bHasItemInPocketOne,
				_bHasItemInPocketTwo, _bHasItemInPocketThree, _bHasItemInPocketFour, _bHasItemInPocketSix, _bUseInkMark,
				_sRenderMode);
	}

	/**
	 * This method creates a bar code to be read by O'Brien's multi-tray inserter to
	 * determine what will go into each envelope.
	 * 
	 * @param _oPDFContentByte    PDF object where content will be rendered
	 * @param _nStartXPos         X-coordinate to begin rendering bar code
	 * @param _nStartYPos         Y-coordinate to begin rendering bar code
	 * @param _bCloseEnvelopeFlag
	 * @param _sRenderMode        Render mode (e.g. TEST for testing mode- will
	 *                            print human-readable bar code content)
	 * @param _sBarcodeContent    Content to be rendered as bar code
	 * @throws DocumentException
	 */
	public void doOBrienBarCode(PdfContentByte _oPDFContentByte, int _nStartXPos, int _nStartYPos,
			double _dPageShimmyLeftRight, double _dPageShimmyUpDown, boolean _bCloseEnvelopeFlag, int _nGroupSequence,
			int _nPageRollSequence, boolean _bHas9Envelope, boolean _bHasItemInPocketOne, boolean _bHasItemInPocketTwo,
			boolean _bHasItemInPocketThree, boolean _bHasItemInPocketFour, boolean _bHasItemInPocketSix,
			boolean _bUseInkMark, String _sRenderMode) throws DocumentException {
		String sBarcodeContent = getOBrienBarcode(_bCloseEnvelopeFlag, _nGroupSequence, _nPageRollSequence,
				_bHas9Envelope, _bHasItemInPocketOne, _bHasItemInPocketTwo, _bHasItemInPocketThree,
				_bHasItemInPocketFour, _bHasItemInPocketSix, _bUseInkMark);

		int nAbsStartXPos = _nStartXPos + (-1 * Math.round((float) _dPageShimmyLeftRight));
		int nAbsStartYPos = _nStartYPos + (-1 * Math.round((float) _dPageShimmyUpDown));

		create128BarcodeCustomHeightRotationScale(_oPDFContentByte, sBarcodeContent, nAbsStartXPos, nAbsStartYPos, 15,
				90, 100);

		if (_sRenderMode.equalsIgnoreCase("TEST")) {
			_oPDFContentByte.beginText();
			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_CENTER, sBarcodeContent, nAbsStartXPos - 3,
					nAbsStartYPos + 25, 90);

			int nRunningYPos = nAbsStartYPos + 55;
			int xPos = nAbsStartXPos - 45;
			String sText = "Close env: ";
			if (_bCloseEnvelopeFlag) {
				sText += "ON";
			} else {
				sText += "OFF";
			}
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sText, xPos, nRunningYPos, 0);

			nRunningYPos -= 8;
			sText = "Tray 1: ";
			if (_bHasItemInPocketOne) {
				sText += "ON";
			} else {
				sText += "OFF";
			}
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sText, xPos, nRunningYPos, 0);

			nRunningYPos -= 8;
			sText = "Tray 2: ";
			if (_bHasItemInPocketTwo) {
				sText += "ON";
			} else {
				sText += "OFF";
			}
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sText, xPos, nRunningYPos, 0);

			nRunningYPos -= 8;
			sText = "Tray 3: ";
			if (_bHasItemInPocketThree) {
				sText += "ON";
			} else {
				sText += "OFF";
			}
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sText, xPos, nRunningYPos, 0);

			nRunningYPos -= 8;
			sText = "Tray 4: ";
			if (_bHasItemInPocketFour) {
				sText += "ON";
			} else {
				sText += "OFF";
			}
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sText, xPos, nRunningYPos, 0);

			nRunningYPos -= 8;
			sText = "Tray 5: ";
			if (_bHas9Envelope) {
				sText += "ON";
			} else {
				sText += "OFF";
			}
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sText, xPos, nRunningYPos, 0);

			nRunningYPos -= 8;
			sText = "Tray 6: ";
			if (_bHasItemInPocketSix) {
				sText += "ON";
			} else {
				sText += "OFF";
			}
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sText, xPos, nRunningYPos, 0);

			nRunningYPos -= 8;
			sText = "Ink Mark: ";
			if (_bUseInkMark) {
				sText += "ON";
			} else {
				sText += "OFF";
			}
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sText, xPos, nRunningYPos, 0);

			_oPDFContentByte.endText();
		} else {

			String sHas9Envelope = "0";
			String sHasItemInPocketOne = "0";
			String sHasItemInPocketTwo = "0";
			String sHasItemInPocketThree = "0";
			String sHasItemInPocketFour = "0";
			String sHasItemInPocketFive = "0";

			String sCloseEnvelopeFlag = "0";

			if (_bHas9Envelope) {
				sHas9Envelope = "X";
			}

			if (_bHasItemInPocketOne) {
				sHasItemInPocketOne = "X";
			}

			if (_bHasItemInPocketTwo) {
				sHasItemInPocketTwo = "X";
			}

			if (_bHasItemInPocketThree) {
				sHasItemInPocketThree = "X";
			}

			if (_bHasItemInPocketFour) {
				sHasItemInPocketFour = "X";
			}

			if (_bHasItemInPocketSix) {
				sHasItemInPocketFive = "X";
			}

			if (_bCloseEnvelopeFlag) {
				sCloseEnvelopeFlag = "X";
			}

			String sHugsAndKissesLine = sHasItemInPocketOne + " " + sHasItemInPocketTwo + " " + sHasItemInPocketThree
					+ " " + sHasItemInPocketFour + " " + sHas9Envelope + " " + sHasItemInPocketFive + "   "
					+ sCloseEnvelopeFlag;

			_oPDFContentByte.beginText();
			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_CENTER, sHugsAndKissesLine, nAbsStartXPos - 7,
					nAbsStartYPos + 28, 270);

			_oPDFContentByte.endText();

		}

	}

	public String getOBrienBarcode(boolean _bCloseEnvelopeFlag, int _nGroupSequence, int _nPageRollSequence,
			boolean _bHas9Envelope, boolean _bHasItemInPocketOne, boolean _bHasItemInPocketTwo,
			boolean _bHasItemInPocketThree, boolean _bHasItemInPocketFour, boolean _bHasItemInPocketSix,
			boolean _bUseInkMark) {
		StringBuffer oBarcodeContent = new StringBuffer();
		if (_bCloseEnvelopeFlag) {
			oBarcodeContent.append("1");
		} else {
			oBarcodeContent.append("0");
		}
		oBarcodeContent.append(String.format("%02d", _nGroupSequence));
		oBarcodeContent.append(String.format("%d", _nPageRollSequence));
		oBarcodeContent.append(getOBrienInserterSequence(_bHas9Envelope, _bHasItemInPocketOne, _bHasItemInPocketTwo,
				_bHasItemInPocketThree, _bHasItemInPocketFour, _bHasItemInPocketSix, _bUseInkMark));

		return oBarcodeContent.toString();
	}

	public String getOBrienInserterSequence(boolean _bHas9Envelope, boolean _bHasItemInPocketOne,
			boolean _bHasItemInPocketTwo, boolean _bHasItemInPocketThree, boolean _bHasItemInPocketFour,
			boolean _bHasItemInPocketSix, boolean _bUseInkMark) {
		int nNumericalValue = 0;
		if (_bHasItemInPocketOne) {
			nNumericalValue += 1;
		}
		if (_bHasItemInPocketTwo) {
			nNumericalValue += 2;
		}
		if (_bHasItemInPocketThree) {
			nNumericalValue += 4;
		}
		if (_bHasItemInPocketFour) {
			nNumericalValue += 8;
		}
		if (_bHas9Envelope) {
			nNumericalValue += 16;
		}

		/*
		 * Currently, we do not use the inkmark. If we need to add the inkmark, here is
		 * the spec from O'Brien: 0 = No insert no inkmark 1 = Insert in the 6th station
		 * and no inkmark G = No insert with an inkmark H = Insert with an inkmark
		 */
		String sSecondDigit = "0";
		if (_bHasItemInPocketSix && !_bUseInkMark) {
			sSecondDigit = "1";
		} else if (!_bHasItemInPocketSix && _bUseInkMark) {
			sSecondDigit = "G";
		} else if (_bHasItemInPocketSix && _bUseInkMark) {
			sSecondDigit = "H";
		}

		return ioOBrienInserterFirstDigitMap.get(Integer.valueOf(nNumericalValue)) + sSecondDigit;
	}

	/**
	 * This method renders the OMR marks for content to include in mailing
	 * envelopes.
	 * 
	 * @param _oPDFContentByte       PDF object where content will be rendered
	 * @param _bHas9Envelope         Flag if #9 envelope should be included
	 * @param _bHasItemInPocketOne   Flag if an item should be included in pocket 1
	 * @param _bHasItemInPocketTwo   Flag if an item should be included in pocket 2
	 * @param _bHasItemInPocketThree Flag if an item should be included in pocket 3
	 * @param _bHasItemInPocketFour  Flag if an item should be included in pocket 4
	 * @param _bHasItemInPocketFive  Flag if an item should be included in pocket 5
	 * @param _sRenderMode           Render mode (e.g. TEST for testing mode)
	 * @param _asQBCodes             String array used in TEST mode for labeling
	 *                               each mark
	 */
//	public void doOmrMarksStandardLocation(PdfContentByte _oPDFContentByte, boolean _bHas9Envelope, boolean _bHasItemInPocketOne, boolean _bHasItemInPocketTwo,
//			boolean _bHasItemInPocketThree, boolean _bHasItemInPocketFour, boolean _bHasItemInPocketFive, String _sRenderMode, String[] _asQBCodes)
//	{
//		doOmrMarks(_oPDFContentByte, 562, 705, _bHas9Envelope, _bHasItemInPocketOne, _bHasItemInPocketTwo, _bHasItemInPocketThree, _bHasItemInPocketFour, _bHasItemInPocketFive,
//				_sRenderMode, _asQBCodes);
//	}

	/**
	 * This method renders the OMR marks for content to include in mailing
	 * envelopes.
	 * 
	 * @param _oPDFContentByte       PDF object where content will be rendered
	 * @param _nStartXPos            X-coordinate to begin rendering marks
	 * @param _nStartYPos            Y-coordinate to begin rendering marks
	 * @param _bHas9Envelope         Flag if #9 envelope should be included
	 * @param _bHasItemInPocketOne   Flag if an item should be included in pocket 1
	 * @param _bHasItemInPocketTwo   Flag if an item should be included in pocket 2
	 * @param _bHasItemInPocketThree Flag if an item should be included in pocket 3
	 * @param _bHasItemInPocketFour  Flag if an item should be included in pocket 4
	 * @param _bHasItemInPocketFive  Flag if an item should be included in pocket 5
	 * @param _sRenderMode           Render mode (e.g. TEST for testing mode)
	 * @param _asQBCodes             String array used in TEST mode for labeling
	 *                               each mark
	 */
//	public void doOmrMarks(PdfContentByte _oPDFContentByte, int _nStartXPos, int _nStartYPos, boolean _bHas9Envelope, boolean _bHasItemInPocketOne, boolean _bHasItemInPocketTwo,
//			boolean _bHasItemInPocketThree, boolean _bHasItemInPocketFour, boolean _bHasItemInPocketFive, String _sRenderMode, String[] _asQBCodes)
//	{
//
//		// These items/variables BELOW will never be changed
//		_oPDFContentByte.setLineWidth(1.5f);
//		int nLineWidth = 35;
//		int nLineOffset = 13;
//		int nRunningLineOffset = 0;
//		// These items/variables ABOVE will never be changed
//
//		// This will always be "on"
//		_oPDFContentByte.moveTo(_nStartXPos, _nStartYPos);
//		_oPDFContentByte.lineTo(_nStartXPos + nLineWidth, _nStartYPos);
//		_oPDFContentByte.stroke();
//
//		if (_sRenderMode.equalsIgnoreCase("TEST"))
//		{
//			_oPDFContentByte.beginText();
//			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
//			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, "START BAR", _nStartXPos - 6, _nStartYPos, 0);
//			_oPDFContentByte.endText();
//		}
//
//		// This will always be "on"
//		nRunningLineOffset = nLineOffset * 1;
//		_oPDFContentByte.moveTo(_nStartXPos, _nStartYPos - nRunningLineOffset);
//		_oPDFContentByte.lineTo(_nStartXPos + nLineWidth, _nStartYPos - nRunningLineOffset);
//		_oPDFContentByte.stroke();
//
//		if (_sRenderMode.equalsIgnoreCase("TEST"))
//		{
//			_oPDFContentByte.beginText();
//			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
//			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, "ALWAYS ON", _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			_oPDFContentByte.endText();
//		}
//
//		// This is the #9 envelope
//		nRunningLineOffset = nLineOffset * 2;
//		if (_bHas9Envelope)
//		{
//			_oPDFContentByte.moveTo(_nStartXPos, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.lineTo(_nStartXPos + nLineWidth, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.stroke();
//		}
//
//		if (_sRenderMode.equalsIgnoreCase("TEST"))
//		{
//			_oPDFContentByte.beginText();
//			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
//			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, "#9 Envelope", _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			_oPDFContentByte.endText();
//		}
//
//		// This is the 1st insert pocket
//		nRunningLineOffset = nLineOffset * 3;
//		if (_bHasItemInPocketOne)
//		{
//			_oPDFContentByte.moveTo(_nStartXPos, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.lineTo(_nStartXPos + nLineWidth, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.stroke();
//		}
//
//		if (_sRenderMode.equalsIgnoreCase("TEST"))
//		{
//			_oPDFContentByte.beginText();
//			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
//			if (_asQBCodes == null)
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, "1st Pocket", _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			else
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _asQBCodes[0], _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			_oPDFContentByte.endText();
//		}
//
//		// This is the 2nd insert pocket
//		nRunningLineOffset = nLineOffset * 4;
//		if (_bHasItemInPocketTwo)
//		{
//			_oPDFContentByte.moveTo(_nStartXPos, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.lineTo(_nStartXPos + nLineWidth, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.stroke();
//		}
//
//		if (_sRenderMode.equalsIgnoreCase("TEST"))
//		{
//			_oPDFContentByte.beginText();
//			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
//			if (_asQBCodes == null)
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, "2nd Pocket", _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			else
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _asQBCodes[1], _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			_oPDFContentByte.endText();
//		}
//
//		// This is the 3rd insert pocket
//		nRunningLineOffset = nLineOffset * 5;
//		if (_bHasItemInPocketThree)
//		{
//			_oPDFContentByte.moveTo(_nStartXPos, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.lineTo(_nStartXPos + nLineWidth, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.stroke();
//		}
//
//		if (_sRenderMode.equalsIgnoreCase("TEST"))
//		{
//			_oPDFContentByte.beginText();
//			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
//			if (_asQBCodes == null)
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, "3rd Pocket", _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			else
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _asQBCodes[2], _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			_oPDFContentByte.endText();
//		}
//
//		// This is the 4th insert pocket
//		nRunningLineOffset = nLineOffset * 6;
//		if (_bHasItemInPocketFour)
//		{
//			_oPDFContentByte.moveTo(_nStartXPos, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.lineTo(_nStartXPos + nLineWidth, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.stroke();
//		}
//
//		if (_sRenderMode.equalsIgnoreCase("TEST"))
//		{
//			_oPDFContentByte.beginText();
//			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
//			if (_asQBCodes == null)
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, "4th Pocket", _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			else
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _asQBCodes[3], _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			_oPDFContentByte.endText();
//		}
//
//		// This is the 5th insert pocket
//		nRunningLineOffset = nLineOffset * 7;
//		if (_bHasItemInPocketFive)
//		{
//			_oPDFContentByte.moveTo(_nStartXPos, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.lineTo(_nStartXPos + nLineWidth, _nStartYPos - nRunningLineOffset);
//			_oPDFContentByte.stroke();
//		}
//
//		if (_sRenderMode.equalsIgnoreCase("TEST"))
//		{
//			_oPDFContentByte.beginText();
//			_oPDFContentByte.setFontAndSize(ioBaseFontHelveticaRegular, 6);
//			if (_asQBCodes == null)
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, "5th Pocket", _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			else
//			{
//				_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _asQBCodes[4], _nStartXPos - 6, _nStartYPos - nRunningLineOffset, 0);
//			}
//			_oPDFContentByte.endText();
//		}
//
//		// These items/variables BELOW will never be changed
//		_oPDFContentByte.setLineWidth(0);
//		// These items/variables ABOVE will never be changed
//
//	}

	/**
	 * This method prints the addressee lines with the IMB record sequence.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sPathToIMBFont  Path to font file
	 * @param _sRecordSequence Sequence to render
	 * @param _sAddresseeName  Name to mail to
	 * @param _sAddress1       Address line 1
	 * @param _sAddress2       Address line 2
	 * @param _sAddress3       Address line 3
	 * @param _sAddress4       Address line 4
	 * @param _sCity           City for mailing
	 * @param _sState          State for mailing
	 * @param _sZip            Zip code for mailing
	 * @param _sZipPlus4       Zip Plus4 code for mailing
	 * @param _sZipDP          NOT USED
	 * @param _sEndorsement    Endorsement to add to the address block
	 * @param _sIMB            String to render as IMB
	 * @param _oBaseFont       Font to use for printing each non-IMB element
	 * @param _sStockNumber    Stock number for print run
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createAddresseeBlockIMB(PdfContentByte _oPDFContentByte, String _sPathToIMBFont,
			String _sRecordSequence, String _sAddresseeName, String _sAddress1, String _sAddress2, String _sAddress3,
			String _sAddress4, String _sCity, String _sState, String _sZip, String _sZipPlus4, String _sZipDP,
			String _sEndorsement, String _sIMB, BaseFont _oBaseFont, String _sStockNumber)
			throws DocumentException, IOException {
		createAddresseeBlockIMB(_oPDFContentByte, _sPathToIMBFont, _sRecordSequence, null, _sAddresseeName, null,
				_sAddress1, _sAddress2, _sAddress3, _sAddress4, _sCity, _sState, _sZip, _sZipPlus4, _sZipDP,
				_sEndorsement, _sIMB, _oBaseFont, 80, 45, _sStockNumber, false, null);
	}

	/**
	 * This method prints the addressee lines with the IMB record sequence (for
	 * utility clients with two addressee names).
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sPathToIMBFont  Path to font file
	 * @param _sRecordSequence Sequence to render
	 * @param _sAddresseeName1 Name to mail to
	 * @param _sAddresseeName2 2nd name to mail to
	 * @param _sAddress1       Address line 1
	 * @param _sAddress2       Address line 2
	 * @param _sAddress3       Address line 3
	 * @param _sAddress4       Address line 4
	 * @param _sCity           City for mailing
	 * @param _sState          State for mailing
	 * @param _sZip            Zip code for mailing
	 * @param _sZipPlus4       Zip Plus4 code for mailing
	 * @param _sZipDP          NOT USED
	 * @param _sEndorsement    Endorsement to add to the address block
	 * @param _sIMB            String to render as IMB
	 * @param _oBaseFont       Font to use for printing each non-IMB element
	 * @param _sStockNumber    Stock number for print run
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createAddresseeBlockIMBUtilityTwoNames(PdfContentByte _oPDFContentByte, String _sPathToIMBFont,
			String _sRecordSequence, String _sAddresseeName1, String _sAddresseeName2, String _sAddress1,
			String _sAddress2, String _sAddress3, String _sAddress4, String _sCity, String _sState, String _sZip,
			String _sZipPlus4, String _sZipDP, String _sEndorsement, String _sIMB, BaseFont _oBaseFont,
			String _sStockNumber) throws DocumentException, IOException {
		createAddresseeBlockIMB(_oPDFContentByte, _sPathToIMBFont, _sRecordSequence, null, _sAddresseeName1,
				_sAddresseeName2, _sAddress1, _sAddress2, _sAddress3, _sAddress4, _sCity, _sState, _sZip, _sZipPlus4,
				_sZipDP, _sEndorsement, _sIMB, _oBaseFont, 80, 45, _sStockNumber, false, null);
	}

	/**
	 * This method prints the addressee lines with the IMB record sequence and OCR-A
	 * sequence.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sPathToIMBFont  Path to font file
	 * @param _sRecordSequence Sequence to render
	 * @param _sAddresseeName  Name to mail to
	 * @param _sAddress1       Address line 1
	 * @param _sAddress2       Address line 2
	 * @param _sAddress3       Address line 3
	 * @param _sAddress4       Address line 4
	 * @param _sCity           City for mailing
	 * @param _sState          State for mailing
	 * @param _sZip            Zip code for mailing
	 * @param _sZipPlus4       Zip Plus4 code for mailing
	 * @param _sZipDP          NOT USED
	 * @param _sEndorsement    Endorsement to add to the address block
	 * @param _sIMB            String to render as IMB
	 * @param _oBaseFont       Font to use for printing each non-IMB element
	 * @param _sStockNumber    Stock number for print run
	 * @param _sPathToOCRAFont Path to find OCR-A text font
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createAddresseeBlockIMBWithRecordSequence(PdfContentByte _oPDFContentByte, String _sPathToIMBFont,
			String _sRecordSequence, String _sAddresseeName, String _sAddress1, String _sAddress2, String _sAddress3,
			String _sAddress4, String _sCity, String _sState, String _sZip, String _sZipPlus4, String _sZipDP,
			String _sEndorsement, String _sIMB, BaseFont _oBaseFont, String _sStockNumber, String _sPathToOCRAFont)
			throws DocumentException, IOException {
		createAddresseeBlockIMB(_oPDFContentByte, _sPathToIMBFont, _sRecordSequence, null, _sAddresseeName, null,
				_sAddress1, _sAddress2, _sAddress3, _sAddress4, _sCity, _sState, _sZip, _sZipPlus4, _sZipDP,
				_sEndorsement, _sIMB, _oBaseFont, 80, 45, _sStockNumber, true, _sPathToOCRAFont);
	}

	/**
	 * This method prints the addressee lines with the IMB record sequence and the
	 * OCR-A sequence.
	 * 
	 * @param _oPDFContentByte          PDF object where content will be rendered
	 * @param _sPathToIMBFont           Path to font file
	 * @param _sRecordSequence          Sequence to render
	 * @param _sAlternateRecordSequence Alternate record to render
	 * @param _sAddresseeName1          First Name to mail to
	 * @param _sAddresseeName2          Second Name to mail to
	 * @param _sAddress1                Address line 1
	 * @param _sAddress2                Address line 2
	 * @param _sAddress3                Address line 3
	 * @param _sAddress4                Address line 4
	 * @param _sCity                    City for mailing
	 * @param _sState                   State for mailing
	 * @param _sZip                     Zip code for mailing
	 * @param _sZipPlus4                Zip Plus4 code for mailing
	 * @param _sZipDP                   NOT USED
	 * @param _sEndorsement             Endorsement to add to the address block
	 * @param _sIMB                     String to render as IMB
	 * @param _oBaseFont                Font to use for printing each non-IMB
	 *                                  element
	 * @param _nLeftEdgeOfPrintElements X-coordinate for left edge of printed
	 *                                  elements
	 * @param _fPostnetYPos             Y-coordinate for printed elements
	 * @param _sStockNumber             Stock number for print run
	 * @param _bPrintRecordSequence     Flag if record sequence should be printed
	 * @param _sPathToOCRAFont          Path to find OCR-A text font
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createAddresseeBlockIMB(PdfContentByte _oPDFContentByte, String _sPathToIMBFont,
			String _sRecordSequence, String _sAlternateRecordSequence, String _sAddresseeName1, String _sAddresseeName2,
			String _sAddress1, String _sAddress2, String _sAddress3, String _sAddress4, String _sCity, String _sState,
			String _sZip, String _sZipPlus4, String _sZipDP, String _sEndorsement, String _sIMB, BaseFont _oBaseFont,
			int _nLeftEdgeOfPrintElements, float _fPostnetYPos, String _sStockNumber, boolean _bPrintRecordSequence,
			String _sPathToOCRAFont) throws DocumentException, IOException {

		int nOriginalLeftEdgeOfPrintElements = _nLeftEdgeOfPrintElements;

		if (_bPrintRecordSequence) {
			_nLeftEdgeOfPrintElements = _nLeftEdgeOfPrintElements + 14;
		}

		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_oBaseFont, 9);

		String sCityStateZipLine = "";

		if (_sCity.length() > 0 && _sState.length() > 0 && _sZip.length() > 0) {
			sCityStateZipLine = _sCity + ", " + _sState + "  " + _sZip;
		} else {
			sCityStateZipLine = _sCity + " " + _sState + "  " + _sZip;
		}

		if (_sZipPlus4 != null && _sZipPlus4.length() > 0) {
			sCityStateZipLine = sCityStateZipLine + "-" + _sZipPlus4;
		}

		float nYPos = _fPostnetYPos + 15;
		float nCodePos = _fPostnetYPos + 50;

		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sCityStateZipLine, _nLeftEdgeOfPrintElements, nYPos,
				0);

		ArrayList<String> oAddressLineArrayList = new ArrayList<String>();

		if (_sAddresseeName1 != null && _sAddresseeName1.length() > 0) {
			oAddressLineArrayList.add(_sAddresseeName1.trim());
		}

		if (_sAddresseeName2 != null && _sAddresseeName2.trim().length() > 0) {
			oAddressLineArrayList.add(_sAddresseeName2.trim());
		}

		if (_sAddress1 != null && _sAddress1.length() > 0 && !oAddressLineArrayList.contains(_sAddress1.trim())) {
			nYPos = nYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddress1.trim(), _nLeftEdgeOfPrintElements,
					nYPos, 0);
			oAddressLineArrayList.add(_sAddress1.trim());
		}

		if (_sAddress2 != null && _sAddress2.length() > 0 && !oAddressLineArrayList.contains(_sAddress2.trim())) {
			nYPos = nYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddress2.trim(), _nLeftEdgeOfPrintElements,
					nYPos, 0);
			oAddressLineArrayList.add(_sAddress2.trim());
		}

		if (_sAddress3 != null && _sAddress3.length() > 0 && !oAddressLineArrayList.contains(_sAddress3.trim())) {
			nYPos = nYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddress3.trim(), _nLeftEdgeOfPrintElements,
					nYPos, 0);
			oAddressLineArrayList.add(_sAddress3.trim());
		}

		if (_sAddress4 != null && _sAddress4.length() > 0 && !oAddressLineArrayList.contains(_sAddress4.trim())) {
			nYPos = nYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddress4.trim(), _nLeftEdgeOfPrintElements,
					nYPos, 0);
			oAddressLineArrayList.add(_sAddress4.trim());
		}

		if (_sAddresseeName2 != null && _sAddresseeName2.trim().length() > 0) {
			nYPos = nYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddresseeName2.trim(),
					_nLeftEdgeOfPrintElements, nYPos, 0);
		}

		if (_sAddresseeName1 != null && _sAddresseeName1.length() > 0) {
			nYPos = nYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddresseeName1, _nLeftEdgeOfPrintElements,
					nYPos, 0);
		}

		if (_sEndorsement != null && _sEndorsement.length() > 0) {
			nYPos = nYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sEndorsement, _nLeftEdgeOfPrintElements, nYPos,
					0);
		}

		_oPDFContentByte.setFontAndSize(_oBaseFont, 5);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sRecordSequence, 270, nCodePos, 0);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sStockNumber, 270, nCodePos - 5, 0);
		_oPDFContentByte.endText();

		if (_bPrintRecordSequence) {

			if (_sAlternateRecordSequence != null) {
				if (_sAlternateRecordSequence.length() > 0) {

					BaseFont oBaseFontOCRA = BaseFont.createFont(_sPathToOCRAFont, BaseFont.CP1252, BaseFont.EMBEDDED);
					_oPDFContentByte.beginText();
					_oPDFContentByte.setFontAndSize(oBaseFontOCRA, 10);
					_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, _sAlternateRecordSequence,
							nOriginalLeftEdgeOfPrintElements, _fPostnetYPos + 53, 90);
					_oPDFContentByte.endText();

				}
			} else {
				String[] asRecordSequence = _sRecordSequence.split(" ");
				String sRecordSequence = asRecordSequence[asRecordSequence.length - 1].replaceAll("\\(", "")
						.replaceAll("\\)", "");

				if (sRecordSequence.length() > 0) {

					BaseFont oBaseFontOCRA = BaseFont.createFont(_sPathToOCRAFont, BaseFont.CP1252, BaseFont.EMBEDDED);
					_oPDFContentByte.beginText();
					_oPDFContentByte.setFontAndSize(oBaseFontOCRA, 10);
					_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_RIGHT, sRecordSequence,
							nOriginalLeftEdgeOfPrintElements, _fPostnetYPos + 53, 90);
					_oPDFContentByte.endText();

				}
			}

		}

		if (_sIMB.length() > 0) {
			_oPDFContentByte.beginText();
			BaseFont oBaseFontIMB = BaseFont.createFont(_sPathToIMBFont, BaseFont.CP1252, BaseFont.EMBEDDED);
			_oPDFContentByte.setFontAndSize(oBaseFontIMB, 16);
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sIMB, _nLeftEdgeOfPrintElements,
					(_fPostnetYPos + 4), 0);
			_oPDFContentByte.endText();

		}

	}

	/**
	 * This method determines the proper postal layout based on the country and
	 * renders the postal information as needed.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sCountry        The country portion of the address
	 * @param _sAddresseeName1 First Name to mail to
	 * @param _sAddresseeName2 Second Name to mail to
	 * @param _sAddress1       Address line 1
	 * @param _sAddress2       Address line 2
	 * @param _sAddress3       Address line 3
	 * @param _sAddress4       Address line 4
	 * @param _sCity           City for mailing
	 * @param _sState          State/province for mailing
	 * @param _sPostalCode     Postal code for mailing
	 * @param _sRecordSequence Sequence to render
	 * @param _oBaseFont       Font to use for printing each non-IMB element
	 * @param _sStockNumber    Stock number for print run
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createInternationalAddressBlock(PdfContentByte _oPDFContentByte, String _sCountry,
			String _sAddresseeName1, String _sAddresseeName2, String _sAddress1, String _sAddress2, String _sAddress3,
			String _sAddress4, String _sCity, String _sState, String _sPostalCode, String _sRecordSequence,
			BaseFont _oBaseFont, String _sStockNumber) throws DocumentException, IOException {
		if (_sCountry != null && _sCountry.equalsIgnoreCase("CANADA")) {
			createCanadianAddressBlock(_oPDFContentByte, _sAddresseeName1, _sAddresseeName2, _sAddress1, _sAddress2,
					_sAddress3, _sAddress4, _sCity, _sState, _sPostalCode, _sRecordSequence, _oBaseFont, _sStockNumber);
		}
	}

	/**
	 * This method the postal layout based on the Canadian postal requirements.
	 * 
	 * @param _oPDFContentByte PDF object where content will be rendered
	 * @param _sAddresseeName1 First Name to mail to
	 * @param _sAddresseeName2 Second Name to mail to
	 * @param _sAddress1       Address line 1
	 * @param _sAddress2       Address line 2
	 * @param _sAddress3       Address line 3
	 * @param _sAddress4       Address line 4
	 * @param _sCity           City for mailing
	 * @param _sState          State/province for mailing
	 * @param _sPostalCode     Postal code for mailing
	 * @param _sRecordSequence Sequence to render
	 * @param _oBaseFont       Font to use for printing each non-IMB element
	 * @param _sStockNumber    Stock number for print run
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createCanadianAddressBlock(PdfContentByte _oPDFContentByte, String _sAddresseeName1,
			String _sAddresseeName2, String _sAddress1, String _sAddress2, String _sAddress3, String _sAddress4,
			String _sCity, String _sState, String _sPostalCode, String _sRecordSequence, BaseFont _oBaseFont,
			String _sStockNumber) throws DocumentException, IOException {
		int nLeftEdgeOfPrintElements = 80;
		float fYPos = 60;
		float nCodePos = 95;
		ArrayList<String> oAddressLineArrayList = new ArrayList<String>();

		_oPDFContentByte.beginText();
		_oPDFContentByte.setFontAndSize(_oBaseFont, 9);

		String sCityStateZipLine = _sCity + " " + _sState + "  " + _sPostalCode;

		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, "CANADA", nLeftEdgeOfPrintElements, fYPos, 0);

		if (sCityStateZipLine.trim().length() > 0) {
			fYPos += 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, sCityStateZipLine, nLeftEdgeOfPrintElements,
					fYPos, 0);
		}

		if (_sAddresseeName1 != null && _sAddresseeName1.length() > 0) {
			oAddressLineArrayList.add(_sAddresseeName1.trim());
		}

		if (_sAddresseeName2 != null && _sAddresseeName2.trim().length() > 0) {
			oAddressLineArrayList.add(_sAddresseeName2.trim());
		}

		if (_sAddress1 != null && _sAddress1.length() > 0 && !oAddressLineArrayList.contains(_sAddress1.trim())) {
			fYPos = fYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddress1.trim(), nLeftEdgeOfPrintElements,
					fYPos, 0);
			oAddressLineArrayList.add(_sAddress1.trim());
		}

		if (_sAddress2 != null && _sAddress2.length() > 0 && !oAddressLineArrayList.contains(_sAddress2.trim())) {
			fYPos = fYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddress2.trim(), nLeftEdgeOfPrintElements,
					fYPos, 0);
			oAddressLineArrayList.add(_sAddress2.trim());
		}

		if (_sAddress3 != null && _sAddress3.length() > 0 && !oAddressLineArrayList.contains(_sAddress3.trim())) {
			fYPos = fYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddress3.trim(), nLeftEdgeOfPrintElements,
					fYPos, 0);
			oAddressLineArrayList.add(_sAddress3.trim());
		}

		if (_sAddress4 != null && _sAddress4.length() > 0 && !oAddressLineArrayList.contains(_sAddress4.trim())) {
			fYPos = fYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddress4.trim(), nLeftEdgeOfPrintElements,
					fYPos, 0);
			oAddressLineArrayList.add(_sAddress4.trim());
		}

		if (_sAddresseeName2 != null && _sAddresseeName2.trim().length() > 0) {
			fYPos = fYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddresseeName2.trim(),
					nLeftEdgeOfPrintElements, fYPos, 0);
		}

		if (_sAddresseeName1 != null && _sAddresseeName1.length() > 0) {
			fYPos = fYPos + 10;
			_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sAddresseeName1, nLeftEdgeOfPrintElements,
					fYPos, 0);
		}

		_oPDFContentByte.setFontAndSize(_oBaseFont, 5);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sRecordSequence, 270, nCodePos, 0);
		_oPDFContentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, _sStockNumber, 270, nCodePos - 5, 0);
		_oPDFContentByte.endText();
	}

	/**
	 * This method calculates the absolute X-coordinate based on the number of
	 * points from the right side of the page.
	 * 
	 * @param _fCoordinateX Number of points from the right side of the page to
	 *                      render
	 * @return Absolute X-coordinate
	 */
	public float fromRight(float _fCoordinateX) {
		return ifMaxPageWidth - _fCoordinateX;
	}

	/**
	 * This method calculates the absolute Y-coordinate calculated form the top of
	 * the page.
	 * 
	 * @param _fCoordinateY Number of points from the top of the page
	 * @return Absolute Y-coordinate
	 */
	public float fromTop(float _fCoordinateY) {
		return ifMaxPageHeight - _fCoordinateY;
	}

	/**
	 * This method returns the calculated absolute Y-coordinate centered vertically
	 * from the top of the page.
	 * 
	 * @param _fDownAdjust Number of points to adjust down from center
	 * @return Absolute Y-coordinate
	 */
	public float centerVertical(float _fDownAdjust) {
		return (ifMaxPageHeight / 2) - _fDownAdjust;
	}

	/**
	 * This method returns the calculated absolute X-coordinate centered
	 * horizontally from the right of the page.
	 * 
	 * @param _fRightAdjust Number of points to adjust right from center
	 * @return Absolute X-coordinate
	 */
	public float centerHorizontal(float _fRightAdjust) {
		return (ifMaxPageWidth / 2) + _fRightAdjust;
	}

	/**
	 * This method returns the absolute Y-coordinate where a tri-folded document
	 * should be folded in the top portion of the document.
	 * 
	 * @return Absolute Y-coordinate
	 */
	public float getTopTrifoldCoordinate() {
		return (float) (ifMaxPageHeight * 2.0 / 3.0);
	}

	/**
	 * This method returns the absolute Y-coordinate where a tri-folded document
	 * should be folded in the bottom portion of the document.
	 * 
	 * @return Absolute Y-coordinate
	 */
	public float getBottomTrifoldCoordinate() {
		return (float) (ifMaxPageHeight / 3.0);
	}

	/**
	 * This method creates an empty PDF cell.
	 * 
	 * @return Empty PDFCell object
	 */
	public PdfPCell getBlankCell() {
		PdfPCell cellBlank = new PdfPCell(new Phrase(""));
		cellBlank.setBorder(Rectangle.NO_BORDER);
		return cellBlank;
	}

	/**
	 * Generates a Message Center or any block of aligned text
	 * 
	 * @param _lsText           Lines of Text
	 * @param _oFont            Font to use
	 * @param _fLeft            Left Edge
	 * @param _fTop             Top Edge
	 * @param _fWidth           Width in points
	 * @param _fHeight          Height In points
	 * @param _bShowBoundingBox Shows Box around text (useful for testing)
	 * @param _bLeftJustify     True: Text is left justified; False: Text is
	 *                          centered
	 * @param _bTopAlign        True: Text is Top aligned; False: Text is centered
	 * @param _fExtraSpacing    Amount of Extra Line spacing (0: Single Spaced; 1:
	 *                          double spaced;...)
	 */
	public void generateMessageCenter(List<String> _lsMessageText, Font _oFont, float _fLeft, float _fTop,
			float _fWidth, float _fHeight, boolean _bShowBoundingBox, boolean _bLeftJustify, boolean _bTopAlign,
			float _fExtraSpacing) {

		boolean bFirstValidLineHit = false;

		List<String> oCleanedMessageCenterReverseOrder = new ArrayList<String>();

		for (int i = _lsMessageText.size() - 1; i >= 0; i--) {

			String sLine = _lsMessageText.get(i).trim();

			if (sLine.length() > 0) {
				oCleanedMessageCenterReverseOrder.add(sLine);
				bFirstValidLineHit = true;
			} else {
				if (bFirstValidLineHit) {
					oCleanedMessageCenterReverseOrder.add(sLine);
				}
			}
		}

		List<String> oCleanedMessageCenterCorrectOrder = new ArrayList<String>();

		for (int i = 0; i < oCleanedMessageCenterReverseOrder.size(); i++) {
			oCleanedMessageCenterCorrectOrder.add(_lsMessageText.get(i));
		}

		Paragraph paraCenter = new Paragraph();
		paraCenter.setFont(_oFont);
		for (int i = 0; i < oCleanedMessageCenterCorrectOrder.size(); i++) {
			Phrase phLine = new Phrase(oCleanedMessageCenterCorrectOrder.get(i));
			paraCenter.add(phLine);
			paraCenter.add(Chunk.NEWLINE);
		}

		PdfPTable tabBox = new PdfPTable(1);
		tabBox.setTotalWidth(_fWidth);
		tabBox.setLockedWidth(true);

		PdfPCell cellBox = new PdfPCell(paraCenter);
		cellBox.setLeading(0, 1 + _fExtraSpacing);
		cellBox.setFixedHeight(_fHeight);

		int nHAlign, nVAlign;

		nHAlign = _bLeftJustify ? Element.ALIGN_LEFT : Element.ALIGN_CENTER;
		nVAlign = _bTopAlign ? Element.ALIGN_TOP : Element.ALIGN_MIDDLE;

		cellBox.setHorizontalAlignment(nHAlign);
		cellBox.setVerticalAlignment(nVAlign);

		if (!_bShowBoundingBox) {
			cellBox.setBorder(Rectangle.NO_BORDER);
		}

		tabBox.addCell(cellBox);
		tabBox.writeSelectedRows(0, -1, _fLeft, _fTop, ioPDFContentByte);

	}

//	/**
//	 * This method generates the message center text in the specified format
//	 * and location.
//	 * 
//	 * @param _fLeft
//	 *            X-coordinate for beginning of message
//	 * @param _fBottom
//	 *            Y-coordinate for bottom of message
//	 * @param _fWidth
//	 *            Width of message
//	 * @param _fHeight
//	 *            Height of message
//	 * @param _sText
//	 *            Text to render in the message
//	 * @param _bShowBoundingBox
//	 *            Flag if message should have a border (not used)
//	 * @param _bLeftJustify
//	 *            Flag if message should be left justified (not used)
//	 * @param _bTopAlign
//	 *            Flag if message should be aligned with the top of the created
//	 *            box (not used)
//	 * @throws FileNotFoundException
//	 * @throws IOException
//	 * @throws DocumentException
//	 */
//	public void generateFormattedMessageCenter(float _fLeft, float _fBottom, float _fWidth, float _fHeight, String _sText, float _fLeading) throws FileNotFoundException,
//			IOException, DocumentException
//	{
//
//		StringReader myStringReader = new StringReader(_sText);
//		ColumnText oColumnText = new ColumnText(ioPDFContentByte);
//		StyleSheet styles = new StyleSheet();
//
//		oColumnText.setSimpleColumn(_fLeft, _fBottom, _fWidth, _fHeight);
//		oColumnText.setLeading(_fLeading);
//		oColumnText.setAlignment(Element.ALIGN_JUSTIFIED);
//
//		List<Element> p = HTMLWorker.parseToList(myStringReader, styles);
//		for (int k = 0; k < p.size(); ++k)
//		{
//			oColumnText.addElement((Element) p.get(k));
//		}
//		oColumnText.go();
//
//	}

	/**
	 * This method calculates the next page roll sequence number based on the
	 * O'Brien specification.
	 * 
	 * @param _nPRSeq Integer value of the current page roll sequence number.
	 * @return The next page roll sequence number based on the O'Brien
	 *         specification.
	 */
	public static int getNextOBrienPageRollSequence(int _nPRSeq) {
		int nReturnValue = _nPRSeq + 1;
		if (nReturnValue < 1 || nReturnValue > 9) {
			nReturnValue = 1;
		}

		return nReturnValue;
	}

	/**
	 * This method calculates the next group sequence number based on the O'Brien
	 * specification.
	 * 
	 * @param _nGrpSeq Integer value of the current group sequence number.
	 * @return The next group sequence number based on the O'Brien specification.
	 */
	public static int getNextOBrienGroupSequence(int _nGrpSeq) {
		int nReturnValue = _nGrpSeq + 1;
		if (nReturnValue < 1 || nReturnValue > 99) {
			nReturnValue = 1;
		}

		return nReturnValue;
	}

	public void createPieChart(String _sTitle, LinkedHashMap<String, Double> _oDataSet, float _fLeft, float _fTop,
			int _nWidth, int _nHeight) throws IOException, DocumentException {
		DefaultPieDataset oChartDataSet = new DefaultPieDataset();
		for (String sKey : _oDataSet.keySet()) {
			oChartDataSet.setValue(sKey, _oDataSet.get(sKey).doubleValue());
		}

		JFreeChart oPieChart = ChartFactory.createPieChart(_sTitle, oChartDataSet, false, false, false);
		PiePlot oPiePlot = (PiePlot) oPieChart.getPlot();
		oPiePlot.setInteriorGap(0);
		oPiePlot.setBackgroundPaint(java.awt.Color.WHITE);

		Image oPCImage = Image.getInstance(oPieChart.createBufferedImage(400, 400), null);
		oPCImage.setAbsolutePosition(_fLeft, _fTop);
		oPCImage.scaleToFit(_nWidth, _nHeight);
		ioPDFContentByte.addImage(oPCImage);
	}

	public void setPaperTray(String _sFormType, BaseColor _oPrintColor) {
		getPDFContentByte().saveState();
		getPDFContentByte().beginText();
		getPDFContentByte().setColorFill(_oPrintColor);
		getPDFContentByte().setFontAndSize(ioBaseFontHelveticaRegular, 9);
		getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER, "FORM::" + _sFormType, 12,
				PageSize.LETTER.getHeight() / 2, 90);
		getPDFContentByte().endText();
		getPDFContentByte().restoreState();
	}

	public void setPaperTrayCustom(String _sCustomCopy, BaseColor _oPrintColor) {
		getPDFContentByte().saveState();
		getPDFContentByte().beginText();
		getPDFContentByte().setColorFill(_oPrintColor);
		getPDFContentByte().setFontAndSize(ioBaseFontHelveticaRegular, 6);
		getPDFContentByte().showTextAligned(PdfContentByte.ALIGN_CENTER, _sCustomCopy, 12,
				PageSize.LETTER.getHeight() / 2, 90);
		getPDFContentByte().endText();
		getPDFContentByte().restoreState();
	}

	public void setPaperTray(String _sFormType) {
		setPaperTray(_sFormType, BaseColor.WHITE);
	}

	protected void loadOBrienInserterMap() {
		ioOBrienInserterFirstDigitMap = new HashMap<Integer, String>();
		ioOBrienInserterFirstDigitMap.put(0, "0");
		ioOBrienInserterFirstDigitMap.put(1, "1");
		ioOBrienInserterFirstDigitMap.put(2, "2");
		ioOBrienInserterFirstDigitMap.put(3, "3");
		ioOBrienInserterFirstDigitMap.put(4, "4");
		ioOBrienInserterFirstDigitMap.put(5, "5");
		ioOBrienInserterFirstDigitMap.put(6, "6");
		ioOBrienInserterFirstDigitMap.put(7, "7");
		ioOBrienInserterFirstDigitMap.put(8, "8");
		ioOBrienInserterFirstDigitMap.put(9, "9");
		ioOBrienInserterFirstDigitMap.put(10, "A");
		ioOBrienInserterFirstDigitMap.put(11, "B");
		ioOBrienInserterFirstDigitMap.put(12, "C");
		ioOBrienInserterFirstDigitMap.put(13, "D");
		ioOBrienInserterFirstDigitMap.put(14, "E");
		ioOBrienInserterFirstDigitMap.put(15, "F");
		ioOBrienInserterFirstDigitMap.put(16, "G");
		ioOBrienInserterFirstDigitMap.put(17, "H");
		ioOBrienInserterFirstDigitMap.put(18, "I");
		ioOBrienInserterFirstDigitMap.put(19, "J");
		ioOBrienInserterFirstDigitMap.put(20, "K");
		ioOBrienInserterFirstDigitMap.put(21, "L");
		ioOBrienInserterFirstDigitMap.put(22, "M");
		ioOBrienInserterFirstDigitMap.put(23, "N");
		ioOBrienInserterFirstDigitMap.put(24, "O");
		ioOBrienInserterFirstDigitMap.put(25, "P");
		ioOBrienInserterFirstDigitMap.put(26, "Q");
		ioOBrienInserterFirstDigitMap.put(27, "R");
		ioOBrienInserterFirstDigitMap.put(28, "S");
		ioOBrienInserterFirstDigitMap.put(29, "T");
		ioOBrienInserterFirstDigitMap.put(30, "U");
		ioOBrienInserterFirstDigitMap.put(31, "V");
	}
}
