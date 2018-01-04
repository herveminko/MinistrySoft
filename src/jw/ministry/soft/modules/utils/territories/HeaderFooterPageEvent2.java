/**
 *
 */
package jw.ministry.soft.modules.utils.territories;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

import jw.ministry.soft.modules.utils.DateUtils;

/**
 * @author HervéClaude
 *
 */
public class HeaderFooterPageEvent2 extends HeaderFooterPageEvent {
	public void onEndPage(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase("MinistrySoft / Bielefeld francophone " + "Date: " + new SimpleDateFormat(DateUtils.getUiDateFormat()).format(new Date()), pageHeaderSmallFontBlue), 300, 20, 0);
//		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
//				new Phrase("page " + document.getPageNumber(), pageHeaderSmallFontBlue), 550, 5, 0);
	}
}
