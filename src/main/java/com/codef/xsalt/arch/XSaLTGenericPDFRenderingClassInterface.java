package com.codef.xsalt.arch;

import java.util.ArrayList;
import java.util.HashMap;

 import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public interface XSaLTGenericPDFRenderingClassInterface
{

	abstract boolean importDataFile();

	abstract boolean generateControlTotals();

	abstract boolean generateControlTotalsOther();  // unused

	abstract boolean generateBills(boolean _bRenderFullPDFFlag, String _sSegmentName, String _sSpecificAccountNumbers, boolean _bRenderBackgroundImageFlag);
	
	abstract boolean renderBillPage(Document _oPdfDocument, PdfContentByte _oPDFContentByte,
			ArrayList<String> _oMessageArrayList, String _sRecordPk, long _lRecordSequence, HashMap<String, String> _oDataValues,
			boolean _bRenderBackgroundImageFlag);

	abstract boolean releaseBills();  // e-pay only
	
}
