package projeto.System.Models.valores;

import java.math.BigDecimal;
import java.util.Currency;

public class Dinheiro {
    
    private Currency real = Currency.getInstance("BRL");
    private BigDecimal quantia;

    //public Dinheiro(BigDecimal insQuantia){
    //    this.quantia = insQuantia.setScale(real.getDefaultFractionDigits());
    //}

    public Dinheiro(double insQuantia){
        if (insQuantia > 0) {
            BigDecimal tempVal = new BigDecimal(insQuantia);
            this.quantia = tempVal.setScale(real.getDefaultFractionDigits());
        }
    }

    public void novaQuantia(double insQuantia) {
        if (insQuantia >= 0) {
            BigDecimal tempVal = new BigDecimal(insQuantia);
            this.quantia = tempVal.setScale(real.getDefaultFractionDigits());
        }
    }
    
    public BigDecimal getQuantia() {
        return quantia;
    }

    public Double getQuantiaDouble(){
        return quantia.doubleValue();
    }

    public String toString(){
        return real.getSymbol() + " " + quantia;
    }

}
