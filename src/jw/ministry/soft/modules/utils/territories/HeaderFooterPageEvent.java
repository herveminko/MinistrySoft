package jw.ministry.soft.modules.utils.territories;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {
	Font pageHeaderSmallFontBlue = new Font(FontFamily.HELVETICA, 6, Font.UNDERLINE, BaseColor.BLUE);

	public void onStartPage(PdfWriter writer, Document document) {
//		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Left"), 30, 800, 0);
//		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Right"), 550, 800,
//				0);
	}

	public void onEndPage(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase("MinistrySoft / Bielefeld francophone", pageHeaderSmallFontBlue), 300, 20, 0);
//		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
//				new Phrase("page " + document.getPageNumber(), pageHeaderSmallFontBlue), 550, 5, 0);
	}
}
