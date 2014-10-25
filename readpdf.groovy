@Grapes(
    @Grab(group='com.lowagie', module='itext', version='2.1.7')
)
import com.lowagie.text.pdf.parser.PdfTextExtractor
import com.lowagie.text.pdf.PdfReader

// Use iText to extract text from PDF
URL url = new URL("http://www.cdc.gov/westnile/resources/pdfs/cummulative/99_2013_cummulativeHumanCases.pdf")
PdfReader reader = new PdfReader(url)
PdfTextExtractor parser = new PdfTextExtractor(reader)

// Write text to a file in the data directory
File dir = new File("data")
dir.mkdir()
File file = new File(dir, "data.dat")
file.write(parser.getTextFromPage(1))
