import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

// public Java IO ve XML Stream API'lerini kullanmak için gerekli sınıfları içe aktarıyoruz.

public class XMLVerifier {

    public static void main(String[] args) {

        if(args.length < 1) {
            System.out.println("Usage: java XMLVerifier InputFile [OutputFile]");
            return;
        }

        String inputFile = args[0];
        String outputFile = (args.length > 1) ? args[1] : null;

        try {
            boolean isValid = verifyXML(inputFile);

            if(isValid) {
                System.out.println("The XML file is valid.");

                if(outputFile != null) {
                    formatXML(inputFile, outputFile);
                    System.out.println("Formatted XML has been saved to" + outputFile);
                }
            } else {
                System.out.println("The XML file is invalid.");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
// Main metodu, uygulamanın başlatılmasında çalıştırılır. Bu metod, kullanıcının argümanları doğru şekilde sağladığından emin olur ve verilen XML dosyasının geçerliliğini kontrol eder. Eğer dosya geçerli ise, belirtilen formatta biçimlendirilip kaydedilir.
    private static boolean verifyXML(String inputFile) throws Exception {
        InputStream inputStream = new FileInputStream(inputFile);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

        // while(reader.hasNext()) {
        //     reader.next();
        // }

        reader.close();
        inputStream.close();

        return reader.hasNext();
    }
// XML dosyasını okuyarak, dosyanın geçerli olup olmadığını kontrol eden metod.
    private static void formatXML(String inputFile, String outputFile) throws Exception {
        InputStream inputStream = new FileInputStream(inputFile);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);
        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(stringWriter);

        int depth = 0;
        boolean hasContent = false;

        while(reader.hasNext()) {
            int eventType = reader.next();

            switch(eventType) {
                case XMLStreamConstants.START_DOCUMENT:
                    writer.writeStartDocument("utf-8", "1.0");
                    writer.writeCharacters("\n");
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    if(hasContent) {
                        writer.writeCharacters("\n");
                        hasContent = false;
                    }
                    writeIndentation(writer, depth);
                    writer.writeStartElement(reader.getLocalName());
                    for(int i=0; i<reader.getAttributeCount(); i++) {
                        writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                    }
                    depth++;
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    depth--;
                    if(hasContent) {
                        writer.writeCharacters("\n");
                        hasContent = false;
                    } else {
                        writeIndentation(writer, depth);
                    }
                    writer.writeEndElement();
                    break;
                case XMLStreamConstants.COMMENT:
                    if(hasContent) {
                        writer.writeCharacters("\n");
                        hasContent = false;
                    }
                    writeIndentation(writer, depth);
                    writer.writeComment(reader.getText());
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if(text.length() > 0) {
                        writer.writeCharacters(text);
                        hasContent = true;
                    }
                    break;
            }
        }

        reader.close();
        inputStream.close();
        writer.close();

        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8");

        outputStreamWriter.write(stringWriter.toString());

        outputStreamWriter.close();
        fileOutputStream.close();
    }
// XML dosyasını belirli bir biçimde biçimlendiren metod. Bu metod, XMLStreamReader ve XMLStreamWriter sınıflarını kullanarak XML dosyasını okur ve belirtilen formatta biçimlendirir.
    private static void writeIndentation(XMLStreamWriter writer, int depth) throws Exception {
        writer.writeCharacters("\n");
        for(int i=0; i<depth; i++) {
            writer.writeCharacters("  ");
        }
    }
}
// XML etiketlerini belirli bir derinlikte yazarken kullanacağımız girintileri oluşturan yardımcı metod.
// Uygulamayı kaydedip, konsola söyle yazilabilir:
// javac XMLVerifier.java
// java XMLVerifier input.xml output.xml
// Yukarıdaki örnek, input.xml dosyasını girdi olarak alacak ve dosyanın geçerli olup olmadığını kontrol edecek. İkinci bir parametre belirtilmişse, dosyayı belirtilen formatta (her etiketin yeni bir satıra yerleştirildiği ve her seviye için iki boşluk kullanıldığı) kaydedecektir. Aksi takdirde, sadece geçerliliğini kontrol edecek ve sonucunu ekrana yazdıracaktır.




