package projeto.System.Services;

import java.time.LocalDateTime;

import ch.qos.logback.classic.html.HTMLLayout;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.pattern.Converter;

public class RelatorioLayout extends HTMLLayout {

    private LocalDateTime dataEnvio = LocalDateTime.now();
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");


    public RelatorioLayout(){
        super.pattern = "%date%msg";
    }
   
    //HTML DE EMAIL CUSTOMIZADO AINDA PARA SER TERMINADO
    /* 
    @Override
    public String getPresentationHeader() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("<hr/>");
        sbuf.append(LINE_SEPARATOR);
        sbuf.append("<p>Relatorio ");
        sbuf.append(
            dataEnvio.getDayOfMonth() + "/" + dataEnvio.getMonthValue() + "/" + dataEnvio.getYear()
        );
        sbuf.append("</p><p></p>");
        sbuf.append(LINE_SEPARATOR);
        sbuf.append(LINE_SEPARATOR);
        sbuf.append("<table cellspacing=\"0\">");
        sbuf.append(LINE_SEPARATOR);

        buildHeaderRowForTable(sbuf);

        return sbuf.toString();
    }

    private void buildHeaderRowForTable(StringBuilder sbuf) {
        Converter<E> c = head;
        String name;
        sbuf.append("<tr class=\"header\">");
        sbuf.append(LINE_SEPARATOR);
        while (c != null) {
            name = computeConverterName(c);
            if (name == null) {
                c = c.getNext();
                continue;
            }
            sbuf.append("<td class=\"");
            sbuf.append(computeConverterName(c));
            sbuf.append("\">");
            sbuf.append(computeConverterName(c));
            sbuf.append("</td>");
            sbuf.append(LINE_SEPARATOR);
            c = c.getNext();
        }
        sbuf.append("</tr>");
        sbuf.append(LINE_SEPARATOR);
    }

    @Override
    public String getPresentationFooter() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("</table>");
        return sbuf.toString();
    }
    */  

}
