package projeto.System.Models;

import java.time.LocalDateTime;

import ch.qos.logback.classic.html.HTMLLayout;
import ch.qos.logback.core.html.CssBuilder;

public class RelatorioLayout extends HTMLLayout {

    private LocalDateTime dataEnvio = LocalDateTime.now();
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public RelatorioLayout(){
        super.pattern = "%date%msg";
        super.title = "Relatorio: " + dataEnvio.getDayOfMonth() +"/"+ dataEnvio.getMonthValue()+"/"+ dataEnvio.getYear();

        CssBuilder cb = new CssBuilder() {
            
            @Override
            public void addCss(StringBuilder sbuf) {
                sbuf.append("<style  type=\"text/css\">");
                sbuf.append(LINE_SEPARATOR);
                
                sbuf.append("table{ margin-left: 2em; margin-right: 2em; border-collapse: collapse; } " );
                sbuf.append(LINE_SEPARATOR);

                sbuf.append("th{ padding: 15px; font-size: medium; background: #A2AEE8; font-family: courier, monospace; } " );
                sbuf.append(LINE_SEPARATOR);

                sbuf.append("td{ font-size: medium; font-family: courier, monospace; padding: 15px; } " );
                sbuf.append(LINE_SEPARATOR);

                sbuf.append("</style>");
                sbuf.append(LINE_SEPARATOR);
            }
        }; 

        this.setCssBuilder(cb);
    }
    
}
